package com.talenttap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.talenttap.entity.ApplicationStatus;
import com.talenttap.entity.Employer;
import com.talenttap.entity.JobApplication;
import com.talenttap.entity.JobSeeker;
import com.talenttap.entity.JobStatus;

public interface JobApplicationRepository extends JpaRepository<JobApplication,Integer>{

	 int countByJob_JobId(int jobId);

	 boolean existsByJobSeekerAndJob_JobId(JobSeeker jobSeeker, int jobId);
	 
	 List<JobApplication> findByJob_EmployerAndStatus(Employer employer, ApplicationStatus status);

	 List<JobApplication> findByJob_Employer(Employer employer);

	List<JobApplication> findTop5ByJob_EmployerAndStatusOrderByDateOfApplicationDesc(Employer employer, ApplicationStatus status);

	@Query("SELECT ja FROM JobApplication ja WHERE ja.jobSeeker = :jobSeeker")
    List<JobApplication> findByJobSeeker(@Param("jobSeeker") JobSeeker jobSeeker);
}
