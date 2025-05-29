document.addEventListener('DOMContentLoaded', () => {
           // Validation regex patterns
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
                   case 'currentPassword':
                       return setFieldState(field, value.length > 0, 'Current password cannot be empty');
                   case 'newPassword':
                       return setFieldState(field, passwordRegex.test(value), 'Password must meet all requirements');
                   case 'confirmPassword':
                       return setFieldState(field, value === document.getElementById('newPassword').value, 'Passwords do not match');
                   case 'confirmPasswordInput':
                       return setFieldState(field, value.length > 0, 'Password cannot be empty');
                   default:
                       return true;
               }
           }

           // Real-time validation
           const inputs = document.querySelectorAll('#currentPassword, #newPassword, #confirmPassword, #confirmPasswordInput');
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

           // Password & Security
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

           // Account Deactivation
           document.getElementById('deactivateAccountBtn').addEventListener('click', () => {
               document.getElementById('passwordOverlay').style.display = 'flex';
               document.getElementById('confirmBtn').dataset.action = 'deactivate';
           });

           // Account Deletion
           document.getElementById('deleteAccountBtn').addEventListener('click', () => {
               document.getElementById('passwordOverlay').style.display = 'flex';
               document.getElementById('confirmBtn').dataset.action = 'delete';
           });

           // Password Confirmation for Deactivation/Deletion
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

                   const action = document.getElementById('confirmBtn').dataset.action;
                   if (action === 'deactivate') {
                       Swal.fire({
                           icon: 'success',
                           title: 'Account Deactivated',
                           text: 'Your account has been deactivated successfully. You can reactivate it by logging in again.',
                           confirmButtonColor: '#5e17eb',
                           timer: 2000,
                           timerProgressBar: true
                       });
                   } else if (action === 'delete') {
                       Swal.fire({
                           icon: 'success',
                           title: 'Account Deleted',
                           text: 'Your account has been permanently deleted.',
                           confirmButtonColor: '#5e17eb',
                           timer: 2000,
                           timerProgressBar: true
                       });
                   }
               } else {
                   Swal.fire({
                       icon: 'error',
                       title: 'Incorrect Password',
                       text: 'The password you entered is incorrect.',
                       confirmButtonColor: '#5e17eb'
                   });
               }
           });

           // Cancel button for password overlay
           document.getElementById('cancelBtn').addEventListener('click', () => {
               document.getElementById('passwordOverlay').style.display = 'none';
               document.getElementById('confirmPasswordInput').value = '';
               document.getElementById('confirmBtn').dataset.action = '';
           });
       });