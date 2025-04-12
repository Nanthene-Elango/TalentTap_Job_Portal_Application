package com.talenttap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talenttap.entity.Skills;

@Repository
public interface SkillsRepository extends JpaRepository<Skills,Integer> {

}
