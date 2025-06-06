package com.talenttap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talenttap.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location,Integer>{

	Optional<Location> findByLocation(String location);

}
