package com.talenttap.DTO;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import com.talenttap.entity.JobApplication;
import com.talenttap.entity.Jobs;

public class CandidatesDTO {
	private int applicantId;
	private String candidateName;
	private String jobName;
	private LocalDateTime appliedOn;
	private LocalDateTime lastUpdatedOn;
	private String appliedAgo;
	private String updatedAgo;
	private String jobType;

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public int getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(int applicantId) {
		this.applicantId = applicantId;
	}

	public String getUpdatedAgo() {
		return updatedAgo;
	}


	public CandidatesDTO(JobApplication application) {
		this.applicantId = application.getJobApplicationId();
		this.jobType = application.getJob().getJobType().getEmploymentType();
		this.candidateName = application.getJobSeeker().getUser().getFullName();
		this.jobName = application.getJob().getJobRole();
		this.appliedOn = application.getDateOfApplication();
		this.lastUpdatedOn = application.getLastUpdated();
		setAppliedAgo(LocalDateTime.now(), application.getDateOfApplication());
		setUpdatedAgo(LocalDateTime.now(), application.getLastUpdated());
	}

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public LocalDateTime getAppliedOn() {
		return appliedOn;
	}

	public void setAppliedOn(LocalDateTime appliedOn) {
		this.appliedOn = appliedOn;
	}

	public LocalDateTime getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(LocalDateTime lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public String getAppliedAgo() {
		return appliedAgo;
	}

	public void setAppliedAgo(LocalDateTime now, LocalDateTime postedDate) {
		Duration duration = Duration.between(postedDate, now);

		String durationString;
		if (duration.toDays() >= 1) {
			durationString = duration.toDays() + " days";
		} else {
			durationString = duration.toHours() + " hours";
		}

		this.appliedAgo = durationString;

	}

	public void setUpdatedAgo(LocalDateTime now, LocalDateTime postedDate) {
		Duration duration = Duration.between(postedDate, now);

		String durationString;
		if (duration.toDays() >= 1) {
			durationString = duration.toDays() + " days";
		} else {
			durationString = duration.toHours() + " hours";
		}

		this.updatedAgo = durationString;
	}

}


