(() => {
    const LS_KEY = 'gs_theme';
    const root = document.documentElement;
    const DROPDOWN_ID = 'theme-switcher';
    const CONTENT_ID = 'theme-menu';

    function applyTheme(name) {
        root.classList.remove('theme-pink', 'theme-light', 'theme-dark');
        if (name !== 'green') root.classList.add(`theme-${name}`);
        localStorage.setItem(LS_KEY, name);
    }

    document.addEventListener('DOMContentLoaded', () => {
        const saved = localStorage.getItem(LS_KEY) || 'green';
        applyTheme(saved);

        const dropdown = document.getElementById(DROPDOWN_ID);
        const content = document.getElementById(CONTENT_ID);
        dropdown.querySelector('.dropbtn').addEventListener('click', () => {
            content.classList.toggle('show');
        });

        content.querySelectorAll('[data-theme]').forEach(el =>
            el.addEventListener('click', () => {
                const t = el.getAttribute('data-theme');
                applyTheme(t);
                content.classList.remove('show');
            })
        );
    });
})();

