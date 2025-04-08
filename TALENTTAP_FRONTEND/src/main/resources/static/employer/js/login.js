function togglePassword() {
	const passwordInput = document.getElementById('password');
	const toggleIcon = document.querySelector('.toggle-password i');
	if (passwordInput.type === 'password') {
		passwordInput.type = 'text';
		toggleIcon.classList.replace('fa-eye', 'fa-eye-slash');
	} else {
		passwordInput.type = 'password';
		toggleIcon.classList.replace('fa-eye-slash', 'fa-eye');
	}
}

document.addEventListener('DOMContentLoaded', function() {
	const form = document.getElementById('loginForm');
	const usernameInput = document.getElementById('username');
	const passwordInput = document.getElementById('password');
	const togglePasswordElement = document.querySelector('.toggle-password');
	const usernameError = document.getElementById('usernameError');
	const passwordError = document.getElementById('passwordError');

	// Toggle password visibility
	togglePasswordElement.addEventListener('click', togglePassword);

	// Simple validation functions
	function validateUsernameOrEmail() {
		const value = usernameInput.value.trim();
		if (!value) {
			usernameInput.classList.add('error');
			usernameError.textContent = 'Username or email is required';
			usernameError.classList.add('visible');
			return false;
		}
		usernameInput.classList.remove('error');
		usernameError.textContent = '';
		usernameError.classList.remove('visible');
		return true;
	}

	function validatePassword() {
		const value = passwordInput.value;
		if (!value) {
			passwordInput.classList.add('error');
			passwordError.textContent = 'Password is required';
			passwordError.classList.add('visible');
			return false;
		}
		passwordInput.classList.remove('error');
		passwordError.textContent = '';
		passwordError.classList.remove('visible');
		return true;
	}

	// Real-time validation
	usernameInput.addEventListener('input', validateUsernameOrEmail);
	passwordInput.addEventListener('input', validatePassword);

	// Form submission
	form.addEventListener('submit', function(event) {
		event.preventDefault();

		const isUsernameValid = validateUsernameOrEmail();
		const isPasswordValid = validatePassword();

		if (isUsernameValid && isPasswordValid) {
			// Here you would typically send the data to your server
			console.log('Form is valid, submitting...');
			window.location.href = "/employer/employerDashboard";
		}
	});
});