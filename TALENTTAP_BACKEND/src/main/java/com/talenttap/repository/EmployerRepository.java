package com.talenttap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talenttap.entity.Employer;
import com.talenttap.entity.Location;
import com.talenttap.entity.Users;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Integer>{

	Optional<Employer> findByUser(Users user);

}
