package com.talenttap.DTO;

public class DashboardMetrics {

	private int activeJobs;
	private int rejectedApplications;
	private int approvedApplications;
	private int totalApplications;
	public int getActiveJobs() {
		return activeJobs;
	}
	public void setActiveJobs(int activeJobs) {
		this.activeJobs = activeJobs;
	}
	public int getRejectedApplications() {
		return rejectedApplications;
	}
	public void setRejectedApplications(int rejectedApplications) {
		this.rejectedApplications = rejectedApplications;
	}
	public int getApprovedApplications() {
		return approvedApplications;
	}
	public void setApprovedApplications(int approvedApplications) {
		this.approvedApplications = approvedApplications;
	}
	public int getTotalApplications() {
		return totalApplications;
	}
	public void setTotalApplications(int totalApplications) {
		this.totalApplications = totalApplications;
	}

	
}
