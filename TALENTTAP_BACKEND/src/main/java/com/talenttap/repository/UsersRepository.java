package com.talenttap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talenttap.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {

}
