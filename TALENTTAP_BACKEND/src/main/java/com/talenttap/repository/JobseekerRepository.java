package com.talenttap.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.talenttap.entity.JobSeeker;
@Repository
public interface JobseekerRepository extends JpaRepository<JobSeeker , Integer> {

}
