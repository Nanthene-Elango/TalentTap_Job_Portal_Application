package com.talenttap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talenttap.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {

	Optional<Users> findByUsername(String username);
	
	boolean existsByUsername(String username);
    boolean existsByEmail(String email);

	Optional<Users> findByUsernameOrEmail(String username, String email);

}
