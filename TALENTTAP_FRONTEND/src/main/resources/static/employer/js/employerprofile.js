document.addEventListener('DOMContentLoaded', () => {
    // Sidebar navigation
    const sidebarLinks = document.querySelectorAll('.sidebar-link');
    const contentSections = document.querySelectorAll('.content-section');
    sidebarLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            sidebarLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');
            const section = this.getAttribute('data-section');
            contentSections.forEach(s => s.style.display = 'none');
            document.getElementById(section).style.display = 'block';
        });
    });

    // Validation regex patterns
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^[7-9]\d{9}$/;
    const companyNameRegex = /^[A-Za-z0-9\s\-.&]+$/;
    const industryRegex = /^[A-Za-z\s]+$/;
    const designationRegex = /^[A-Za-z\s]+$/;
    const companySizeRegex = /^\d+-\d+$/;
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;

    // Function to set field state
    function setFieldState(field, isValid, errorMessage) {
        let errorElement = field.nextElementSibling;
        if (!errorElement || !errorElement.classList.contains('error-message')) {
            errorElement = document.createElement('div');
            errorElement.className = 'error-message';
            field.parentNode.insertBefore(errorElement, field.nextSibling);
        }
        
        if (isValid) {
            field.classList.remove('error');
            errorElement.textContent = '';
            errorElement.classList.remove('visible');
        } else {
            field.classList.add('error');
            errorElement.textContent = errorMessage;
            errorElement.classList.add('visible');
        }
        return isValid;
    }

    // Validation function
    function validateField(field) {
        const value = field.value.trim();
        switch(field.id) {
            case 'companyEmailInput':
            case 'companyEmailVerification':
                return setFieldState(field, emailRegex.test(value), 'Please enter a valid email address');
            case 'companyPhoneInput':
                return setFieldState(field, phoneRegex.test(value), 'Phone number must be 10 digits starting with 7, 8, or 9');
            case 'companyNameInput':
                return setFieldState(field, companyNameRegex.test(value) && value.length >= 2, 'Company name must be at least 2 characters');
            case 'companyIndustryInput':
                return setFieldState(field, value !== '', 'Please select an industry');
            case 'designationInput':
                return setFieldState(field, designationRegex.test(value) && value.length >= 2, 'Designation must be at least 2 characters (letters only)');
            case 'companyAboutInput':
                return setFieldState(field, value.length >= 10, 'Company description must be at least 10 characters');
            case 'companySizeInput':
                if (!companySizeRegex.test(value)) {
                    return setFieldState(field, false, 'Must be a range like 0-100');
                } else {
                    const [min, max] = value.split('-').map(Number);
                    return setFieldState(field, min >= 0 && max > min, 'Invalid range: min must be >= 0 and max must be greater than min');
                }
            case 'locationInput':
                return setFieldState(field, value !== '', 'Please select a location');
            case 'currentPassword':
                return setFieldState(field, value.length > 0, 'Current password cannot be empty');
            case 'newPassword':
                return setFieldState(field, passwordRegex.test(value), 'Password must meet all requirements');
            case 'confirmPassword':
                return setFieldState(field, value === document.getElementById('newPassword').value, 'Passwords do not match');
            case 'confirmPasswordInput':
                return setFieldState(field, value.length > 0, 'Password cannot be empty');
            case 'govId':
                return setFieldState(field, field.files.length > 0, 'Please upload a Government-Issued ID');
            case 'companyId':
                return setFieldState(field, field.files.length > 0 || document.getElementById('companyEmailVerification').value.trim(), 'Please provide either a Company ID or email');
            default:
                return true;
        }
    }

    // Real-time validation
    const inputs = document.querySelectorAll('.edit-field, #currentPassword, #newPassword, #confirmPassword, #confirmPasswordInput, #companyEmailVerification');
    inputs.forEach(input => {
        input.addEventListener('input', () => validateField(input));
    });

    // Password requirements real-time validation
    const newPasswordInput = document.getElementById('newPassword');
    newPasswordInput.addEventListener('input', () => {
        const value = newPasswordInput.value;
        document.getElementById('length').className = value.length >= 8 ? 'valid' : 'invalid';
        document.getElementById('uppercase').className = /[A-Z]/.test(value) ? 'valid' : 'invalid';
        document.getElementById('lowercase').className = /[a-z]/.test(value) ? 'valid' : 'invalid';
        document.getElementById('number').className = /\d/.test(value) ? 'valid' : 'invalid';
        document.getElementById('special').className = /[!@#$%^&*]/.test(value) ? 'valid' : 'invalid';
        validateField(newPasswordInput);
    });

    // Phone number input restrictions
    const companyPhoneInput = document.getElementById('companyPhoneInput');
    if (companyPhoneInput) {
        companyPhoneInput.addEventListener('keypress', function(e) {
            if (!/[0-9]/.test(e.key)) e.preventDefault();
            if (this.value.length >= 10 && e.key !== 'Backspace' && e.key !== 'Delete') e.preventDefault();
            if (this.value.length === 0 && !['7','8','9'].includes(e.key)) e.preventDefault();
        });
        companyPhoneInput.addEventListener('paste', function(e) {
            e.preventDefault();
            const pasteData = (e.clipboardData || window.clipboardData).getData('text');
            const filteredData = pasteData.replace(/[^0-9]/g, '').substring(0, 10);
            if (filteredData.length === 10 && /^[7-9]/.test(filteredData)) {
                this.value = filteredData;
            }
        });
    }

    // Edit functionality with password confirmation
    let isEditingCompany = false;

    document.getElementById('editCompanyBtn').addEventListener('click', () => {
        document.getElementById('passwordOverlay').style.display = 'flex';
        isEditingCompany = true;
    });

    document.getElementById('cancelBtn').addEventListener('click', () => {
        document.getElementById('passwordOverlay').style.display = 'none';
        document.getElementById('confirmPasswordInput').value = '';
        isEditingCompany = false;
    });

    document.getElementById('confirmBtn').addEventListener('click', () => {
        const password = document.getElementById('confirmPasswordInput');
        if (!validateField(password)) {
            Swal.fire({
                icon: 'error',
                title: 'Password Required',
                text: 'Please enter your password to proceed.',
                confirmButtonColor: '#5e17eb'
            });
            return;
        }

        if (password.value === "password123") { // Simulated correct password
            document.getElementById('passwordOverlay').style.display = 'none';
            document.getElementById('confirmPasswordInput').value = '';

            if (isEditingCompany) {
                const companyCard = document.querySelector('#my-info .content-card:last-of-type');
                const fieldValues = companyCard.querySelectorAll('.field-value');
                const editFields = companyCard.querySelectorAll('.edit-field');
                fieldValues.forEach(el => el.style.display = 'none');
                editFields.forEach(el => el.style.display = 'block');
                document.getElementById('companyLogoDisplay').style.display = 'none';
                document.getElementById('editCompanyLogoField').style.display = 'block';
                document.getElementById('saveCompanyBtn').style.display = 'block';
                document.getElementById('editCompanyBtn').style.display = 'none';
            }
            isEditingCompany = false;
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Incorrect Password',
                text: 'The password you entered is incorrect.',
                confirmButtonColor: '#5e17eb'
            });
        }
    });

    // Save Company Details
    document.getElementById('saveCompanyBtn').addEventListener('click', () => {
        const companyCard = document.querySelector('#my-info .content-card:last-of-type');
        const editFields = companyCard.querySelectorAll('.edit-field');
        let allValid = true;

        editFields.forEach(field => {
            if (!validateField(field)) allValid = false;
        });

        if (allValid) {
            const fieldValues = companyCard.querySelectorAll('.field-value');
            fieldValues.forEach((el) => {
                const inputField = document.getElementById(`${el.id}Input`);
                if (inputField) {
                    if (el.id === 'companyIndustry' || el.id === 'location') {
                        el.textContent = inputField.options[inputField.selectedIndex].text;
                    } else {
                        el.textContent = inputField.value;
                    }
                }
                el.style.display = 'block';
            });

            const fileInput = document.getElementById('companyLogo');
            if (fileInput.files.length > 0) {
                const file = fileInput.files[0];
                const companyLogoImg = document.getElementById('companyLogoImg');
                companyLogoImg.src = URL.createObjectURL(file);
            }

            editFields.forEach(el => el.style.display = 'none');
            document.getElementById('companyLogoDisplay').style.display = 'block';
            document.getElementById('editCompanyLogoField').style.display = 'none';
            document.getElementById('saveCompanyBtn').style.display = 'none';
            document.getElementById('editCompanyBtn').style.display = 'block';

            Swal.fire({
                icon: 'success',
                title: 'Saved!',
                text: 'Company details updated successfully.',
                confirmButtonColor: '#5e17eb',
                timer: 2000,
                timerProgressBar: true
            });
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Validation Error',
                text: 'Please correct the errors in the form.',
                confirmButtonColor: '#5e17eb'
            });
        }
    });

    // Company logo preview
    document.getElementById('companyLogo').addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            const companyLogoImg = document.getElementById('companyLogoImg');
            companyLogoImg.src = URL.createObjectURL(file);
        }
    });

    // Password & Security
    document.getElementById('changePasswordBtn').addEventListener('click', () => {
        document.getElementById('passwordStatus').style.display = 'none';
        document.getElementById('changePasswordFields').style.display = 'block';
        document.getElementById('changePasswordBtn').style.display = 'none';
    });

    document.getElementById('savePasswordBtn').addEventListener('click', () => {
        const current = document.getElementById('currentPassword');
        const newPass = document.getElementById('newPassword');
        const confirmPass = document.getElementById('confirmPassword');

        const isCurrentValid = validateField(current);
        const isNewValid = validateField(newPass);
        const isConfirmValid = validateField(confirmPass);

        if (isCurrentValid && isNewValid && isConfirmValid) {
            if (current.value !== "password123") {
                setFieldState(current, false, 'Current password is incorrect');
                Swal.fire({
                    icon: 'error',
                    title: 'Incorrect Password',
                    text: 'The current password you entered is incorrect.',
                    confirmButtonColor: '#5e17eb'
                });
            } else if (newPass.value !== confirmPass.value) {
                setFieldState(confirmPass, false, 'Passwords do not match');
                Swal.fire({
                    icon: 'error',
                    title: 'Mismatch',
                    text: 'New password and confirmation do not match.',
                    confirmButtonColor: '#5e17eb'
                });
            } else {
                Swal.fire({
                    icon: 'success',
                    title: 'Password Updated!',
                    text: 'Your password has been changed successfully.',
                    confirmButtonColor: '#5e17eb',
                    timer: 2000,
                    timerProgressBar: true
                });
                current.value = '';
                newPass.value = '';
                confirmPass.value = '';
                document.getElementById('passwordStatus').style.display = 'block';
                document.getElementById('changePasswordFields').style.display = 'none';
                document.getElementById('changePasswordBtn').style.display = 'block';
            }
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Validation Error',
                text: 'Please correct the errors in the form.',
                confirmButtonColor: '#5e17eb'
            });
        }
    });

    // Identity Verification
    let govIdUploaded = false;
    let companyIdUploaded = false;

    document.getElementById('govId').addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            const preview = document.getElementById('govIdPreview');
            preview.src = URL.createObjectURL(file);
            preview.style.display = 'block';
            govIdUploaded = true;
            validateField(document.getElementById('govId'));
        }
    });

    document.getElementById('companyId').addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            const preview = document.getElementById('companyIdPreview');
            preview.src = URL.createObjectURL(file);
            preview.style.display = 'block';
            companyIdUploaded = true;
            validateField(document.getElementById('companyId'));
        }
    });

    document.getElementById('submitVerificationBtn').addEventListener('click', () => {
        const govId = document.getElementById('govId');
        const companyId = document.getElementById('companyId');
        const companyEmail = document.getElementById('companyEmailVerification');

        const isGovIdValid = validateField(govId);
        const isCompanyIdOrEmailValid = companyId.files.length > 0 || (companyEmail.value.trim() && validateField(companyEmail));

        if (!isGovIdValid) {
            Swal.fire({
                icon: 'error',
                title: 'Missing ID',
                text: 'Please upload a Government-Issued ID.',
                confirmButtonColor: '#5e17eb'
            });
        } else if (!isCompanyIdOrEmailValid) {
            Swal.fire({
                icon: 'error',
                title: 'Missing Information',
                text: 'Please provide either a Company ID or a valid Company Email.',
                confirmButtonColor: '#5e17eb'
            });
        } else {
            document.getElementById('verificationStatus').textContent = 'Status: Submitted for Verification';
            setTimeout(() => {
                if (govIdUploaded) {
                    document.getElementById('govIdVerified').style.display = 'inline-block';
                }
                if (companyIdUploaded) {
                    document.getElementById('companyIdVerified').style.display = 'inline-block';
                }
                document.getElementById('verificationStatus').textContent = 'Status: Verified';
                document.getElementById('verificationStatus').style.color = '#28a745';
                Swal.fire({
                    icon: 'success',
                    title: 'Submitted!',
                    text: 'Verification documents submitted successfully.',
                    confirmButtonColor: '#5e17eb',
                    timer: 2000,
                    timerProgressBar: true
                });
            }, 2000);
        }
    });
});