$(document).ready(function() {
    $('#skills, #locations').select2({
        placeholder: 'Search and select',
        tags: true,
        maximumSelectionLength: 10,
        width: '100%'
    });

    const form = $('#jobPostForm');
    const formContainer = $('#formContainer');
    const previewContainer = $('#previewContainer');
    let editingRequirementIndex = null;
    let editingBenefitIndex = null;
    let editingRoleIndex = null;
    let isSubmitting = false; // Flag to prevent infinite loop

    // Dynamic Validation Functions
    function validateJobRole() {
        const jobRole = $('#jobRole');
        const value = jobRole.val() ? jobRole.val().trim() : '';
        const isValid = value.length >= 3;
        if (!isValid) {
            jobRole.removeClass('is-valid').addClass('is-invalid');
            console.log('Job Role validation failed: Must be at least 3 characters');
        } else {
            jobRole.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    function validateJobType() {
        const jobType = $('#jobType');
        const value = jobType.val();
        const isValid = !!value;
        if (!isValid) {
            jobType.removeClass('is-valid').addClass('is-invalid');
            console.log('Job Type validation failed: Must select a type');
        } else {
            jobType.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    function validateJobCategory() {
        const jobCategory = $('#jobCategory');
        const value = jobCategory.val();
        const isValid = !!value;
        if (!isValid) {
            jobCategory.removeClass('is-valid').addClass('is-invalid');
            console.log('Job Category validation failed: Must select a category');
        } else {
            jobCategory.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    function validateDescription() {
        const description = $('#description');
        const value = description.val().trim();
        const count = value.length;
        $('#charCount').text(count);
        const isValid = count >= 100 && count <= 3000;
        if (!isValid) {
            description.removeClass('is-valid').addClass('is-invalid');
            console.log('Description validation failed: Must be 100-3000 characters');
        } else {
            description.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    function validateSkills() {
        const skills = $('#skills');
        const value = skills.val();
        const isValid = value && value.length > 0;
        if (!isValid) {
            skills.removeClass('is-valid').addClass('is-invalid');
            console.log('Skills validation failed: Must select at least one skill');
        } else {
            skills.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    function validateRequirements() {
        const requirementsList = $('#requirementsList');
        const items = requirementsList.find('li').length;
        const isValid = items > 0;
        if (!isValid) {
            requirementsList.next('.input-group').next('.invalid-feedback').show();
            requirementsList.next('.input-group').next('.valid-feedback').hide();
            console.log('Requirements validation failed: Must add at least one requirement');
        } else {
            requirementsList.next('.input-group').next('.invalid-feedback').hide();
            requirementsList.next('.input-group').next('.valid-feedback').show();
        }
        return isValid;
    }

    function validateRoles() {
        const rolesList = $('#rolesList');
        const items = rolesList.find('li').length;
        const isValid = items > 0;
        if (!isValid) {
            rolesList.next('.invalid-feedback').show();
            console.log('Roles validation failed: Must add at least one role');
        } else {
            rolesList.next('.invalid-feedback').hide();
        }
        return isValid;
    }

    function validateWorkType() {
        const workType = $('#workType');
        const value = workType.val();
        const isValid = !!value;
        if (!isValid) {
            workType.removeClass('is-valid').addClass('is-invalid');
            console.log('Work Type validation failed: Must select a work type');
        } else {
            workType.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    function validateLocations() {
        const locations = $('#locations');
        const value = locations.val();
        const isValid = value && value.length > 0;
        if (!isValid) {
            locations.removeClass('is-valid').addClass('is-invalid');
            console.log('Locations validation failed: Must select at least one location');
        } else {
            locations.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    function validateExperienceYears() {
        const experienceYears = $('#yearsOfExperience');
        const value = experienceYears.val() ? experienceYears.val().trim() : '';
        const regex = /^(0|[0-9]+(-[0-9]+)?)$/;
        const isValid = regex.test(value);
        if (!isValid) {
            experienceYears.removeClass('is-valid').addClass('is-invalid');
            console.log('Experience Years validation failed: Must be 0 or range (e.g., 4-5)');
        } else {
            experienceYears.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    function validateSalaryRange(minField, maxField) {
        const min = $(minField);
        const max = $(maxField);
        const minValue = parseFloat(min.val());
        const maxValue = parseFloat(max.val());
        const isValid = !isNaN(minValue) && minValue >= 0 && !isNaN(maxValue) && maxValue > minValue && maxValue >= 0;
        if (!isValid) {
            min.removeClass('is-valid').addClass('is-invalid');
            max.removeClass('is-valid').addClass('is-invalid');
            console.log('Salary Range validation failed: Invalid range');
        } else {
            min.removeClass('is-invalid').addClass('is-valid');
            max.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    function validateDuration() {
        const duration = $('#duration');
        const value = parseInt(duration.val());
        const isValid = !isNaN(value) && value >= 1 && value <= 24;
        if (!isValid) {
            duration.removeClass('is-valid').addClass('is-invalid');
            console.log('Duration validation failed: Must be 1-24 months');
        } else {
            duration.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    function validateStipend() {
        const stipend = $('#stipend');
        const value = parseFloat(stipend.val());
        const isValid = !isNaN(value) && value >= 0;
        if (!isValid) {
            stipend.removeClass('is-valid').addClass('is-invalid');
            console.log('Stipend validation failed: Must be at least 0');
        } else {
            stipend.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    function validateOpenings() {
        const openings = $('#openings');
        const value = parseInt(openings.val());
        const isValid = !isNaN(value) && value >= 1;
        if (!isValid) {
            openings.removeClass('is-valid').addClass('is-invalid');
            console.log('Openings validation failed: Must be at least 1');
        } else {
            openings.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    function validateDeadline() {
        const deadline = $('#deadline');
        const value = deadline.val();
        const today = new Date().toISOString().split('T')[0];
        const isValid = value && value > today;
        if (!isValid) {
            deadline.removeClass('is-valid').addClass('is-invalid');
            console.log('Deadline validation failed: Must be a future date');
        } else {
            deadline.removeClass('is-invalid').addClass('is-valid');
        }
        return isValid;
    }

    // Attach event listeners for dynamic validation
    $('#jobRole').on('input blur', validateJobRole);
    $('#jobType').on('change blur', validateJobType);
    $('#jobCategory').on('change blur', validateJobCategory);
    $('#description').on('input blur', validateDescription);
    $('#skills').on('change blur', validateSkills);
    $('#workType').on('change blur', validateWorkType);
    $('#locations').on('change blur', validateLocations);
    $('#yearsOfExperience').on('input blur', validateExperienceYears);
    $('#salaryMinFT, #salaryMaxFT').on('input blur', () => validateSalaryRange('#salaryMinFT', '#salaryMaxFT'));
    $('#salaryMinPT, #salaryMaxPT').on('input blur', () => validateSalaryRange('#salaryMinPT', '#salaryMaxPT'));
    $('#duration').on('input blur', validateDuration);
    $('#stipend').on('input blur', validateStipend);
    $('#openings').on('input blur', validateOpenings);
    $('#deadline').on('change blur', validateDeadline);

    // Employment type toggle
    $('#jobType').on('change', function() {
        const value = $(this).val();
        $('#fullTimeFields, #partTimeFields, #internshipFields').hide();
        $('#yearsOfExperience, #salaryMinFT, #salaryMaxFT, #salaryMinPT, #salaryMaxPT, #duration, #stipend').prop('required', false).val('');

        if (value === '1') { // Full Time
            $('#fullTimeFields').show();
            $('#yearsOfExperience, #salaryMinFT, #salaryMaxFT').prop('required', true);
        } else if (value === '2') { // Part Time
            $('#partTimeFields').show();
            $('#salaryMinPT, #salaryMaxPT').prop('required', true);
        } else if (value === '3') { // Internship
            $('#internshipFields').show();
            $('#duration, #stipend').prop('required', true);
        }
    });

    // Set default to Full Time (ID 1) on page load
    $(document).ready(function() {
        $('#jobType').val('1').trigger('change');
    });

    // Add/Edit Requirement
    $('#addRequirementBtn').on('click', function() {
        const input = $('#requirementInput');
        const value = input.val().trim();
        if (!value) return;

        if (editingRequirementIndex !== null) {
            $(`#requirementsList li:eq(${editingRequirementIndex})`).html(`${value}<button type="button" class="btn btn-outline-violet edit-requirement-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-requirement-btn">Remove</button>`);
            editingRequirementIndex = null;
            $(this).text('Add');
        } else {
            if ($('#requirementsList').length === 0) {
                $('body').append('<ul id="requirementsList" class="list-group"></ul>');
            }
            const li = $(`<li class="list-item">${value}<button type="button" class="btn btn-outline-violet edit-requirement-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-requirement-btn">Remove</button></li>`);
            $('#requirementsList').append(li);
        }
        input.val('');
        updateRequirements();
        validateRequirements();
    });

    $(document).on('click', '.edit-requirement-btn', function() {
        const li = $(this).parent();
        const text = li.contents().filter(function() { return this.nodeType === 3; }).text().trim();
        $('#requirementInput').val(text);
        editingRequirementIndex = li.index();
        $('#addRequirementBtn').text('Update');
    });

    $(document).on('click', '.remove-requirement-btn', function() {
        $(this).parent().remove();
        if (editingRequirementIndex !== null) {
            editingRequirementIndex = null;
            $('#addRequirementBtn').text('Add');
            $('#requirementInput').val('');
        }
        updateRequirements();
        validateRequirements();
    });

    // Add/Edit Benefit
    $('#addBenefitBtn').on('click', function() {
        const input = $('#benefitInput');
        const value = input.val().trim();
        if (!value) return;

        if (editingBenefitIndex !== null) {
            $(`#benefitsList li:eq(${editingBenefitIndex})`).html(`${value}<button type="button" class="btn btn-outline-violet edit-benefit-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-benefit-btn">Remove</button>`);
            editingBenefitIndex = null;
            $(this).text('Add');
        } else {
            if ($('#benefitsList').length === 0) {
                $('body').append('<ul id="benefitsList" class="list-group"></ul>');
            }
            const li = $(`<li class="list-item">${value}<button type="button" class="btn btn-outline-violet edit-benefit-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-benefit-btn">Remove</button></li>`);
            $('#benefitsList').append(li);
        }
        input.val('');
        updateBenefits();
    });

    $(document).on('click', '.edit-benefit-btn', function() {
        const li = $(this).parent();
        const text = li.contents().filter(function() { return this.nodeType === 3; }).text().trim();
        $('#benefitInput').val(text);
        editingBenefitIndex = li.index();
        $('#addBenefitBtn').text('Update');
    });

    $(document).on('click', '.remove-benefit-btn', function() {
        $(this).parent().remove();
        if (editingBenefitIndex !== null) {
            editingBenefitIndex = null;
            $('#addBenefitBtn').text('Add');
            $('#benefitInput').val('');
        }
        updateBenefits();
    });

    // Add/Edit Role
    $('#addRoleBtn').on('click', function() {
        const input = $('#roleInput');
        const value = input.val().trim();
        if (!value) return;

        if (editingRoleIndex !== null) {
            $(`#rolesList li:eq(${editingRoleIndex})`).html(`${value}<button type="button" class="btn btn-outline-violet edit-role-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-role-btn">Remove</button>`);
            editingRoleIndex = null;
            $(this).text('Add');
        } else {
            if ($('#rolesList').length === 0) {
                $('body').append('<ul id="rolesList" class="list-group"></ul>');
            }
            const li = $(`<li class="list-item">${value}<button type="button" class="btn btn-outline-violet edit-role-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-role-btn">Remove</button></li>`);
            $('#rolesList').append(li);
        }
        input.val('');
        updateResponsibilities();
        validateRoles();
    });

    $(document).on('click', '.edit-role-btn', function() {
        const li = $(this).parent();
        const text = li.contents().filter(function() { return this.nodeType === 3; }).text().trim();
        $('#roleInput').val(text);
        editingRoleIndex = li.index();
        $('#addRoleBtn').text('Update');
    });

    $(document).on('click', '.remove-role-btn', function() {
        $(this).parent().remove();
        if (editingRoleIndex !== null) {
            editingRoleIndex = null;
            $('#addRoleBtn').text('Add');
            $('#roleInput').val('');
        }
        updateResponsibilities();
        validateRoles();
    });

    // Validate entire form
    function validateForm() {
        const validations = [
            validateJobRole(),
            validateJobType(),
            validateJobCategory(),
            validateDescription(),
            validateSkills(),
            validateRequirements(),
            validateRoles(),
            validateWorkType(),
            validateLocations(),
            validateOpenings(),
            validateDeadline()
        ];

        const jobTypeValue = $('#jobType').val();
        console.log('Validating for job type:', jobTypeValue);
        // Clear unused fields
        if (jobTypeValue === '1') { // Full Time
            $('#yearsOfExperience, #salaryMinFT, #salaryMaxFT').prop('required', true);
            $('#salaryMinPT, #salaryMaxPT, #duration, #stipend').prop('required', false).val('');
            validations.push(validateExperienceYears());
            validations.push(validateSalaryRange('#salaryMinFT', '#salaryMaxFT'));
        } else if (jobTypeValue === '2') { // Part Time
            $('#salaryMinPT, #salaryMaxPT').prop('required', true);
            $('#yearsOfExperience, #salaryMinFT, #salaryMaxFT, #duration, #stipend').prop('required', false).val('');
            validations.push(validateSalaryRange('#salaryMinPT', '#salaryMaxPT'));
        } else if (jobTypeValue === '3') { // Internship
            $('#duration, #stipend').prop('required', true);
            $('#yearsOfExperience, #salaryMinFT, #salaryMaxFT, #salaryMinPT, #salaryMaxPT').prop('required', false).val('');
            validations.push(validateDuration());
            validations.push(validateStipend());
        }

        const isValid = validations.every(v => v);
        console.log('Form validation result:', isValid);
        return isValid;
    }

    // Preview
    $('#previewBtn').on('click', function() {
        if (validateForm()) {
            populatePreview();
            formContainer.hide();
            previewContainer.show();
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Validation Error',
                text: 'Please fill all required fields correctly. Check console for details.',
                confirmButtonColor: '#5e17eb'
            });
        }
    });

    $('#backToFormBtn').on('click', function() {
        previewContainer.hide();
        formContainer.show();
    });

    $('.edit-icon').on('click', function() {
        const field = $(this).data('field');
        previewContainer.hide();
        formContainer.show();
        $(`#${field}`).focus();
    });

    $('#draftBtn').on('click', function() {
        if (validateForm()) {
            Swal.fire({
                icon: 'success',
                title: 'Draft Saved',
                text: 'Job draft saved successfully!',
                confirmButtonColor: '#5e17eb'
            });
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Validation Error',
                text: 'Please fill all required fields correctly. Check console for details.',
                confirmButtonColor: '#5e17eb'
            });
        }
    });

    // Handle form submission
    function handleSubmission() {
        if (isSubmitting) return; // Prevent re-entry
        if (validateForm()) {
            console.log('Form is valid, submitting...');
            isSubmitting = true; // Set flag
            // Ensure all hidden inputs are updated
            updateRequirements();
            updateBenefits();
            updateResponsibilities();
            console.log('Submitting form data:', {
                responsibilities: $('#responsibilities').val(),
                requirements: $('#requirements').val(),
                benefits: $('#benefits').val()
            });
            form.off('submit'); // Temporarily disable submit handler to avoid loop
            form.submit(); // Submit the form
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Validation Error',
                text: 'Please fill all required fields correctly. Check console for details.',
                confirmButtonColor: '#5e17eb'
            });
        }
    }

    form.on('submit', function(e) {
        e.preventDefault();
        handleSubmission();
    });

    $('#submitFromPreviewBtn').on('click', function() {
        handleSubmission();
    });

    function populatePreview() {
        $('#previewJobRole').text($('#jobRole').val());
        $('#previewJobType').text($('#jobType option:selected').text());
        $('#previewJobCategory').text($('#jobCategory').val());
        $('#previewDescription').text($('#description').val());
        $('#previewSkills').html($('#skills').val().map(skill => `<span class="skill-badge">${skill}</span>`).join(''));
        $('#previewRequirements').html($('#requirementsList li').map((_, li) => `<span class="skill-badge">${$(li).contents().filter(function() { return this.nodeType === 3; }).text().trim()}</span>`).get().join(''));
        $('#previewBenefits').html($('#benefitsList li').length ? $('#benefitsList li').map((_, li) => `<span class="skill-badge">${$(li).contents().filter(function() { return this.nodeType === 3; }).text().trim()}</span>`).get().join('') : 'None');
        updateResponsibilities();
        $('#previewRoles').html($('#rolesList li').map((_, li) => `<span class="skill-badge">${$(li).contents().filter(function() { return this.nodeType === 3; }).text().trim()}</span>`).get().join(''));
        $('#previewWorkType').text($('#workType').val());
        $('#previewLocations').text($('#locations').val().join(', '));
        
        $('#previewFullTimeFields, #previewPartTimeFields, #previewInternshipFields').hide();
        const jobTypeValue = $('#jobType').val();
        if (jobTypeValue === '1') {
            $('#previewFullTimeFields').show();
            $('#previewExperienceYears').text($('#yearsOfExperience').val() + ' years');
            $('#previewSalaryRangeFT').text(`$${$('#salaryMinFT').val()} - $${$('#salaryMaxFT').val()}`);
        } else if (jobTypeValue === '2') {
            $('#previewPartTimeFields').show();
            $('#previewSalaryRangePT').text(`$${$('#salaryMinPT').val()} - $${$('#salaryMaxPT').val()}`);
        } else if (jobTypeValue === '3') {
            $('#previewInternshipFields').show();
            $('#previewDuration').text($('#duration').val() + ' months');
            $('#previewStipend').text(`$${$('#stipend').val()}`);
        }

        $('#previewOpenings').text($('#openings').val());
        $('#previewDeadline').text($('#deadline').val());
    }

    // Update hidden input functions
    function updateRequirements() {
        const requirements = $('#requirementsList li').map(function() {
            return $(this).contents().filter(function() { return this.nodeType === 3; }).text().trim();
        }).get().join(', ');
        $('#requirements').val(requirements);
        console.log('Requirements updated to:', requirements);
    }

    function updateBenefits() {
        const benefits = $('#benefitsList li').map(function() {
            return $(this).contents().filter(function() { return this.nodeType === 3; }).text().trim();
        }).get().join(', ');
        $('#benefits').val(benefits);
        console.log('Benefits updated to:', benefits);
    }

    function updateResponsibilities() {
        const roles = $('#rolesList li').map(function() {
            return $(this).contents().filter(function() { return this.nodeType === 3; }).text().trim();
        }).get().join(', ');
        $('#responsibilities').val(roles);
        console.log('Responsibilities updated to:', roles);
    }

    // MutationObserver for requirementsList
    if ($('#requirementsList').length) {
        const requirementsObserver = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                if (mutation.type === 'childList') {
                    updateRequirements();
                    validateRequirements();
                }
            });
        });
        requirementsObserver.observe($('#requirementsList')[0], { childList: true, subtree: true });
    }

    // MutationObserver for rolesList
    if ($('#rolesList').length) {
        const rolesObserver = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                if (mutation.type === 'childList') {
                    updateResponsibilities();
                    validateRoles();
                }
            });
        });
        rolesObserver.observe($('#rolesList')[0], { childList: true, subtree: true });
    }
});