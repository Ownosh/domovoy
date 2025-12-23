// Авторизация администратора

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
});

async function handleLogin(e) {
    e.preventDefault();
    
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('errorMessage');
    const submitButton = e.target.querySelector('button[type="submit"]');
    
    // Очистка предыдущих ошибок
    errorMessage.textContent = '';
    errorMessage.classList.remove('active');
    
    // Валидация
    if (!email || !password) {
        showLoginError('Заполните все поля');
        return;
    }
    
    if (!isValidEmail(email)) {
        showLoginError('Введите корректный email');
        return;
    }
    
    // Блокируем кнопку во время запроса
    const originalText = submitButton.textContent;
    submitButton.disabled = true;
    submitButton.textContent = 'Вход...';
    
    try {
        // Получаем пользователя по email через API
        const user = await window.apiUsers.getByEmail(email);
        
        if (!user) {
            showLoginError('Пользователь не найден');
            return;
        }
        
        if (user.role !== 'admin') {
            showLoginError('Доступ разрешён только администраторам');
            return;
        }
        
        // В реальном приложении здесь должна быть проверка пароля через API
        // Для демо-версии проверяем, что пользователь существует и не заблокирован
        // В продакшене пароль должен проверяться на сервере
        if (user.isBlocked) {
            showLoginError('Ваш аккаунт заблокирован');
            return;
        }
        
        // Успешная авторизация
        const currentUser = {
            id: user.id || user.userId,
            userId: user.userId,
            email: user.email,
            fullName: user.fullName,
            role: user.role,
            loginTime: new Date().toISOString()
        };
        
        localStorage.setItem('currentUser', JSON.stringify(currentUser));
        
        // Перенаправление на главную страницу
        window.location.href = 'pages/dashboard.html';
    } catch (error) {
        console.error('Login error:', error);
        showLoginError('Ошибка подключения к серверу. Проверьте, что API запущен на http://localhost:8080');
    } finally {
        submitButton.disabled = false;
        submitButton.textContent = originalText;
    }
}

function showLoginError(message) {
    const errorMessage = document.getElementById('errorMessage');
    errorMessage.textContent = message;
    errorMessage.classList.add('active');
}