package com.classrecorder.teacherserver.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classrecorder.teacherserver.server.entities.YoutubeVideo;

public interface VideoRepository extends JpaRepository<YoutubeVideo, Long>{

}
