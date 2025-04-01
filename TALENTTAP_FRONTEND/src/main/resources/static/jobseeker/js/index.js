function scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

window.onscroll = function () {
    let btn = document.getElementById("goTopBtn");
    if (document.documentElement.scrollTop > 500) {
        btn.classList.remove("d-none");
    } else {
        btn.classList.add("d-none");
    }
};