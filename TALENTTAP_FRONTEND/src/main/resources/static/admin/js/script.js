document.getElementById('sidebarToggle').addEventListener('click', function() {
    document.getElementById('sidebar').classList.toggle('active');
    document.getElementById('mainContent').classList.toggle('expanded');
});

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

document.addEventListener('DOMContentLoaded', function() {
    const hash = window.location.hash.replace('#', '');
    const validSections = ['dashboard', 'jobs', 'employers', 'jobseekers', 'applications', 'settings'];
    if (hash && validSections.includes(hash)) {
        showSection(hash);
    } else {
        showSection('dashboard');
    }
});

function getJwtFromCookie() {
    const cookies = document.cookie.split(';');
    for (let cookie of cookies) {
        const [name, value] = cookie.trim().split('=');
        if (name === 'jwt') {
            return value;
        }
    }
    return null;
}

function toggleSelectAll(source) {
    const checkboxes = document.querySelectorAll('.job-select');
    checkboxes.forEach(checkbox => {
        checkbox.checked = source.checked;
    });
    updateBulkButtons();
}

function updateBulkButtons() {
    const checkboxes = document.querySelectorAll('.job-select:checked');
    const bulkApproveBtn = document.getElementById('bulkApprove');
    const bulkRejectBtn = document.getElementById('bulkReject');
    if (checkboxes.length > 0) {
        bulkApproveBtn.disabled = false;
        bulkRejectBtn.disabled = false;
    } else {
        bulkApproveBtn.disabled = true;
        bulkRejectBtn.disabled = true;
    }
}

function getSelectedJobIds() {
    const checkboxes = document.querySelectorAll('.job-select:checked');
    return Array.from(checkboxes).map(checkbox => parseInt(checkbox.value));
}

function submitBulkApprove() {
    const jobIds = getSelectedJobIds();
    if (jobIds.length === 0) {
        alert("Please select at least one job to approve.");
        return;
    }

    if (confirm("Are you sure you want to approve the selected jobs?")) {
        fetch('/admin/jobs/approve', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + getJwtFromCookie(),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jobIds)
        })
        .then(response => {
            if (response.ok) {
                alert("Jobs approved successfully");
                location.reload();
            } else {
                alert("Failed to approve jobs");
            }
        })
        .catch(error => {
            console.error("Error approving jobs:", error);
            alert("Error approving jobs");
        });
    }
}

function submitBulkReject() {
    const jobIds = getSelectedJobIds();
    if (jobIds.length === 0) {
        alert("Please select at least one job to reject.");
        return;
    }

    if (confirm("Are you sure you want to reject the selected jobs?")) {
        fetch('/admin/jobs/reject', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + getJwtFromCookie(),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jobIds)
        })
        .then(response => {
            if (response.ok) {
                alert("Jobs rejected successfully");
                location.reload();
            } else {
                alert("Failed to reject jobs");
            }
        })
        .catch(error => {
            console.error("Error rejecting jobs:", error);
            alert("Error rejecting jobs");
        });
    }
}

function approveJob(jobId) {
    if (confirm("Are you sure you want to approve this job?")) {
        fetch('/admin/jobs/approve', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + getJwtFromCookie(),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify([jobId])
        })
        .then(response => {
            if (response.ok) {
                alert("Job approved successfully");
                location.reload();
            } else {
                alert("Failed to approve job");
            }
        })
        .catch(error => {
            console.error("Error approving job:", error);
            alert("Error approving job");
        });
    }
}

function rejectJob(jobId) {
    if (confirm("Are you sure you want to reject this job?")) {
        fetch('/admin/jobs/reject', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + getJwtFromCookie(),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify([jobId])
        })
        .then(response => {
            if (response.ok) {
                alert("Job rejected successfully");
                location.reload();
            } else {
                alert("Failed to reject job");
            }
        })
        .catch(error => {
            console.error("Error rejecting job:", error);
            alert("Error rejecting job");
        });
    }
}

function deleteJob(jobId) {
    if (confirm("Are you sure you want to delete this job?")) {
        fetch(`/admin/jobs/delete/${jobId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + getJwtFromCookie(),
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

function filterJobs() {
    const statusFilter = document.getElementById('statusFilter').value.toLowerCase();
    const approvalFilter = document.getElementById('approvalFilter').value.toLowerCase();
    const employerSearch = document.getElementById('employerSearch').value.toLowerCase();
    const startDate = document.getElementById('postedStartDate').value;
    const endDate = document.getElementById('postedEndDate').value;

    const rows = document.querySelectorAll('.job-row');
    rows.forEach(row => {
        const status = row.getAttribute('data-status');
        const approval = row.getAttribute('data-approval');
        const company = row.getAttribute('data-company');
        const postedDate = row.getAttribute('data-posted');

        let matchesStatus = !statusFilter || status === statusFilter || (statusFilter === 'expired' && row.querySelector('.badge').textContent.toLowerCase() === 'expired');
        let matchesApproval = !approvalFilter || approval === approvalFilter;
        let matchesSearch = !employerSearch || company.includes(employerSearch);
        let matchesDate = true;

        if (startDate && postedDate < startDate) {
            matchesDate = false;
        }
        if (endDate && postedDate > endDate) {
            matchesDate = false;
        }

        if (matchesStatus && matchesApproval && matchesSearch && matchesDate) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });

    updateBulkButtons();
}

document.querySelectorAll('.job-select').forEach(checkbox => {
    checkbox.addEventListener('change', updateBulkButtons);
});

// Employers Section Functionality
const employerTable = document.getElementById('employerTable');
const employerStatusFilter = document.getElementById('employerStatusFilter');
const industryFilter = document.getElementById('industryFilter');
const employerSearch = document.getElementById('employerSearch');
const selectAllEmployers = document.getElementById('selectAllEmployers');
const bulkApproveEmployers = document.getElementById('bulkApproveEmployers');
const bulkRejectEmployers = document.getElementById('bulkRejectEmployers');
const registeredStartDate = document.getElementById('postedStartDate');
const registeredEndDate = document.getElementById('postedEndDate');

function filterEmployers() {
    const status = employerStatusFilter.value.toLowerCase();
    const industry = industryFilter.value.toLowerCase();
    const search = employerSearch.value.toLowerCase();
    const startDate = registeredStartDate.value ? new Date(registeredStartDate.value) : null;
    const endDate = registeredEndDate.value ? new Date(registeredEndDate.value) : null;

    const rows = employerTable.querySelectorAll('tbody tr');
    rows.forEach(row => {
        const company = row.cells[1].textContent.toLowerCase();
        const email = row.cells[2].textContent.toLowerCase();
        const rowIndustry = row.cells[3].textContent.toLowerCase();
        const statusText = row.cells[6].textContent.toLowerCase();
        const registeredDate = new Date(row.cells[7].textContent);

        const matchesSearch = company.includes(search) || email.includes(search);
        const matchesStatus = !status || statusText.includes(status);
        const matchesIndustry = !industry || rowIndustry.includes(industry);
        const matchesDate = (!startDate || registeredDate >= startDate) && (!endDate || registeredDate <= endDate);

        row.style.display = matchesSearch && matchesStatus && matchesIndustry && matchesDate ? '' : 'none';
    });
}

if (employerStatusFilter) employerStatusFilter.addEventListener('change', filterEmployers);
if (industryFilter) industryFilter.addEventListener('change', filterEmployers);
if (employerSearch) employerSearch.addEventListener('input', filterEmployers);
if (registeredStartDate) registeredStartDate.addEventListener('change', filterEmployers);
if (registeredEndDate) registeredEndDate.addEventListener('change', filterEmployers);

function updateEmployerBulkActions() {
    const selected = document.querySelectorAll('.employer-select:checked').length;
    bulkApproveEmployers.disabled = selected === 0;
    bulkRejectEmployers.disabled = selected === 0;
}

if (selectAllEmployers) {
    selectAllEmployers.addEventListener('change', function() {
        document.querySelectorAll('.employer-select').forEach(checkbox => {
            checkbox.checked = this.checked;
        });
        updateEmployerBulkActions();
    });
}

if (employerTable) {
    document.querySelectorAll('.employer-select').forEach(checkbox => {
        checkbox.addEventListener('change', updateEmployerBulkActions);
    });

    employerTable.querySelectorAll('thead th').forEach((th, index) => {
        if (index === 0 || index === 8) return;
        th.addEventListener('click', () => {
            const rows = Array.from(employerTable.querySelectorAll('tbody tr'));
            const isAscending = th.dataset.sort !== 'asc';
            th.dataset.sort = isAscending ? 'asc' : 'desc';

            rows.sort((a, b) => {
                let aText = a.cells[index].textContent.trim();
                let bText = b.cells[index].textContent.trim();

                if (index === 4 || index === 5) {
                    aText = parseInt(aText) || 0;
                    bText = parseInt(bText) || 0;
                }
                else if (index === 7) {
                    aText = new Date(aText);
                    bText = new Date(bText);
                }

                if (aText < bText) return isAscending ? -1 : 1;
                if (aText > bText) return isAscending ? 1 : -1;
                return 0;
            });

            employerTable.querySelector('tbody').innerHTML = '';
            rows.forEach(row => employerTable.querySelector('tbody').appendChild(row));
        });
    });

    employerTable.addEventListener('click', function(e) {
        const row = e.target.closest('tr');
        if (!row) return;

        if (e.target.closest('.approve-btn')) {
            row.cells[6].innerHTML = '<span class="badge bg-success">Approved</span>';
            filterEmployers();
        } else if (e.target.closest('.reject-btn')) {
            row.cells[6].innerHTML = '<span class="badge bg-danger">Rejected</span>';
            filterEmployers();
        } else if (e.target.closest('.delete-btn')) {
            row.remove();
            updateEmployerBulkActions();
        }
    });
}

if (bulkApproveEmployers) {
    bulkApproveEmployers.addEventListener('click', () => {
        document.querySelectorAll('.employer-select:checked').forEach(checkbox => {
            const row = checkbox.closest('tr');
            row.cells[6].innerHTML = '<span class="badge bg-success">Approved</span>';
            checkbox.checked = false;
        });
        selectAllEmployers.checked = false;
        updateEmployerBulkActions();
        filterEmployers();
    });
}

if (bulkRejectEmployers) {
    bulkRejectEmployers.addEventListener('click', () => {
        document.querySelectorAll('.employer-select:checked').forEach(checkbox => {
            const row = checkbox.closest('tr');
            row.cells[6].innerHTML = '<span class="badge bg-danger">Rejected</span>';
            checkbox.checked = false;
        });
        selectAllEmployers.checked = false;
        updateEmployerBulkActions();
        filterEmployers();
    });
}

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
    const status = seekerStatusFilter.value.toLowerCase();
    const location = locationFilter.value.toLowerCase();
    const search = seekerSearch.value.toLowerCase();
    const startDate = seekerRegisteredStartDate.value ? new Date(seekerRegisteredStartDate.value) : null;
    const endDate = seekerRegisteredEndDate.value ? new Date(seekerRegisteredEndDate.value) : null;

    const rows = seekerTable.querySelectorAll('tbody tr');
    rows.forEach(row => {
        const name = row.cells[1].textContent.toLowerCase();
        const email = row.cells[2].textContent.toLowerCase();
        const rowLocation = row.cells[3].textContent.toLowerCase();
        const statusText = row.cells[7].textContent.toLowerCase();
        const registeredDate = new Date(row.cells[6].textContent);

        const matchesSearch = name.includes(search) || email.includes(search);
        const matchesStatus = !status || statusText.includes(status);
        const matchesLocation = !location || rowLocation.includes(location);
        const matchesDate = (!startDate || registeredDate >= startDate) && (!endDate || registeredDate <= endDate);

        row.style.display = matchesSearch && matchesStatus && matchesLocation && matchesDate ? '' : 'none';
    });
}

if (seekerStatusFilter) seekerStatusFilter.addEventListener('change', filterSeekers);
if (locationFilter) locationFilter.addEventListener('change', filterSeekers);
if (seekerSearch) seekerSearch.addEventListener('input', filterSeekers);
if (seekerRegisteredStartDate) seekerRegisteredStartDate.addEventListener('change', filterSeekers);
if (seekerRegisteredEndDate) seekerRegisteredEndDate.addEventListener('change', filterSeekers);

function updateSeekerBulkActions() {
    const selected = document.querySelectorAll('.seeker-select:checked').length;
    bulkActivateSeekers.disabled = selected === 0;
    bulkSuspendSeekers.disabled = selected === 0;
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
                let aText = a.cells[index].textContent.trim();
                let bText = b.cells[index].textContent.trim();

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
            const currentStatus = row.cells[7].textContent.trim();
            row.cells[7].innerHTML = currentStatus === 'Suspended' ?
                '<span class="badge bg-success">Active</span>' :
                '<span class="badge bg-danger">Suspended</span>';
            filterSeekers();
        }
    });
}

if (bulkActivateSeekers) {
    bulkActivateSeekers.addEventListener('click', () => {
        document.querySelectorAll('.seeker-select:checked').forEach(checkbox => {
            const row = checkbox.closest('tr');
            row.cells[7].innerHTML = '<span class="badge bg-success">Active</span>';
            checkbox.checked = false;
        });
        selectAllSeekers.checked = false;
        updateSeekerBulkActions();
        filterSeekers();
    });
}

if (bulkSuspendSeekers) {
    bulkSuspendSeekers.addEventListener('click', () => {
        document.querySelectorAll('.seeker-select:checked').forEach(checkbox => {
            const row = checkbox.closest('tr');
            row.cells[7].innerHTML = '<span class="badge bg-danger">Suspended</span>';
            checkbox.checked = false;
        });
        selectAllSeekers.checked = false;
        updateSeekerBulkActions();
        filterSeekers();
    });
}
// Dashboard Charts
document.addEventListener('DOMContentLoaded', function() {
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