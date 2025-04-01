document.addEventListener("DOMContentLoaded", function() {

	const navLinks = document.querySelectorAll(".navbar-link");
	const currentPath = window.location.pathname;
	navLinks.forEach(link => {
		if (currentPath.includes(link.getAttribute("href"))) {
			document.querySelectorAll(".navbar-link").forEach(nav => nav.classList.remove("active"));
			link.classList.add("active");
		}
	});
});
