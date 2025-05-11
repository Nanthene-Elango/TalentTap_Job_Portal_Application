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

    if (togglePasswordElement) {
        togglePasswordElement.addEventListener('click', togglePassword);
    }

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
        if (!username) {
            setFieldState(usernameInput, false, 'Username or email is required');
            return false;
        }
        if (!emailRegex.test(username) && (!usernameRegex.test(username) || username.length < 3)) {
            setFieldState(usernameInput, false, 'Invalid username or email');
            return false;
        }
        setFieldState(usernameInput, true, '');
        return true;
    }

    function validatePassword() {
        const password = passwordInput.value;
        if (!password) {
            setFieldState(passwordInput, false, 'Password is required');
            return false;
        }
        if (!passwordRegex.test(password)) {
            setFieldState(passwordInput, false, 'Must be 8+ characters with uppercase, lowercase, number, and special character');
            return false;
        }
        setFieldState(passwordInput, true, '');
        return true;
    }

    usernameInput.addEventListener('input', validateUsernameOrEmail);
    passwordInput.addEventListener('input', validatePassword);

    form.addEventListener('submit', function(event) {
        const isUsernameValid = validateUsernameOrEmail();
        const isPasswordValid = validatePassword();

        if (!isUsernameValid || !isPasswordValid) {
            event.preventDefault();
        }
    });
});