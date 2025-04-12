package com.talenttap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talenttap.entity.Roles;

public interface RoleRepository extends JpaRepository<Roles , Integer>{

	Roles findByRole(String string);

}
