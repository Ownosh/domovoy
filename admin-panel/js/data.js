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
            const now = new Date().toISOString();
            users = [
                {
                    id: 'admin-1',
                    fullName: 'Администратор',
                    email: 'admin@domovoy.ru',
                    password: 'admin123',
                    role: 'admin',
                    isBlocked: false,
                    createdAt: now
                },
                {
                    id: 'user-101',
                    fullName: 'Иванов Иван Иванович',
                    email: 'ivanov@example.com',
                    password: 'demo123',
                    role: 'resident',
                    phone: '+7 (900) 111-22-33',
                    isBlocked: false,
                    createdAt: now,
                    verification: {
                        status: 'approved',
                        apartmentNumber: '45',
                        submittedAt: now,
                        documents: 'Договор найма, квитанции об оплате коммунальных услуг.'
                    }
                },
                {
                    id: 'user-102',
                    fullName: 'Петров Пётр Петрович',
                    email: 'petrov@example.com',
                    password: 'demo123',
                    role: 'resident',
                    phone: '+7 (900) 222-33-44',
                    isBlocked: false,
                    createdAt: now,
                    verification: {
                        status: 'pending',
                        apartmentNumber: '12',
                        submittedAt: now,
                        documents: 'Заявление на верификацию, сканы паспорта.'
                    }
                },
                {
                    id: 'user-103',
                    fullName: 'Сидорова Анна Сергеевна',
                    email: 'sidorova@example.com',
                    password: 'demo123',
                    role: 'resident',
                    phone: '+7 (900) 333-44-55',
                    isBlocked: false,
                    createdAt: now
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
        if (Array.isArray(n)) return n;

        const now = new Date().toISOString();
        const seed = [
            {
                id: 'notif-1',
                type: 'outage',
                title: 'Плановое отключение воды',
                message: '15 декабря с 10:00 до 14:00 будет отключена холодная вода для проведения профилактических работ. Приносим извинения за временные неудобства.',
                targetAudience: 'all',
                sentBy: 'admin-1',
                senderName: 'Администратор',
                sentAt: now
            },
            {
                id: 'notif-2',
                type: 'meeting',
                title: 'Общее собрание жильцов',
                message: '20 декабря в 19:00 в помещении УК состоится общее собрание жильцов. Повестка: отчёт УК и обсуждение планов на следующий год.',
                targetAudience: 'verified',
                sentBy: 'admin-1',
                senderName: 'Администратор',
                sentAt: now
            }
        ];
        write(KEYS.notifications, seed);
        return seed;
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
        if (Array.isArray(n)) return n;

        const now = new Date().toISOString();
        const seed = [
            {
                id: 'news-1',
                type: 'news',
                title: 'Запуск новой системы «Домовой»',
                content: 'Мы запустили новую систему взаимодействия с управляющей компанией. Теперь вы можете отправлять заявки, получать уведомления и новости прямо в приложении.',
                isPublished: true,
                authorId: 'admin-1',
                authorName: 'Администратор',
                createdAt: now
            },
            {
                id: 'news-2',
                type: 'announcement',
                title: 'Уборка подъездов по новому графику',
                content: 'С 1 января уборка подъездов будет проводиться два раза в неделю. Подробный график размещён на информационных стендах и в мобильном приложении.',
                isPublished: true,
                authorId: 'admin-1',
                authorName: 'Администратор',
                createdAt: now
            }
        ];
        write(KEYS.news, seed);
        return seed;
    };

    // Внутренние функции для работы с новостями (не светим в глобальной области,
    // чтобы не пересекаться с функциями на странице управления новостями)
    function saveNewsInternal(news){
        const all = getNews();
        if (news.id) {
            const idx = all.findIndex(n => n.id === news.id);
            if (idx !== -1) {
                all[idx] = Object.assign({}, all[idx], news);
                write(KEYS.news, all);
                return;
            }
        }
        const item = Object.assign({}, news, {
            id: generateId('news'),
            createdAt: new Date().toISOString()
        });
        all.push(item);
        write(KEYS.news, all);
    }

    function deleteNewsInternal(newsId){
        const all = getNews();
        const filtered = all.filter(n => n.id !== newsId);
        write(KEYS.news, filtered);
    }

    // Requests (заявки)
    window.getRequests = function(){
        const r = read(KEYS.requests);
        if (Array.isArray(r)) return r;

        const now = new Date().toISOString();
        const seed = [
            {
                id: 'req-1',
                userId: 'user-101',
                userName: 'Иванов Иван Иванович',
                subject: 'Протечка в подвале',
                message: 'Обнаружена протечка трубы в подвале возле 3-го подъезда. Нужен приход слесаря.',
                status: 'inprogress',
                adminComment: 'Заявка передана слесарю.',
                createdAt: now
            },
            {
                id: 'req-2',
                userId: 'user-102',
                userName: 'Петров Пётр Петрович',
                subject: 'Не работает освещение на лестничной клетке',
                message: 'На площадке между 5 и 6 этажами перегорела лампочка.',
                status: 'new',
                adminComment: '',
                createdAt: now
            },
            {
                id: 'req-3',
                userId: 'user-103',
                userName: 'Сидорова Анна Сергеевна',
                subject: 'Шум в ночное время',
                message: 'Соседи с квартиры 37 регулярно шумят после 23:00. Просьба принять меры.',
                status: 'done',
                adminComment: 'Шум прекратился после беседы с жильцами.',
                createdAt: now
            }
        ];
        write(KEYS.requests, seed);
        return seed;
    };

    // В текущей демо-версии администратор не создаёт заявки вручную,
    // они «приходят» из внешней системы. Оставляем только обновление.
    window.saveRequest = function(req){
        const all = getRequests();
        const item = Object.assign({}, req, {
            id: generateId('req'),
            createdAt: new Date().toISOString()
        });
        all.push(item);
        write(KEYS.requests, all);
    };

    window.updateRequest = function(id, patch){
        const all = getRequests();
        const idx = all.findIndex(r => r.id === id);
        if (idx === -1) return;
        all[idx] = Object.assign({}, all[idx], patch);
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
        return saveNewsInternal(news);
    };

    window.deleteNews = function(id){
        return deleteNewsInternal(id);
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
