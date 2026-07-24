/* ===================================================
   Interview Problems — Main JavaScript
   =================================================== */

(function () {
  'use strict';

  // ──────────────────────────────────────────────────
  // 1. Theme Toggle
  // ──────────────────────────────────────────────────
  function initTheme() {
    const saved = localStorage.getItem('theme');
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    const isLight = saved === 'light' || (!saved && !prefersDark);

    if (isLight) {
      document.body.classList.add('light');
    }
  }

  function setupThemeToggle() {
    document.querySelectorAll('.theme-toggle').forEach(function (btn) {
      btn.addEventListener('click', function () {
        const isLight = document.body.classList.toggle('light');
        localStorage.setItem('theme', isLight ? 'light' : 'dark');
      });
    });
  }

  // ──────────────────────────────────────────────────
  // 2. Copy Code Buttons
  // ──────────────────────────────────────────────────
  function initCopyButtons() {
    document.querySelectorAll('.highlight').forEach(function (block) {
      var btn = document.createElement('button');
      btn.className = 'copy-btn';
      btn.textContent = 'Copy';
      btn.setAttribute('aria-label', 'Copy code to clipboard');

      btn.addEventListener('click', function () {
        var code = block.querySelector('pre code, pre');
        var text = code ? code.innerText : '';

        navigator.clipboard.writeText(text).then(function () {
          btn.textContent = 'Copied!';
          btn.classList.add('copied');
          setTimeout(function () {
            btn.textContent = 'Copy';
            btn.classList.remove('copied');
          }, 2000);
        }).catch(function () {
          // Fallback for older browsers
          var ta = document.createElement('textarea');
          ta.value = text;
          ta.style.position = 'fixed';
          ta.style.opacity = '0';
          document.body.appendChild(ta);
          ta.select();
          document.execCommand('copy');
          document.body.removeChild(ta);
          btn.textContent = 'Copied!';
          btn.classList.add('copied');
          setTimeout(function () {
            btn.textContent = 'Copy';
            btn.classList.remove('copied');
          }, 2000);
        });
      });

      block.appendChild(btn);
    });
  }

  // ──────────────────────────────────────────────────
  // 3. Sidebar Mobile Toggle
  // ──────────────────────────────────────────────────
  function initSidebarToggle() {
    var hamburger = document.getElementById('hamburger');
    var sidebar = document.getElementById('sidebar');
    var overlay = document.getElementById('sidebar-overlay');

    if (!hamburger || !sidebar) return;

    function openSidebar() {
      sidebar.classList.add('open');
      if (overlay) overlay.classList.add('visible');
      document.body.style.overflow = 'hidden';
      hamburger.setAttribute('aria-expanded', 'true');
    }

    function closeSidebar() {
      sidebar.classList.remove('open');
      if (overlay) overlay.classList.remove('visible');
      document.body.style.overflow = '';
      hamburger.setAttribute('aria-expanded', 'false');
    }

    hamburger.addEventListener('click', function () {
      if (sidebar.classList.contains('open')) {
        closeSidebar();
      } else {
        openSidebar();
      }
    });

    if (overlay) {
      overlay.addEventListener('click', closeSidebar);
    }

    // Close on Escape
    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' && sidebar.classList.contains('open')) {
        closeSidebar();
      }
    });

    // Close when clicking a nav link on mobile
    sidebar.querySelectorAll('.nav-link, .nav-sub-link').forEach(function (link) {
      link.addEventListener('click', function () {
        if (window.innerWidth <= 768) {
          closeSidebar();
        }
      });
    });
  }

  // ──────────────────────────────────────────────────
  // 4. LLD File Tabs
  // ──────────────────────────────────────────────────
  function initFileTabs() {
    document.querySelectorAll('.lld-tabs').forEach(function (tabContainer) {
      var tabs = tabContainer.querySelectorAll('.tab-btn');
      var contents = tabContainer.querySelectorAll('.tab-content');

      tabs.forEach(function (tab) {
        tab.addEventListener('click', function () {
          var target = tab.getAttribute('data-tab');
          // Convert filename to id format (dots → dashes)
          var targetId = target.replace(/\./g, '-');

          // Deactivate all
          tabs.forEach(function (t) { t.classList.remove('active'); });
          contents.forEach(function (c) { c.classList.remove('active'); });

          // Activate clicked
          tab.classList.add('active');
          var content = document.getElementById(targetId);
          if (content) content.classList.add('active');
        });
      });
    });
  }

  // ──────────────────────────────────────────────────
  // 5. Active Navigation Highlighting
  // ──────────────────────────────────────────────────
  function initActiveNav() {
    var path = window.location.pathname;

    // Exact match first, then prefix match
    var navLinks = document.querySelectorAll('.nav-link, .nav-sub-link');
    var bestMatch = null;
    var bestLength = 0;

    navLinks.forEach(function (link) {
      var href = link.getAttribute('href');
      if (!href) return;

      // Normalize trailing slash
      var normPath = path.replace(/\/$/, '') || '/';
      var normHref = href.replace(/\/$/, '') || '/';

      if (normPath === normHref) {
        // Exact match wins
        bestMatch = link;
        bestLength = Infinity;
      } else if (normPath.startsWith(normHref) && normHref.length > bestLength) {
        bestMatch = link;
        bestLength = normHref.length;
      }
    });

    if (bestMatch) {
      bestMatch.classList.add('active');

      // Expand parent category if sub-link is active
      if (bestMatch.classList.contains('nav-sub-link')) {
        var prevSibling = bestMatch.parentElement.previousElementSibling;
        if (prevSibling && prevSibling.classList.contains('nav-category')) {
          prevSibling.classList.add('expanded');
        }
      }
    }
  }

  // ──────────────────────────────────────────────────
  // 6. Collapsible Nav Categories
  // ──────────────────────────────────────────────────
  function initNavCategories() {
    document.querySelectorAll('.nav-category').forEach(function (cat) {
      cat.addEventListener('click', function () {
        var expanded = cat.classList.toggle('expanded');
        cat.setAttribute('aria-expanded', expanded ? 'true' : 'false');
      });
    });
  }

  // ──────────────────────────────────────────────────
  // 7. Smooth Scroll for anchor links
  // ──────────────────────────────────────────────────
  function initSmoothScroll() {
    document.querySelectorAll('a[href^="#"]').forEach(function (anchor) {
      anchor.addEventListener('click', function (e) {
        var target = document.querySelector(anchor.getAttribute('href'));
        if (target) {
          e.preventDefault();
          target.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
      });
    });
  }

  // ──────────────────────────────────────────────────
  // 8. Set data-lang attributes on highlight blocks
  // ──────────────────────────────────────────────────
  function initCodeLangLabels() {
    document.querySelectorAll('.highlight').forEach(function (block) {
      // Try to detect language from class
      var pre = block.querySelector('pre');
      if (!pre) return;

      var classes = Array.from(pre.classList);
      var langClass = classes.find(function (c) { return c.startsWith('language-'); });
      if (langClass) {
        var lang = langClass.replace('language-', '').toUpperCase();
        block.setAttribute('data-lang', lang);
      } else if (block.classList.contains('language-java') || block.querySelector('code.language-java')) {
        block.setAttribute('data-lang', 'JAVA');
      } else if (block.querySelector('code')) {
        // Check code element classes
        var codeClasses = Array.from(block.querySelector('code').classList);
        var codeLang = codeClasses.find(function (c) { return c.startsWith('language-'); });
        if (codeLang) {
          block.setAttribute('data-lang', codeLang.replace('language-', '').toUpperCase());
        }
      }
    });
  }

  // ──────────────────────────────────────────────────
  // Init all
  // ──────────────────────────────────────────────────
  initTheme();

  document.addEventListener('DOMContentLoaded', function () {
    setupThemeToggle();
    initCopyButtons();
    initSidebarToggle();
    initFileTabs();
    initActiveNav();
    initNavCategories();
    initSmoothScroll();
    initCodeLangLabels();
  });

})();
