package com.classrecorder.teacherserver.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classrecorder.teacherserver.server.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{

}
