// Авторизация администратора

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
});

function handleLogin(e) {
    e.preventDefault();
    
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('errorMessage');
    
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
    
    // Получаем пользователей
    const users = getUsers();
    const user = users.find(u => u.email === email && u.role === 'admin');
    
    if (!user) {
        showLoginError('Пользователь не найден');
        return;
    }
    
    if (user.password !== password) {
        showLoginError('Неверный пароль');
        return;
    }
    
    if (user.isBlocked) {
        showLoginError('Ваш аккаунт заблокирован');
        return;
    }
    
    // Успешная авторизация
    const currentUser = {
        id: user.id,
        email: user.email,
        fullName: user.fullName,
        role: user.role,
        loginTime: new Date().toISOString()
    };
    
    localStorage.setItem('currentUser', JSON.stringify(currentUser));
    
    // Перенаправление на главную страницу
    window.location.href = 'pages/dashboard.html';
}

function showLoginError(message) {
    const errorMessage = document.getElementById('errorMessage');
    errorMessage.textContent = message;
    errorMessage.classList.add('active');
}