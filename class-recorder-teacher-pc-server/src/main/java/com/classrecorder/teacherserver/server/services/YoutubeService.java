package com.classrecorder.teacherserver.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import com.classrecorder.teacherserver.modules.youtube.YoutubeOutputObserver;
import com.classrecorder.teacherserver.modules.youtube.YoutubeUploader;
import com.classrecorder.teacherserver.modules.youtube.YoutubeVideoInfo;
import com.classrecorder.teacherserver.modules.youtube.YoutubeUploader.YoutubeUploaderState;
import com.classrecorder.teacherserver.server.entities.Course;
import com.classrecorder.teacherserver.server.entities.YoutubeVideo;
import com.classrecorder.teacherserver.server.properties.YoutubeApiProperties;
import com.classrecorder.teacherserver.server.repository.CourseRepository;
import com.classrecorder.teacherserver.server.repository.VideoRepository;
import com.google.api.services.youtube.model.Video;

@Service
public class YoutubeService {
	
    @Autowired VideoRepository videoRepository;

    @Autowired CourseRepository courseRepository;
    
    private YoutubeUploader youtubeUploader;

	
	public YoutubeService(YoutubeApiProperties properties) {
		this.youtubeUploader = new YoutubeUploader(properties);
    }
    
    public void addObserver(YoutubeOutputObserver observer) {
        this.youtubeUploader.addObserver(observer);
    }

    public YoutubeUploaderState getState() {
        return this.youtubeUploader.getState();
    }

    public double getProgress() {
        return this.youtubeUploader.getProgress();
    }
	
    @Async
    public CompletableFuture<Video> uploadVideo(YoutubeVideoInfo ytVideoInfo, long courseId) throws Exception {
        Video video = this.youtubeUploader.uploadVideo(ytVideoInfo);
        Course course = courseRepository.findOne(courseId);
        YoutubeVideo youtubeVideo = new YoutubeVideo();
        youtubeVideo.setCourse(course);
        youtubeVideo.setDescription(ytVideoInfo.getDescription());
        youtubeVideo.setTags(ytVideoInfo.getTags());
        youtubeVideo.setTitle(ytVideoInfo.getVideoTitle());
        youtubeVideo.setYoutubeId(video.getId());
        videoRepository.save(youtubeVideo);
        return CompletableFuture.completedFuture(video);
    }

}
