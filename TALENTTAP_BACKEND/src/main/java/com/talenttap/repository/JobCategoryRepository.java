package com.talenttap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talenttap.entity.JobCategory;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Integer> {

}
