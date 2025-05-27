document.addEventListener('DOMContentLoaded', () => {
           console.log('Company profile page loaded, initializing modals...');

           // Validation regex patterns
           const companyNameRegex = /^[A-Za-z0-9\s\-.&]+$/;
           const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
           const phoneRegex = /^[7-9]\d{9}$/;
           const designationRegex = /^[A-Za-z\s]+$/;
           const companySizeRegex = /^\d+-\d+$/;

           // Function to set field state
           function setFieldState(field, isValid, errorMessage) {
               const errorElement = field.nextElementSibling;
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
                   case 'companyNameInput':
                       return setFieldState(field, companyNameRegex.test(value) && value.length >= 2, 'Company name must be at least 2 characters');
                   case 'companyIndustryInput':
                       return setFieldState(field, value !== '', 'Please select an industry');
                   case 'companyEmailInput':
                       return setFieldState(field, emailRegex.test(value), 'Please enter a valid email address');
                   case 'companyPhoneInput':
                       return setFieldState(field, phoneRegex.test(value), 'Phone number must be 10 digits starting with 7, 8, or 9');
                   case 'designationInput':
                       return setFieldState(field, designationRegex.test(value) && value.length >= 2, 'Designation must be at least 2 characters (letters only)');
                   case 'companyAboutInput':
                       return setFieldState(field, value.length >= 10, 'Company description must be at least 10 characters');
                   case 'companySizeInput':
                       if (!companySizeRegex.test(value)) {
                           return setFieldState(field, false, 'Must be a range like 0-100');
                       } else {
                           const [min, max] = value.split('-').map(Number);
                           return setFieldState(field, min >= 0 && max > min, 'Invalid range: min must be >= 0 and max must be greater');
                       }
                   case 'locationInput':
                       return setFieldState(field, value !== '', 'Please select a location');
                   case 'companyLogo':
                       return setFieldState(field, field.files.length > 0, 'Please upload a logo');
                   default:
                       return true;
               }
           }

           // Real-time validation
           const inputs = document.querySelectorAll('#companyForm .form-control, #logoForm input[type="file"]');
           inputs.forEach(input => {
               input.addEventListener('input', () => validateField(input));
           });

           // Phone number input restrictions
           const phoneInput = document.getElementById('companyPhoneInput');
           if (phoneInput) {
               phoneInput.addEventListener('keypress', function(e) {
                   if (!/[0-9]/.test(e.key)) e.preventDefault();
                   if (this.value.length >= 10 && e.key !== 'Backspace' && e.key !== 'Delete') e.preventDefault();
                   if (this.value.length === 0 && !['7','8','9'].includes(e.key)) e.preventDefault();
               });
           }

           // Save Company Logo
           document.getElementById('saveLogoBtn').addEventListener('click', () => {
               const fileInput = document.getElementById('companyLogo');
               if (validateField(fileInput)) {
                   const file = fileInput.files[0];
                   if (file) {
                       const companyLogoImg = document.getElementById('companyLogoImg');
                       companyLogoImg.src = URL.createObjectURL(file);
                   }
                   const modal = bootstrap.Modal.getInstance(document.getElementById('editLogoModal'));
                   modal.hide();
                   Swal.fire({
                       icon: 'success',
                       title: 'Saved!',
                       text: 'Company logo updated successfully.',
                       confirmButtonColor: '#5e17eb',
                       timer: 2000
                   });
               } else {
                   Swal.fire({
                       icon: 'error',
                       title: 'Error',
                       text: 'Please upload a company logo.',
                       confirmButtonColor: '#5e17eb'
                   });
               }
           });

           // Save Company Details
           document.getElementById('saveCompanyBtn').addEventListener('click', () => {
               const editFields = document.querySelectorAll('#companyForm .form-control');
               let allValid = true;

               editFields.forEach(field => {
                   if (!validateField(field)) allValid = false;
               });

               if (allValid) {
                   document.getElementById('companyName').textContent = document.getElementById('companyNameInput').value;
                   document.getElementById('companyIndustry').textContent = document.getElementById('companyIndustryInput').options[document.getElementById('companyIndustryInput').selectedIndex].text;
                   document.getElementById('companyEmail').textContent = document.getElementById('companyEmailInput').value;
                   document.getElementById('companyPhone').textContent = document.getElementById('companyPhoneInput').value;
                   document.getElementById('designation').textContent = document.getElementById('designationInput').value;
                   document.getElementById('companyAbout').textContent = document.getElementById('companyAboutInput').value;
                   document.getElementById('companySize').textContent = document.getElementById('companySizeInput').value;
                   document.getElementById('location').textContent = document.getElementById('locationInput').options[document.getElementById('locationInput').selectedIndex].text;

                   const modal = bootstrap.Modal.getInstance(document.getElementById('editCompanyModal'));
                   modal.hide();
                   Swal.fire({
                       icon: 'success',
                       title: 'Saved!',
                       text: 'Company profile updated successfully.',
                       confirmButtonColor: '#5e17eb',
                       timer: 2000
                   });
               } else {
                   Swal.fire({
                       icon: 'error',
                       title: 'Error',
                       text: 'Please correct the errors in the form.',
                       confirmButtonColor: '#5e17eb'
                   });
               }
           });
       });