package com.talenttap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talenttap.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location,Integer>{

}
