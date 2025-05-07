package com.talenttap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talenttap.DTO.EducationDTO;
import com.talenttap.entity.Education;

@Repository
public interface EducationRepository extends JpaRepository<Education,Integer>{

	List<Education> findByJobSeeker_JobSeekerId(Integer id);

}
