// Sidebar Toggle
document.getElementById('sidebarToggle').addEventListener('click', function() {
    document.getElementById('sidebar').classList.toggle('active');
    document.getElementById('mainContent').classList.toggle('expanded');
});

// Section Navigation
function showSection(sectionId) {
    document.querySelectorAll('.sidebar .nav-link').forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('data-section') === sectionId) {
            link.classList.add('active');
        }
    });

    document.getElementById('section-title').textContent = sectionId.charAt(0).toUpperCase() + sectionId.slice(1) + ' Overview';

    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
        section.style.display = 'none';
    });
    const sectionElement = document.getElementById(sectionId + 'Section');
    if (sectionElement) {
        sectionElement.classList.add('active');
        sectionElement.style.display = 'block';
    }
}

document.querySelectorAll('.sidebar .nav-link').forEach(link => {
    link.addEventListener('click', function(e) {
        e.preventDefault();
        const sectionId = this.getAttribute('data-section');
        showSection(sectionId);
    });
});

// Function to get JWT token from cookies
function getJwtToken() {
    const cookies = document.cookie.split(';');
    for (let cookie of cookies) {
        const [name, value] = cookie.trim().split('=');
        if (name === 'jwt') {
            return value;
        }
    }
    return null;
}

// Jobs Section Pagination and Functionality
const jobsPerPage = 4;
let currentJobPage = 1;
let allJobRows = [];
let filteredJobRows = [];

function initJobPagination() {
    // Get all job rows
    const initialRows = Array.from(document.querySelectorAll('#jobTable tbody .job-row'));
    
    // Remove duplicates based on jobId
    const seenJobIds = new Set();
    allJobRows = initialRows.filter(row => {
        const jobId = row.querySelector('.job-select')?.value;
        if (!jobId) return false; // Skip rows without a jobId
        if (seenJobIds.has(jobId)) {
            row.remove(); // Remove duplicate row from DOM
            return false; // Exclude from allJobRows
        }
        seenJobIds.add(jobId);
        return true; // Keep unique row
    });

    filteredJobRows = [...allJobRows];
    showJobPage(currentJobPage);
    setupJobPagination();
}

function showJobPage(page) {
    currentJobPage = page;
    const startIndex = (page - 1) * jobsPerPage;
    const endIndex = startIndex + jobsPerPage;

    filteredJobRows.forEach(row => row.style.display = 'none');
    const rowsToShow = filteredJobRows.slice(startIndex, endIndex);
    console.log(`Showing page ${page}: ${rowsToShow.length} rows`);
    rowsToShow.forEach(row => row.style.display = '');

    setupJobPagination();
    updateSelectAllJobsState();
}

function setupJobPagination() {
    const paginationUl = document.getElementById('jobPagination');
    if (!paginationUl) {
        console.error("jobPagination element not found. Ensure the ul has id='jobPagination'.");
        return;
    }
    const totalJobs = filteredJobRows.length;
    const totalPages = Math.ceil(totalJobs / jobsPerPage);
    paginationUl.innerHTML = '';

    const prevLi = document.createElement('li');
    prevLi.className = 'page-item' + (currentJobPage === 1 ? ' disabled' : '');
    const prevLink = document.createElement('a');
    prevLink.className = 'page-link';
    prevLink.href = '#';
    prevLink.textContent = 'Previous';
    prevLink.onclick = (e) => {
        e.preventDefault();
        if (currentJobPage > 1) showJobPage(currentJobPage - 1);
    };
    prevLi.appendChild(prevLink);
    paginationUl.appendChild(prevLi);

    for (let i = 1; i <= totalPages; i++) {
        const pageLi = document.createElement('li');
        pageLi.className = 'page-item' + (i === currentJobPage ? ' active' : '');
        const pageLink = document.createElement('a');
        pageLink.className = 'page-link';
        pageLink.href = '#';
        pageLink.textContent = i;
        pageLink.onclick = (e) => {
            e.preventDefault();
            showJobPage(i);
        };
        pageLi.appendChild(pageLink);
        paginationUl.appendChild(pageLi);
    }

    const nextLi = document.createElement('li');
    nextLi.className = 'page-item' + (currentJobPage === totalPages ? ' disabled' : '');
    const nextLink = document.createElement('a');
    nextLink.className = 'page-link';
    nextLink.href = '#';
    nextLink.textContent = 'Next';
    nextLink.onclick = (e) => {
        e.preventDefault();
        if (currentJobPage < totalPages) showJobPage(currentJobPage + 1);
    };
    nextLi.appendChild(nextLink);
    paginationUl.appendChild(nextLi);
}

function filterJobs() {
    const statusFilter = document.getElementById('statusFilter')?.value.toLowerCase() || '';
    const approvalFilter = document.getElementById('approvalFilter')?.value.toLowerCase() || '';
    const employerSearch = document.getElementById('employerSearch')?.value.toLowerCase() || '';
    const startDate = document.getElementById('postedStartDate')?.value || '';
    const endDate = document.getElementById('postedEndDate')?.value || '';

    filteredJobRows = allJobRows.filter(row => {
        const status = row.getAttribute('data-status') || '';
        const approval = row.getAttribute('data-approval') || '';
        const company = row.getAttribute('data-company') || '';
        const postedDate = row.getAttribute('data-posted') || '';

        let matchesStatus = !statusFilter || status === statusFilter || (statusFilter === 'expired' && row.querySelector('.badge')?.textContent.toLowerCase() === 'expired');
        let matchesApproval = !approvalFilter || approval === approvalFilter;
        let matchesSearch = !employerSearch || company.includes(employerSearch);
        let matchesDate = true;

        if (startDate && postedDate < startDate) {
            matchesDate = false;
        }
        if (endDate && postedDate > endDate) {
            matchesDate = false;
        }

        return matchesStatus && matchesApproval && matchesSearch && matchesDate;
    });

    console.log(`Filtered rows: ${filteredJobRows.length}`);
    showJobPage(1);
}

function updateSelectAllJobsState() {
    const visibleCheckboxes = filteredJobRows
        .filter((row, index) => {
            const startIndex = (currentJobPage - 1) * jobsPerPage;
            const endIndex = startIndex + jobsPerPage;
            return index >= startIndex && index < endIndex && row.style.display !== 'none';
        })
        .map(row => row.querySelector('.job-select'));

    const selectAllCheckbox = document.getElementById('select-all-jobs');
    if (selectAllCheckbox) {
        const allChecked = visibleCheckboxes.length > 0 && visibleCheckboxes.every(checkbox => checkbox.checked);
        selectAllCheckbox.checked = allChecked;
    }

    updateBulkButtons();
}

function toggleSelectAllJobs() {
    const selectAllCheckbox = document.getElementById('select-all-jobs');
    const visibleCheckboxes = filteredJobRows
        .filter((row, index) => {
            const startIndex = (currentJobPage - 1) * jobsPerPage;
            const endIndex = startIndex + jobsPerPage;
            return index >= startIndex && index < endIndex && row.style.display !== 'none';
        })
        .map(row => row.querySelector('.job-select'));

    visibleCheckboxes.forEach(checkbox => {
        if (checkbox) checkbox.checked = selectAllCheckbox.checked;
    });

    updateBulkButtons();
}

function approveSelectedJobs() {
    const selectedJobs = filteredJobRows
        .filter((row, index) => {
            const startIndex = (currentJobPage - 1) * jobsPerPage;
            const endIndex = startIndex + jobsPerPage;
            return index >= startIndex && index < endIndex && row.style.display !== 'none';
        })
        .map(row => row.querySelector('.job-select'))
        .filter(checkbox => checkbox && checkbox.checked)
        .map(checkbox => parseInt(checkbox.value));

    if (selectedJobs.length === 0) {
        alert('Please select at least one job to approve.');
        return false;
    }
    if (!confirm('Are you sure you want to approve the selected jobs?')) {
        return false;
    }

    const form = document.getElementById('approve-form');
    if (form) {
        form.innerHTML = '';
        selectedJobs.forEach(jobId => {
            const newInput = document.createElement('input');
            newInput.type = 'hidden';
            newInput.name = 'jobIds';
            newInput.value = jobId;
            form.appendChild(newInput);
        });
    }
    return true;
}

function rejectSelectedJobs() {
    const selectedJobs = filteredJobRows
        .filter((row, index) => {
            const startIndex = (currentJobPage - 1) * jobsPerPage;
            const endIndex = startIndex + jobsPerPage;
            return index >= startIndex && index < endIndex && row.style.display !== 'none';
        })
        .map(row => row.querySelector('.job-select'))
        .filter(checkbox => checkbox && checkbox.checked)
        .map(checkbox => parseInt(checkbox.value));

    if (selectedJobs.length === 0) {
        alert('Please select at least one job to reject.');
        return false;
    }
    if (!confirm('Are you sure you want to reject the selected jobs?')) {
        return false;
    }

    const form = document.getElementById('reject-form');
    if (form) {
        form.innerHTML = '';
        selectedJobs.forEach(jobId => {
            const newInput = document.createElement('input');
            newInput.type = 'hidden';
            newInput.name = 'jobIds';
            newInput.value = jobId;
            form.appendChild(newInput);
        });
    }
    return true;
}

function updateBulkButtons() {
    const selectedCheckboxes = filteredJobRows
        .filter((row, index) => {
            const startIndex = (currentJobPage - 1) * jobsPerPage;
            const endIndex = startIndex + jobsPerPage;
            return index >= startIndex && index < endIndex && row.style.display !== 'none';
        })
        .map(row => row.querySelector('.job-select'))
        .filter(checkbox => checkbox && checkbox.checked);

    const bulkApproveBtn = document.getElementById('bulkApprove');
    const bulkRejectBtn = document.getElementById('bulkReject');

    if (bulkApproveBtn) bulkApproveBtn.disabled = selectedCheckboxes.length === 0;
    if (bulkRejectBtn) bulkRejectBtn.disabled = selectedCheckboxes.length === 0;
}

function deleteJob(jobId) {
    if (confirm("Are you sure you want to delete this job?")) {
        fetch(`/admin/jobs/delete/${jobId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + getJwtToken(),
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                alert("Job deleted successfully");
                location.reload();
            } else {
                alert("Failed to delete job");
            }
        })
        .catch(error => {
            console.error("Error deleting job:", error);
            alert("Error deleting job");
        });
    }
}

// Event Listeners for Jobs Section
document.getElementById('statusFilter')?.addEventListener('change', filterJobs);
document.getElementById('approvalFilter')?.addEventListener('change', filterJobs);
document.getElementById('employerSearch')?.addEventListener('input', filterJobs);
document.getElementById('postedStartDate')?.addEventListener('change', filterJobs);
document.getElementById('postedEndDate')?.addEventListener('change', filterJobs);

document.querySelectorAll('.job-select').forEach(checkbox => {
    checkbox.addEventListener('change', () => {
        updateSelectAllJobsState();
        updateBulkButtons();
    });
});

document.getElementById('selectAllJobs')?.addEventListener('change', toggleSelectAllJobs);

// Employers Section Pagination and Functionality
const employersPerPage = 4;
let currentEmployerPage = 1;
let allEmployerRows = [];
let filteredEmployerRows = [];

function initEmployerPagination() {
    // Get all employer rows
    const initialRows = Array.from(document.querySelectorAll('#employerTable tbody tr'));
    
    // Remove duplicates based on employerId
    const seenEmployerIds = new Set();
    allEmployerRows = initialRows.filter(row => {
        const employerId = row.querySelector('.employer-select')?.value;
        if (!employerId) return false;
        if (seenEmployerIds.has(employerId)) {
            row.remove();
            return false;
        }
        seenEmployerIds.add(employerId);
        return true;
    });

    filteredEmployerRows = [...allEmployerRows];
    showEmployerPage(currentEmployerPage);
    setupEmployerPagination();
}

function showEmployerPage(page) {
    currentEmployerPage = page;
    const startIndex = (page - 1) * employersPerPage;
    const endIndex = startIndex + employersPerPage;

    filteredEmployerRows.forEach(row => row.style.display = 'none');
    const rowsToShow = filteredEmployerRows.slice(startIndex, endIndex);
    rowsToShow.forEach(row => row.style.display = '');

    setupEmployerPagination();
    updateSelectAllEmployersState();
}

function setupEmployerPagination() {
    const paginationUl = document.getElementById('employerPagination');
    if (!paginationUl) return;
    const totalEmployers = filteredEmployerRows.length;
    const totalPages = Math.ceil(totalEmployers / employersPerPage);
    paginationUl.innerHTML = '';

    const prevLi = document.createElement('li');
    prevLi.className = 'page-item' + (currentEmployerPage === 1 ? ' disabled' : '');
    const prevLink = document.createElement('a');
    prevLink.className = 'page-link';
    prevLink.href = '#';
    prevLink.textContent = 'Previous';
    prevLink.onclick = (e) => {
        e.preventDefault();
        if (currentEmployerPage > 1) showEmployerPage(currentEmployerPage - 1);
    };
    prevLi.appendChild(prevLink);
    paginationUl.appendChild(prevLi);

    for (let i = 1; i <= totalPages; i++) {
        const pageLi = document.createElement('li');
        pageLi.className = 'page-item' + (i === currentEmployerPage ? ' active' : '');
        const pageLink = document.createElement('a');
        pageLink.className = 'page-link';
        pageLink.href = '#';
        pageLink.textContent = i;
        pageLink.onclick = (e) => {
            e.preventDefault();
            showEmployerPage(i);
        };
        pageLi.appendChild(pageLink);
        paginationUl.appendChild(pageLi);
    }

    const nextLi = document.createElement('li');
    nextLi.className = 'page-item' + (currentEmployerPage === totalPages ? ' disabled' : '');
    const nextLink = document.createElement('a');
    nextLink.className = 'page-link';
    nextLink.href = '#';
    nextLink.textContent = 'Next';
    nextLink.onclick = (e) => {
        e.preventDefault();
        if (currentEmployerPage < totalPages) showEmployerPage(currentEmployerPage + 1);
    };
    nextLi.appendChild(nextLink);
    paginationUl.appendChild(nextLi);
}

function filterEmployers() {
    const status = employerStatusFilter?.value.toLowerCase() || '';
    const industry = industryFilter?.value.toLowerCase() || '';
    const search = employerSearch?.value.toLowerCase() || '';
    const startDate = registeredStartDate?.value ? new Date(registeredStartDate.value) : null;
    const endDate = registeredEndDate?.value ? new Date(registeredEndDate.value) : null;

    filteredEmployerRows = allEmployerRows.filter(row => {
        const company = row.getAttribute('data-company') || '';
        const email = row.getAttribute('data-email') || '';
        const rowIndustry = row.getAttribute('data-industry') || '';
        const statusText = row.getAttribute('data-status') || '';
        const registeredDate = row.getAttribute('data-registered') ? new Date(row.getAttribute('data-registered')) : null;

        const matchesSearch = company.includes(search) || email.includes(search);
        const matchesStatus = !status || statusText.includes(status);
        const matchesIndustry = !industry || rowIndustry.includes(industry);
        const matchesDate = (!startDate || (registeredDate && registeredDate >= startDate)) && (!endDate || (registeredDate && registeredDate <= endDate));

        return matchesSearch && matchesStatus && matchesIndustry && matchesDate;
    });

    showEmployerPage(1);
}

function updateSelectAllEmployersState() {
    const visibleCheckboxes = filteredEmployerRows
        .filter((row, index) => {
            const startIndex = (currentEmployerPage - 1) * employersPerPage;
            const endIndex = startIndex + employersPerPage;
            return index >= startIndex && index < endIndex && row.style.display !== 'none';
        })
        .map(row => row.querySelector('.employer-select'));

    const selectAllCheckbox = document.getElementById('selectAllEmployers');
    if (selectAllCheckbox) {
        const allChecked = visibleCheckboxes.length > 0 && visibleCheckboxes.every(checkbox => checkbox.checked);
        selectAllCheckbox.checked = allChecked;
    }

    updateEmployerBulkActions();
}

function toggleSelectAllEmployers() {
    const selectAllCheckbox = document.getElementById('selectAllEmployers');
    const visibleCheckboxes = filteredEmployerRows
        .filter((row, index) => {
            const startIndex = (currentEmployerPage - 1) * employersPerPage;
            const endIndex = startIndex + employersPerPage;
            return index >= startIndex && index < endIndex && row.style.display !== 'none';
        })
        .map(row => row.querySelector('.employer-select'));

    visibleCheckboxes.forEach(checkbox => {
        if (checkbox) checkbox.checked = selectAllCheckbox.checked;
    });

    updateEmployerBulkActions();
}

function verifySelectedEmployers() {
    const selectedEmployers = filteredEmployerRows
        .filter((row, index) => {
            const startIndex = (currentEmployerPage - 1) * employersPerPage;
            const endIndex = startIndex + employersPerPage;
            return index >= startIndex && index < endIndex && row.style.display !== 'none';
        })
        .map(row => row.querySelector('.employer-select'))
        .filter(checkbox => checkbox && checkbox.checked)
        .map(checkbox => parseInt(checkbox.value));

    if (selectedEmployers.length === 0) {
        alert('Please select at least one employer to verify.');
        return false;
    }
    if (!confirm('Are you sure you want to verify the selected employers?')) {
        return false;
    }

    const form = document.querySelector('form[action="/admin/employers/verify"]');
    if (form) {
        form.innerHTML = '';
        selectedEmployers.forEach(employerId => {
            const newInput = document.createElement('input');
            newInput.type = 'hidden';
            newInput.name = 'employerIds';
            newInput.value = employerId;
            form.appendChild(newInput);
        });
    }
    return true;
}

function unverifySelectedEmployers() {
    const selectedEmployers = filteredEmployerRows
        .filter((row, index) => {
            const startIndex = (currentEmployerPage - 1) * employersPerPage;
            const endIndex = startIndex + employersPerPage;
            return index >= startIndex && index < endIndex && row.style.display !== 'none';
        })
        .map(row => row.querySelector('.employer-select'))
        .filter(checkbox => checkbox && checkbox.checked)
        .map(checkbox => parseInt(checkbox.value));

    if (selectedEmployers.length === 0) {
        alert('Please select at least one employer to unverify.');
        return false;
    }
    if (!confirm('Are you sure you want to unverify the selected employers?')) {
        return false;
    }

    const form = document.querySelector('form[action="/admin/employers/unverify"]');
    if (form) {
        form.innerHTML = '';
        selectedEmployers.forEach(employerId => {
            const newInput = document.createElement('input');
            newInput.type = 'hidden';
            newInput.name = 'employerIds';
            newInput.value = employerId;
            form.appendChild(newInput);
        });
    }
    return true;
}

function updateEmployerBulkActions() {
    const selected = filteredEmployerRows
        .filter((row, index) => {
            const startIndex = (currentEmployerPage - 1) * employersPerPage;
            const endIndex = startIndex + employersPerPage;
            return index >= startIndex && index < endIndex && row.style.display !== 'none';
        })
        .map(row => row.querySelector('.employer-select'))
        .filter(checkbox => checkbox && checkbox.checked);

    const bulkApproveBtn = document.getElementById('bulkApproveEmployers');
    const bulkRejectBtn = document.getElementById('bulkRejectEmployers');

    if (bulkApproveBtn) bulkApproveBtn.disabled = selected.length === 0;
    if (bulkRejectBtn) bulkRejectBtn.disabled = selected.length === 0;
}

// Event Listeners for Employers Section
document.getElementById('employerStatusFilter')?.addEventListener('change', filterEmployers);
document.getElementById('industryFilter')?.addEventListener('change', filterEmployers);
document.getElementById('employerSearch')?.addEventListener('input', filterEmployers);
document.getElementById('postedStartDate')?.addEventListener('change', filterEmployers);
document.getElementById('postedEndDate')?.addEventListener('change', filterEmployers);

document.querySelectorAll('.employer-select').forEach(checkbox => {
    checkbox.addEventListener('change', () => {
        updateSelectAllEmployersState();
        updateEmployerBulkActions();
    });
});

document.getElementById('selectAllEmployers')?.addEventListener('change', toggleSelectAllEmployers);

// Initialize pagination after DOM content is loaded
document.addEventListener('DOMContentLoaded', function() {
	showSectionFromHash();
	    window.addEventListener('hashchange', showSectionFromHash);

	    // Initialize pagination for all sections
	    initJobPagination();
	    initEmployerPagination();
	    initCharts();
});

// Job Seekers Section Functionality
const seekerTable = document.getElementById('seekerTable');
const seekerStatusFilter = document.getElementById('seekerStatusFilter');
const locationFilter = document.getElementById('locationFilter');
const seekerSearch = document.getElementById('seekerSearch');
const selectAllSeekers = document.getElementById('selectAllSeekers');
const bulkActivateSeekers = document.getElementById('bulkActivateSeekers');
const bulkSuspendSeekers = document.getElementById('bulkSuspendSeekers');
const seekerRegisteredStartDate = document.getElementById('registeredStartDate');
const seekerRegisteredEndDate = document.getElementById('registeredEndDate');

function filterSeekers() {
    const status = seekerStatusFilter?.value.toLowerCase() || '';
    const location = locationFilter?.value.toLowerCase() || '';
    const search = seekerSearch?.value.toLowerCase() || '';
    const startDate = seekerRegisteredStartDate?.value ? new Date(seekerRegisteredStartDate.value) : null;
    const endDate = seekerRegisteredEndDate?.value ? new Date(seekerRegisteredEndDate.value) : null;

    const rows = seekerTable?.querySelectorAll('tbody tr') || [];
    rows.forEach(row => {
        const name = row.cells[1]?.textContent.toLowerCase() || '';
        const email = row.cells[2]?.textContent.toLowerCase() || '';
        const rowLocation = row.cells[3]?.textContent.toLowerCase() || '';
        const statusText = row.cells[7]?.textContent.toLowerCase() || '';
        const registeredDate = row.cells[6] ? new Date(row.cells[6].textContent) : null;

        const matchesSearch = name.includes(search) || email.includes(search);
        const matchesStatus = !status || statusText.includes(status);
        const matchesLocation = !location || rowLocation.includes(location);
        const matchesDate = (!startDate || (registeredDate && registeredDate >= startDate)) && (!endDate || (registeredDate && registeredDate <= endDate));

        row.style.display = matchesSearch && matchesStatus && matchesLocation && matchesDate ? '' : 'none';
    });
}

if (seekerStatusFilter) seekerStatusFilter.addEventListener('change', filterSeekers);
if (locationFilter) locationFilter.addEventListener('change', filterSeekers);
if (seekerSearch) seekerSearch.addEventListener('input', filterSeekers);
if (seekerRegisteredStartDate) seekerRegisteredStartDate.addEventListener('change', filterSeekers);
if (seekerRegisteredEndDate) seekerRegisteredEndDate.addEventListener('change', filterSeekers);

function updateSeekerBulkActions() {
    const selected = document.querySelectorAll('.seeker-select:checked')?.length || 0;
    if (bulkActivateSeekers) bulkActivateSeekers.disabled = selected === 0;
    if (bulkSuspendSeekers) bulkSuspendSeekers.disabled = selected === 0;
}

if (selectAllSeekers) {
    selectAllSeekers.addEventListener('change', function() {
        document.querySelectorAll('.seeker-select').forEach(checkbox => {
            checkbox.checked = this.checked;
        });
        updateSeekerBulkActions();
    });
}

if (seekerTable) {
    document.querySelectorAll('.seeker-select').forEach(checkbox => {
        checkbox.addEventListener('change', updateSeekerBulkActions);
    });

    seekerTable.querySelectorAll('thead th').forEach((th, index) => {
        if (index === 0 || index === 8) return;
        th.addEventListener('click', () => {
            const rows = Array.from(seekerTable.querySelectorAll('tbody tr'));
            const isAscending = th.dataset.sort !== 'asc';
            th.dataset.sort = isAscending ? 'asc' : 'desc';

            rows.sort((a, b) => {
                let aText = a.cells[index]?.textContent.trim() || '';
                let bText = b.cells[index]?.textContent.trim() || '';

                if (index === 4 || index === 5) {
                    aText = parseInt(aText) || 0;
                    bText = parseInt(bText) || 0;
                }
                else if (index === 6) {
                    aText = new Date(aText);
                    bText = new Date(bText);
                }

                if (aText < bText) return isAscending ? -1 : 1;
                if (aText > bText) return isAscending ? 1 : -1;
                return 0;
            });

            seekerTable.querySelector('tbody').innerHTML = '';
            rows.forEach(row => seekerTable.querySelector('tbody').appendChild(row));
        });
    });

    seekerTable.addEventListener('click', function(e) {
        const row = e.target.closest('tr');
        if (!row) return;

        if (e.target.closest('.suspend-btn')) {
            const currentStatus = row.cells[7]?.textContent.trim() || '';
            if (row.cells[7]) {
                row.cells[7].innerHTML = currentStatus === 'Suspended' ?
                    '<span class="badge bg-success">Active</span>' :
                    '<span class="badge bg-danger">Suspended</span>';
                filterSeekers();
            }
        }
    });
}

if (bulkActivateSeekers) {
    bulkActivateSeekers.addEventListener('click', () => {
        document.querySelectorAll('.seeker-select:checked').forEach(checkbox => {
            const row = checkbox.closest('tr');
            if (row && row.cells[7]) {
                row.cells[7].innerHTML = '<span class="badge bg-success">Active</span>';
                checkbox.checked = false;
            }
        });
        if (selectAllSeekers) selectAllSeekers.checked = false;
        updateSeekerBulkActions();
        filterSeekers();
    });
}

if (bulkSuspendSeekers) {
    bulkSuspendSeekers.addEventListener('click', () => {
        document.querySelectorAll('.seeker-select:checked').forEach(checkbox => {
            const row = checkbox.closest('tr');
            if (row && row.cells[7]) {
                row.cells[7].innerHTML = '<span class="badge bg-danger">Suspended</span>';
                checkbox.checked = false;
            }
        });
        if (selectAllSeekers) selectAllSeekers.checked = false;
        updateSeekerBulkActions();
        filterSeekers();
    });
}

// Dashboard Charts
document.addEventListener('DOMContentLoaded', function() {
    const hash = window.location.hash.replace('#', '');
    const validSections = ['dashboard', 'jobs', 'employers', 'jobseekers', 'applications', 'settings'];
    if (hash && validSections.includes(hash)) {
        showSection(hash);
    } else {
        showSection('dashboard');
    }

    initJobPagination();

    if (typeof Chart !== 'undefined') {
        new Chart(document.getElementById('userGrowthChart'), {
            type: 'pie',
            data: {
                labels: ['Job Seekers', 'Employers'],
                datasets: [{
                    data: [750, 250],
                    backgroundColor: ['#5e17eb', '#2a0a5a']
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { labels: { color: '#2a0a5a', font: { family: 'Poppins', size: 12 } } }
                }
            }
        });

        new Chart(document.getElementById('jobPostingsChart'), {
            type: 'line',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
                datasets: [{
                    label: 'IT',
                    data: [50, 60, 70, 65, 80],
                    borderColor: '#5e17eb',
                    fill: false
                }, {
                    label: 'Marketing',
                    data: [30, 40, 35, 45, 50],
                    borderColor: '#c4a0ff',
                    fill: false
                }, {
                    label: 'Healthcare',
                    data: [20, 25, 30, 35, 40],
                    borderColor: '#2a0a5a',
                    fill: false
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { labels: { color: '#2a0a5a', font: { family: 'Poppins', size: 12 } } }
                },
                scales: {
                    x: { ticks: { color: '#2a0a5a', font: { family: 'Poppins', size: 12 } } },
                    y: { ticks: { color: '#2a0a5a', font: { family: 'Poppins', size: 12 } } }
                }
            }
        });

        new Chart(document.getElementById('jobApplicationsChart'), {
            type: 'line',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
                datasets: [{
                    label: 'IT',
                    data: [100, 120, 130, 125, 140],
                    borderColor: '#5e17eb',
                    fill: false
                }, {
                    label: 'Marketing',
                    data: [60, 70, 65, 75, 80],
                    borderColor: '#c4a0ff',
                    fill: false
                }, {
                    label: 'Healthcare',
                    data: [40, 45, 50, 55, 60],
                    borderColor: '#2a0a5a',
                    fill: false
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { labels: { color: '#2a0a5a', font: { family: 'Poppins', size: 12 } } }
                },
                scales: {
                    x: { ticks: { color: '#2a0a5a', font: { family: 'Poppins', size: 12 } } },
                    y: { ticks: { color: '#2a0a5a', font: { family: 'Poppins', size: 12 } } }
                }
            }
        });

        new Chart(document.getElementById('recruiterAnalyticsChart'), {
            type: 'bar',
            data: {
                labels: ['TechCorp', 'MarketPro', 'HealthInc'],
                datasets: [{
                    label: 'Job Postings',
                    data: [50, 30, 20],
                    backgroundColor: '#5e17eb',
                    barThickness: 18
                }, {
                    label: 'Applications',
                    data: [150, 90, 60],
                    backgroundColor: '#000000',
                    barThickness: 18
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: { ticks: { color: '#2a0a5a', font: { family: 'Poppins', size: 12 } } },
                    y: { ticks: { color: '#2a0a5a', font: { family: 'Poppins', size: 12 } } }
                },
                plugins: {
                    legend: { labels: { color: '#2a0a5a', font: { family: 'Poppins', size: 12 } } }
                }
            }
        });
    } else {
        console.warn("Chart.js library not found. Charts will not be rendered.");
    }
});