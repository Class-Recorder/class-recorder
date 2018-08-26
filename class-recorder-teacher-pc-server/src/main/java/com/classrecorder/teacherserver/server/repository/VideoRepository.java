package com.classrecorder.teacherserver.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.classrecorder.teacherserver.server.entities.Course;
import com.classrecorder.teacherserver.server.entities.YoutubeVideo;

public interface VideoRepository extends JpaRepository<YoutubeVideo, Long>{

    Page<YoutubeVideo> findByCourse(Course course, Pageable pageable);

}
