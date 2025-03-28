$(document).ready(function() {
    let currentStep = 1;
    let emailVerified = false;
    let phoneVerified = false;
    const DUMMY_OTP = "123456";
    let phoneResendTimer = 30;
    let emailResendTimer = 30;
    let phoneTimerInterval, emailTimerInterval;
    let companyErrorsShown = false; // Flag to track if errors have been shown

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&*])[A-Za-z\d@#$%^&*]{8,}$/;
    const fullNameRegex = /^[A-Za-z\s]+$/;
    const usernameRegex = /^[A-Za-z0-9]+$/;
    const phoneRegex = /^[7-9]\d{9}$/;

    function updateProgressBar() {
        $('.progress-step').each(function(index) {
            $(this).toggleClass('active', index < currentStep);
        });
    }

    function showStep(stepNumber) {
        $('.form-step').removeClass('active');
        $(`#${getStepName(stepNumber)}Step`).addClass('active');
    }

    function getStepName(stepNumber) {
        switch(stepNumber) {
            case 1: return 'basicDetails';
            case 2: return 'phoneVerification';
            case 3: return 'emailVerification';
            case 4: return 'companyDetails';
            case 5: return 'confirmation';
        }
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
                resendBtn.prop('disabled', false);
                if (type === 'phone') phoneResendTimer = 30;
                else emailResendTimer = 30;
            }
        }, 1000);

        if (type === 'phone') phoneTimerInterval = interval;
        else emailTimerInterval = interval;
    }

    function sendPhoneOTP() {
        if (!validatePhoneNumber()) return;
        const phone = $('#phoneNumber').val();
        $('#displayPhone').text(phone.replace(/.(?=.{4})/g, '*'));
        $('#phoneOtpMessage, #phoneOtpContainer, #phoneResendContainer').show();
        $('#phoneVerifyBtn').hide();
        $('#phoneNextBtn').show();
        console.log(`Phone OTP: ${DUMMY_OTP}`);
        $('#phoneStatus').text('Enter the OTP');
        $('#phoneOtp1').focus();
        startResendTimer('phone');
    }

    function sendEmailOTP() {
        if (!validateEmail()) return;
        const email = $('#email').val();
        $('#displayEmail').text(email.replace(/(?<=.{3}).(?=[^@]*@)/g, '*'));
        $('#emailOtpMessage, #emailOtpContainer, #emailResendContainer').show();
        $('#emailVerifyBtn').hide();
        $('#emailNextBtn').show();
        console.log(`Email OTP: ${DUMMY_OTP}`);
        $('#emailStatus').text('Enter the OTP');
        $('#otp1').focus();
        startResendTimer('email');
    }

    function verifyOTP(type) {
        if (!validateOTPInputs(type)) return;
        const otpInputs = type === 'email' 
            ? ['otp1', 'otp2', 'otp3', 'otp4', 'otp5', 'otp6']
            : ['phoneOtp1', 'phoneOtp2', 'phoneOtp3', 'phoneOtp4', 'phoneOtp5', 'phoneOtp6'];
        const enteredOTP = otpInputs.map(id => $(`#${id}`).val()).join('');
        const errorElement = $(`#otp${type === 'email' ? 'Email' : 'Phone'}Error`);
        const statusElement = $(`#${type}Status`);

        statusElement.html('Verifying <span class="loading"></span>');
        setTimeout(() => {
            if (enteredOTP === DUMMY_OTP) {
                errorElement.text('').removeClass('visible');
                statusElement.addClass('verified').text('Verified');
                if (type === 'phone') {
                    clearInterval(phoneTimerInterval);
                    phoneVerified = true;
                } else {
                    clearInterval(emailTimerInterval);
                    emailVerified = true;
                }
                setTimeout(() => {
                    currentStep++;
                    showStep(currentStep);
                    updateProgressBar();
                }, 1000);
            } else {
                errorElement.text('Invalid OTP. Please check and try again.').addClass('visible');
                statusElement.text('');
                otpInputs.forEach(id => $(`#${id}`).addClass('error'));
            }
        }, 1500);
    }

    function validateOTPInputs(type) {
        const otpInputs = type === 'email' 
            ? ['otp1', 'otp2', 'otp3', 'otp4', 'otp5', 'otp6']
            : ['phoneOtp1', 'phoneOtp2', 'phoneOtp3', 'phoneOtp4', 'phoneOtp5', 'phoneOtp6'];
        const errorElement = $(`#otp${type === 'email' ? 'Email' : 'Phone'}Error`);
        const statusElement = $(`#${type}Status`);
        const enteredOTP = otpInputs.map(id => $(`#${id}`).val()).join('');

        if (enteredOTP.length !== 6) {
            errorElement.text('Please enter all 6 digits of the OTP').addClass('visible');
            otpInputs.forEach(id => $(`#${id}`).addClass('error'));
            return false;
        }
        errorElement.text('').removeClass('visible');
        otpInputs.forEach(id => $(`#${id}`).removeClass('error'));
        return true;
    }

    function validateBasicDetails() {
        let isValid = true;
        const fullName = $('#fullName').val().trim();
        const username = $('#username').val().trim();
        const password = $('#password').val();

        if (!fullName) {
            $('#fullNameError').text('Full name is required').addClass('visible');
            $('#fullName').addClass('error');
            isValid = false;
        } else if (!fullNameRegex.test(fullName)) {
            $('#fullNameError').text('Full name must only contain letters and spaces').addClass('visible');
            $('#fullName').addClass('error');
            isValid = false;
        } else if (fullName.length < 2) {
            $('#fullNameError').text('Must be at least 2 characters').addClass('visible');
            $('#fullName').addClass('error');
            isValid = false;
        } else {
            $('#fullNameError').text('').removeClass('visible');
            $('#fullName').removeClass('error');
        }

        if (!username) {
            $('#usernameError').text('Username is required').addClass('visible');
            $('#username').addClass('error');
            isValid = false;
        } else if (!usernameRegex.test(username)) {
            $('#usernameError').text('Username must only contain letters and numbers').addClass('visible');
            $('#username').addClass('error');
            isValid = false;
        } else if (username.length < 3) {
            $('#usernameError').text('Must be at least 3 characters').addClass('visible');
            $('#username').addClass('error');
            isValid = false;
        } else {
            $('#usernameError').text('').removeClass('visible');
            $('#username').removeClass('error');
        }

        if (!password) {
            $('#passwordError').text('Password is required').addClass('visible');
            $('#password').addClass('error');
            isValid = false;
        } else if (!passwordRegex.test(password)) {
            $('#passwordError').text('Must be 8+ characters with uppercase, lowercase, number, and special character').addClass('visible');
            $('#password').addClass('error');
            isValid = false;
        } else {
            $('#passwordError').text('').removeClass('visible');
            $('#password').removeClass('error');
        }
        return isValid;
    }

    function validatePhoneNumber() {
        const phoneNumber = $('#phoneNumber').val().trim();
        if (!phoneNumber) {
            $('#phoneNumberError').text('Phone number is required').addClass('visible');
            $('#phoneNumber').addClass('error');
            return false;
        } else if (!phoneRegex.test(phoneNumber)) {
            $('#phoneNumberError').text('Must be exactly 10 digits, start with 7-9, and contain no letters').addClass('visible');
            $('#phoneNumber').addClass('error');
            return false;
        }
        $('#phoneNumberError').text('').removeClass('visible');
        $('#phoneNumber').removeClass('error');
        return true;
    }

    function validateEmail() {
        const email = $('#email').val().trim();
        if (!email) {
            $('#emailError').text('Email is required').addClass('visible');
            $('#email').addClass('error');
            return false;
        } else if (!emailRegex.test(email)) {
            $('#emailError').text('Must be a valid email address').addClass('visible');
            $('#email').addClass('error');
            return false;
        }
        $('#emailError').text('').removeClass('visible');
        $('#email').removeClass('error');
        return true;
    }

    function validateCompanyDetails() {
        let isValid = true;
        const companyName = $('#companyName').val().trim();
        const companyIndustry = $('#companyIndustry').val().trim();
        const companyAbout = $('#companyAbout').val().trim();
        const companySize = $('#companySize').val();
        const location = $('#location').val();
        const designation = $('#designation').val();

        if (!companyName) {
            $('#companyNameError').text('Company name is required').addClass('visible');
            $('#companyName').addClass('error');
            isValid = false;
        } else if (companyName.length < 2) {
            $('#companyNameError').text('Must be at least 2 characters').addClass('visible');
            $('#companyName').addClass('error');
            isValid = false;
        } else {
            $('#companyNameError').text('').removeClass('visible');
            $('#companyName').removeClass('error');
        }

        if (!companyIndustry) {
            $('#companyIndustryError').text('Industry is required').addClass('visible');
            $('#companyIndustry').addClass('error');
            isValid = false;
        } else if (companyIndustry.length < 2) {
            $('#companyIndustryError').text('Must be at least 2 characters').addClass('visible');
            $('#companyIndustry').addClass('error');
            isValid = false;
        } else {
            $('#companyIndustryError').text('').removeClass('visible');
            $('#companyIndustry').removeClass('error');
        }

        if (!companyAbout) {
            $('#companyAboutError').text('Please provide a description').addClass('visible');
            $('#companyAbout').addClass('error');
            isValid = false;
        } else if (companyAbout.length < 20) {
            $('#companyAboutError').text('Must be at least 20 characters').addClass('visible');
            $('#companyAbout').addClass('error');
            isValid = false;
        } else {
            $('#companyAboutError').text('').removeClass('visible');
            $('#companyAbout').removeClass('error');
        }

        if (!companySize) {
            $('#companySizeError').text('Company size is required').addClass('visible');
            $('#companySize').addClass('error');
            isValid = false;
        } else {
            $('#companySizeError').text('').removeClass('visible');
            $('#companySize').removeClass('error');
        }

        if (!location) {
            $('#locationError').text('Location is required').addClass('visible');
            $('#location').addClass('error');
            isValid = false;
        } else {
            $('#locationError').text('').removeClass('visible');
            $('#location').removeClass('error');
        }

        if (!designation){
            $('#designationError').text('Designation is required').addClass('visible');
            $('#designation').addClass('error');
            isValid = false;
        } else {
            $('#designationError').text('').removeClass('visible');
            $('#designation').removeClass('error');
        }
        if (!isValid) {
            companyErrorsShown = true; // Set flag to enable dynamic error hiding
            bindCompanyFieldListeners(); // Bind listeners for dynamic error hiding
        }

        return isValid;
    }

    function bindCompanyFieldListeners() {
        // Company Name
        $('#companyName').off('input').on('input', function() {
            if (!companyErrorsShown) return;
            const value = $(this).val().trim();
            if (value && value.length >= 2) {
                $('#companyNameError').text('').removeClass('visible');
                $(this).removeClass('error');
            }
        });

        // Company Industry
        $('#companyIndustry').off('input').on('input', function() {
            if (!companyErrorsShown) return;
            const value = $(this).val().trim();
            if (value && value.length >= 2) {
                $('#companyIndustryError').text('').removeClass('visible');
                $(this).removeClass('error');
            }
        });

        // Company About
        $('#companyAbout').off('input').on('input', function() {
            if (!companyErrorsShown) return;
            const value = $(this).val().trim();
            if (value && value.length >= 20) {
                $('#companyAboutError').text('').removeClass('visible');
                $(this).removeClass('error');
            }
        });

        // Company Size
        $('#companySize').off('change').on('change', function() {
            if (!companyErrorsShown) return;
            const value = $(this).val();
            if (value) {
                $('#companySizeError').text('').removeClass('visible');
                $(this).removeClass('error');
            }
        });

        // Location
        $('#location').off('change').on('change', function() {
            if (!companyErrorsShown) return;
            const value = $(this).val();
            if (value) {
                $('#locationError').text('').removeClass('visible');
                $(this).removeClass('error');
            }
        });

        $('#designation').off('input').on('input', function() {
            if (!companyErrorsShown) return;
            const value = $(this).val();
            if (value) {
                $('#designationError').text('').removeClass('visible');
                $(this).removeClass('error');
            }
        });
    }

    $('.next-btn').on('click', function() {
        const stepNumber = parseInt($(this).data('step'));
        let isValid = false;

        if (stepNumber === 1) isValid = validateBasicDetails();
        else if (stepNumber === 2 && phoneVerified) isValid = true;
        else if (stepNumber === 3 && emailVerified) isValid = true;
        else if (stepNumber === 4) {
            isValid = validateCompanyDetails();
            if (isValid) {
                $('#summaryDetails').html(`
                    <p><strong>Name:</strong> ${$('#fullName').val()}</p>
                    <p><strong>Username:</strong> ${$('#username').val()}</p>
                    <p><strong>Password:</strong> ${'*'.repeat($('#password').val().length)}</p>
                    <p><strong>Email:</strong> ${$('#email').val()}</p>
                    <p><strong>Phone:</strong> ${$('#phoneNumber').val()}</p>
                    <p><strong>Company:</strong> ${$('#companyName').val()}</p>
                    <p><strong>Industry:</strong> ${$('#companyIndustry').val()}</p>
                    <p><strong>Designation:</strong> ${$('#designation').val()}</p>
                    <p><strong>About:</strong> ${$('#companyAbout').val()}</p>
                    <p><strong>Size:</strong> ${$('#companySize option:selected').text()}</p>
                    <p><strong>Location:</strong> ${$('#location option:selected').text()}</p>
                `);
            }
        }

        if (isValid) {
            currentStep++;
            showStep(currentStep);
            updateProgressBar();
        }
    });

    $('.prev-btn').on('click', function() {
        currentStep--;
        showStep(currentStep);
        updateProgressBar();
    });

    $('#phoneVerifyBtn').on('click', sendPhoneOTP);
    $('#phoneNextBtn').on('click', function() { verifyOTP('phone'); });
    $('#phoneResendBtn').on('click', function() {
        sendPhoneOTP();
    });

    $('#emailVerifyBtn').on('click', sendEmailOTP);
    $('#emailNextBtn').on('click', function() { verifyOTP('email'); });
    $('#emailResendBtn').on('click', function() {
        sendEmailOTP();
    });

    $('.otp-input').on('input', function() {
        if ($(this).val().length === 1) $(this).next('.otp-input').focus();
    }).on('keydown', function(e) {
        if (e.key === 'Backspace' && !$(this).val()) $(this).prev('.otp-input').focus();
    });

    $('#fullName, #username, #password').on('input', validateBasicDetails);
    $('#phoneNumber').on('input', validatePhoneNumber);
    $('#email').on('input', validateEmail);

    $('#registrationForm').on('submit', function(e) {
        e.preventDefault();
        if (currentStep !== 5 || !validateBasicDetails() || !validatePhoneNumber() || 
            !validateEmail() || !validateCompanyDetails() || !emailVerified || !phoneVerified) {
            alert('Please complete all steps and verifications.');
            return;
        }
        alert('Registration Successful!');
    });
});