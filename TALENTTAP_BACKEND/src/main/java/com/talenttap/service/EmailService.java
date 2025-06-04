package com.talenttap.service;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.talenttap.entity.JobSeeker;
import com.talenttap.entity.Jobs;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	public void jobAppliedMail(JobSeeker jobseeker, Jobs job) throws MessagingException {
	    
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);

	    helper.setTo(jobseeker.getUser().getEmail());
	    helper.setSubject("Your Job Application Has Been Successfully Submitted!");

	    // Plain Text (optional fallback)
	    String plainText = "Hello " + jobseeker.getUser().getFullName() + "!\n\n"
	            + "Thank you for applying to the position of " + job.getJobRole() + " at " + job.getEmployer().getCompany().getCompanyName() + ".\n"
	            + "Your application has been successfully submitted through TalentTap.\n\n"
	            + "We wish you the best of luck in your job search!\n\n"
	            + "Best regards,\n"
	            + "TalentTap Team";

	    // HTML Email Content
	    String htmlContent = "<html><body>"
	            + "<p>Hello " + jobseeker.getUser().getFullName() + "!</p>"
	            + "<p>Thank you for applying to the position of <strong>" + job.getJobRole() + "</strong> at <strong>" + job.getEmployer().getCompany().getCompanyName() + "</strong>.</p>"
	            + "<p>Your application has been <strong>successfully submitted</strong> through <strong>TalentTap</strong>.</p>"
	            + "<p>We wish you the very best in the hiring process!</p>"
	            + "<p>Best regards,<br><strong>TalentTap Team</strong></p>"
	            + "</body></html>";

	    helper.setText(plainText, htmlContent);

	    helper.setFrom("nantheneelango@gmail.com"); 

	    mailSender.send(message);

	    System.out.println("Job application mail sent successfully!");
	}

	
}
