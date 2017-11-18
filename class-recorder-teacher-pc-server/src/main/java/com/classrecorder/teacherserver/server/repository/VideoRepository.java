package com.classrecorder.teacherserver.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classrecorder.teacherserver.server.entities.Video;

public interface VideoRepository extends JpaRepository<Video, Long>{

}
