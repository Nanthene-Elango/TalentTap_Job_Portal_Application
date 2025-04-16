package com.talenttap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talenttap.entity.IndustryType;

@Repository
public interface IndustryTypeRepository extends JpaRepository<IndustryType, Integer>{
}
