// API обёртка для работы с бэкендом
(function(){
    // Кэш для данных (опционально, можно убрать для всегда свежих данных)
    let usersCache = null;
    let newsCache = null;
    let requestsCache = null;
    let notificationsCache = null;
    let verificationsCache = null;
    
    // Users
    window.getUsers = async function(){
        try {
            const users = await window.apiUsers.getAll();
            usersCache = users;
            return users;
        } catch (error) {
            console.error('Failed to fetch users:', error);
            showError('Не удалось загрузить пользователей');
            return usersCache || [];
        }
    };

    window.saveUsers = async function(users){
        // Эта функция больше не используется, но оставляем для совместимости
        console.warn('saveUsers is deprecated, use individual user save/update methods');
    };

    // Notifications
    window.getNotifications = async function(){
        try {
            const notifications = await window.apiNotifications.getAll();
            notificationsCache = notifications;
            return notifications;
        } catch (error) {
            console.error('Failed to fetch notifications:', error);
            showError('Не удалось загрузить уведомления');
            return notificationsCache || [];
        }
    };

    window.saveNotification = async function(notification){
        try {
            const created = await window.apiNotifications.create(notification);
            notificationsCache = null; // Сбрасываем кэш
            return created;
        } catch (error) {
            console.error('Failed to create notification:', error);
            showError('Не удалось создать уведомление');
            throw error;
        }
    };

    // News
    window.getNews = async function(){
        try {
            const news = await window.apiNews.getAll();
            newsCache = news;
            return news;
        } catch (error) {
            console.error('Failed to fetch news:', error);
            showError('Не удалось загрузить новости');
            return newsCache || [];
        }
    };

    window.getNewsById = async function(id){
        try {
            return await window.apiNews.getById(id);
        } catch (error) {
            console.error('Failed to fetch news by id:', error);
            return null;
        }
    };

    window.saveNews = async function(news){
        try {
            if (news.id) {
                const updated = await window.apiNews.update(news.id, news);
                newsCache = null; // Сбрасываем кэш
                return updated;
            } else {
                const created = await window.apiNews.create(news);
                newsCache = null; // Сбрасываем кэш
                return created;
            }
        } catch (error) {
            console.error('Failed to save news:', error);
            showError('Не удалось сохранить новость');
            throw error;
        }
    };

    window.deleteNews = async function(id){
        try {
            await window.apiNews.delete(id);
            newsCache = null; // Сбрасываем кэш
        } catch (error) {
            console.error('Failed to delete news:', error);
            showError('Не удалось удалить новость');
            throw error;
        }
    };

    // Requests (заявки)
    window.getRequests = async function(){
        try {
            const requests = await window.apiRequests.getAll();
            requestsCache = requests;
            return requests;
        } catch (error) {
            console.error('Failed to fetch requests:', error);
            showError('Не удалось загрузить заявки');
            return requestsCache || [];
        }
    };

    window.saveRequest = async function(req){
        try {
            const created = await window.apiRequests.create(req);
            requestsCache = null; // Сбрасываем кэш
            return created;
        } catch (error) {
            console.error('Failed to create request:', error);
            showError('Не удалось создать заявку');
            throw error;
        }
    };

    window.updateRequest = async function(id, patch){
        try {
            // Сначала получаем текущую заявку
            const current = await window.apiRequests.getById(id);
            if (!current) {
                throw new Error('Request not found');
            }
            // Объединяем с патчем
            const updated = await window.apiRequests.update(id, { ...current, ...patch });
            requestsCache = null; // Сбрасываем кэш
            return updated;
        } catch (error) {
            console.error('Failed to update request:', error);
            showError('Не удалось обновить заявку');
            throw error;
        }
    };

    // Helpers
    window.getUserById = async function(userId){
        if (!userId) return null;
        try {
            return await window.apiUsers.getById(userId);
        } catch (error) {
            console.error('Failed to fetch user by id:', error);
            return null;
        }
    };

    window.saveUser = async function(user){
        try {
            if (user.id || user.userId) {
                const id = user.userId || user.id;
                const updated = await window.apiUsers.update(id, user);
                usersCache = null; // Сбрасываем кэш
                return updated;
            } else {
                const created = await window.apiUsers.create(user);
                usersCache = null; // Сбрасываем кэш
                return created;
            }
        } catch (error) {
            console.error('Failed to save user:', error);
            showError('Не удалось сохранить пользователя');
            throw error;
        }
    };

    window.getStatistics = async function(){
        try {
            const [users, requests, verifications] = await Promise.all([
                window.apiUsers.getAll(),
                window.apiRequests.getAll(),
                window.apiVerifications.getAll()
            ]);
            
            const totalUsers = users.filter(u => u.role === 'resident').length;
            const verifiedUsers = verifications.filter(v => v.status === 'approved').length;
            const newRequests = requests.filter(r => !r.status || r.status === 'new').length;
            const pendingVerifications = verifications.filter(v => v.status === 'pending').length;
            
            return { totalUsers, verifiedUsers, newRequests, pendingVerifications };
        } catch (error) {
            console.error('Failed to fetch statistics:', error);
            return { totalUsers: 0, verifiedUsers: 0, newRequests: 0, pendingVerifications: 0 };
        }
    };

    // Функции для работы с верификациями
    window.getVerifications = async function(){
        try {
            const verifications = await window.apiVerifications.getAll();
            verificationsCache = verifications;
            return verifications;
        } catch (error) {
            console.error('Failed to fetch verifications:', error);
            showError('Не удалось загрузить верификации');
            return verificationsCache || [];
        }
    };

    window.getVerificationById = async function(id){
        try {
            return await window.apiVerifications.getById(id);
        } catch (error) {
            console.error('Failed to fetch verification by id:', error);
            return null;
        }
    };

    window.updateVerification = async function(id, verification){
        try {
            const updated = await window.apiVerifications.update(id, verification);
            verificationsCache = null; // Сбрасываем кэш
            return updated;
        } catch (error) {
            console.error('Failed to update verification:', error);
            showError('Не удалось обновить верификацию');
            throw error;
        }
    };

})();
