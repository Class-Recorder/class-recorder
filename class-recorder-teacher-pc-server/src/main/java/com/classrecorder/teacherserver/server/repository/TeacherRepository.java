package com.classrecorder.teacherserver.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classrecorder.teacherserver.server.entities.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long>{

}
