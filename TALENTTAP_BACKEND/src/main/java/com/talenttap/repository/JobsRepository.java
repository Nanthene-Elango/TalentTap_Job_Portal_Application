package com.talenttap.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.talenttap.entity.Employer;
import com.talenttap.entity.JobStatus;
import com.talenttap.entity.Jobs;

public interface JobsRepository extends JpaRepository<Jobs,Integer> {

	List<Jobs> findByEmployer(Employer employer);

    List<Jobs> findTop2ByEmployerAndJobStatusNotAndDeadlineAfterOrderByPostedDateDesc(
            Employer employer, JobStatus jobStatus, LocalDateTime deadline);
    
    @Query("SELECT DISTINCT j FROM Jobs j " +
    	       "LEFT JOIN j.jobType jt " +
    	       "LEFT JOIN j.jobCategory jc " +
    	       "LEFT JOIN j.requiredSkills s " +
    	       "LEFT JOIN j.jobLocation l " +
    	       "WHERE LOWER(j.jobRole) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(j.jobDescription) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(j.roles) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(j.responsibilities) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(j.benefits) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(j.yearsOfExperience) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(CAST(j.workType AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(CAST(j.jobStatus AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(CAST(j.approvalStatus AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(jt.employmentType) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(jc.jobCategory) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(s.skill) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(l.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    	List<Jobs> searchByKeywordNoPagination(@Param("keyword") String keyword);

    
    @Modifying
    @Transactional
    @Query("UPDATE Jobs j SET j.jobStatus = :expiredStatus WHERE j.deadline < :now AND j.jobStatus <> :expiredStatus AND j.employer = :employer")
    int markExpiredJobs(@Param("expiredStatus") JobStatus expiredStatus,
                        @Param("now") LocalDateTime now,
                        @Param("employer") Employer employer);
    
    // Find jobs with "open" status for a specific employer
    //List<Jobs> findByEmployerIdAndJobStatus(int employerId, String status);

}
