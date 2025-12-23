// API клиент для работы с бэкендом
(function(){
    const API_BASE_URL = 'http://localhost:8080/api';
    
    // Вспомогательная функция для выполнения HTTP запросов
    async function apiRequest(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
            },
        };
        
        const config = {
            ...defaultOptions,
            ...options,
            headers: {
                ...defaultOptions.headers,
                ...(options.headers || {}),
            },
        };
        
        try {
            const response = await fetch(url, config);
            
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP ${response.status}: ${errorText || response.statusText}`);
            }
            
            // Если ответ пустой (например, DELETE 204), возвращаем null
            if (response.status === 204 || response.headers.get('content-length') === '0') {
                return null;
            }
            
            return await response.json();
        } catch (error) {
            console.error('API request failed:', error);
            throw error;
        }
    }
    
    // Преобразование данных из API формата в формат админ-панели
    function mapUserFromApi(apiUser) {
        return {
            id: apiUser.userId?.toString() || apiUser.id,
            userId: apiUser.userId,
            email: apiUser.email,
            fullName: apiUser.fullName || '',
            phone: apiUser.phone || '',
            role: apiUser.role || 'resident',
            isBlocked: apiUser.isBlocked || false,
            isActive: apiUser.isActive !== undefined ? apiUser.isActive : true,
            createdAt: apiUser.createdAt ? new Date(apiUser.createdAt).toISOString() : new Date().toISOString(),
            updatedAt: apiUser.updatedAt ? new Date(apiUser.updatedAt).toISOString() : null,
        };
    }
    
    function mapUserToApi(user) {
        return {
            email: user.email,
            fullName: user.fullName,
            phone: user.phone,
            role: user.role,
            isBlocked: user.isBlocked,
            isActive: user.isActive,
        };
    }
    
    function mapNewsFromApi(apiNews) {
        return {
            id: apiNews.newsId?.toString() || apiNews.id,
            newsId: apiNews.newsId,
            title: apiNews.title || '',
            content: apiNews.content || '',
            type: apiNews.type || 'news',
            imageUrl: apiNews.imageUrl || '',
            isPublished: apiNews.isPublished !== undefined ? apiNews.isPublished : false,
            authorId: apiNews.author?.userId?.toString() || apiNews.authorId,
            authorName: apiNews.author?.fullName || 'Администратор',
            createdAt: apiNews.createdAt ? new Date(apiNews.createdAt).toISOString() : new Date().toISOString(),
            updatedAt: apiNews.updatedAt ? new Date(apiNews.updatedAt).toISOString() : null,
        };
    }
    
    function mapNewsToApi(news) {
        return {
            title: news.title,
            content: news.content,
            type: news.type,
            imageUrl: news.imageUrl || null,
            isPublished: news.isPublished !== undefined ? news.isPublished : false,
        };
    }
    
    function mapRequestFromApi(apiRequest) {
        // Нормализуем статус: new_ -> new, in_progress -> inprogress, resolved -> resolved
        let status = apiRequest.status || 'new';
        if (status === 'new_') status = 'new';
        else if (status === 'in_progress') status = 'inprogress';
        // resolved остается resolved
        
        return {
            id: apiRequest.requestId?.toString() || apiRequest.id,
            requestId: apiRequest.requestId,
            userId: apiRequest.user?.userId?.toString() || apiRequest.userId,
            userName: apiRequest.user?.fullName || '',
            subject: apiRequest.subject || '',
            message: apiRequest.description || apiRequest.message || '',
            description: apiRequest.description || '',
            category: apiRequest.category || 'other',
            status: status,
            priority: apiRequest.priority || 'medium',
            adminComment: apiRequest.rejectionReason || '',
            rejectionReason: apiRequest.rejectionReason || '',
            createdAt: apiRequest.createdAt ? new Date(apiRequest.createdAt).toISOString() : new Date().toISOString(),
            updatedAt: apiRequest.updatedAt ? new Date(apiRequest.updatedAt).toISOString() : null,
            resolvedAt: apiRequest.resolvedAt ? new Date(apiRequest.resolvedAt).toISOString() : null,
        };
    }
    
    function mapRequestToApi(request) {
        // Преобразуем статус обратно в формат API
        let status = request.status || 'new';
        if (status === 'new') status = 'new_';
        else if (status === 'inprogress') status = 'in_progress';
        else if (status === 'done') status = 'resolved';
        // resolved остается resolved
        
        return {
            subject: request.subject,
            description: request.description || request.message,
            category: request.category || 'other',
            status: status,
            priority: request.priority || 'medium',
            rejectionReason: request.rejectionReason || request.adminComment || null,
        };
    }
    
    function mapNotificationFromApi(apiNotification) {
        return {
            id: apiNotification.notificationId?.toString() || apiNotification.id,
            notificationId: apiNotification.notificationId,
            type: apiNotification.type || 'general',
            title: apiNotification.title || '',
            message: apiNotification.message || '',
            targetAudience: apiNotification.targetAudience || 'all',
            sentBy: apiNotification.sentBy?.userId?.toString() || apiNotification.sentBy,
            senderName: apiNotification.sentBy?.fullName || 'Администратор',
            sentAt: apiNotification.sentAt ? new Date(apiNotification.sentAt).toISOString() : new Date().toISOString(),
        };
    }
    
    function mapNotificationToApi(notification) {
        return {
            title: notification.title,
            message: notification.message,
            type: notification.type,
            targetAudience: notification.targetAudience || 'all',
        };
    }
    
    function mapVerificationFromApi(apiVerification) {
        return {
            id: apiVerification.verificationId?.toString() || apiVerification.id,
            verificationId: apiVerification.verificationId,
            userId: apiVerification.user?.userId?.toString() || apiVerification.userId,
            userName: apiVerification.user?.fullName || '',
            userEmail: apiVerification.user?.email || '',
            apartmentNumber: apiVerification.apartmentNumber || '',
            documentType: apiVerification.documentType || 'ownership',
            documentUrl: apiVerification.documentUrl || '',
            status: apiVerification.status || 'pending',
            submittedAt: apiVerification.submittedAt ? new Date(apiVerification.submittedAt).toISOString() : new Date().toISOString(),
            reviewedAt: apiVerification.reviewedAt ? new Date(apiVerification.reviewedAt).toISOString() : null,
            reviewedBy: apiVerification.reviewedBy?.userId?.toString() || apiVerification.reviewedBy,
            rejectionReason: apiVerification.rejectionReason || '',
        };
    }
    
    function mapVerificationToApi(verification) {
        return {
            apartmentNumber: verification.apartmentNumber,
            documentType: verification.documentType,
            documentUrl: verification.documentUrl || null,
            status: verification.status,
            rejectionReason: verification.rejectionReason || null,
        };
    }
    
    // API методы для пользователей
    window.apiUsers = {
        async getAll() {
            const users = await apiRequest('/users');
            return users.map(mapUserFromApi);
        },
        
        async getById(id) {
            const user = await apiRequest(`/users/${id}`);
            return user ? mapUserFromApi(user) : null;
        },
        
        async getByEmail(email) {
            try {
                const user = await apiRequest(`/users/email/${encodeURIComponent(email)}`);
                return user ? mapUserFromApi(user) : null;
            } catch (error) {
                if (error.message.includes('404')) {
                    return null;
                }
                throw error;
            }
        },
        
        async create(user) {
            const apiUser = mapUserToApi(user);
            const created = await apiRequest('/users', {
                method: 'POST',
                body: JSON.stringify(apiUser),
            });
            return mapUserFromApi(created);
        },
        
        async update(id, user) {
            const apiUser = mapUserToApi(user);
            const updated = await apiRequest(`/users/${id}`, {
                method: 'PUT',
                body: JSON.stringify(apiUser),
            });
            return mapUserFromApi(updated);
        },
        
        async delete(id) {
            await apiRequest(`/users/${id}`, {
                method: 'DELETE',
            });
        },
    };
    
    // API методы для новостей
    window.apiNews = {
        async getAll() {
            const news = await apiRequest('/news');
            return news.map(mapNewsFromApi);
        },
        
        async getById(id) {
            const news = await apiRequest(`/news/${id}`);
            return news ? mapNewsFromApi(news) : null;
        },
        
        async create(news) {
            const apiNews = mapNewsToApi(news);
            const created = await apiRequest('/news', {
                method: 'POST',
                body: JSON.stringify(apiNews),
            });
            return mapNewsFromApi(created);
        },
        
        async update(id, news) {
            const apiNews = mapNewsToApi(news);
            const updated = await apiRequest(`/news/${id}`, {
                method: 'PUT',
                body: JSON.stringify(apiNews),
            });
            return mapNewsFromApi(updated);
        },
        
        async delete(id) {
            await apiRequest(`/news/${id}`, {
                method: 'DELETE',
            });
        },
    };
    
    // API методы для заявок
    window.apiRequests = {
        async getAll() {
            const requests = await apiRequest('/requests');
            return requests.map(mapRequestFromApi);
        },
        
        async getById(id) {
            const request = await apiRequest(`/requests/${id}`);
            return request ? mapRequestFromApi(request) : null;
        },
        
        async getByStatus(status) {
            const requests = await apiRequest(`/requests/status/${status}`);
            return requests.map(mapRequestFromApi);
        },
        
        async create(request) {
            const apiRequest = mapRequestToApi(request);
            const created = await apiRequest('/requests', {
                method: 'POST',
                body: JSON.stringify(apiRequest),
            });
            return mapRequestFromApi(created);
        },
        
        async update(id, request) {
            const apiRequest = mapRequestToApi(request);
            const updated = await apiRequest(`/requests/${id}`, {
                method: 'PUT',
                body: JSON.stringify(apiRequest),
            });
            return mapRequestFromApi(updated);
        },
        
        async delete(id) {
            await apiRequest(`/requests/${id}`, {
                method: 'DELETE',
            });
        },
    };
    
    // API методы для уведомлений
    window.apiNotifications = {
        async getAll() {
            const notifications = await apiRequest('/notifications');
            return notifications.map(mapNotificationFromApi);
        },
        
        async getById(id) {
            const notification = await apiRequest(`/notifications/${id}`);
            return notification ? mapNotificationFromApi(notification) : null;
        },
        
        async create(notification) {
            const apiNotification = mapNotificationToApi(notification);
            const created = await apiRequest('/notifications', {
                method: 'POST',
                body: JSON.stringify(apiNotification),
            });
            return mapNotificationFromApi(created);
        },
        
        async update(id, notification) {
            const apiNotification = mapNotificationToApi(notification);
            const updated = await apiRequest(`/notifications/${id}`, {
                method: 'PUT',
                body: JSON.stringify(apiNotification),
            });
            return mapNotificationFromApi(updated);
        },
        
        async delete(id) {
            await apiRequest(`/notifications/${id}`, {
                method: 'DELETE',
            });
        },
    };
    
    // API методы для верификаций
    window.apiVerifications = {
        async getAll() {
            const verifications = await apiRequest('/user-verifications');
            return verifications.map(mapVerificationFromApi);
        },
        
        async getById(id) {
            const verification = await apiRequest(`/user-verifications/${id}`);
            return verification ? mapVerificationFromApi(verification) : null;
        },
        
        async getByStatus(status) {
            const verifications = await apiRequest(`/user-verifications/status/${status}`);
            return verifications.map(mapVerificationFromApi);
        },
        
        async getByUserId(userId) {
            const verifications = await apiRequest(`/user-verifications/user/${userId}`);
            return verifications.map(mapVerificationFromApi);
        },
        
        async update(id, verification) {
            const apiVerification = mapVerificationToApi(verification);
            const updated = await apiRequest(`/user-verifications/${id}`, {
                method: 'PUT',
                body: JSON.stringify(apiVerification),
            });
            return mapVerificationFromApi(updated);
        },
    };
    
})();

