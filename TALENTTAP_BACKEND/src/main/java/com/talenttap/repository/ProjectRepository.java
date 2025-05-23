package com.talenttap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talenttap.entity.Project;

public interface ProjectRepository extends JpaRepository<Project,Integer>{

	Optional<Project> findByJobSeeker_JobSeekerId(Integer id);

}
