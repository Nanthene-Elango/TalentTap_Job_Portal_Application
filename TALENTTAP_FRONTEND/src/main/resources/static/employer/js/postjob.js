$(document).ready(function() {
    $('#skills, #locations').select2({
        placeholder: 'Search and select',
        tags: true,
        maximumSelectionLength: 10,
        width: '100%'
    });
    
    console.log('Initial jobType value:', $('#jobType').val());
    console.log('Initial duration value:', $('#duration').val());
    console.log('Initial stipend value:', $('#stipend').val());
    
    // Preserve and set values on load
    const initialJobType = $('#jobType').val() || '3'; // Fallback to Internship
    const initialDuration = $('#duration').val() || '2'; // Fallback to 2
    const initialStipend = $('#stipend').val() || '0'; // Fallback to 0
    $('#duration').val(initialDuration);
    $('#stipend').val(initialStipend);
    console.log('Set duration on load:', $('#duration').val());
    console.log('Set stipend on load:', $('#stipend').val());
    
    // Show fields and validate based on jobType
    if (initialJobType === '3') {
        $('#internshipFields').show();
        $('#duration, #stipend').prop('required', true);
        validateDuration();
        validateStipend();
    } else if (initialJobType === '1') {
        $('#fullTimeFields').show();
        $('#yearsOfExperience, #salaryMinFT, #salaryMaxFT').prop('required', true);
    } else if (initialJobType === '2') {
        $('#partTimeFields').show();
        $('#salaryMinPT, #salaryMaxPT').prop('required', true);
    }
    console.log('Fields initialized for jobType:', initialJobType);

    const form = $('#jobPostForm');
    const formContainer = $('#formContainer');
    const previewContainer = $('#previewContainer');
    let editingRequirementIndex = null;
    let editingBenefitIndex = null;
    let editingRoleIndex = null;
    let isSubmitting = false;

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
        const rawValue = duration.val();
        const value = rawValue ? parseInt(rawValue) : NaN;
        console.log('Validating duration, raw value:', rawValue, 'parsed value:', value);
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
        const rawValue = stipend.val();
        const value = parseFloat(rawValue);
        console.log('Validating stipend, raw value:', rawValue, 'parsed value:', value);
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
        const value = deadline.val().trim();
        const today = new Date().toISOString().split('T')[0];
        const isValid = value && value >= today;
        if (!isValid) {
            deadline.removeClass('is-valid').addClass('is-invalid');
            console.log('Deadline validation failed: Must be today or a future date');
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

    $('#jobType').on('change', function() {
        console.log('jobType change handler triggered');
        const value = $(this).val();
        console.log('jobType changed to:', value);

        $('#fullTimeFields, #partTimeFields, #internshipFields').hide();
        if (value === '1') {
            $('#fullTimeFields').show();
            $('#yearsOfExperience, #salaryMinFT, #salaryMaxFT').prop('required', true);
            $('#salaryMinPT, #salaryMaxPT').prop('required', false).val('');
        } else if (value === '2') {
            $('#partTimeFields').show();
            $('#salaryMinPT, #salaryMaxPT').prop('required', true);
            $('#yearsOfExperience, #salaryMinFT, #salaryMaxFT').prop('required', false).val('');
        } else if (value === '3') {
            $('#internshipFields').show();
            $('#duration, #stipend').prop('required', true);
            // Values are preserved from load
        }
    });

	let isEditingRequirement = false;

	$('#addRequirementBtn').on('click', function() {
	    const input = $('#requirementInput');
	    const value = input.val().trim();
	    if (!value) return;

	    if (isEditingRequirement) {
	        // Update the single requirement string in the list
	        $('#requirementsList li span').text(value);
	        isEditingRequirement = false;
	        $(this).text('Add');
	    } else {
	        // If no <li> yet, create it; else update existing
	        if ($('#requirementsList li').length === 0) {
	            const li = $(`
	                <li class="list-item">
	                    <span>${value}</span>
	                    <button type="button" class="btn btn-outline-violet edit-requirement-btn">Edit</button>
	                    <button type="button" class="btn btn-outline-violet remove-requirement-btn">Remove</button>
	                </li>`);
	            $('#requirementsList').append(li);
	        } else {
	            $('#requirementsList li span').text(value);
	        }
	    }

	    input.val('');
	    updateRequirements();
	    validateRequirements();
	});

	$(document).on('click', '.edit-requirement-btn', function() {
	    const text = $('#requirementsList li span').text().trim();
	    $('#requirementInput').val(text);
	    isEditingRequirement = true;
	    $('#addRequirementBtn').text('Update');
	});

	$(document).on('click', '.remove-requirement-btn', function() {
	    $('#requirementsList').empty();
	    $('#requirementInput').val('');
	    isEditingRequirement = false;
	    $('#addRequirementBtn').text('Add');
	    updateRequirements();
	    validateRequirements();
	});

	function updateRequirements() {
	    const text = $('#requirementsList li span').text().trim() || '';
	    $('#requirements').val(text);
	}

	

	// add edit benefit 
	let isEditing = false;
	$('#addBenefitBtn').on('click', function() {
	    const input = $('#benefitInput');
	    const value = input.val().trim();
	    if (!value) return;

	    if (isEditing) {
	        // Update the single benefit string in the list
	        $('#benefitsList li span').text(value);
	        isEditing = false;
	        $(this).text('Add');
	    } else {
	        // If no <li> yet, create it; else update existing
	        if ($('#benefitsList li').length === 0) {
	            const li = $(`<li class="list-item"><span>${value}</span><button type="button" class="btn btn-outline-violet edit-benefit-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-benefit-btn">Remove</button></li>`);
	            $('#benefitsList').append(li);
	        } else {
	            $('#benefitsList li span').text(value);
	        }
	    }
	    input.val('');
	    updateBenefits();
	});

	$(document).on('click', '.edit-benefit-btn', function() {
	    const text = $('#benefitsList li span').text().trim();
	    $('#benefitInput').val(text);
	    isEditing = true;
	    $('#addBenefitBtn').text('Update');
	});

	$(document).on('click', '.remove-benefit-btn', function() {
	    $('#benefitsList').empty();
	    $('#benefitInput').val('');
	    isEditing = false;
	    $('#addBenefitBtn').text('Add');
	    updateBenefits();
	});

	


	let isEditingRole = false;

	$('#addRoleBtn').on('click', function() {
	    const input = $('#roleInput');
	    const value = input.val().trim();
	    if (!value) return;

	    if (isEditingRole) {
	        // Update the single role string in the list
	        $('#rolesList li span').text(value);
	        isEditingRole = false;
	        $(this).text('Add');
	    } else {
	        // If no <li> yet, create it; else update existing
	        if ($('#rolesList li').length === 0) {
	            const li = $(`
	                <li class="list-item">
	                    <span>${value}</span>
	                    <button type="button" class="btn btn-outline-violet edit-role-btn">Edit</button>
	                    <button type="button" class="btn btn-outline-violet remove-role-btn">Remove</button>
	                </li>`);
	            $('#rolesList').append(li);
	        } else {
	            $('#rolesList li span').text(value);
	        }
	    }

	    input.val('');
	    updateResponsibilities();
	    validateRoles();
	});

	$(document).on('click', '.edit-role-btn', function() {
	    const text = $('#rolesList li span').text().trim();
	    $('#roleInput').val(text);
	    isEditingRole = true;
	    $('#addRoleBtn').text('Update');
	});

	$(document).on('click', '.remove-role-btn', function() {
	    $('#rolesList').empty();
	    $('#roleInput').val('');
	    isEditingRole = false;
	    $('#addRoleBtn').text('Add');
	    updateResponsibilities();
	    validateRoles();
	});

	function updateResponsibilities() {
	    const text = $('#rolesList li span').text().trim() || '';
	    $('#responsibilities').val(text);
	}


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
        if (jobTypeValue === '1') {
            $('#yearsOfExperience, #salaryMinFT, #salaryMaxFT').prop('required', true);
            $('#salaryMinPT, #salaryMaxPT').prop('required', false).val('');
            validations.push(validateExperienceYears());
            validations.push(validateSalaryRange('#salaryMinFT', '#salaryMaxFT'));
        } else if (jobTypeValue === '2') {
            $('#salaryMinPT, #salaryMaxPT').prop('required', true);
            $('#yearsOfExperience, #salaryMinFT, #salaryMaxFT').prop('required', false).val('');
            validations.push(validateSalaryRange('#salaryMinPT', '#salaryMaxPT'));
        } else if (jobTypeValue === '3') {
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
        if (isSubmitting) return;
        if (validateForm()) {
            console.log('Form is valid, submitting...');
            isSubmitting = true;
            updateRequirements();
            updateBenefits();
            updateResponsibilities();
            console.log('Submitting form data:', {
                responsibilities: $('#responsibilities').val(),
                requirements: $('#requirements').val(),
                benefits: $('#benefits').val(),
                duration: $('#duration').val(),
                stipend: $('#stipend').val()
            });
            form.off('submit');
            form.submit();
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
            $('#previewSalaryRangePT').text(`$${$('#salaryMinPT').val()} - $${$('#salaryMaxFT').val()}`);
        } else if (jobTypeValue === '3') {
            $('#previewInternshipFields').show();
            $('#previewDuration').text($('#duration').val() + ' months');
            $('#previewStipend').text(`$${$('#stipend').val()}`);
        }

        $('#previewOpenings').text($('#openings').val());
        $('#previewDeadline').text($('#deadline').val());
    }

    function updateRequirements() {
		const text = $('#requirementsList li span').text().trim() || '';
	    $('#requirements').val(text);
    }

	function updateBenefits() {
		    // Get the single benefit string or empty
		    const text = $('#benefitsList li span').text().trim() || '';
		    $('#benefits').val(text);
		}



    function updateResponsibilities() {
		const text = $('#rolesList li span').text().trim() || '';
	    $('#responsibilities').val(text);
		
    }

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