package com.talenttap.model;

public class EmployerDashBoardMetrics {
	private int activeJobs;
	private int closeJobs;
	private int expiredJobs;
	private int pendingApplication;
	private int totalJobs;
	private int rejectedApplications;
	private int approvedApplications;
	private int totalApplications;
	public int getActiveJobs() {
		return activeJobs;
	}
	public int getCloseJobs() {
		return closeJobs;
	}
	public void setCloseJobs(int closeJobs) {
		this.closeJobs = closeJobs;
	}
	public int getExpiredJobs() {
		return expiredJobs;
	}
	public void setExpiredJobs(int expiredJobs) {
		this.expiredJobs = expiredJobs;
	}
	public int getPendingApplication() {
		return pendingApplication;
	}
	public void setPendingApplication(int pendingApplication) {
		this.pendingApplication = pendingApplication;
	}
	public int getTotalJobs() {
		return totalJobs;
	}
	public void setTotalJobs(int totalJobs) {
		this.totalJobs = totalJobs;
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
