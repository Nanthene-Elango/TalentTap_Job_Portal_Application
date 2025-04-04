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

            // Dynamic Validation Functions
            function validateJobRole() {
                const jobRole = $('#jobRole');
                const value = jobRole.val().trim();
                if (value.length < 3) {
                    jobRole.removeClass('is-valid').addClass('is-invalid');
                    return false;
                } else {
                    jobRole.removeClass('is-invalid').addClass('is-valid');
                    return true;
                }
            }

            function validateJobType() {
                const jobType = $('#jobType');
                const value = jobType.val();
                if (!value) {
                    jobType.removeClass('is-valid').addClass('is-invalid');
                    return false;
                } else {
                    jobType.removeClass('is-invalid').addClass('is-valid');
                    return true;
                }
            }

            function validateJobCategory() {
                const jobCategory = $('#jobCategory');
                const value = jobCategory.val();
                if (!value) {
                    jobCategory.removeClass('is-valid').addClass('is-invalid');
                    return false;
                } else {
                    jobCategory.removeClass('is-invalid').addClass('is-valid');
                    return true;
                }
            }

            function validateDescription() {
                const description = $('#description');
                const value = description.val().trim();
                const count = value.length;
                $('#charCount').text(count);
                if (count < 100 || count > 3000) {
                    description.removeClass('is-valid').addClass('is-invalid');
                    return false;
                } else {
                    description.removeClass('is-invalid').addClass('is-valid');
                    return true;
                }
            }

            function validateSkills() {
                const skills = $('#skills');
                const value = skills.val();
                if (!value || value.length === 0) {
                    skills.removeClass('is-valid').addClass('is-invalid');
                    return false;
                } else {
                    skills.removeClass('is-invalid').addClass('is-valid');
                    return true;
                }
            }

            function validateRequirements() {
                const requirementsList = $('#requirementsList');
                const items = requirementsList.find('li').length;
                if (items === 0) {
                    requirementsList.next('.input-group').next('.invalid-feedback').show();
                    requirementsList.next('.input-group').next('.valid-feedback').hide();
                    return false;
                } else {
                    requirementsList.next('.input-group').next('.invalid-feedback').hide();
                    requirementsList.next('.input-group').next('.valid-feedback').show();
                    return true;
                }
            }

            function validateWorkType() {
                const workType = $('#workType');
                const value = workType.val();
                if (!value) {
                    workType.removeClass('is-valid').addClass('is-invalid');
                    return false;
                } else {
                    workType.removeClass('is-invalid').addClass('is-valid');
                    return true;
                }
            }

            function validateLocations() {
                const locations = $('#locations');
                const value = locations.val();
                if (!value || value.length === 0) {
                    locations.removeClass('is-valid').addClass('is-invalid');
                    return false;
                } else {
                    locations.removeClass('is-invalid').addClass('is-valid');
                    return true;
                }
            }

            function validateExperienceYears() {
                const experienceYears = $('#experienceYears');
                const value = experienceYears.val().trim();
                const regex = /^(0|[0-9]+(-[0-9]+)?)$/;
                if (!regex.test(value)) {
                    experienceYears.removeClass('is-valid').addClass('is-invalid');
                    return false;
                } else {
                    experienceYears.removeClass('is-invalid').addClass('is-valid');
                    return true;
                }
            }

            function validateSalaryRange(minField, maxField) {
                const min = $(minField);
                const max = $(maxField);
                const minValue = parseFloat(min.val());
                const maxValue = parseFloat(max.val());
                let isValid = true;

                if (isNaN(minValue) || minValue < 0) {
                    min.removeClass('is-valid').addClass('is-invalid');
                    isValid = false;
                } else {
                    min.removeClass('is-invalid').addClass('is-valid');
                }

                if (isNaN(maxValue) || maxValue < 0 || maxValue <= minValue) {
                    max.removeClass('is-valid').addClass('is-invalid');
                    isValid = false;
                } else {
                    max.removeClass('is-invalid').addClass('is-valid');
                }

                return isValid;
            }

            function validateDuration() {
                const duration = $('#duration');
                const value = parseInt(duration.val());
                if (isNaN(value) || value < 1 || value > 24) {
                    duration.removeClass('is-valid').addClass('is-invalid');
                    return false;
                } else {
                    duration.removeClass('is-invalid').addClass('is-valid');
                    return true;
                }
            }

            function validateStipend() {
                const stipend = $('#stipend');
                const value = parseFloat(stipend.val());
                if (isNaN(value) || value < 0) {
                    stipend.removeClass('is-valid').addClass('is-invalid');
                    return false;
                } else {
                    stipend.removeClass('is-invalid').addClass('is-valid');
                    return true;
                }
            }

            function validateOpenings() {
                const openings = $('#openings');
                const value = parseInt(openings.val());
                if (isNaN(value) || value < 1) {
                    openings.removeClass('is-valid').addClass('is-invalid');
                    return false;
                } else {
                    openings.removeClass('is-invalid').addClass('is-valid');
                    return true;
                }
            }

            function validateDeadline() {
                const deadline = $('#deadline');
                const value = deadline.val();
                const today = new Date().toISOString().split('T')[0];
                if (!value || value < today) {
                    deadline.removeClass('is-valid').addClass('is-invalid');
                    return false;
                } else {
                    deadline.removeClass('is-invalid').addClass('is-valid');
                    return true;
                }
            }

            // Attach event listeners for dynamic validation
            $('#jobRole').on('input blur', validateJobRole);
            $('#jobType').on('change blur', validateJobType);
            $('#jobCategory').on('change blur', validateJobCategory);
            $('#description').on('input blur', validateDescription);
            $('#skills').on('change blur', validateSkills);
            $('#workType').on('change blur', validateWorkType);
            $('#locations').on('change blur', validateLocations);
            $('#experienceYears').on('input blur', validateExperienceYears);
            $('#salaryMinFT, #salaryMaxFT').on('input blur', () => validateSalaryRange('#salaryMinFT', '#salaryMaxFT'));
            $('#salaryMinPT, #salaryMaxPT').on('input blur', () => validateSalaryRange('#salaryMinPT', '#salaryMaxPT'));
            $('#duration').on('input blur', validateDuration);
            $('#stipend').on('input blur', validateStipend);
            $('#openings').on('input blur', validateOpenings);
            $('#deadline').on('change blur', validateDeadline);

            $('#requirementsList').on('DOMSubtreeModified', validateRequirements);

            // Employment type toggle
            $('#jobType').on('change', function() {
                const value = $(this).val();
                $('#fullTimeFields, #partTimeFields, #internshipFields').hide();
                if (value === 'Full-time') {
                    $('#fullTimeFields').show();
                    $('#experienceYears, #salaryMinFT, #salaryMaxFT').prop('required', true);
                    $('#salaryMinPT, #salaryMaxPT, #duration, #stipend').prop('required', false);
                } else if (value === 'Part-time') {
                    $('#partTimeFields').show();
                    $('#salaryMinPT, #salaryMaxPT').prop('required', true);
                    $('#experienceYears, #salaryMinFT, #salaryMaxFT, #duration, #stipend').prop('required', false);
                } else if (value === 'Internship') {
                    $('#internshipFields').show();
                    $('#duration, #stipend').prop('required', true);
                    $('#experienceYears, #salaryMinFT, #salaryMaxFT, #salaryMinPT, #salaryMaxPT').prop('required', false);
                }
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
                    const li = $(`<li class="list-item">${value}<button type="button" class="btn btn-outline-violet edit-requirement-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-requirement-btn">Remove</button></li>`);
                    $('#requirementsList').append(li);
                }
                input.val('');
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
                    const li = $(`<li class="list-item">${value}<button type="button" class="btn btn-outline-violet edit-benefit-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-benefit-btn">Remove</button></li>`);
                    $('#benefitsList').append(li);
                }
                input.val('');
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
                    validateWorkType(),
                    validateLocations(),
                    validateOpenings(),
                    validateDeadline()
                ];

                if ($('#jobType').val() === 'Full-time') {
                    validations.push(validateExperienceYears());
                    validations.push(validateSalaryRange('#salaryMinFT', '#salaryMaxFT'));
                } else if ($('#jobType').val() === 'Part-time') {
                    validations.push(validateSalaryRange('#salaryMinPT', '#salaryMaxPT'));
                } else if ($('#jobType').val() === 'Internship') {
                    validations.push(validateDuration());
                    validations.push(validateStipend());
                }

                return validations.every(v => v);
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
                        text: 'Please fill all required fields correctly.',
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
                Swal.fire({
                    icon: 'success',
                    title: 'Draft Saved',
                    text: 'Job draft saved successfully!',
                    confirmButtonColor: '#5e17eb'
                });
            });

            // Handle form submission with confirmation
            function handleSubmission() {
                if (validateForm()) {
                    Swal.fire({
                        title: 'Confirm Posting',
                        text: 'Are you sure you want to post this job?',
                        icon: 'question',
                        showCancelButton: true,
                        confirmButtonColor: '#5e17eb',
                        cancelButtonColor: '#dc3545',
                        confirmButtonText: 'Yes, Post It',
                        cancelButtonText: 'No, Cancel'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            Swal.fire({
                                title: 'Success!',
                                text: 'Job posted successfully!',
                                icon: 'success',
                                confirmButtonColor: '#5e17eb',
                                timer: 1500,
                                timerProgressBar: true
                            }).then(() => {
                                window.location.href = 'posted-jobs.html';
                            });
                        }
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Validation Error',
                        text: 'Please fill all required fields correctly.',
                        confirmButtonColor: '#5e17eb'
                    });
                }
            }

            form.on('submit', function(e) {
                e.preventDefault();
                handleSubmission();
            });

            $('#submitFromPreviewBtn').on('click', function() {
                Swal.fire({
                    title: 'Confirm Posting',
                    text: 'Are you sure you want to post this job?',
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonColor: '#5e17eb',
                    cancelButtonColor: '#dc3545',
                    confirmButtonText: 'Yes, Post It',
                    cancelButtonText: 'No, Cancel'
                }).then((result) => {
                    if (result.isConfirmed) {
                        Swal.fire({
                            title: 'Success!',
                            text: 'Job posted successfully!',
                            icon: 'success',
                            confirmButtonColor: '#5e17eb',
                            timer: 1500,
                            timerProgressBar: true
                        }).then(() => {
                            window.location.href = 'posted-jobs.html';
                        });
                    }
                });
            });

            function populatePreview() {
                $('#previewJobRole').text($('#jobRole').val());
                $('#previewJobType').text($('#jobType').val());
                $('#previewJobCategory').text($('#jobCategory').val());
                $('#previewDescription').text($('#description').val());
                $('#previewSkills').html($('#skills').val().map(skill => `<span class="skill-badge">${skill}</span>`).join(''));
                $('#previewRequirements').html($('#requirementsList li').map((_, li) => `<span class="skill-badge">${$(li).contents().filter(function() { return this.nodeType === 3; }).text().trim()}</span>`).get().join(''));
                $('#previewBenefits').html($('#benefitsList li').length ? $('#benefitsList li').map((_, li) => `<span class="skill-badge">${$(li).contents().filter(function() { return this.nodeType === 3; }).text().trim()}</span>`).get().join('') : 'None');
                $('#previewWorkType').text($('#workType').val());
                $('#previewLocations').text($('#locations').val().join(', '));
                
                $('#previewFullTimeFields, #previewPartTimeFields, #previewInternshipFields').hide();
                if ($('#jobType').val() === 'Full-time') {
                    $('#previewFullTimeFields').show();
                    $('#previewExperienceYears').text($('#experienceYears').val() + ' years');
                    $('#previewSalaryRangeFT').text(`$${$('#salaryMinFT').val()} - $${$('#salaryMaxFT').val()}`);
                } else if ($('#jobType').val() === 'Part-time') {
                    $('#previewPartTimeFields').show();
                    $('#previewSalaryRangePT').text(`$${$('#salaryMinPT').val()} - $${$('#salaryMaxPT').val()}`);
                } else if ($('#jobType').val() === 'Internship') {
                    $('#previewInternshipFields').show();
                    $('#previewDuration').text($('#duration').val() + ' months');
                    $('#previewStipend').text(`$${$('#stipend').val()}`);
                }

                $('#previewOpenings').text($('#openings').val());
                $('#previewDeadline').text($('#deadline').val());
            }
        });