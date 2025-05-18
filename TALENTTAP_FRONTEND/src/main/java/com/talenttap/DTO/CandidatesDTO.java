package com.talenttap.DTO;

import java.time.LocalDateTime;

public class CandidatesDTO {
	private int applicantId;
	private String candidateName;
	private String jobName;
	private LocalDateTime appliedOn;
	private LocalDateTime lastUpdatedOn;
	private String appliedAgo;
	private String updatedAgo;
	private String jobType;

	public int getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(int applicantId) {
		this.applicantId = applicantId;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
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
	public void setAppliedAgo(String appliedAgo) {
		this.appliedAgo = appliedAgo;
	}
	public String getUpdatedAgo() {
		return updatedAgo;
	}
	public void setUpdatedAgo(String updatedAgo) {
		this.updatedAgo = updatedAgo;
	}

}
