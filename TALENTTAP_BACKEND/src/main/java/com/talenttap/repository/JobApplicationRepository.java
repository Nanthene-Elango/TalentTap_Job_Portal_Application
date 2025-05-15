package com.talenttap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talenttap.entity.JobApplication;
import com.talenttap.entity.JobSeeker;

public interface JobApplicationRepository extends JpaRepository<JobApplication,Integer>{

	 int countByJob_JobId(int jobId);

	 boolean existsByJobSeekerAndJob_JobId(JobSeeker jobSeeker, int jobId);

}
