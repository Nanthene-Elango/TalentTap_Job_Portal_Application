$(document).ready(function() {
           // Initialize Select2
           $('#editSkills, #editLocations').select2({
               placeholder: 'Search and select',
               tags: true,
               maximumSelectionLength: 10,
               width: '100%'
           });

           const editForm = $('#editJobForm');
           const successToast = new bootstrap.Toast($('#successToast'));
           let editingRequirementIndex = null;
           let editingBenefitIndex = null;

           // Check for expired jobs and move them to Archived Jobs
           function checkExpiredJobs() {
               const today = new Date().toISOString().split('T')[0];
               $('#activeJobs .job-card').each(function() {
                   const $card = $(this);
                   const deadline = $card.data('deadline');
                   if (deadline && deadline < today) {
                       $card.fadeOut(300, function() {
                           $card.data('job-status', 'Expired').find('.status-badge')
                               .removeClass('status-open status-closing').addClass('status-expired')
                               .html('<i class="fas fa-circle"></i> Expired');
                           $card.find('.action-buttons').html(`
                               <button class="btn btn-outline-violet action-btn view-details" data-bs-toggle="modal" data-bs-target="#viewModal" data-job-id="${$card.data('job-id')}"><i class="fas fa-eye"></i> View</button>
                               <button class="btn btn-outline-success action-btn restore-job" data-job-id="${$card.data('job-id')}"><i class="fas fa-redo"></i> Restore</button>
                               <button class="btn btn-outline-danger action-btn delete-job-btn" data-bs-toggle="modal" data-bs-target="#deleteModal" data-job-id="${$card.data('job-id')}"><i class="fas fa-trash-alt"></i> Delete</button>
                           `);
                           $('#archivedJobs').append($card);
                           $card.fadeIn(300);
                           filterJobs();
                       });
                   }
               });
           }

           // Tab navigation
           $('.job-tab').click(function() {
               $('.job-tab').removeClass('active');
               $(this).addClass('active');
               $('.job-list-section').hide();
               $('#' + $(this).data('tab')).show();
               filterJobs();
           });

           // Filter functionality
           function filterJobs() {
               const searchTerm = $('#searchInput').val().toLowerCase();
               const statusFilter = $('#statusFilter').val();
               const typeFilter = $('#typeFilter').val();
               const categoryFilter = $('#categoryFilter').val();
               const activeTab = $('.job-tab.active').data('tab');
               let visibleCount = 0;

               $('#' + activeTab + ' .job-card').each(function() {
                   const jobTitle = $(this).find('.job-title').text().toLowerCase();
                   const jobLocation = $(this).find('.job-info').first().text().toLowerCase();
                   const jobType = $(this).data('job-type');
                   const jobStatus = $(this).data('job-status');
                   const jobCategory = $(this).data('job-category');

                   const matchesSearch = jobTitle.includes(searchTerm) || jobLocation.includes(searchTerm);
                   const matchesStatus = !statusFilter || jobStatus === statusFilter;
                   const matchesType = !typeFilter || jobType === typeFilter;
                   const matchesCategory = !categoryFilter || jobCategory === categoryFilter;

                   if (matchesSearch && matchesStatus && matchesType && matchesCategory) {
                       $(this).show();
                       visibleCount++;
                   } else {
                       $(this).hide();
                   }
               });

               $('#emptyState').toggle(visibleCount === 0);
           }

           $('#searchInput').on('input', filterJobs);
           $('#statusFilter, #typeFilter, #categoryFilter').on('change', filterJobs);
           $('#resetFilters').click(function() {
               $('#searchInput').val('');
               $('#statusFilter').val('');
               $('#typeFilter').val('');
               $('#categoryFilter').val('');
               filterJobs();
           });

           // Toggle job status
           $(document).on('click', '.toggle-status', function() {
               const $btn = $(this);
               const jobId = $btn.data('job-id');
               const currentStatus = $btn.data('current-status');
               const $card = $btn.closest('.job-card');
               const $badge = $card.find('.status-badge');

               if (currentStatus === 'Open') {
                   $btn.data('current-status', 'Closing');
                   $btn.html('<i class="fas fa-play-circle"></i> Set Open');
                   $badge.removeClass('status-open').addClass('status-closing').html('<i class="fas fa-circle"></i> Closing');
                   $card.data('job-status', 'Closing');
               } else {
                   $btn.data('current-status', 'Open');
                   $btn.html('<i class="fas fa-pause-circle"></i> Set Closing');
                   $badge.removeClass('status-closing').addClass('status-open').html('<i class="fas fa-circle"></i> Open');
                   $card.data('job-status', 'Open');
               }
               filterJobs();
           });

           // Restore job
           $(document).on('click', '.restore-job', function() {
               const $btn = $(this);
               const jobId = $btn.data('job-id');
               const $card = $btn.closest('.job-card');
               $card.fadeOut(300, function() {
                   $card.data('job-status', 'Open').find('.status-badge')
                       .removeClass('status-expired').addClass('status-open')
                       .html('<i class="fas fa-circle"></i> Open');
                   $card.find('.action-buttons').html(`
                       <button class="btn btn-outline-violet action-btn view-details" data-bs-toggle="modal" data-bs-target="#viewModal" data-job-id="${jobId}"><i class="fas fa-eye"></i> View</button>
                       <button class="btn btn-outline-violet action-btn edit-job" data-bs-toggle="modal" data-bs-target="#editModal" data-job-id="${jobId}"><i class="fas fa-edit"></i> Edit</button>
                       <button class="btn btn-outline-warning action-btn toggle-status" data-job-id="${jobId}" data-current-status="Open"><i class="fas fa-pause-circle"></i> Set Closing</button>
                       <button class="btn btn-outline-danger action-btn delete-job-btn" data-bs-toggle="modal" data-bs-target="#deleteModal" data-job-id="${jobId}"><i class="fas fa-trash-alt"></i> Delete</button>
                   `);
                   $('#activeJobs').append($card);
                   $card.fadeIn(300);
                   filterJobs();
               });
           });

           // View Job Modal Population
           $(document).on('click', '.view-details', function() {
               const jobId = $(this).data('job-id');
               const $card = $(this).closest('.job-card');

               // Populate view modal fields
               $('#viewJobRole').text($card.find('.job-title').text());
               $('#viewJobType').text($card.data('job-type'));
               $('#viewJobCategory').text($card.data('job-category'));
               $('#viewDescription').text($card.data('description') || '');
               const skills = JSON.parse($card.attr('data-skills') || '[]');
               $('#viewSkills').text(skills.join(', ') || 'None');
               const requirements = JSON.parse($card.attr('data-requirements') || '[]');
               $('#viewRequirementsList').empty();
               requirements.forEach(req => $('#viewRequirementsList').append(`<li class="list-item">${req}</li>`));
               const benefits = JSON.parse($card.attr('data-benefits') || '[]');
               $('#viewBenefitsList').empty();
               benefits.forEach(ben => $('#viewBenefitsList').append(`<li class="list-item">${ben}</li>`));
               $('#viewWorkType').text($card.data('job-type') === 'Remote' ? 'Remote' : 'On-site');
               const locations = JSON.parse($card.attr('data-locations') || '[]');
               $('#viewLocations').text(locations.join(', ') || 'None');
               
               // Employment type specific fields
               $('#viewFullTimeFields, #viewPartTimeFields, #viewInternshipFields').hide();
               if ($card.data('job-type') === 'Full-time') {
                   $('#viewFullTimeFields').show();
                   $('#viewExperienceYears').text($card.data('experience-years') || 'Not specified');
                   $('#viewSalaryMinFT').text($card.data('salary-min') || '0');
                   $('#viewSalaryMaxFT').text($card.data('salary-max') || '0');
               } else if ($card.data('job-type') === 'Part-time') {
                   $('#viewPartTimeFields').show();
                   $('#viewSalaryMinPT').text($card.data('salary-min') || '0');
                   $('#viewSalaryMaxPT').text($card.data('salary-max') || '0');
               } else if ($card.data('job-type') === 'Internship') {
                   $('#viewInternshipFields').show();
                   $('#viewDuration').text($card.data('duration') || 'Not specified');
                   $('#viewStipend').text($card.data('stipend') || '0');
               }
               
               $('#viewOpenings').text($card.data('openings') || '1');
               $('#viewDeadline').text(new Date($card.data('deadline')).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }));
               const appProcess = $card.data('application-email') ? 'Apply via Email' : 'Apply via TalentTap';
               $('#viewApplicationProcess').text(appProcess);
               $('#viewEmailApplicationFields').toggle(appProcess === 'Apply via Email');
               $('#viewApplicationEmail').text($card.data('application-email') || '');

               $('#viewModal').modal('show');
           });

           // Edit Job Modal Population and Validation
           $(document).on('click', '.edit-job', function() {
               const jobId = $(this).data('job-id');
               const $card = $(this).closest('.job-card');

               // Populate form fields
               $('#editJobId').val(jobId);
               $('#editJobRole').val($card.find('.job-title').text());
               $('#editJobType').val($card.data('job-type'));
               $('#editJobCategory').val($card.data('job-category'));
               $('#editDescription').val($card.data('description') || '');
               $('#editCharCount').text($('#editDescription').val().length);
               const skills = JSON.parse($card.attr('data-skills') || '[]');
               $('#editSkills').val(skills).trigger('change');
               const requirements = JSON.parse($card.attr('data-requirements') || '[]');
               $('#editRequirementsList').empty();
               requirements.forEach(req => {
                   $('#editRequirementsList').append(`<li class="list-item">${req}<button type="button" class="btn btn-outline-violet edit-requirement-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-requirement-btn">Remove</button></li>`);
               });
               const benefits = JSON.parse($card.attr('data-benefits') || '[]');
               $('#editBenefitsList').empty();
               benefits.forEach(ben => {
                   $('#editBenefitsList').append(`<li class="list-item">${ben}<button type="button" class="btn btn-outline-violet edit-benefit-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-benefit-btn">Remove</button></li>`);
               });
               $('#editWorkType').val($card.data('job-type') === 'Remote' ? 'Remote' : 'On-site');
               const locations = JSON.parse($card.attr('data-locations') || '[]');
               $('#editLocations').val(locations).trigger('change');
               if ($card.data('job-type') === 'Full-time') {
                   $('#editExperienceYears').val($card.data('experience-years'));
                   $('#editSalaryMinFT').val($card.data('salary-min'));
                   $('#editSalaryMaxFT').val($card.data('salary-max'));
               } else if ($card.data('job-type') === 'Part-time') {
                   $('#editSalaryMinPT').val($card.data('salary-min'));
                   $('#editSalaryMaxPT').val($card.data('salary-max'));
               } else if ($card.data('job-type') === 'Internship') {
                   $('#editDuration').val($card.data('duration'));
                   $('#editStipend').val($card.data('stipend'));
               }
               $('#editOpenings').val($card.data('openings') || 1);
               $('#editDeadline').val($card.data('deadline'));
               $('#editApplicationProcess').val($card.data('application-email') ? 'Apply via Email' : 'Apply via TalentTap');
               $('#editApplicationEmail').val($card.data('application-email') || '');

               // Trigger change events to update UI
               $('#editJobType').trigger('change');
               $('#editApplicationProcess').trigger('change');
               $('#editModal').modal('show');
           });

           // Dynamic Validation Functions
           function validateJobRole() {
               const jobRole = $('#editJobRole');
               const value = jobRole.val().trim();
               return value.length >= 3 ? (jobRole.removeClass('is-invalid').addClass('is-valid'), true) : (jobRole.removeClass('is-valid').addClass('is-invalid'), false);
           }

           function validateJobType() {
               const jobType = $('#editJobType');
               const value = jobType.val();
               return value ? (jobType.removeClass('is-invalid').addClass('is-valid'), true) : (jobType.removeClass('is-valid').addClass('is-invalid'), false);
           }

           function validateJobCategory() {
               const jobCategory = $('#editJobCategory');
               const value = jobCategory.val();
               return value ? (jobCategory.removeClass('is-invalid').addClass('is-valid'), true) : (jobCategory.removeClass('is-valid').addClass('is-invalid'), false);
           }

           function validateDescription() {
               const description = $('#editDescription');
               const value = description.val().trim();
               const count = value.length;
               $('#editCharCount').text(count);
               return (count >= 100 && count <= 3000) ? (description.removeClass('is-invalid').addClass('is-valid'), true) : (description.removeClass('is-valid').addClass('is-invalid'), false);
           }

           function validateSkills() {
               const skills = $('#editSkills');
               const value = skills.val();
               return (value && value.length > 0) ? (skills.removeClass('is-invalid').addClass('is-valid'), true) : (skills.removeClass('is-valid').addClass('is-invalid'), false);
           }

           function validateRequirements() {
               const requirementsList = $('#editRequirementsList');
               const items = requirementsList.find('li').length;
               return items > 0 ? (requirementsList.next('.input-group').next('.invalid-feedback').hide(), requirementsList.next('.input-group').next('.valid-feedback').show(), true) : (requirementsList.next('.input-group').next('.invalid-feedback').show(), requirementsList.next('.input-group').next('.valid-feedback').hide(), false);
           }

           function validateWorkType() {
               const workType = $('#editWorkType');
               const value = workType.val();
               return value ? (workType.removeClass('is-invalid').addClass('is-valid'), true) : (workType.removeClass('is-valid').addClass('is-invalid'), false);
           }

           function validateLocations() {
               const locations = $('#editLocations');
               const value = locations.val();
               return (value && value.length > 0) ? (locations.removeClass('is-invalid').addClass('is-valid'), true) : (locations.removeClass('is-valid').addClass('is-invalid'), false);
           }

           function validateExperienceYears() {
               const experienceYears = $('#editExperienceYears');
               const value = experienceYears.val().trim();
               const regex = /^(0|[0-9]+(-[0-9]+)?)$/;
               return regex.test(value) ? (experienceYears.removeClass('is-invalid').addClass('is-valid'), true) : (experienceYears.removeClass('is-valid').addClass('is-invalid'), false);
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
               const duration = $('#editDuration');
               const value = parseInt(duration.val());
               return (!isNaN(value) && value >= 1 && value <= 24) ? (duration.removeClass('is-invalid').addClass('is-valid'), true) : (duration.removeClass('is-valid').addClass('is-invalid'), false);
           }

           function validateStipend() {
               const stipend = $('#editStipend');
               const value = parseFloat(stipend.val());
               return (!isNaN(value) && value >= 0) ? (stipend.removeClass('is-invalid').addClass('is-valid'), true) : (stipend.removeClass('is-valid').addClass('is-invalid'), false);
           }

           function validateOpenings() {
               const openings = $('#editOpenings');
               const value = parseInt(openings.val());
               return (!isNaN(value) && value >= 1) ? (openings.removeClass('is-invalid').addClass('is-valid'), true) : (openings.removeClass('is-valid').addClass('is-invalid'), false);
           }

           function validateDeadline() {
               const deadline = $('#editDeadline');
               const value = deadline.val();
               const today = new Date().toISOString().split('T')[0];
               return (value && value >= today) ? (deadline.removeClass('is-invalid').addClass('is-valid'), true) : (deadline.removeClass('is-valid').addClass('is-invalid'), false);
           }

           function validateApplicationProcess() {
               const applicationProcess = $('#editApplicationProcess');
               const value = applicationProcess.val();
               return value ? (applicationProcess.removeClass('is-invalid').addClass('is-valid'), true) : (applicationProcess.removeClass('is-valid').addClass('is-invalid'), false);
           }

           function validateApplicationEmail() {
               const applicationEmail = $('#editApplicationEmail');
               const value = applicationEmail.val().trim();
               const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
               return ($('#editApplicationProcess').val() !== 'Apply via Email' || emailRegex.test(value)) ? (applicationEmail.removeClass('is-invalid').addClass('is-valid'), true) : (applicationEmail.removeClass('is-valid').addClass('is-invalid'), false);
           }

           // Attach event listeners for dynamic validation
           $('#editJobRole').on('input blur', validateJobRole);
           $('#editJobType').on('change blur', validateJobType);
           $('#editJobCategory').on('change blur', validateJobCategory);
           $('#editDescription').on('input blur', validateDescription);
           $('#editSkills').on('change blur', validateSkills);
           $('#editRequirementsList').on('DOMSubtreeModified', validateRequirements);
           $('#editWorkType').on('change blur', validateWorkType);
           $('#editLocations').on('change blur', validateLocations);
           $('#editExperienceYears').on('input blur', validateExperienceYears);
           $('#editSalaryMinFT, #editSalaryMaxFT').on('input blur', () => validateSalaryRange('#editSalaryMinFT', '#editSalaryMaxFT'));
           $('#editSalaryMinPT, #editSalaryMaxPT').on('input blur', () => validateSalaryRange('#editSalaryMinPT', '#editSalaryMaxPT'));
           $('#editDuration').on('input blur', validateDuration);
           $('#editStipend').on('input blur', validateStipend);
           $('#editOpenings').on('input blur', validateOpenings);
           $('#editDeadline').on('change blur', validateDeadline);
           $('#editApplicationProcess').on('change blur', validateApplicationProcess);
           $('#editApplicationEmail').on('input blur', validateApplicationEmail);

           // Employment type toggle
           $('#editJobType').on('change', function() {
               const value = $(this).val();
               $('#editFullTimeFields, #editPartTimeFields, #editInternshipFields').hide();
               if (value === 'Full-time') {
                   $('#editFullTimeFields').show();
                   $('#editExperienceYears, #editSalaryMinFT, #editSalaryMaxFT').prop('required', true);
                   $('#editSalaryMinPT, #editSalaryMaxPT, #editDuration, #editStipend').prop('required', false);
               } else if (value === 'Part-time') {
                   $('#editPartTimeFields').show();
                   $('#editSalaryMinPT, #editSalaryMaxPT').prop('required', true);
                   $('#editExperienceYears, #editSalaryMinFT, #editSalaryMaxFT, #editDuration, #editStipend').prop('required', false);
               } else if (value === 'Internship') {
                   $('#editInternshipFields').show();
                   $('#editDuration, #editStipend').prop('required', true);
                   $('#editExperienceYears, #editSalaryMinFT, #editSalaryMaxFT, #editSalaryMinPT, #editSalaryMaxPT').prop('required', false);
               } else {
                   $('#editExperienceYears, #editSalaryMinFT, #editSalaryMaxFT, #editSalaryMinPT, #editSalaryMaxPT, #editDuration, #editStipend').prop('required', false);
               }
           });

           // Application process toggle
           $('#editApplicationProcess').on('change', function() {
               const showEmail = $(this).val() === 'Apply via Email';
               $('#editEmailApplicationFields').toggle(showEmail);
               $('#editApplicationEmail').prop('required', showEmail);
               validateApplicationEmail();
           });

           // Add/Edit Requirement
           $('#editAddRequirementBtn').on('click', function() {
               const input = $('#editRequirementInput');
               const value = input.val().trim();
               if (!value) return;

               if (editingRequirementIndex !== null) {
                   $(`#editRequirementsList li:eq(${editingRequirementIndex})`).html(`${value}<button type="button" class="btn btn-outline-violet edit-requirement-btn">
                                       edit-requirement-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-requirement-btn">Remove</button>`);
                   editingRequirementIndex = null;
               } else {
                   $('#editRequirementsList').append(`<li class="list-item">${value}<button type="button" class="btn btn-outline-violet edit-requirement-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-requirement-btn">Remove</button></li>`);
               }
               input.val('');
               validateRequirements();
           });

           // Edit Requirement
           $(document).on('click', '.edit-requirement-btn', function() {
               const $li = $(this).closest('li');
               editingRequirementIndex = $li.index();
               const currentText = $li.text().replace('EditRemove', '').trim();
               $('#editRequirementInput').val(currentText);
           });

           // Remove Requirement
           $(document).on('click', '.remove-requirement-btn', function() {
               $(this).closest('li').remove();
               validateRequirements();
           });

           // Add/Edit Benefit
           $('#editAddBenefitBtn').on('click', function() {
               const input = $('#editBenefitInput');
               const value = input.val().trim();
               if (!value) return;

               if (editingBenefitIndex !== null) {
                   $(`#editBenefitsList li:eq(${editingBenefitIndex})`).html(`${value}<button type="button" class="btn btn-outline-violet edit-benefit-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-benefit-btn">Remove</button>`);
                   editingBenefitIndex = null;
               } else {
                   $('#editBenefitsList').append(`<li class="list-item">${value}<button type="button" class="btn btn-outline-violet edit-benefit-btn">Edit</button><button type="button" class="btn btn-outline-violet remove-benefit-btn">Remove</button></li>`);
               }
               input.val('');
           });

           // Edit Benefit
           $(document).on('click', '.edit-benefit-btn', function() {
               const $li = $(this).closest('li');
               editingBenefitIndex = $li.index();
               const currentText = $li.text().replace('EditRemove', '').trim();
               $('#editBenefitInput').val(currentText);
           });

           // Remove Benefit
           $(document).on('click', '.remove-benefit-btn', function() {
               $(this).closest('li').remove();
           });

           // Form Submission for Edit Job
           editForm.on('submit', function(e) {
               e.preventDefault();

               // Run all validations
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
                   validateDeadline(),
                   validateApplicationProcess(),
                   validateApplicationEmail()
               ];

               const jobType = $('#editJobType').val();
               if (jobType === 'Full-time') {
                   validations.push(validateExperienceYears());
                   validations.push(validateSalaryRange('#editSalaryMinFT', '#editSalaryMaxFT'));
               } else if (jobType === 'Part-time') {
                   validations.push(validateSalaryRange('#editSalaryMinPT', '#editSalaryMaxPT'));
               } else if (jobType === 'Internship') {
                   validations.push(validateDuration());
                   validations.push(validateStipend());
               }

               if (!validations.every(Boolean)) {
                   Swal.fire({
                       icon: 'error',
                       title: 'Validation Error',
                       text: 'Please correct the errors in the form before submitting.',
                   });
                   return;
               }

               // Update job card data
               const jobId = $('#editJobId').val();
               const $card = $(`.job-card[data-job-id="${jobId}"]`);
               $card.find('.job-title').text($('#editJobRole').val());
               $card.data('job-type', $('#editJobType').val());
               $card.find('.job-info:eq(1)').text(`<i class="fas fa-briefcase"></i> ${$('#editJobType').val()}`);
               $card.data('job-category', $('#editJobCategory').val());
               $card.data('description', $('#editDescription').val());
               $card.attr('data-skills', JSON.stringify($('#editSkills').val() || []));
               const requirements = $('#editRequirementsList li').map((_, el) => $(el).text().replace('EditRemove', '').trim()).get();
               $card.attr('data-requirements', JSON.stringify(requirements));
               const benefits = $('#editBenefitsList li').map((_, el) => $(el).text().replace('EditRemove', '').trim()).get();
               $card.attr('data-benefits', JSON.stringify(benefits));
               $card.data('work-type', $('#editWorkType').val());
               const locations = $('#editLocations').val() || [];
               $card.attr('data-locations', JSON.stringify(locations));
               $card.find('.job-info:eq(0)').html(`<i class="fas fa-map-marker-alt"></i> ${locations.join(', ')}`);
               if (jobType === 'Full-time') {
                   $card.data('experience-years', $('#editExperienceYears').val());
                   $card.data('salary-min', $('#editSalaryMinFT').val());
                   $card.data('salary-max', $('#editSalaryMaxFT').val());
               } else if (jobType === 'Part-time') {
                   $card.data('salary-min', $('#editSalaryMinPT').val());
                   $card.data('salary-max', $('#editSalaryMaxPT').val());
               } else if (jobType === 'Internship') {
                   $card.data('duration', $('#editDuration').val());
                   $card.data('stipend', $('#editStipend').val());
               }
               $card.data('openings', $('#editOpenings').val());
               $card.data('deadline', $('#editDeadline').val());
               $card.find('.job-info:eq(3)').html(`<i class="fas fa-calendar-alt"></i> Expires: ${new Date($('#editDeadline').val()).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })}`);
               if ($('#editApplicationProcess').val() === 'Apply via Email') {
                   $card.data('application-email', $('#editApplicationEmail').val());
               } else {
                   $card.removeData('application-email');
               }

               $('#editModal').modal('hide');
               successToast.show();
               filterJobs();
           });

           // Delete Job Modal
           $(document).on('click', '.delete-job-btn', function() {
               const jobId = $(this).data('job-id');
               const $card = $(this).closest('.job-card');
               $('#deleteJobId').val(jobId);
               $('#deleteJobTitle').text($card.find('.job-title').text());
           });

           $('#deleteReason').on('change', function() {
               const showOther = $(this).val() === 'Other';
               $('#deleteOtherReasonField').toggle(showOther);
               $('#deleteOtherReason').prop('required', showOther);
           });

           $('#confirmDelete').on('click', function() {
               const jobId = $('#deleteJobId').val();
               const reason = $('#deleteReason').val();
               const otherReason = $('#deleteOtherReason').val().trim();

               if (!reason || (reason === 'Other' && !otherReason)) {
                   Swal.fire({
                       icon: 'error',
                       title: 'Validation Error',
                       text: 'Please provide a reason for deletion.',
                   });
                   return;
               }

               $(`.job-card[data-job-id="${jobId}"]`).fadeOut(300, function() {
                   $(this).remove();
                   filterJobs();
                   $('#deleteModal').modal('hide');
                   Swal.fire({
                       icon: 'success',
                       title: 'Job Deleted',
                       text: 'The job has been successfully deleted.',
                       timer: 2000,
                       showConfirmButton: false
                   });
               });
           });

           // Initialize
           checkExpiredJobs();
           filterJobs();
       });