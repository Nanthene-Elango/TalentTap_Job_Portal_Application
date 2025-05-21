package com.talenttap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talenttap.entity.Certification;

public interface CertificationRepository extends JpaRepository<Certification,Integer> {

	List<Certification> findByJobSeeker_JobSeekerId(Integer id);
	
}
