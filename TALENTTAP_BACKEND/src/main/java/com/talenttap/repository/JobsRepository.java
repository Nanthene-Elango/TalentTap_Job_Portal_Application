package com.talenttap.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talenttap.DTO.EmploymentTypeDTO;
import com.talenttap.entity.Employer;
import com.talenttap.entity.JobStatus;
import com.talenttap.entity.Jobs;

public interface JobsRepository extends JpaRepository<Jobs,Integer> {

	List<Jobs> findByEmployer(Employer employer);

    List<Jobs> findTop2ByEmployerAndJobStatusNotAndDeadlineAfterOrderByPostedDateDesc(
            Employer employer, JobStatus jobStatus, LocalDateTime deadline);

}
