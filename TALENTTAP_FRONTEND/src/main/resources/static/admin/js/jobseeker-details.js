$(document).ready(function() {
    // Handle resume download
    $('#resume').on('click', function(e) {
        e.preventDefault();
        const resumeBase64 = $(this).data('resume');
        if (resumeBase64) {
            try {
                const byteCharacters = atob(resumeBase64);
                const byteNumbers = new Array(byteCharacters.length);
                for (let i = 0; i < byteCharacters.length; i++) {
                    byteNumbers[i] = byteCharacters.charCodeAt(i);
                }
                const byteArray = new Uint8Array(byteNumbers);
                const blob = new Blob([byteArray], { type: 'application/pdf' });
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'resume.pdf';
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                window.URL.revokeObjectURL(url);
            } catch (error) {
                console.error('Error downloading resume:', error);
                alert('Failed to download resume. Please try again.');
            }
        }
    });
});