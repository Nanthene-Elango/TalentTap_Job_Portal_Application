function updateStatus(action) {
           const applicantId = document.querySelector('.applicant-card').getAttribute('data-applicant-id');
           const timestamp = new Date().toLocaleString();
           const url = '/api/update-application';

           let data = { applicantId, action, lastUpdated: timestamp };
           if (action === 'approve') {
               data.subject = document.getElementById('approveSubject').value;
               data.message = document.getElementById('approveMessage').value;
           } else if (action === 'reject') {
               data.subject = document.getElementById('rejectSubject').value;
               data.message = document.getElementById('rejectMessage').value;
           } else if (action === 'email') {
               data.subject = document.getElementById('emailSubject').value;
               data.message = document.getElementById('emailMessage').value;
           }

           fetch(url, {
               method: 'POST',
               headers: { 'Content-Type': 'application/json' },
               body: JSON.stringify(data)
           })
           .then(response => response.json())
           .then(result => {
               if (result.success) {
                   document.querySelector('.timestamp').textContent = 'Just now';
                   const badge = document.querySelector('.status-badge');
                   badge.textContent = action.charAt(0).toUpperCase() + action.slice(1);
                   badge.className = `status-badge status-${action}`;
                   const modal = bootstrap.Modal.getInstance(document.querySelector(`#${action}Modal`));
                   modal.hide();
                   alert(`${action.charAt(0).toUpperCase() + action.slice(1)} action completed!`);
               }
           })
           .catch(error => console.error('Error:', error));
       }