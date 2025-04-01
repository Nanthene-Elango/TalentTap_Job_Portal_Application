let currentPage = 1;
const itemsPerPage = 12;

function goToCompanyProfile(companyId) {
	window.location.href = `companyprofile.html?company_id=${companyId}`;
}

function filterCompanies() {
	let commonSearch = document.getElementById('common-search').value.toLowerCase();
	let companySelect = document.getElementById('company-select').value.toLowerCase();
	let industry = document.getElementById('industry-filter').value.toLowerCase();
	let locationInput = document.getElementById('location-filter').value.toLowerCase();
	let companies = document.getElementsByClassName('company-item');

	let visibleCompanies = [];
	for (let i = 0; i < companies.length; i++) {
		let company = companies[i].querySelector('.company-card');
		let name = company.getAttribute('data-name').toLowerCase();
		let companyIndustry = company.getAttribute('data-industry').toLowerCase();
		let location = company.getAttribute('data-location').toLowerCase();

		let matchesCommonSearch = name.includes(commonSearch) || companyIndustry.includes(commonSearch) || location.includes(commonSearch);
		let matchesCompany = !companySelect || name === companySelect;
		let matchesIndustry = !industry || companyIndustry === industry;
		let matchesLocation = location.includes(locationInput);

		if (matchesCommonSearch && matchesCompany && matchesIndustry && matchesLocation) {
			visibleCompanies.push(companies[i]);
			companies[i].style.display = '';
		} else {
			companies[i].style.display = 'none';
		}
	}

	// Update pagination based on visible companies
	updatePagination(visibleCompanies);
}

function updatePagination(visibleCompanies) {
	let totalPages = Math.ceil(visibleCompanies.length / itemsPerPage);
	let pagination = document.getElementById('pagination');
	pagination.innerHTML = '';

	for (let i = 1; i <= totalPages; i++) {
		let li = document.createElement('li');
		li.className = 'page-item' + (i === currentPage ? ' active' : '');
		li.innerHTML = `<a class="page-link" href="#" onclick="changePage(${i})">${i}</a>`;
		pagination.appendChild(li);
	}

	// Show only the companies for the current page
	for (let i = 0; i < visibleCompanies.length; i++) {
		let page = Math.floor(i / itemsPerPage) + 1;
		if (page === currentPage) {
			visibleCompanies[i].style.display = '';
		} else {
			visibleCompanies[i].style.display = 'none';
		}
	}
}

function changePage(page) {
	currentPage = page;
	filterCompanies();
}

// Initial load
window.onload = function() {
	filterCompanies();
};