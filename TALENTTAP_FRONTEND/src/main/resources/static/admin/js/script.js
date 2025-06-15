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
let filteredJobRows = Array.from(document.querySelectorAll('.job-row'));
let currentJobPage = 1;
let jobsPerPage = 4;

function filterJobs() {
    const statusFilter = document.getElementById('statusFilter').value.toLowerCase();
    const approvalFilter = document.getElementById('approvalFilter').value.toLowerCase();
    const employerSearch = document.getElementById('employerSearch').value.toLowerCase();
    const postedStartDate = document.getElementById('postedStartDate').value;
    const postedEndDate = document.getElementById('postedEndDate').value;

    filteredJobRows = Array.from(document.querySelectorAll('.job-row')).filter(row => {
        const status = row.getAttribute('data-status').toLowerCase();
        const approval = row.getAttribute('data-approval').toLowerCase();
        const company = row.getAttribute('data-company').toLowerCase();
        const postedDate = row.getAttribute('data-posted');

        const statusMatch = !statusFilter || status.includes(statusFilter);
        const approvalMatch = !approvalFilter || approval.includes(approvalFilter);
        const companyMatch = !employerSearch || company.includes(employerSearch);

        let dateMatch = true;
        if (postedStartDate) {
            dateMatch = dateMatch && postedDate >= postedStartDate;
        }
        if (postedEndDate) {
            dateMatch = dateMatch && postedDate <= postedEndDate;
        }

        return statusMatch && approvalMatch && companyMatch && dateMatch;
    });

    filteredJobRows.forEach((row, index) => {
        row.style.display = index >= (currentJobPage - 1) * jobsPerPage && index < currentJobPage * jobsPerPage ? '' : 'none';
    });

    Array.from(document.querySelectorAll('.job-row')).forEach(row => {
        if (!filteredJobRows.includes(row)) {
            row.style.display = 'none';
        }
    });

    updateJobPagination();
    updateSelectAllJobsState();
}

function updateSelectAllJobsState() {
    const visibleCheckboxes = filteredJobRows
        .filter((row, index) => {
            const startIndex = (currentJobPage - 1) * jobsPerPage;
            const endIndex = startIndex + jobsPerPage;
            return index >= startIndex && index < endIndex && row.style.display !== 'none';
        })
        .map(row => row.querySelector('.job-select'));

    const selectAllCheckbox = document.getElementById('selectAllJobs');
    if (selectAllCheckbox) {
        const allChecked = visibleCheckboxes.length > 0 && visibleCheckboxes.every(checkbox => checkbox.checked);
        selectAllCheckbox.checked = allChecked;
    }

    updateJobBulkActions();
}

function toggleSelectAllJobs() {
    const selectAllCheckbox = document.getElementById('selectAllJobs');
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

    updateJobBulkActions();
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
   /* if (!confirm('Are you sure you want to approve the selected jobs?')) {
        return false;
    }*/

    const approveForm = document.getElementById('approve-form');
    const approveInput = document.getElementById('approve-job-ids');
    if (approveForm && approveInput) {
        approveInput.value = selectedJobs.join(',');
        approveForm.submit();
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
    

    const rejectForm = document.getElementById('reject-form');
    const rejectInput = document.getElementById('reject-job-ids');
    if (rejectForm && rejectInput) {
        rejectInput.value = selectedJobs.join(',');
        rejectForm.submit();
    }
    return true;
}

function updateJobBulkActions() {
    const selected = filteredJobRows
        .filter((row, index) => {
            const startIndex = (currentJobPage - 1) * jobsPerPage;
            const endIndex = startIndex + jobsPerPage;
            return index >= startIndex && index < endIndex && row.style.display !== 'none';
        })
        .map(row => row.querySelector('.job-select'))
        .filter(checkbox => checkbox && checkbox.checked);

    const bulkApproveBtn = document.getElementById('bulkApprove');
    const bulkRejectBtn = document.getElementById('bulkReject');

    if (bulkApproveBtn) bulkApproveBtn.disabled = selected.length === 0;
    if (bulkRejectBtn) bulkRejectBtn.disabled = selected.length === 0;
}

function updateJobPagination() {
    const totalPages = Math.ceil(filteredJobRows.length / jobsPerPage);
    const pagination = document.getElementById('jobPagination');
    if (!pagination) return;

    pagination.innerHTML = '';

    const prevLi = document.createElement('li');
    prevLi.className = 'page-item' + (currentJobPage === 1 ? ' disabled' : '');
    prevLi.innerHTML = `<a class="page-link" href="#" onclick="changeJobPage(${currentJobPage - 1})">Previous</a>`;
    pagination.appendChild(prevLi);

    for (let i = 1; i <= totalPages; i++) {
        const li = document.createElement('li');
        li.className = 'page-item' + (i === currentJobPage ? ' active' : '');
        li.innerHTML = `<a class="page-link" href="#" onclick="changeJobPage(${i})">${i}</a>`;
        pagination.appendChild(li);
    }

    const nextLi = document.createElement('li');
    nextLi.className = 'page-item' + (currentJobPage === totalPages ? ' disabled' : '');
    nextLi.innerHTML = `<a class="page-link" href="#" onclick="changeJobPage(${currentJobPage + 1})">Next</a>`;
    pagination.appendChild(nextLi);

    filterJobs();
}

function changeJobPage(page) {
    const totalPages = Math.ceil(filteredJobRows.length / jobsPerPage);
    if (page < 1 || page > totalPages) return;
    currentJobPage = page;
    filterJobs();
}

function deleteJob(jobId) {

    const jwt = getJwtToken();
    if (!jwt) {
        alert("Please log in to perform this action.");
        window.location.href = "/admin/login";
        return;
    }

    fetch(`/admin/jobs/delete/${jobId}`, {
        method: 'POST', // Use POST to match PageRenderController endpoint
        headers: {
            'Authorization': `Bearer ${jwt}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            return response.text().then(text => {
                throw new Error(`Failed to delete job: ${text}`);
            });
        }
    })
    .then(message => {
        // Remove the job row from filteredJobRows and DOM
        const row = document.querySelector(`.job-row[data-job-id="${jobId}"]`);
        if (row) {
            row.remove();
            filteredJobRows = filteredJobRows.filter(r => r.getAttribute('data-job-id') !== jobId.toString());
        }

        // Update pagination and table
        updateJobPagination();
        updateSelectAllJobsState();
        alert(message || "Job deleted successfully");
    })
    .catch(error => {
        console.error("Error deleting job:", error);
        alert(error.message || "Error deleting job");
    });
}

// Event Listeners for Jobs Section
document.addEventListener('DOMContentLoaded', function() {

    // Add event listeners for delete buttons
    document.querySelectorAll('.delete-job-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault(); // Prevent form submission if clicked directly
            const jobId = this.getAttribute('data-job-id');
            deleteJob(jobId);
        });
    });
		    
// Event Listeners for Jobs Section
document.getElementById('statusFilter')?.addEventListener('change', filterJobs);
document.getElementById('approvalFilter')?.addEventListener('change', filterJobs);
document.getElementById('employerSearch')?.addEventListener('input', filterJobs);
document.getElementById('postedStartDate')?.addEventListener('change', filterJobs);
document.getElementById('postedEndDate')?.addEventListener('change', filterJobs);

document.querySelectorAll('.job-select').forEach(checkbox => {
    checkbox.addEventListener('change', () => {
        updateSelectAllJobsState();
        updateJobBulkActions();
    });
});

document.getElementById('selectAllJobs')?.addEventListener('change', toggleSelectAllJobs);
document.getElementById('bulkApprove')?.addEventListener('click', approveSelectedJobs);
document.getElementById('bulkReject')?.addEventListener('click', rejectSelectedJobs);

// Initialize
filterJobs();
updateJobBulkActions();
});

// Employers Section Pagination and Functionality
const employersPerPage = 3;
let currentEmployerPage = 1;
let allEmployerRows = [];
let filteredEmployerRows = [];

function initEmployerPagination() {
    const initialRows = Array.from(document.querySelectorAll('#employerTable tbody tr'));
    
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
    const status = document.getElementById('employerStatusFilter')?.value.toLowerCase() || '';
    const industry = document.getElementById('industryFilter')?.value.toLowerCase() || '';
    const search = document.getElementById('employerSearch')?.value.toLowerCase() || '';
    const startDate = document.getElementById('postedStartDate')?.value ? new Date(document.getElementById('postedStartDate').value) : null;
    const endDate = document.getElementById('postedEndDate')?.value ? new Date(document.getElementById('postedEndDate').value) : null;

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

function verifySingleEmployer(employerId) {
    if (!confirm(`Are you sure you want to verify employer ID ${employerId}?`)) {
        return;
    }

    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/admin/employer/verify-single';
    
    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'employerId';
    input.value = employerId;
    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
}

function unverifySingleEmployer(employerId) {
    if (!confirm(`Are you sure you want to unverify employer ID ${employerId}?`)) {
        return;
    }

    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/admin/employer/unverify-single';
    
    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'employerId';
    input.value = employerId;
    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
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

document.querySelectorAll('.approve-btn').forEach(button => {
    button.addEventListener('click', () => {
        const employerId = button.getAttribute('data-employer-id');
        verifySingleEmployer(employerId);
    });
});

document.querySelectorAll('.reject-btn').forEach(button => {
    button.addEventListener('click', () => {
        const employerId = button.getAttribute('data-employer-id');
        unverifySingleEmployer(employerId);
    });
});

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

    const verifyForm = document.getElementById('verify-form');
    const verifyInput = document.getElementById('verify-emp-ids');
    if (verifyForm && verifyInput) {
        // Convert the array of IDs to a comma-separated string
        verifyInput.value = selectedEmployers.join(',');
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

    const unverifyForm = document.getElementById('unverify-form');
    const unverifyInput = document.getElementById('unverify-emp-ids');
    if (unverifyForm && unverifyInput) {
        // Convert the array of IDs to a comma-separated string
        unverifyInput.value = selectedEmployers.join(',');
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

document.addEventListener('DOMContentLoaded', function() {
    initEmployerPagination();
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
	});
