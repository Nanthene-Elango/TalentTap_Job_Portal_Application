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

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&*])[A-Za-z\d@#$%^&*]{8,}$/;
    const fullNameRegex = /^[A-Za-z\s]+$/;
    const usernameRegex = /^[A-Za-z0-9]+$/;
    const phoneRegex = /^[7-9]\d{9}$/;
    const urlRegex = /^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})([/\w .-]*)*\/?$/;
    const companySizeRegex = /^\d+-\d+$/;

    // Show toast notification
    function showToast(message) {
        const toast = $('#toast');
        toast.text(message).addClass('show');
        setTimeout(() => {
            toast.removeClass('show');
        }, 3000);
    }

    // Prevent non-numeric input in OTP fields
    $('.otp-input').on('input', function() {
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    function disableOTPInputs(type) {
        const otpInputs = type === 'email' 
            ? ['otp1', 'otp2', 'otp3', 'otp4', 'otp5', 'otp6']
            : ['phoneOtp1', 'phoneOtp2', 'phoneOtp3', 'phoneOtp4', 'phoneOtp5', 'phoneOtp6'];
        
        otpInputs.forEach(id => {
            $(`#${id}`).prop('disabled', true).removeClass('error');
        });
    }

    function resetVerification(type) {
        if (type === 'phone') {
            phoneVerified = false;
            $('#phoneOtpContainer input').val('').prop('disabled', false);
            $('#phoneOtpContainer, #phoneResendContainer, #phoneNextBtn, #changePhoneBtn').hide();
            $('#phoneVerifyBtn').show();
            $('#phoneStatus').removeClass('verified').text('');
            $('#phoneOtpMessage').hide();
            $('#phoneNumber').prop('disabled', false).attr('placeholder', 'Enter your phone number');
            clearInterval(phoneTimerInterval);
            phoneAttempts = 0;
            verifiedPhoneNumber = null;
            $('#hiddenPhoneNumber').val(''); // Clear hidden input
        } else {
            emailVerified = false;
            $('#emailOtpContainer input').val('').prop('disabled', false);
            $('#emailOtpContainer, #emailResendContainer, #emailNextBtn, #changeEmailBtn').hide();
            $('#emailVerifyBtn').show();
            $('#emailStatus').removeClass('verified').text('');
            $('#emailOtpMessage').hide();
            $('#email').prop('disabled', false).attr('placeholder', 'Enter your email');
            clearInterval(emailTimerInterval);
            emailAttempts = 0;
            verifiedEmail = null;
            $('#hiddenEmailId').val(''); // Clear hidden input
        }
    }

    function updateProgressBar() {
        const progressPercentage = (currentStep / 5) * 100;
        $('#progressBarFill').css('width', `${progressPercentage}%`);
    }

    function showStep(stepNumber) {
        $('.form-step').removeClass('active');
        $(`#${getStepName(stepNumber)}Step`).addClass('active');
        
        if (stepNumber === 2) {
            if (phoneVerified && verifiedPhoneNumber === $('#phoneNumber').val()) {
                $('#phoneOtpContainer, #phoneResendContainer, #phoneVerifyBtn, #phoneNextBtn').hide();
                $('#changePhoneBtn').show();
                $('#phoneStatus').addClass('verified').text('Verified');
                disableOTPInputs('phone');
                $('#phoneNumber').prop('disabled', true).attr('placeholder', '');
                $('#hiddenPhoneNumber').val(verifiedPhoneNumber); // Ensure hidden input is set
            }
        }
        
        if (stepNumber === 3) {
            if (emailVerified && verifiedEmail === $('#email').val()) {
                $('#emailOtpContainer, #emailResendContainer, #emailVerifyBtn, #emailNextBtn').hide();
                $('#emailStatus').addClass('verified').text('Verified');
                $('#changeEmailBtn').show();
                disableOTPInputs('email');
                $('#email').prop('disabled', true).attr('placeholder', '');
                $('#hiddenEmailId').val(verifiedEmail); // Ensure hidden input is set
            }
        }
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
                resendBtn.prop('disabled', false);
                if (type === 'phone') phoneResendTimer = 30;
                else emailResendTimer = 30;
            }
        }, 1000);

        if (type === 'phone') phoneTimerInterval = interval;
        else emailTimerInterval = interval;
    }

    function showError(element, message) {
        const errorElement = $(`#${element}Error`);
        errorElement.text(message).addClass('visible');
        $(`#${element}`).addClass('error');
    }

    function clearError(element) {
        const errorElement = $(`#${element}Error`);
        errorElement.text('').removeClass('visible');
        $(`#${element}`).removeClass('error');
    }

    function sendPhoneOTP() {
        if (!validatePhoneNumber()) return;
        const phoneVerifyBtn = $('#phoneVerifyBtn');
        animateProgressButton(phoneVerifyBtn, function() {
            const phone = $('#phoneNumber').val();
            $('#phoneNumber').prop('disabled', true).attr('placeholder', '');
            $('#displayPhone').text(phone.replace(/.(?=.{4})/g, '*'));
            $('#phoneOtpMessage, #phoneOtpContainer, #phoneResendContainer').show();
            $('#phoneVerifyBtn').hide();
            $('#phoneNextBtn').show();
            $('#phoneStatus').text('Enter the OTP');
            $('#phoneOtp1').focus();
            startResendTimer('phone');
            console.log('Phone OTP sent (to be implemented later)');
        });
    }

    function sendEmailOTP() {
        if (!validateEmail()) return;
        const emailVerifyBtn = $('#emailVerifyBtn');
        animateProgressButton(emailVerifyBtn, function() {
            const email = $('#email').val();
            $('#email').prop('disabled', true).attr('placeholder', '');
            $('#displayEmail').text(email.replace(/(?<=.{3}).(?=[^@]*@)/g, '*'));
            $('#emailOtpMessage, #emailOtpContainer, #emailResendContainer').show();
            $('#emailVerifyBtn').hide();
            $('#emailNextBtn').show();
            $('#emailStatus').text('Enter the OTP');
            $('#otp1').focus();
            startResendTimer('email');
            console.log('Email OTP sent (to be implemented later)');
        });
    }

    function verifyOTP(type) {
        if (!validateOTPInputs(type)) return;
        
        const nextBtn = type === 'email' ? $('#emailNextBtn') : $('#phoneNextBtn');
        
        if (type === 'email') {
            emailAttempts++;
        } else {
            phoneAttempts++;
        }

        if ((type === 'email' && emailAttempts > 3) || (type === 'phone' && phoneAttempts > 3)) {
            showToast('Maximum OTP attempts reached. Please request a new OTP.');
            nextBtn.prop('disabled', true);
            return;
        }

        animateProgressButton(nextBtn, function() {
            const otpInputs = type === 'email' 
                ? ['otp1', 'otp2', 'otp3', 'otp4', 'otp5', 'otp6']
                : ['phoneOtp1', 'phoneOtp2', 'phoneOtp3', 'phoneOtp4', 'phoneOtp5', 'phoneOtp6'];
            
            const statusElement = $(`#${type}Status`);
            statusElement.html('Verifying <span class="loading"></span>');

            setTimeout(() => {
                const isValidOTP = true; // Replace with actual API call
                if (isValidOTP) {
                    if (type === 'phone') {
                        phoneVerified = true;
                        verifiedPhoneNumber = $('#phoneNumber').val();
                        $('#hiddenPhoneNumber').val(verifiedPhoneNumber); // Set hidden input
                        console.log('Verified Phone Number:', verifiedPhoneNumber);
                        disableOTPInputs('phone');
                    } else {
                        emailVerified = true;
                        verifiedEmail = $('#email').val();
                        $('#hiddenEmailId').val(verifiedEmail); // Set hidden input
                        console.log('Verified Email:', verifiedEmail);
                        disableOTPInputs('email');
                    }
                    
                    statusElement.addClass('verified').text('Verified');
                    currentStep++;
                    showStep(currentStep);
                    updateProgressBar();
                } else {
                    $(`#otp${type === 'email' ? 'Email' : 'Phone'}Error`)
                        .text('Invalid OTP. Please try again.')
                        .addClass('visible');
                    statusElement.text('');
                }
            }, 1500);
        });
    }

    function validateOTPInputs(type) {
        const otpInputs = type === 'email' 
            ? ['otp1', 'otp2', 'otp3', 'otp4', 'otp5', 'otp6']
            : ['phoneOtp1', 'phoneOtp2', 'phoneOtp3', 'phoneOtp4', 'phoneOtp5', 'phoneOtp6'];
        const errorElement = $(`#otp${type === 'email' ? 'Email' : 'Phone'}Error`);
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

        if (!fullName || !fullNameRegex.test(fullName) || fullName.length < 2) {
            showError('fullName', fullName ? (!fullNameRegex.test(fullName) ? 'Only letters and spaces allowed' : 'Minimum 2 characters') : 'Full name is required');
            isValid = false;
        } else {
            clearError('fullName');
        }

        if (!username || !usernameRegex.test(username) || username.length < 3) {
            showError('username', username ? (!usernameRegex.test(username) ? 'Only letters and numbers allowed' : 'Minimum 3 characters') : 'Username is required');
            isValid = false;
        } else {
            clearError('username');
        }

        if (!password || !passwordRegex.test(password)) {
            showError('password', password ? 'Must be 8+ characters with uppercase, lowercase, number, and special character' : 'Password is required');
            isValid = false;
        } else {
            clearError('password');
        }
        return isValid;
    }

    function validatePhoneNumber() {
        const phoneNumber = $('#phoneNumber').val().trim();
        if (!phoneNumber || !phoneRegex.test(phoneNumber)) {
            showError('phoneNumber', phoneNumber ? 'Must be 10 digits starting with 7-9' : 'Phone number is required');
            return false;
        }
        clearError('phoneNumber');
        return true;
    }

    function validateEmail() {
        const email = $('#email').val().trim();
        if (!email || !emailRegex.test(email)) {
            showError('email', email ? 'Must be a valid email address' : 'Email is required');
            return false;
        }
        clearError('email');
        return true;
    }

    function validateCompanyDetails() {
        let isValid = true;
        const companyName = $('#companyName').val().trim();
        const companyLogo = $('#companyLogo')[0].files.length;
        const companyIndustry = $('#companyIndustry').val();
        const companyEmail = $('#companyEmail').val().trim();
        const companyPhone = $('#companyPhone').val().trim();
        const companySize = $('#companySize').val().trim();
        const location = $('#location').val();
        const websiteUrl = $('#websiteUrl').val().trim();
        const foundedAt = $('#foundedAt').val();
        const companyAbout = $('#companyAbout').val().trim();
        const designation = $('#designation').val().trim();

        console.log('Validating Company Details...');
        console.log('Company Name:', companyName);
        console.log('Company Logo:', companyLogo);
        console.log('Company Industry:', companyIndustry);
        console.log('Company Email:', companyEmail);
        console.log('Company Phone:', companyPhone);
        console.log('Company Size:', companySize);
        console.log('Location:', location);
        console.log('Website URL:', websiteUrl);
        console.log('Founded At:', foundedAt);
        console.log('Company About:', companyAbout);
        console.log('Designation:', designation);

        if (!companyName || companyName.length < 2) {
            showError('companyName', companyName ? 'Minimum 2 characters' : 'Company name is required');
            isValid = false;
        } else {
            clearError('companyName');
        }

        if (companyLogo === 0) {
            showError('companyLogo', 'Company logo is required');
            isValid = false;
        } else {
            clearError('companyLogo');
        }

        if (!companyIndustry) {
            showError('companyIndustry', 'Please select an industry');
            isValid = false;
        } else {
            clearError('companyIndustry');
        }

        if (!companyEmail || !emailRegex.test(companyEmail)) {
            showError('companyEmail', companyEmail ? 'Must be a valid email address' : 'Company email is required');
            isValid = false;
        } else {
            clearError('companyEmail');
        }

        if (!companyPhone || !phoneRegex.test(companyPhone)) {
            showError('companyPhone', companyPhone ? 'Must be 10 digits starting with 7-9' : 'Company phone is required');
            isValid = false;
        } else {
            clearError('companyPhone');
        }

        if (!companySize || !companySizeRegex.test(companySize)) {
            showError('companySize', companySize ? 'Must be a range like 0-100' : 'Company size is required');
            isValid = false;
        } else {
            const [min, max] = companySize.split('-').map(Number);
            if (min < 0 || max <= min) {
                showError('companySize', 'Invalid range: min must be >= 0 and max must be greater than min');
                isValid = false;
            } else {
                clearError('companySize');
            }
        }

        if (!location) {
            showError('location', 'Please select a location');
            isValid = false;
        } else {
            clearError('location');
        }

        if (!websiteUrl || !urlRegex.test(websiteUrl)) {
            showError('websiteUrl', websiteUrl ? 'Must be a valid URL' : 'Website URL is required');
            isValid = false;
        } else {
            clearError('websiteUrl');
        }

        if (!foundedAt || foundedAt < 1800 || foundedAt > 2025) {
            showError('foundedAt', foundedAt ? 'Must be between 1800 and 2025' : 'Founding year is required');
            isValid = false;
        } else {
            clearError('foundedAt');
        }

        if (!companyAbout || companyAbout.length < 10) {
            showError('companyAbout', companyAbout ? 'Minimum 10 characters required' : 'About company is required');
            isValid = false;
        } else {
            clearError('companyAbout');
        }

        if (!designation || designation.length < 2) {
            showError('designation', designation ? 'Minimum 2 characters' : 'Designation is required');
            isValid = false;
        } else {
            clearError('designation');
        }

        console.log('Validation Result:', isValid);
        return isValid;
    }

    function prepareConfirmation() {
        const fullName = $('#fullName').val().trim();
        const username = $('#username').val().trim();
        const phone = verifiedPhoneNumber || $('#phoneNumber').val().trim();
        const email = verifiedEmail || $('#email').val().trim();
        const companyName = $('#companyName').val().trim();
        const companyIndustry = $('#companyIndustry option:selected').text();
        const companyEmail = $('#companyEmail').val().trim();
        const companyPhone = $('#companyPhone').val().trim();
        const companySize = $('#companySize').val().trim();
        const location = $('#location option:selected').text();
        const websiteUrl = $('#websiteUrl').val().trim();
        const foundedAt = $('#foundedAt').val();
        const companyAbout = $('#companyAbout').val().trim();
        const designation = $('#designation').val().trim();

        let html = `
            <p><strong>Full Name:</strong> ${fullName}</p>
            <p><strong>Username:</strong> ${username}</p>
            <p><strong>Phone:</strong> ${phone}</p>
            <p><strong>Email:</strong> ${email}</p>
            <p><strong>Company Name:</strong> ${companyName}</p>
            <p><strong>Industry:</strong> ${companyIndustry}</p>
            <p><strong>Company Email:</strong> ${companyEmail}</p>
            <p><strong>Company Phone:</strong> ${companyPhone}</p>
            <p><strong>Company Size:</strong> ${companySize}</p>
            <p><strong>Location:</strong> ${location}</p>
            <p><strong>Website URL:</strong> ${websiteUrl}</p>
            <p><strong>Founded At:</strong> ${foundedAt}</p>
            <p><strong>About Company:</strong> ${companyAbout}</p>
            <p><strong>Designation:</strong> ${designation}</p>
        `;
        $('#summaryDetails').html(html);
    }

    $('.next-btn').click(function() {
        const step = parseInt($(this).data('step'));
        console.log('Next button clicked, current step:', step);

        if (step === 1 && !validateBasicDetails()) {
            console.log('Basic Details validation failed');
            return;
        }
        if (step === 2) {
            if (!validatePhoneNumber()) {
                showToast('Please enter a valid phone number.');
                return;
            }
            if (!phoneVerified) {
                console.log('Phone verification required');
                verifyOTP('phone');
                return;
            }
            // Ensure hidden input is set
            if (verifiedPhoneNumber) {
                $('#hiddenPhoneNumber').val(verifiedPhoneNumber);
            }
        }
        if (step === 3) {
            if (!validateEmail()) {
                showToast('Please enter a valid email.');
                return;
            }
            if (!emailVerified) {
                console.log('Email verification required');
                verifyOTP('email');
                return;
            }
            // Ensure hidden input is set
            if (verifiedEmail) {
                $('#hiddenEmailId').val(verifiedEmail);
            }
        }
        if (step === 4 && !validateCompanyDetails()) {
            console.log('Company Details validation failed');
            return;
        }
        
        animateProgressButton($(this), function() {
            if (step === 4) {
                console.log('Preparing confirmation step');
                prepareConfirmation();
            }
            currentStep = step + 1;
            console.log('Advancing to step:', currentStep);
            showStep(currentStep);
            updateProgressBar();
        });
    });

    $('.prev-btn').click(function() {
        currentStep = parseInt($(this).data('step')) - 1;
        console.log('Previous button clicked, going to step:', currentStep);
        showStep(currentStep);
        updateProgressBar();
    });

    $('.otp-input').keyup(function() {
        const maxLength = parseInt($(this).attr('maxlength'));
        if ($(this).val().length >= maxLength) $(this).next('.otp-input').focus();
    });

    $('.otp-input').keydown(function(e) {
        if (e.key === 'Backspace' && $(this).val().length === 0) $(this).prev('.otp-input').focus();
    });

    $('#phoneVerifyBtn').click(sendPhoneOTP);
    $('#emailVerifyBtn').click(sendEmailOTP);

    $('#phoneResendBtn').click(function() {
        $('.otp-input').val('');
        $('#phoneOtp1').focus();
        $('#phoneStatus').text('Enter the OTP');
        $('.otp-input').removeClass('error');
        $('#otpPhoneError').text('').removeClass('visible');
        phoneAttempts = 0;
        $('#phoneNextBtn').prop('disabled', false);
        startResendTimer('phone');
        console.log('Phone OTP resent (to be implemented later)');
    });

    $('#emailResendBtn').click(function() {
        $('.otp-input').val('');
        $('#otp1').focus();
        $('#emailStatus').text('Enter the OTP');
        $('.otp-input').removeClass('error');
        $('#otpEmailError').text('').removeClass('visible');
        emailAttempts = 0;
        $('#emailNextBtn').prop('disabled', false);
        startResendTimer('email');
        console.log('Email OTP resent (to be implemented later)');
    });

    $('#changePhoneBtn').click(function() {
        resetVerification('phone');
        $('#phoneNumber').val('').focus();
    });

    $('#changeEmailBtn').click(function() {
        resetVerification('email');
        $('#email').val('').focus();
    });

    $('#phoneNumber').on('input', function() {
        const currentVal = $(this).val();
        if (phoneVerified && verifiedPhoneNumber !== currentVal) {
            resetVerification('phone');
        }
    });

    $('#email').on('input', function() {
        const currentVal = $(this).val();
        if (emailVerified && verifiedEmail !== currentVal) {
            resetVerification('email');
        }
    });

    $('#registrationForm').submit(function(e) {
        e.preventDefault();
        const form = this;
        const formData = new FormData(form);
        console.log('Form Data:', Object.fromEntries(formData));
        console.log('Visible Phone Number:', $('#phoneNumber').val());
        console.log('Hidden Phone Number:', $('#hiddenPhoneNumber').val());
        console.log('Verified Phone Number:', verifiedPhoneNumber);

        if (!validatePhoneNumber() || !validateEmail()) {
            showToast('Please enter a valid phone number and email.');
            return;
        }

        if (!phoneVerified || !emailVerified) {
            showToast('Please complete phone and email verification.');
            return;
        }

        // Ensure hidden input is set before submission
        if (verifiedPhoneNumber) {
            $('#hiddenPhoneNumber').val(verifiedPhoneNumber);
        }
        if (verifiedEmail) {
            $('#hiddenEmailId').val(verifiedEmail);
        }

        // Validate hidden phone number
        const hiddenPhone = $('#hiddenPhoneNumber').val();
        if (!hiddenPhone || !phoneRegex.test(hiddenPhone)) {
            showToast('Phone number verification is incomplete.');
            return;
        }

        animateProgressButton($('#submitBtn'), function() {
            Swal.fire({
                title: 'Success!',
                text: 'Employer registration completed successfully!',
                icon: 'success',
                confirmButtonText: 'OK',
                confirmButtonColor: '#5e17eb'
            }).then((result) => {
                if (result.isConfirmed) {
                    form.submit();
                }
            });
        });
    });

    // Real-time validation for Basic Details
    $('#fullName').on('input', function() {
        const val = $(this).val().trim();
        if (!val || !fullNameRegex.test(val) || val.length < 2) {
            showError('fullName', val ? (!fullNameRegex.test(val) ? 'Only letters and spaces allowed' : 'Minimum 2 characters') : 'Full name is required');
        } else {
            clearError('fullName');
        }
    });

    $('#username').on('input', function() {
        const val = $(this).val().trim();
        if (!val || !usernameRegex.test(val) || val.length < 3) {
            showError('username', val ? (!usernameRegex.test(val) ? 'Only letters and numbers allowed' : 'Minimum 3 characters') : 'Username is required');
        } else {
            clearError('username');
        }
    });

    $('#password').on('input', function() {
        const val = $(this).val();
        if (!val || !passwordRegex.test(val)) {
            showError('password', val ? 'Must be 8+ characters with uppercase, lowercase, number, and special character' : 'Password is required');
        } else {
            clearError('password');
        }
    });

    $('#phoneNumber').on('input', function() {
        const val = $(this).val().trim();
        if (!val || !phoneRegex.test(val)) {
            showError('phoneNumber', val ? 'Must be 10 digits starting with 7-9' : 'Phone number is required');
        } else {
            clearError('phoneNumber');
        }
    });

    $('#email').on('input', function() {
        const val = $(this).val().trim();
        if (!val || !emailRegex.test(val)) {
            showError('email', val ? 'Must be a valid email address' : 'Email is required');
        } else {
            clearError('email');
        }
    });

    // Real-time validation for Company Details
    $('#companyName').on('input', function() {
        const val = $(this).val().trim();
        if (!val || val.length < 2) {
            showError('companyName', val ? 'Minimum 2 characters' : 'Company name is required');
        } else {
            clearError('companyName');
        }
    });

    $('#companyLogo').on('change', function() {
        const files = $(this)[0].files.length;
        if (files === 0) {
            showError('companyLogo', 'Company logo is required');
        } else {
            clearError('companyLogo');
        }
    });

    $('#companyIndustry').on('change', function() {
        const val = $(this).val();
        if (!val) {
            showError('companyIndustry', 'Please select an industry');
        } else {
            clearError('companyIndustry');
        }
    });

    $('#companyEmail').on('input', function() {
        const val = $(this).val().trim();
        if (!val || !emailRegex.test(val)) {
            showError('companyEmail', val ? 'Must be a valid email address' : 'Company email is required');
        } else {
            clearError('companyEmail');
        }
    });

    $('#companyPhone').on('input', function() {
        const val = $(this).val().trim();
        if (!val || !phoneRegex.test(val)) {
            showError('companyPhone', val ? 'Must be 10 digits starting with 7-9' : 'Company phone is required');
        } else {
            clearError('companyPhone');
        }
    });

    $('#companySize').on('input', function() {
        const val = $(this).val().trim();
        if (!val || !companySizeRegex.test(val)) {
            showError('companySize', val ? 'Must be a range like 0-100' : 'Company size is required');
        } else {
            const [min, max] = val.split('-').map(Number);
            if (min < 0 || max <= min) {
                showError('companySize', 'Invalid range: min must be >= 0 and max must be greater than min');
            } else {
                clearError('companySize');
            }
        }
    });

    $('#location').on('change', function() {
        const val = $(this).val();
        if (!val) {
            showError('location', 'Please select a location');
        } else {
            clearError('location');
        }
    });

    $('#websiteUrl').on('input', function() {
        const val = $(this).val().trim();
        if (!val || !urlRegex.test(val)) {
            showError('websiteUrl', val ? 'Must be a valid URL' : 'Website URL is required');
        } else {
            clearError('websiteUrl');
        }
    });

    $('#foundedAt').on('input', function() {
        const val = $(this).val();
        if (!val || val < 1800 || val > 2025) {
            showError('foundedAt', val ? 'Must be between 1800 and 2025' : 'Founding year is required');
        } else {
            clearError('foundedAt');
        }
    });

    $('#companyAbout').on('input', function() {
        const val = $(this).val().trim();
        if (!val || val.length < 10) {
            showError('companyAbout', val ? 'Minimum 10 characters required' : 'About company is required');
        } else {
            clearError('companyAbout');
        }
    });

    $('#designation').on('input', function() {
        const val = $(this).val().trim();
        if (!val || val.length < 2) {
            showError('designation', val ? 'Minimum 2 characters' : 'Designation is required');
        } else {
            clearError('designation');
        }
    });

    updateProgressBar();
});