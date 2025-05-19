package com.talenttap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talenttap.entity.EducationLevel;
import com.talenttap.entity.Location;
public interface EducationLevelRepository extends JpaRepository<EducationLevel,Integer>{

	Optional<EducationLevel> findByEducationLevel(String educationLevel);

}
