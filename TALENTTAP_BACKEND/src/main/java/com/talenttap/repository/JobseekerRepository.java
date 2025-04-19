package com.talenttap.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.talenttap.entity.JobSeeker;
import com.talenttap.entity.Users;
@Repository
public interface JobseekerRepository extends JpaRepository<JobSeeker , Integer> {

	Optional<JobSeeker> findByUser(Users user);

}
