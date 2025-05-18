package com.talenttap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.talenttap.entity.Employer;
import com.talenttap.entity.JobApplication;
import com.talenttap.entity.JobSeeker;
import com.talenttap.projections.JobApplicantCountProjection;

public interface JobApplicationRepository extends JpaRepository<JobApplication,Integer>{

	 int countByJob_JobId(int jobId);

	 boolean existsByJobSeekerAndJob_JobId(JobSeeker jobSeeker, int jobId);
	 
	 @Query("SELECT ja.job.jobId AS jobId, COUNT(ja) AS applicantCount " +
	           "FROM JobApplication ja WHERE ja.job.jobId IN :jobIds GROUP BY ja.job.jobId")
	    List<JobApplicantCountProjection> countApplicantsForJobs(@Param("jobIds") List<Integer> jobIds);
	 
	 
	 List<JobApplication> findByJob_Employer(Employer employer);

}
