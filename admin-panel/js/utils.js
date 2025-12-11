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
            t.innerHTML = `
                <div class="toast-icon">${type==='success' ? '✅' : type==='error' ? '⚠️' : 'ℹ️'}</div>
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

})();
