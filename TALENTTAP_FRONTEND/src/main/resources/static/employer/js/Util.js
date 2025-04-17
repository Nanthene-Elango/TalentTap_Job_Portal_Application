document.addEventListener("DOMContentLoaded", function () {
    const currentPath = window.location.pathname;

    const navLinks = document.querySelectorAll(".navbar .nav-link");

    navLinks.forEach(link => {
        const linkPath = link.getAttribute("href");
        
        if (currentPath.includes(linkPath)) {
            link.classList.add("active");
        } else {
            link.classList.remove("active");
        }
    });
});
