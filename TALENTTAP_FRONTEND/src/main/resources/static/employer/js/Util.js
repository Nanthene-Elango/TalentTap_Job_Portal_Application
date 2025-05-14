document.addEventListener("DOMContentLoaded", function () {
    const currentPath = window.location.pathname.replace(/\/$/, ''); // Remove trailing slash
    const navLinks = document.querySelectorAll(".navbar .nav-link");

    navLinks.forEach(link => {
        // Get the href attribute, which Thymeleaf resolves to a full path
        let linkPath = link.getAttribute("href");
        
        // Remove trailing slash and context path if needed
        if (linkPath) {
            linkPath = linkPath.replace(/\/$/, '');
            
            // Optional: Remove context path if your app has one (e.g., '/your-app')
            // const contextPath = '/your-app'; // Replace with your actual context path
            // if (linkPath.startsWith(contextPath)) {
            //     linkPath = linkPath.substring(contextPath.length);
            // }

            // Check if the current path matches or starts with the link path (for dynamic routes)
            if (currentPath === linkPath || currentPath.startsWith(linkPath + '/')) {
                link.classList.add("active");
            } else {
                link.classList.remove("active");
            }
        }
    });
});