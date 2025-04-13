$(document).ready(function() {
	let currentStep = 1;
	let emailVerified = false;
	let phoneVerified = false;
	let phoneResendTimer = 30;
	let emailResendTimer = 30;
	let phoneTimerInterval, emailTimerInterval;
	let verifiedPhoneNumber = null;
	let verifiedEmail = null;
	let phoneAttempts = 0;
	let emailAttempts = 0;

	new Choices('.choices-multiple', {
		removeItemButton: true,
		placeholder: true,
		placeholderValue: 'Select skills',
		searchEnabled: true
	});

	const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&*])[A-Za-z\d@#$%^&*]{8,}$/;
	const fullNameRegex = /^[A-Za-z\s]+$/;
	const usernameRegex = /^[A-Za-z0-9]+$/;
	const phoneRegex = /^\d{10}$/;
	const textOnlyRegex = /^[A-Za-z\s]+$/;



	const currentYear = new Date().getFullYear();
	for (let year = currentYear; year >= 1970; year--) {
		$('#startYear').append(`<option value="${year}">${year}</option>`);
	}

	function showToast(message) {
		$('#toast').text(message).addClass('show');
		setTimeout(() => $('#toast').removeClass('show'), 3000);
	}

	function showError(element, message) {
		$(`#${element}Error`).text(message).addClass('visible');
		$(`#${element}`).addClass('error');
	}

	function clearError(element) {
		$(`#${element}Error`).text('').removeClass('visible');
		$(`#${element}`).removeClass('error');
	}

	function validateFullNameDynamic() {
		const val = $('#fullName').val().trim();
		if (!val) showError('fullName', 'Full name is required');
		else if (!fullNameRegex.test(val)) showError('fullName', 'Only letters and spaces allowed');
		else clearError('fullName');
	}

	function validateUsernameDynamic() {
		const val = $('#username').val().trim();
		if (!val) showError('username', 'Username is required');
		else if (!usernameRegex.test(val)) showError('username', 'Only letters and numbers allowed');
		else clearError('username');
	}

	function validatePasswordDynamic() {
		const val = $('#password').val();
		if (!val) showError('password', 'Password is required');
		else if (!passwordRegex.test(val)) showError('password', 'Must be 8+ chars with uppercase, lowercase, number, and special char');
		else clearError('password');
	}

	function validatePhoneNumberDynamic() {
		const val = $('#phoneNumber').val().trim();
		if (!val) showError('phoneNumber', 'Phone number is required');
		else if (!phoneRegex.test(val)) showError('phoneNumber', 'Must be exactly 10 digits');
		else clearError('phoneNumber');
		return phoneRegex.test(val);
	}

	function validateEmailDynamic() {
		const val = $('#email').val().trim();
		if (!val) showError('email', 'Email is required');
		else if (!emailRegex.test(val)) showError('email', 'Invalid email format');
		else clearError('email');
		return emailRegex.test(val);
	}

	function validateBoardOfStudyDynamic() {
		const val = $('#boardOfStudy').val().trim();
		if ($('#boardOfStudyGroup').is(':visible')) {
			if (!val) showError('boardOfStudy', 'Board of study is required');
			else if (!textOnlyRegex.test(val)) showError('boardOfStudy', 'Only letters and spaces allowed');
			else clearError('boardOfStudy');
		}
	}

	function validateUniversityDynamic() {
		const val = $('#university').val().trim();
		if (!val) showError('university', 'University is required');
		else if (!textOnlyRegex.test(val)) showError('university', 'Only letters and spaces allowed');
		else clearError('university');
	}

	function validatePercentageDynamic() {
		const val = $('#percentage').val();
		if (!val) {
			showError('percentage', 'Percentage is required');
		} else if (val < 0 || val > 100) {
			showError('percentage', 'Must be between 0 and 100');
		} else {
			clearError('percentage');
		}
	}

	function validateBasicDetails() {
		validateFullNameDynamic();
		validateUsernameDynamic();
		validatePasswordDynamic();
		return !$('#fullName').hasClass('error') &&
			!$('#username').hasClass('error') &&
			!$('#password').hasClass('error');
	}

	function validatePhoneNumber() {
		validatePhoneNumberDynamic();
		return !$('#phoneNumber').hasClass('error');
	}

	function validateEmail() {
		validateEmailDynamic();
		return !$('#email').hasClass('error');
	}

	function validateProfessionalDetails() {
		const years = $('#yearsOfExperience').val();
		const location = $('#location').val();
		let isValid = true;

		if (!years || years < 0) {
			showError('yearsOfExperience', years ? 'Must be 0 or greater' : 'Required');
			isValid = false;
		} else clearError('yearsOfExperience');

		if (!location) {
			showError('location', 'Please select a location');
			isValid = false;
		} else clearError('location');

		return isValid;
	}

	function validateEducationDetails() {
		let isValid = true;
		const qual = $('#highestQualification').val();
		const degree = $('#degree').val()?.trim();
		const startYear = $('#startYear').val();
		const endYear = $('#endYear').val();

		if (!qual) {
			showError('highestQualification', 'Please select a qualification');
			isValid = false;
		} else {
			clearError('highestQualification');
			if (qual === 'ug' || qual === 'pg') {
				if (!degree) {
					showError('degree', 'Degree is required');
					isValid = false;
				} else clearError('degree');
			}
		}

		validateBoardOfStudyDynamic();
		validateUniversityDynamic();
		validatePercentageDynamic();

		if (!startYear) {
			showError('startYear', 'Please select a starting year');
			isValid = false;
		} else clearError('startYear');

		if (!endYear) {
			showError('endYear', 'Please select an end year');
			isValid = false;
		} else if (parseInt(endYear) < parseInt(startYear)) {
			showError('endYear', 'End year must be after start year');
			isValid = false;
		} else clearError('endYear');

		return isValid &&
			!$('#boardOfStudy').hasClass('error') &&
			!$('#university').hasClass('error') &&
			!$('#percentage').hasClass('error');
	}

	function validateKeySkills() {
		if (selectedSkills.length === 0) {
			showError('keySkills', 'Please select at least one skill');
			return false;
		}
		clearError('keySkills');
		return true;
	}

	function disableOTPInputs(type) {
		const otpInputs = type === 'email'
			? ['otp1', 'otp2', 'otp3', 'otp4', 'otp5', 'otp6']
			: ['phoneOtp1', 'phoneOtp2', 'phoneOtp3', 'phoneOtp4', 'phoneOtp5', 'phoneOtp6'];
		otpInputs.forEach(id => $(`#${id}`).prop('disabled', true).removeClass('error'));
	}

	function resetVerification(type) {
		if (type === 'phone') {
			phoneVerified = false;
			verifiedPhoneNumber = null;
			$('#phoneNumber').prop('readonly', false);
			$('#phoneOtpContainer input').val('').prop('disabled', false);
			$('#phoneOtpContainer, #phoneResendContainer, #phoneNextBtn, #changePhoneBtn, #phoneSkipBtn').hide();
			$('#phoneVerifyBtn').show();
			$('#phoneStatus').removeClass('verified').text('');
			$('#phoneOtpMessage').hide();
			clearInterval(phoneTimerInterval);
			phoneAttempts = 0;
		} else {
			emailVerified = false;
			verifiedEmail = null;
			$('#email').prop('readonly', false);
			$('#emailOtpContainer input').val('').prop('disabled', false);
			$('#emailOtpContainer, #emailResendContainer, #emailNextBtn, #changeEmailBtn, #emailSkipBtn').hide();
			$('#emailVerifyBtn').show();
			$('#emailStatus').removeClass('verified').text('');
			$('#emailOtpMessage').hide();
			clearInterval(emailTimerInterval);
			emailAttempts = 0;
		}
	}

	function updateProgressBar() {
		const progressPercentage = (currentStep / 7) * 100;
		$('#progressBarFill').css('width', `${progressPercentage}%`);
	}

	function showStep(stepNumber) {
		$('.form-step').removeClass('active');
		$(`#${getStepName(stepNumber)}Step`).addClass('active');

		if (stepNumber === 2 && phoneVerified && verifiedPhoneNumber === $('#phoneNumber').val()) {
			$('#phoneOtpContainer, #phoneResendContainer, #phoneVerifyBtn, #phoneNextBtn').hide();
			$('#phoneResendBtn').prop('disabled', true);
			$('#changePhoneBtn, #phoneSkipBtn').show();
			$('#phoneStatus').addClass('verified').text('Verified');
			disableOTPInputs('phone');
		}

		if (stepNumber === 3 && emailVerified && verifiedEmail === $('#email').val()) {
			$('#emailOtpContainer, #emailResendContainer, #emailVerifyBtn, #emailNextBtn').hide();
			$('#emailResendBtn').prop('disabled', true);
			$('#changeEmailBtn, #emailSkipBtn').show();
			$('#emailStatus').addClass('verified').text('Verified');
			disableOTPInputs('email');
		}

	}

	function getStepName(stepNumber) {
		switch (stepNumber) {
			case 1: return 'basicDetails';
			case 2: return 'phoneVerification';
			case 3: return 'emailVerification';
			case 4: return 'professionalDetails';
			case 5: return 'educationDetails';
			case 6: return 'keySkills';
			case 7: return 'confirmation';
		}
	}

	function animateProgressButton(button, callback) {
		const progressFill = button.find('.progress-fill');
		progressFill.css('width', '0%');
		button.prop('disabled', true);
		progressFill.animate({ width: '100%' }, 1000, function() {
			button.prop('disabled', false);
			if (callback) callback();
		});
	}

	function startResendTimer(type) {
		const timerElement = $(`#${type}Timer`);
		const resendBtn = $(`#${type}ResendBtn`);
		let timeLeft = type === 'phone' ? phoneResendTimer : emailResendTimer;
		timerElement.text(` (Resend in ${timeLeft}s)`);
		resendBtn.prop('disabled', true);

		const interval = setInterval(() => {
			timeLeft--;
			timerElement.text(` (Resend in ${timeLeft}s)`);
			if (timeLeft <= 0) {
				clearInterval(interval);
				timerElement.text('');
				if ((type === 'phone' && !phoneVerified) || (type === 'email' && !emailVerified)) {
					resendBtn.prop('disabled', false);
				}
			}
		}, 1000);

		if (type === 'phone') phoneTimerInterval = interval;
		else emailTimerInterval = interval;
	}

	function sendPhoneOTP() {
		if (!validatePhoneNumber()) return;
		animateProgressButton($('#phoneVerifyBtn'), function() {
			const phone = $('#phoneNumber').val();
			$('#phoneNumber').prop('readonly', true);
			$('#displayPhone').text(phone.replace(/.(?=.{4})/g, '*'));
			$('#phoneOtpMessage, #phoneOtpContainer, #phoneResendContainer').show();
			$('#phoneVerifyBtn').hide();
			$('#phoneNextBtn').show();
			$('#phoneStatus').text('Enter the OTP');
			$('#phoneOtp1').focus();
			startResendTimer('phone');
		});
	}

	function sendEmailOTP() {
		if (!validateEmail()) return;
		animateProgressButton($('#emailVerifyBtn'), function() {
			const email = $('#email').val();
			$('#email').prop('readonly', true);
			$('#displayEmail').text(email.replace(/(?<=.{3}).(?=[^@]*@)/g, '*'));
			$('#emailOtpMessage, #emailOtpContainer, #emailResendContainer').show();
			$('#emailVerifyBtn').hide();
			$('#emailNextBtn').show();
			$('#emailStatus').text('Enter the OTP');
			$('#otp1').focus();
			startResendTimer('email');
		});
	}

	function verifyOTP(type) {
		const otpInputs = type === 'email'
			? ['otp1', 'otp2', 'otp3', 'otp4', 'otp5', 'otp6']
			: ['phoneOtp1', 'phoneOtp2', 'phoneOtp3', 'phoneOtp4', 'phoneOtp5', 'phoneOtp6'];
		const enteredOTP = otpInputs.map(id => $(`#${id}`).val()).join('');

		if (enteredOTP.length !== 6) {
			$(`#otp${type === 'email' ? 'Email' : 'Phone'}Error`)
				.text('Please enter all 6 digits')
				.addClass('visible');
			return;
		} else {
			$(`#otp${type === 'email' ? 'Email' : 'Phone'}Error`).text('').removeClass('visible');
		}

		const verifyBtn = type === 'email' ? $('#emailNextBtn') : $('#phoneNextBtn');
		if (type === 'email') emailAttempts++;
		else phoneAttempts++;

		if ((type === 'email' && emailAttempts > 3) || (type === 'phone' && phoneAttempts > 3)) {
			showToast('Maximum OTP attempts reached');
			verifyBtn.prop('disabled', true);
			return;
		}

		animateProgressButton(verifyBtn, function() {
			const statusElement = $(`#${type}Status`);
			statusElement.text('Verifying...');

			setTimeout(() => {
				const isValidOTP = true; // Simulated validation
				if (isValidOTP) {
					if (type === 'phone') {
						phoneVerified = true;
						verifiedPhoneNumber = $('#phoneNumber').val();
						disableOTPInputs('phone');
						$('#phoneNextBtn').hide();
						$('#phoneSkipBtn, #changePhoneBtn').show();
						$('#phoneResendBtn').prop('disabled', true);
					} else {
						emailVerified = true;
						verifiedEmail = $('#email').val();
						disableOTPInputs('email');
						$('#emailNextBtn').hide();
						$('#emailSkipBtn, #changeEmailBtn').show();
						$('#emailResendBtn').prop('disabled', true);
					}
					statusElement.addClass('verified').text('Verified');
				} else {
					$(`#otp${type === 'email' ? 'Email' : 'Phone'}Error`)
						.text('Invalid OTP')
						.addClass('visible');
					statusElement.text('');
				}
			}, 1000);
		});
	}




	// Event Handlers
	$('#fullName').on('input', validateFullNameDynamic);
	$('#username').on('input', validateUsernameDynamic);
	$('#password').on('input', validatePasswordDynamic);

	$('#phoneNumber').on('input', function() {
		this.value = this.value.replace(/[^0-9]/g, '').slice(0, 10);
		validatePhoneNumberDynamic();
	});

	$('#email').on('input', validateEmailDynamic);

	$('#boardOfStudy').on('input', function() {
		this.value = this.value.replace(/[^A-Za-z\s]/g, '');
		validateBoardOfStudyDynamic();
	});

	$('#university').on('input', function() {
		this.value = this.value.replace(/[^A-Za-z\s]/g, '');
		validateUniversityDynamic();
	});

	$('#percentage').on('input', function() {
		const val = $(this).val();
		if (val) {
			const numVal = parseFloat(val);
			if (numVal < 0) $(this).val(0);
			if (numVal > 100) $(this).val(100);
		}
		validatePercentageDynamic();
	});



	$('#startYear').on('change', function() {
		const startYear = parseInt($(this).val());
		const endYearSelect = $('#endYear');
		endYearSelect.empty().append('<option value=null disabled th:selected>Select end year</option>');

		if (startYear) {
			for (let year = startYear; year <= currentYear; year++) {
				endYearSelect.append(`<option value="${year}">${year}</option>`);
			}
			endYearSelect.prop('disabled', false);
		} else {
			endYearSelect.prop('disabled', true);
		}
	});

	$('.next-btn').click(function() {
		const step = parseInt($(this).data('step'));
		if (step === 1 && !validateBasicDetails()) return;
		if (step === 2 && !phoneVerified) {
			showToast('Please verify your phone number');
			return;
		}
		if (step === 3 && !emailVerified) {
			showToast('Please verify your email');
			return;
		}
		if (step === 4 && !validateProfessionalDetails()) return;
		if (step === 5 && !validateEducationDetails()) return;

		animateProgressButton($(this), function() {
			currentStep = step + 1;
			showStep(currentStep);
			updateProgressBar();
		});
	});

	$('.prev-btn').click(function() {
		currentStep = parseInt($(this).data('step')) - 1;
		showStep(currentStep);
		updateProgressBar();
	});

	$('.otp-input').on('input', function() {
		this.value = this.value.replace(/[^0-9]/g, '');
		if (this.value.length === 1) {
			$(this).next('.otp-input:not(:disabled)').focus();
		}
	});

	$('.otp-input').on('keydown', function(e) {
		if (e.key === 'Backspace' && !$(this).val()) {
			$(this).prev('.otp-input:not(:disabled)').focus();
		}
	});

	$('#phoneVerifyBtn').click(sendPhoneOTP);
	$('#phoneNextBtn').click(function() { verifyOTP('phone'); });

	$('#emailVerifyBtn').click(sendEmailOTP);
	$('#emailNextBtn').click(function() { verifyOTP('email'); });

	$('#phoneResendBtn').click(function() {
		if (!phoneVerified) {
			$('#phoneOtpContainer input').val('');
			$('#phoneOtp1').focus();
			$('#phoneStatus').text('Enter the OTP');
			$('#otpPhoneError').text('').removeClass('visible');
			phoneAttempts = 0;
			$('#phoneNextBtn').prop('disabled', false);
			startResendTimer('phone');
		}
	});

	$('#emailResendBtn').click(function() {
		if (!emailVerified) {
			$('#emailOtpContainer input').val('');
			$('#otp1').focus();
			$('#emailStatus').text('Enter the OTP');
			$('#otpEmailError').text('').removeClass('visible');
			emailAttempts = 0;
			$('#emailNextBtn').prop('disabled', false);
			startResendTimer('email');
		}
	});

	$('#changePhoneBtn').click(function() {
		resetVerification('phone');
		$('#phoneNumber').val('').focus();
	});

	$('#changeEmailBtn').click(function() {
		resetVerification('email');
		$('#email').val('').focus();
	});

	$('#highestQualification').change(function() {
		const val = $(this).val();
		console.log(val);
		if (val == 1 || val == 2) {
			$('#boardOfStudyGroup').show();
			$('#degreeGroup').hide();
			$('#degree').val(null);
		} else if (val == 3 || val == 4) {
			$('#boardOfStudyGroup').hide();
			$('#degreeGroup').show();
			$('#boardOfStudy').val(null);
		} else {
			$('#boardOfStudyGroup, #degreeGroup').hide();
			$('#boardOfStudy, #degree').val('');
		}
		clearError('highestQualification');
	});

	$('#registrationForm').submit(function(e) {
		e.preventDefault(); // prevent default form submission

		const form = this; // reference to the form

		animateProgressButton($('#submitBtn'), function() {
			Swal.fire({
				title: 'Success!',
				text: 'Registration completed successfully!',
				icon: 'success',
				confirmButtonText: 'OK',
				confirmButtonColor: '#5e17eb'
			}).then((result) => {
				if (result.isConfirmed) {
					form.submit(); // trigger actual form submission
				}
			});
		});
	});



	updateProgressBar();
});