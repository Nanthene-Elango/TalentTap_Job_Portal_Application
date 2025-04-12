package com.talenttap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talenttap.entity.Education;

@Repository
public interface EducationRepository extends JpaRepository<Education,Integer>{

}
