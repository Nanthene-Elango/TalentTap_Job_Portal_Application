document.addEventListener('DOMContentLoaded', function () {
    toggleFilters();

    // Monitor filter changes
    const filterSelects = document.querySelectorAll('.filter-select');
    filterSelects.forEach(select => {
        select.addEventListener('change', updateFilterCount);
    });

    // Initial count update
    updateFilterCount();
});

function updateFilterCount() {
    const filterSelects = document.querySelectorAll('.filter-select');
    let activeFilters = 0;
    let activeTags = [];

    filterSelects.forEach(select => {
        if (select.value) {
            activeFilters++;

            const filterName = select.previousElementSibling.textContent;
            const optionText = select.options[select.selectedIndex].text;
            activeTags.push({ id: select.id, name: filterName, value: optionText });
        }
    });

    // Update count badge
    document.querySelector('.filter-count').textContent = activeFilters + ' selected';

    // Update filter tags
    const filterTagsContainer = document.getElementById('activeFilterTags');
    filterTagsContainer.innerHTML = '';

    activeTags.forEach(tag => {
        const tagElement = document.createElement('div');
        tagElement.className = 'filter-tag';
        tagElement.innerHTML = `
${tag.name}: ${tag.value}
<button class="close-tag" onclick="removeFilter('${tag.id}')">×</button>
`;
        filterTagsContainer.appendChild(tagElement);
    });
}

function removeFilter(filterId) {
    const filter = document.getElementById(filterId);
    filter.selectedIndex = 0;
    updateFilterCount();
    toggleFilters();
}

// Updated toggle filters function
function toggleFilters() {
    const jobType = document.getElementById('job-type').value;

    // Get filter elements
    const salaryFilter = document.querySelector('.salary-filter');
    const durationFilter = document.querySelector('.duration-filter');
    const stipendFilter = document.querySelector('.stipend-filter');

    // Show all filters initially
    salaryFilter.style.display = 'block';
    durationFilter.style.display = 'block';
    stipendFilter.style.display = 'block';

    // Hide appropriate filters based on job type
    if (jobType === 'internship') {
        salaryFilter.style.display = 'none';
    } else if (jobType === 'fulltime' || jobType === 'parttime') {
        durationFilter.style.display = 'none';
        stipendFilter.style.display = 'none';
    }

    updateFilterCount();
}

// Updated clear filters function
function clearFilters() {
    const selects = document.querySelectorAll('.filter-select');
    selects.forEach(select => {
        select.selectedIndex = 0;
    });

    // Reset filter visibility
    document.querySelector('.salary-filter').style.display = 'block';
    document.querySelector('.duration-filter').style.display = 'block';
    document.querySelector('.stipend-filter').style.display = 'block';

    updateFilterCount();
}
// Function to toggle visibility of salary, duration, and stipend filters based on job type
function toggleFilters() {
    const jobType = document.getElementById('job-type').value;

    // Get filter elements
    const salaryFilter = document.querySelector('.salary-filter');
    const durationFilter = document.querySelector('.duration-filter');
    const stipendFilter = document.querySelector('.stipend-filter');

    // Show all filters initially
    salaryFilter.style.display = 'block';
    durationFilter.style.display = 'block';
    stipendFilter.style.display = 'block';

    // Hide appropriate filters based on job type
    if (jobType === 'internship') {
        salaryFilter.style.display = 'none';
    } else if (jobType === 'fulltime' || jobType === 'parttime') {
        durationFilter.style.display = 'none';
        stipendFilter.style.display = 'none';
    }
}

// Function to show job details
function showJobDetail(jobId) {
    // Remove active class from all job cards
    const jobCards = document.querySelectorAll('.job-card');
    jobCards.forEach(card => {
        card.classList.remove('active');
    });

    // Add active class to selected job card
    const selectedJobIndex = Math.min(jobId - 1, jobCards.length - 1);
    jobCards[selectedJobIndex].classList.add('active');

    // In a real application, you would fetch job details from backend
    // and update the job detail section with the fetched data

    // For mobile view, scroll to job detail section
    if (window.innerWidth < 768) {
        document.querySelector('.job-detail').scrollIntoView({ behavior: 'smooth' });
    }
}

// Initial call to set up filters
document.addEventListener('DOMContentLoaded', function () {
    toggleFilters();
});