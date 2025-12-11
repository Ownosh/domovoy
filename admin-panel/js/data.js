// Простая клиентская "бд" на localStorage для демо
(function(){
    const KEYS = {
        users: 'domovoy_users',
        notifications: 'domovoy_notifications',
        news: 'domovoy_news',
        requests: 'domovoy_requests'
    };

    function generateId(prefix){
        return (prefix?prefix+'-':'') + Date.now().toString(36) + '-' + Math.random().toString(36).slice(2,9);
    }

    function read(key){
        try {
            const raw = localStorage.getItem(key);
            return raw ? JSON.parse(raw) : null;
        } catch(e){
            console.error('read', key, e);
            return null;
        }
    }

    function write(key, value){
        try {
            localStorage.setItem(key, JSON.stringify(value));
        } catch(e){
            console.error('write', key, e);
        }
    }

    // Users
    window.getUsers = function(){
        let users = read(KEYS.users);
        if (!users) {
            users = [
                {
                    id: 'admin-1',
                    fullName: 'Администратор',
                    email: 'admin@domovoy.ru',
                    password: 'admin123',
                    role: 'admin',
                    isBlocked: false,
                    createdAt: new Date().toISOString()
                }
            ];
            write(KEYS.users, users);
        }
        return users;
    };

    window.saveUsers = function(users){ write(KEYS.users, users); };

    // Notifications
    window.getNotifications = function(){
        const n = read(KEYS.notifications);
        return Array.isArray(n) ? n : [];
    };

    window.saveNotification = function(notification){
        const all = getNotifications();
        const now = new Date().toISOString();
        const item = Object.assign({}, notification, {
            id: generateId('notif'),
            sentAt: now
        });
        all.push(item);
        write(KEYS.notifications, all);
    };

    // News
    window.getNews = function(){
        const n = read(KEYS.news);
        return Array.isArray(n) ? n : [];
    };

    window.saveNewsItem = function(news){
        const all = getNews();
        if (news.id) {
            const idx = all.findIndex(n => n.id === news.id);
            if (idx !== -1) { all[idx] = Object.assign({}, all[idx], news); write(KEYS.news, all); return; }
        }
        const item = Object.assign({}, news, { id: generateId('news'), createdAt: new Date().toISOString() });
        all.push(item);
        write(KEYS.news, all);
    };

    window.deleteNewsItem = function(newsId){
        const all = getNews();
        const filtered = all.filter(n => n.id !== newsId);
        write(KEYS.news, filtered);
    };

    // Requests (заявки)
    window.getRequests = function(){
        const r = read(KEYS.requests);
        return Array.isArray(r) ? r : [];
    };

    window.saveRequest = function(req){
        const all = getRequests();
        const item = Object.assign({}, req, { id: generateId('req'), createdAt: new Date().toISOString() });
        all.push(item);
        write(KEYS.requests, all);
    };

    // Helpers and missing API used by pages
    window.getUserById = function(userId){
        if (!userId) return null;
        return getUsers().find(u => u.id === userId) || null;
    };

    window.saveUser = function(user){
        const users = getUsers();
        if (!user.id) {
            user.id = generateId('user');
            user.createdAt = new Date().toISOString();
            users.push(user);
            write(KEYS.users, users);
            return;
        }
        const idx = users.findIndex(u => u.id === user.id);
        if (idx !== -1) { users[idx] = Object.assign({}, users[idx], user); write(KEYS.users, users); return; }
        users.push(Object.assign({}, user));
        write(KEYS.users, users);
    };

    window.getNewsById = function(id){
        return getNews().find(n => n.id === id) || null;
    };

    window.saveNews = function(news){
        return window.saveNewsItem(news);
    };

    window.deleteNews = function(id){
        return window.deleteNewsItem(id);
    };

    window.getStatistics = function(){
        const users = getUsers();
        const requests = getRequests();
        const totalUsers = users.filter(u => u.role === 'resident').length;
        const verifiedUsers = users.filter(u => u.verification && u.verification.status === 'approved').length;
        const newRequests = requests.filter(r => !r.status || r.status === 'new').length;
        const pendingVerifications = users.filter(u => u.verification && u.verification.status === 'pending').length;
        return { totalUsers, verifiedUsers, newRequests, pendingVerifications };
    };

})();
