$(document).ready(function () {
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
    const textRegex = /^[A-Za-z\s]+$/;


    function updateProgressBar() {
        $('.progress-step').each(function (index) {
            $(this).toggleClass('active', index < currentStep);
        });
    }

    function showStep(stepNumber) {
        $('.form-step').removeClass('active');
        $(`#${getStepName(stepNumber)}Step`).addClass('active');
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

     // Skills data
     const skills = [
        { value: 'javascript', label: 'JavaScript' },
        { value: 'python', label: 'Python' },
        { value: 'java', label: 'Java' },
        { value: 'html', label: 'HTML' },
        { value: 'css', label: 'CSS' },
        { value: 'sql', label: 'SQL' },
        { value: 'react', label: 'React' },
        { value: 'nodejs', label: 'Node.js' },
        { value: 'communication', label: 'Communication' },
        { value: 'teamwork', label: 'Teamwork' }
    ];

    let selectedSkills = [];

    // Track interaction state for all fields across steps
    const interactionState = {
        // Basic Details
        fullName: false,
        username: false,
        email: false,
        phoneNumber: false,
        password: false,
        // Professional Details
        yearsOfExperience: false,
        location: false,
        // Education Details
        highestQualification: false,
        boardOfStudy: false,
        degree: false,
        university: false,
        startYear: false,
        endYear: false,
        percentage: false,
        // Key Skills
        keySkills: false
    };

    // Populate skills list
    skills.forEach(skill => {
        $('#skillsList').append(`
            <div class="skill-option">
                <input type="checkbox" value="${skill.value}" id="skill-${skill.value}">
                <label for="skill-${skill.value}">${skill.label}</label>
            </div>
        `);
    });

    // Handle skill selection
    $('#skillsList').on('change', 'input[type="checkbox"]', function() {
        const skillValue = $(this).val();
        const skillLabel = $(this).next('label').text();

        if ($(this).is(':checked')) {
            selectedSkills.push({ value: skillValue, label: skillLabel });
        } else {
            selectedSkills = selectedSkills.filter(skill => skill.value !== skillValue);
        }

        interactionState.keySkills = true;
        updateSelectedSkills();
        validateKeySkills();
    });

    // Handle remove skill from tag
    $('#selectedSkills').on('click', '.remove-skill', function() {
        const skillValue = $(this).data('value');
        selectedSkills = selectedSkills.filter(skill => skill.value !== skillValue);
        $(`#skill-${skillValue}`).prop('checked', false);
        interactionState.keySkills = true;
        updateSelectedSkills();
        validateKeySkills();
    });

    // Handle clear all skills
    $('#clearSkills').on('click', function() {
        selectedSkills = [];
        $('#skillsList input[type="checkbox"]').prop('checked', false);
        interactionState.keySkills = true;
        updateSelectedSkills();
        validateKeySkills();
    });

    function updateSelectedSkills() {
        $('#selectedSkills').empty();
        selectedSkills.forEach(skill => {
            $('#selectedSkills').append(`
                <div class="skill-tag">
                    ${skill.label}
                    <span class="remove-skill" data-value="${skill.value}">×</span>
                </div>
            `);
        });
    }

    // Populate year dropdowns
    const currentYear = new Date().getFullYear();
    for (let year = 1980; year <= currentYear; year++) {
        $('#startYear').append(`<option value="${year}">${year}</option>`);
    }
    $('#endYear').append(`<option value="present">Present</option>`);
    for (let year = 1980; year <= currentYear; year++) {
        $('#endYear').append(`<option value="${year}">${year}</option>`);
    }

    // Update end year dropdown based on start year
    $('#startYear').on('change', function() {
        const startYear = parseInt($(this).val());
        $('#endYear').empty();
        $('#endYear').append(`<option value="" disabled selected>Select end year</option>`);
        $('#endYear').append(`<option value="present">Present</option>`);
        for (let year = startYear; year <= currentYear; year++) {
            $('#endYear').append(`<option value="${year}">${year}</option>`);
        }
        interactionState.startYear = true;
        validateEducationDetails();
    });

    // Show/hide conditional fields based on highest qualification
    $('#highestQualification').on('change', function() {
        const value = $(this).val();
        if (value === '10th' || value === '12th') {
            $('#boardOfStudyGroup').show();
            $('#degreeGroup').hide();
            $('#degree').val('');
            interactionState.boardOfStudy = false;
            interactionState.degree = false;
        } else if (value === 'ug' || value === 'pg') {
            $('#boardOfStudyGroup').hide();
            $('#degreeGroup').show();
            $('#boardOfStudy').val('');
            interactionState.boardOfStudy = false;
            interactionState.degree = false;
        } else {
            $('#boardOfStudyGroup').hide();
            $('#degreeGroup').hide();
            $('#boardOfStudy').val('');
            $('#degree').val('');
            interactionState.boardOfStudy = false;
            interactionState.degree = false;
        }
        interactionState.highestQualification = true;
        validateEducationDetails();
    });

    // Professional Details
    $('#yearsOfExperience').on('input', function() {
        interactionState.yearsOfExperience = true;
        validateProfessionalDetails();
    });
    $('#location').on('change', function() {
        interactionState.location = true;
        validateProfessionalDetails();
    });

    // Education Details
    $('#boardOfStudy').on('input', function() {
        interactionState.boardOfStudy = true;
        validateEducationDetails();
    });
    $('#degree').on('input', function() {
        interactionState.degree = true;
        validateEducationDetails();
    });
    $('#university').on('input', function() {
        interactionState.university = true;
        validateEducationDetails();
    });
    $('#endYear').on('change', function() {
        interactionState.endYear = true;
        validateEducationDetails();
    });
    $('#percentage').on('input', function() {
        interactionState.percentage = true;
        validateEducationDetails();
    });

    function setSkillsListState(isValid, errorMessage) {
        const skillsList = $('#skillsList');
        const errorElement = $('#keySkillsError');
        if (isValid) {
            skillsList.removeClass('error').addClass('valid');
            errorElement.text('').removeClass('visible');
        } else {
            skillsList.removeClass('valid').addClass('error');
            errorElement.text(errorMessage).addClass('visible');
        }
    }

    function setFieldState(field, isValid, errorMessage) {
        const errorElement = $(`#${field.attr('id')}Error`);
        if (isValid) {
            field.removeClass('error').addClass('valid');
            errorElement.text('').removeClass('visible');
        } else {
            field.removeClass('valid').addClass('error');
            errorElement.text(errorMessage).addClass('visible');
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

    function validateProfessionalDetails() {
        let isValid = true;
        const yearsOfExperience = $('#yearsOfExperience').val();
        const location = $('#location').val();

        // Years of Experience
        if (yearsOfExperience === '' && interactionState.yearsOfExperience) {
            setFieldState($('#yearsOfExperience'), false, 'Years of experience is required');
            isValid = false;
        } else if (yearsOfExperience && yearsOfExperience < 0) {
            setFieldState($('#yearsOfExperience'), false, 'Cannot be negative');
            isValid = false;
        } else if (yearsOfExperience) {
            setFieldState($('#yearsOfExperience'), true, '');
        } else {
            $('#yearsOfExperienceError').text('').removeClass('visible');
            $('#yearsOfExperience').removeClass('error valid');
        }

        // Location
        if (!location && interactionState.location) {
            setFieldState($('#location'), false, 'Location is required');
            isValid = false;
        } else if (location) {
            setFieldState($('#location'), true, '');
        } else {
            $('#locationError').text('').removeClass('visible');
            $('#location').removeClass('error valid');
        }

        $(`#professionalDetailsStep .next-btn`).prop('disabled', !isValid);
        return isValid;
    }

    function validateEducationDetails() {
        let isValid = true;
        const highestQualification = $('#highestQualification').val();
        const boardOfStudy = $('#boardOfStudy').val().trim();
        const degree = $('#degree').val().trim();
        const university = $('#university').val().trim();
        const startYear = $('#startYear').val();
        const endYear = $('#endYear').val();
        const percentage = $('#percentage').val();

        // Highest Qualification
        if (!highestQualification && interactionState.highestQualification) {
            setFieldState($('#highestQualification'), false, 'Highest qualification is required');
            isValid = false;
        } else if (highestQualification) {
            setFieldState($('#highestQualification'), true, '');
        } else {
            $('#highestQualificationError').text('').removeClass('visible');
            $('#highestQualification').removeClass('error valid');
        }

        // Board of Study (for 10th/12th)
        if (highestQualification === '10th' || highestQualification === '12th') {
            if (!boardOfStudy && interactionState.boardOfStudy) {
                setFieldState($('#boardOfStudy'), false, 'Board of study is required');
                isValid = false;
            } else if (boardOfStudy && !textRegex.test(boardOfStudy)) {
                setFieldState($('#boardOfStudy'), false, 'Must only contain letters and spaces');
                isValid = false;
            } else if (boardOfStudy && boardOfStudy.length < 2) {
                setFieldState($('#boardOfStudy'), false, 'Must be at least 2 characters');
                isValid = false;
            } else if (boardOfStudy) {
                setFieldState($('#boardOfStudy'), true, '');
            } else {
                $('#boardOfStudyError').text('').removeClass('visible');
                $('#boardOfStudy').removeClass('error valid');
            }
        }

        // Degree (for UG/PG)
        if (highestQualification === 'ug' || highestQualification === 'pg') {
            if (!degree && interactionState.degree) {
                setFieldState($('#degree'), false, 'Degree is required');
                isValid = false;
            } else if (degree && !textRegex.test(degree)) {
                setFieldState($('#degree'), false, 'Must only contain letters and spaces');
                isValid = false;
            } else if (degree && degree.length < 2) {
                setFieldState($('#degree'), false, 'Must be at least 2 characters');
                isValid = false;
            } else if (degree) {
                setFieldState($('#degree'), true, '');
            } else {
                $('#degreeError').text('').removeClass('visible');
                $('#degree').removeClass('error valid');
            }
        }

        // University
        if (!university && interactionState.university) {
            setFieldState($('#university'), false, 'University/institution is required');
            isValid = false;
        } else if (university && !textRegex.test(university)) {
            setFieldState($('#university'), false, 'Must only contain letters and spaces');
            isValid = false;
        } else if (university && university.length < 2) {
            setFieldState($('#university'), false, 'Must be at least 2 characters');
            isValid = false;
        } else if (university) {
            setFieldState($('#university'), true, '');
        } else {
            $('#universityError').text('').removeClass('visible');
            $('#university').removeClass('error valid');
        }

        // Start Year
        if (!startYear && interactionState.startYear) {
            setFieldState($('#startYear'), false, 'Starting year is required');
            isValid = false;
        } else if (startYear) {
            setFieldState($('#startYear'), true, '');
        } else {
            $('#startYearError').text('').removeClass('visible');
            $('#startYear').removeClass('error valid');
        }

        // End Year
        if (!endYear && interactionState.endYear) {
            setFieldState($('#endYear'), false, 'End year is required');
            isValid = false;
        } else if (endYear && endYear !== 'present' && parseInt(endYear) < parseInt(startYear)) {
            setFieldState($('#endYear'), false, 'End year cannot be before start year');
            isValid = false;
        } else if (endYear) {
            setFieldState($('#endYear'), true, '');
        } else {
            $('#endYearError').text('').removeClass('visible');
            $('#endYear').removeClass('error valid');
        }

        // Percentage
        if (percentage === '' && interactionState.percentage) {
            setFieldState($('#percentage'), false, 'Percentage is required');
            isValid = false;
        } else if (percentage && (percentage < 0 || percentage > 100)) {
            setFieldState($('#percentage'), false, 'Must be between 0 and 100');
            isValid = false;
        } else if (percentage) {
            setFieldState($('#percentage'), true, '');
        } else {
            $('#percentageError').text('').removeClass('visible');
            $('#percentage').removeClass('error valid');
        }

        $(`#educationDetailsStep .next-btn`).prop('disabled', !isValid);
        return isValid;
    }

    function validateKeySkills() {
        let isValid = true;

        if (selectedSkills.length === 0 && interactionState.keySkills) {
            setSkillsListState(false, 'Please select at least one skill');
            isValid = false;
        } else if (selectedSkills.length > 0) {
            setSkillsListState(true, '');
        } else {
            $('#keySkillsError').text('').removeClass('visible');
            $('#skillsList').removeClass('error valid');
        }

        $('#submitBtn').prop('disabled', !isValid);
        return isValid;
    }

    $('.next-btn').on('click', function () {
        const stepNumber = parseInt($(this).data('step'));
        let isValid = false;

        if (stepNumber === 1) isValid = validateBasicDetails();
        else if (stepNumber === 2 && phoneVerified) isValid = true;
        else if (stepNumber === 3 && emailVerified) isValid = true;
        else if (stepNumber === 4) isValid = validateProfessionalDetails();
        else if (stepNumber === 5) isValid = validateEducationDetails();
        else if (stepNumber === 6) {
            isValid = validateKeySkills();
            if (isValid) {
                $('#summaryDetails').html(`
                    <p><strong>Name:</strong> ${$('#fullName').val()}</p>
                    <p><strong>Username:</strong> ${$('#username').val()}</p>
                    <p><strong>Password:</strong> ${'*'.repeat($('#password').val().length)}</p>
                    <p><strong>Email:</strong> ${$('#email').val()}</p>
                    <p><strong>Phone:</strong> ${$('#phoneNumber').val()}</p>
                `);
            }
        }

        if (isValid) {
            currentStep++;
            showStep(currentStep);
            updateProgressBar();
        }
    });

    $('.prev-btn').on('click', function () {
        currentStep--;
        showStep(currentStep);
        updateProgressBar();
    });

    $('#phoneVerifyBtn').on('click', sendPhoneOTP);
    $('#phoneNextBtn').on('click', function () { verifyOTP('phone'); });
    $('#phoneResendBtn').on('click', function () {
        sendPhoneOTP();
    });

    $('#emailVerifyBtn').on('click', sendEmailOTP);
    $('#emailNextBtn').on('click', function () { verifyOTP('email'); });
    $('#emailResendBtn').on('click', function () {
        sendEmailOTP();
    });

    $('.otp-input').on('input', function () {
        if ($(this).val().length === 1) $(this).next('.otp-input').focus();
    }).on('keydown', function (e) {
        if (e.key === 'Backspace' && !$(this).val()) $(this).prev('.otp-input').focus();
    });

    $('#fullName, #username, #password').on('input', validateBasicDetails);
    $('#phoneNumber').on('input', validatePhoneNumber);
    $('#email').on('input', validateEmail);

    $('#registrationForm').on('submit', function (e) {
        e.preventDefault();
        if (currentStep !== 7 || !validateBasicDetails() || !validatePhoneNumber() ||
            !validateEmail() || !validateProfessionalDetails() || !validateEducationDetails()|| !validateKeySkills()|| !emailVerified || !phoneVerified) {
            alert('Please complete all steps and verifications.');
            return;
        }
        alert('Registration Successful!');
    });
});