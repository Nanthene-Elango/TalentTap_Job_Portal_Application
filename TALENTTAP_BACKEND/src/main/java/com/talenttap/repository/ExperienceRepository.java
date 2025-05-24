package com.talenttap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talenttap.entity.Experience;

public interface ExperienceRepository extends JpaRepository<Experience , Integer>{

	List<Experience> findByJobSeeker_JobSeekerId(Integer id);
}
