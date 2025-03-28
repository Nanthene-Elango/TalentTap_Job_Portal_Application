 // Define togglePassword in the global scope
 function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleIcon = document.querySelector('.toggle-password i');
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleIcon.classList.remove('fa-eye');
        toggleIcon.classList.add('fa-eye-slash');
    } else {
        passwordInput.type = 'password';
        toggleIcon.classList.remove('fa-eye-slash');
        toggleIcon.classList.add('fa-eye');
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('loginForm');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const togglePasswordElement = document.querySelector('.toggle-password');
    const usernameError = document.getElementById('usernameError');
    const passwordError = document.getElementById('passwordError');

    // Attach the togglePassword event listener programmatically
    togglePasswordElement.addEventListener('click', togglePassword);

    // Regex patterns from the registration page
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const usernameRegex = /^[A-Za-z0-9]+$/;
    const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&*])[A-Za-z\d@#$%^&*]{8,}$/;

    function setFieldState(field, isValid, errorMessage) {
        const errorElement = document.getElementById(`${field.id}Error`);
        if (isValid) {
            field.classList.remove('error');
            errorElement.textContent = '';
            errorElement.classList.remove('visible');
        } else {
            field.classList.add('error');
            errorElement.textContent = errorMessage;
            errorElement.classList.add('visible');
        }
    }

    function validateUsernameOrEmail() {
        const username = usernameInput.value.trim();
        let isValid = true;

        if (!username) {
            setFieldState(usernameInput, false, 'Username or email is required');
            isValid = false;
        } else if (!emailRegex.test(username) && (!usernameRegex.test(username) || username.length < 3)) {
            setFieldState(usernameInput, false, !usernameRegex.test(username) ? 'Username must only contain letters and numbers' : 'Must be at least 3 characters');
            isValid = false;
        } else {
            setFieldState(usernameInput, true, '');
        }

        return isValid;
    }

    function validatePassword() {
        const password = passwordInput.value;
        let isValid = true;

        if (!password) {
            setFieldState(passwordInput, false, 'Password is required');
            isValid = false;
        } else if (!passwordRegex.test(password)) {
            setFieldState(passwordInput, false, 'Must be 8+ characters with uppercase, lowercase, number, and special character');
            isValid = false;
        } else {
            setFieldState(passwordInput, true, '');
        }

        return isValid;
    }

    // Real-time validation
    usernameInput.addEventListener('input', validateUsernameOrEmail);
    passwordInput.addEventListener('input', validatePassword);

    // Form submission validation
    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const isUsernameValid = validateUsernameOrEmail();
        const isPasswordValid = validatePassword();

        if (isUsernameValid && isPasswordValid) {
            // Simulate login process
            alert('Login successful!');
            // In a real application, you would send the login request here
        }
    });
});