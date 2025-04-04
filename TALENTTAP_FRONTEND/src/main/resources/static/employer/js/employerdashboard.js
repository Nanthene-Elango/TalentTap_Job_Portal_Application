const isVerified = false; 

const verificationMessage = document.getElementById('verificationMessage');
	if (!isVerified) {
		verificationMessage.style.display = 'flex';
    } else {
          verificationMessage.style.display = 'none';
}