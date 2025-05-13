function goToStep(step) {
	// Hide all steps
	document.getElementById('step1').style.display = 'none';
	document.getElementById('step2').style.display = 'none';
	document.getElementById('step3').style.display = 'none';
	document.getElementById('success').style.display = 'none';

	// Hide all buttons
	document.getElementById('step1-buttons').style.display = 'none';
	document.getElementById('step2-buttons').style.display = 'none';
	document.getElementById('step3-buttons').style.display = 'none';

	// Reset all indicators
	document.getElementById('step1-indicator').className = 'step';
	document.getElementById('step2-indicator').className = 'step';
	document.getElementById('step3-indicator').className = 'step';

	// Show selected step
	document.getElementById('step' + step).style.display = 'block';
	document.getElementById('step' + step + '-buttons').style.display = 'flex';

	// Update indicators
	for (let i = 1; i <= step; i++) {
		if (i < step) {
			document.getElementById('step' + i + '-indicator').className = 'step completed';
		} else {
			document.getElementById('step' + i + '-indicator').className = 'step active';
		}
	}
}

function showSuccess() {
	// Hide all steps and buttons
	document.getElementById('step1').style.display = 'none';
	document.getElementById('step2').style.display = 'none';
	document.getElementById('step3').style.display = 'none';
	document.getElementById('step1-buttons').style.display = 'none';
	document.getElementById('step2-buttons').style.display = 'none';
	document.getElementById('step3-buttons').style.display = 'none';

	// Show success message
	document.getElementById('success').style.display = 'block';

	// Update indicators
	document.getElementById('step1-indicator').className = 'step completed';
	document.getElementById('step2-indicator').className = 'step completed';
	document.getElementById('step3-indicator').className = 'step completed';
}