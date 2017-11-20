package com.classrecorder.teacherserver.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classrecorder.teacherserver.server.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByUserName(String name);
	
	public User findByEmail(String email);
	
}
