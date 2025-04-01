document.addEventListener("DOMContentLoaded", function () {

    fetch("../js/navbar.html")
        .then(response => response.text())
        .then(data => {
            document.getElementById("nav-container").innerHTML = data;
            const navLinks = document.querySelectorAll(".navbar-link");
            const currentPath = window.location.pathname; 
            navLinks.forEach(link => {
                if (currentPath.includes(link.getAttribute("href"))) {
                    document.querySelectorAll(".navbar-link").forEach(nav => nav.classList.remove("active"));
                    link.classList.add("active"); 
                }
            });
        });

    fetch("./footer.html")
        .then(response => response.text())
        .then(data => {
            document.getElementById("footer-container").innerHTML = data;
        });
});
