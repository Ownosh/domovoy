// Общие утилиты для админ-панели
(function(){
    // Простая валидация email
    window.isValidEmail = function(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    };

    window.escapeHtml = function(str) {
        if (str === null || str === undefined) return '';
        return String(str)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/\"/g, '&quot;')
            .replace(/'/g, '&#039;');
    };

    function pad(n){ return n < 10 ? '0' + n : n; }
    window.formatDate = function(iso) {
        if (!iso) return '';
        const d = new Date(iso);
        if (isNaN(d)) return iso;
        return pad(d.getDate()) + '.' + pad(d.getMonth()+1) + '.' + d.getFullYear() + ' ' + pad(d.getHours()) + ':' + pad(d.getMinutes());
    };

    window.formatDateShort = function(iso) {
        if (!iso) return '';
        const d = new Date(iso);
        if (isNaN(d)) return iso;
        return pad(d.getDate()) + '.' + pad(d.getMonth()+1) + '.' + d.getFullYear();
    };

    // Статусы заявок/верификаций → css-класс бэйджа
    window.getStatusBadgeClass = function(status) {
        if (!status || status === 'none') return 'badge-secondary';
        switch (status) {
            case 'new':
                return 'badge-info';
            case 'accepted':
                return 'badge-info';
            case 'inprogress':
            case 'pending':
                return 'badge-warning';
            case 'done':
            case 'approved':
                return 'badge-success';
            case 'rejected':
                return 'badge-danger';
            default:
                return 'badge-secondary';
        }
    };

    // Человекочитаемый текст статуса заявки
    window.getStatusText = function(status) {
        switch (status) {
            case 'new':
            case undefined:
            case null:
                return 'Новая';
            case 'accepted':
                return 'Принята';
            case 'inprogress':
                return 'В работе';
            case 'done':
                return 'Выполнена';
            case 'rejected':
                return 'Отклонена';
            default:
                return status;
        }
    };

    window.openModal = function(id) {
        const modal = document.getElementById(id);
        if (!modal) return;
        modal.style.display = 'block';
        modal.classList.add('open');
        document.body.classList.add('modal-open');
    };

    window.closeModal = function(id) {
        const modal = document.getElementById(id);
        if (!modal) return;
        modal.style.display = 'none';
        modal.classList.remove('open');
        document.body.classList.remove('modal-open');
    };

    // Close modal when clicking outside content
    document.addEventListener('click', function(e){
        const open = document.querySelector('.modal.open');
        if (!open) return;
        if (e.target === open) closeModal(open.id);
    });

    // Toast system
    (function(){
        let container = null;
        function ensureContainer(){
            if (container) return container;
            container = document.createElement('div');
            container.className = 'toast-container';
            document.body.appendChild(container);
            return container;
        }

        function showToast(message, type = 'info', timeout = 4000){
            const c = ensureContainer();
            const t = document.createElement('div');
            t.className = 'toast toast-' + (type || 'info');

            const iconClass = type === 'success'
                ? 'ri-checkbox-circle-line'
                : type === 'error'
                    ? 'ri-error-warning-line'
                    : 'ri-information-line';

            t.innerHTML = `
                <div class="toast-icon"><i class="${iconClass}"></i></div>
                <div class="toast-body">${escapeHtml(message)}</div>
                <button class="toast-close" aria-label="close">✕</button>
            `;

            const closeBtn = t.querySelector('.toast-close');
            closeBtn.addEventListener('click', () => { t.remove(); });

            c.appendChild(t);
            if (timeout > 0) setTimeout(() => t.remove(), timeout);
        }

        window.showError = function(message, timeout){ showToast(message, 'error', timeout === undefined ? 5000 : timeout); };
        window.showSuccess = function(message, timeout){ showToast(message, 'success', timeout === undefined ? 3000 : timeout); };
        window.showInfo = function(message, timeout){ showToast(message, 'info', timeout === undefined ? 3000 : timeout); };

        window.confirmAction = function(message){
            // keep native confirm for now (modal replacement could be added later)
            return confirm(message);
        };
    })();

    window.checkAuth = function(){
        try {
            const currentUser = JSON.parse(localStorage.getItem('currentUser'));
            if (!currentUser || currentUser.role !== 'admin') {
                // redirect to login
                window.location.href = '../index.html';
            }
        } catch(e){
            window.location.href = '../index.html';
        }
    };

    window.logout = function(){
        localStorage.removeItem('currentUser');
        window.location.href = '../index.html';
    };

    // Общий переключатель бокового меню (используется на всех страницах)
    window.toggleSidebar = function(){
        const sidebar = document.getElementById('sidebar');
        if (!sidebar) return;
        sidebar.classList.toggle('active');
    };

    // Dropdown меню
    window.initDropdowns = function(){
        document.addEventListener('click', function(e){
            const dropdown = e.target.closest('.dropdown');
            const allDropdowns = document.querySelectorAll('.dropdown');
            
            allDropdowns.forEach(d => {
                if (d !== dropdown) {
                    d.classList.remove('active');
                }
            });
            
            if (dropdown) {
                const toggle = dropdown.querySelector('.dropdown-toggle');
                if (e.target === toggle || toggle.contains(e.target)) {
                    dropdown.classList.toggle('active');
                } else if (!dropdown.querySelector('.dropdown-menu').contains(e.target)) {
                    dropdown.classList.remove('active');
                }
            } else {
                allDropdowns.forEach(d => d.classList.remove('active'));
            }
        });
    };

    // Инициализация dropdown при загрузке
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initDropdowns);
    } else {
        initDropdowns();
    }

})();
