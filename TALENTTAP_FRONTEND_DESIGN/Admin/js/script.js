// Sidebar Toggle
document.getElementById('sidebarToggle').addEventListener('click', function() {
    document.getElementById('sidebar').classList.toggle('-translate-x-full');
});

// Logout Simulation
document.getElementById('logout').addEventListener('click', function() {
    alert('Logging out...');
});

// Section Toggle
const dashboardSection = document.getElementById('dashboardSection');
const jobsSection = document.getElementById('jobsSection');
const employersSection = document.getElementById('employersSection');
const usersSection = document.getElementById('usersSection');
const dashboardLink = document.getElementById('dashboardLink');
const jobsLink = document.getElementById('jobsLink');
const employersLink = document.getElementById('employersLink');
const usersLink = document.getElementById('usersLink');

dashboardLink.addEventListener('click', function(e) {
    e.preventDefault();
    dashboardSection.classList.remove('hidden');
    jobsSection.classList.add('hidden');
    employersSection.classList.add('hidden');
    usersSection.classList.add('hidden');
    dashboardLink.classList.add('bg-[var(--primary)]');
    jobsLink.classList.remove('bg-[var(--primary)]');
    employersLink.classList.remove('bg-[var(--primary)]');
    usersLink.classList.remove('bg-[var(--primary)]');
});

jobsLink.addEventListener('click', function(e) {
    e.preventDefault();
    dashboardSection.classList.add('hidden');
    jobsSection.classList.remove('hidden');
    employersSection.classList.add('hidden');
    usersSection.classList.add('hidden');
    dashboardLink.classList.remove('bg-[var(--primary)]');
    jobsLink.classList.add('bg-[var(--primary)]');
    employersLink.classList.remove('bg-[var(--primary)]');
    usersLink.classList.remove('bg-[var(--primary)]');
});

employersLink.addEventListener('click', function(e) {
    e.preventDefault();
    dashboardSection.classList.add('hidden');
    jobsSection.classList.add('hidden');
    employersSection.classList.remove('hidden');
    usersSection.classList.add('hidden');
    dashboardLink.classList.remove('bg-[var(--primary)]');
    jobsLink.classList.remove('bg-[var(--primary)]');
    employersLink.classList.add('bg-[var(--primary)]');
    usersLink.classList.remove('bg-[var(--primary)]');
});

usersLink.addEventListener('click', function(e) {
    e.preventDefault();
    dashboardSection.classList.add('hidden');
    jobsSection.classList.add('hidden');
    employersSection.classList.add('hidden');
    usersSection.classList.remove('hidden');
    dashboardLink.classList.remove('bg-[var(--primary)]');
    jobsLink.classList.remove('bg-[var(--primary)]');
    employersLink.classList.remove('bg-[var(--primary)]');
    usersLink.classList.add('bg-[var(--primary)]');
});

// Action Simulations
function deleteJob(button) {
    button.parentElement.remove();
    alert('Job deleted successfully');
}

function approveUser(button) {
    button.parentElement.innerHTML = '<span class="text-[var(--primary)]">Approved</span>';
}

function rejectUser(button) {
    button.parentElement.innerHTML = '<span class="text-[var(--dark)]">Rejected</span>';
}

function shortlistCandidate(button) {
    button.parentElement.innerHTML = '<span class="text-[var(--primary)]">Shortlisted</span>';
}

function rejectCandidate(button) {
    button.parentElement.innerHTML = '<span class="text-[var(--dark)]">Rejected</span>';
}

function updateApproval(select) {
    const value = select.value;
    alert(`Job approval status updated to: ${value}`);
}

function viewEmployerProfile(employerId) {
    alert(`Viewing employer profile for ID: ${employerId}`);
}

function viewUserProfile(userId) {
    alert(`Viewing job seeker profile for ID: ${userId}`);
}

// Job Search
document.getElementById('jobSearch').addEventListener('input', function(e) {
    const searchTerm = e.target.value.toLowerCase();
    const rows = document.getElementById('jobTableBody').getElementsByTagName('tr');
    for (let row of rows) {
        const title = row.cells[1].textContent.toLowerCase();
        const type = row.cells[2].textContent.toLowerCase();
        const status = row.cells[8].textContent.toLowerCase();
        if (title.includes(searchTerm) || type.includes(searchTerm) || status.includes(searchTerm)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    }
});

// Employer Search
document.getElementById('employerSearch').addEventListener('input', function(e) {
    const searchTerm = e.target.value.toLowerCase();
    const rows = document.getElementById('employerTableBody').getElementsByTagName('tr');
    for (let row of rows) {
        const name = row.cells[1].textContent.toLowerCase();
        const company = row.cells[2].textContent.toLowerCase();
        const designation = row.cells[3].textContent.toLowerCase();
        if (name.includes(searchTerm) || company.includes(searchTerm) || designation.includes(searchTerm)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    }
});

// Job Seeker Search
document.getElementById('userSearch').addEventListener('input', function(e) {
    const searchTerm = e.target.value.toLowerCase();
    const rows = document.getElementById('userTableBody').getElementsByTagName('tr');
    for (let row of rows) {
        const name = row.cells[1].textContent.toLowerCase();
        const jobsApplied = row.cells[2].textContent.toLowerCase();
        if (name.includes(searchTerm) || jobsApplied.includes(searchTerm)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    }
});

// Charts
document.addEventListener('DOMContentLoaded', function() {
    new Chart(document.getElementById('userGrowthChart'), {
        type: 'pie',
        data: {
            labels: ['Job Seekers', 'Employers'],
            datasets: [{
                data: [750, 250],
                backgroundColor: ['#5e17eb', '#000000']
            }]
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
                borderColor: '#000000',
                fill: false
            }, {
                label: 'Healthcare',
                data: [20, 25, 30, 35, 40],
                borderColor: '#5e17eb',
                fill: false
            }]
        },
        options: {
            plugins: { legend: { labels: { color: '#000000' } } },
            scales: { x: { ticks: { color: '#000000' } }, y: { ticks: { color: '#000000' } } }
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
                borderColor: '#000000',
                fill: false
            }, {
                label: 'Healthcare',
                data: [40, 45, 50, 55, 60],
                borderColor: '#5e17eb',
                fill: false
            }]
        },
        options: {
            plugins: { legend: { labels: { color: '#000000' } } },
            scales: { x: { ticks: { color: '#000000' } }, y: { ticks: { color: '#000000' } } }
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
                barThickness: 20
            }, {
                label: 'Applications',
                data: [150, 90, 60],
                backgroundColor: '#000000',
                barThickness: 20
            }]
        },
        options: {
            scales: { x: { ticks: { color: '#000000' } }, y: { ticks: { color: '#000000' } } },
            plugins: { legend: { labels: { color: '#000000' } } }
        }
    });
});