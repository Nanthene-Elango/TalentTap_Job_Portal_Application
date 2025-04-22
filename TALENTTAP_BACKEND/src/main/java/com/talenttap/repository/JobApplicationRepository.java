package com.talenttap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talenttap.entity.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication,Integer>{

	 int countByJob_JobId(int jobId);

}
