package com.classrecorder.teacherserver.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.classrecorder.teacherserver.server.entities.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long>{

    List<Teacher> findByUserName(String userName);

    List<Teacher> findByEmail(String email);

}
