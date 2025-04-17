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

    // Admin data
    const adminData = {
        username: "admin123",
        email: "admin@talenttap.com",
        password: "Secret@123"
    };

    togglePasswordElement.addEventListener('click', togglePassword);

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
            setFieldState(usernameInput, false, !usernameRegex.test(username) ? 'Username must contain only letters and numbers' : 'Username must be at least 3 characters');
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
            setFieldState(passwordInput, false, 'Password must be 8+ characters with uppercase, lowercase, number, and special character');
            isValid = false;
        } else {
            setFieldState(passwordInput, true, '');
        }

        return isValid;
    }

    usernameInput.addEventListener('input', validateUsernameOrEmail);
    passwordInput.addEventListener('input', validatePassword);

    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const isUsernameValid = validateUsernameOrEmail();
        const isPasswordValid = validatePassword();

        if (isUsernameValid && isPasswordValid) {
            const username = usernameInput.value.trim();
            const password = passwordInput.value;

            if ((username === adminData.username || username === adminData.email) && password === adminData.password) {
                console.log('Login successful, redirecting to index.html');
                sessionStorage.setItem('isLoggedIn', 'true');
                window.location.href = 'index.html';
            } else {
                setFieldState(passwordInput, false, 'Invalid username/email or password');
            }
        }
    });
});