package com.classrecorder.teacherserver.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classrecorder.teacherserver.server.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long>{

}
