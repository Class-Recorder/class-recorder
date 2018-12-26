package com.classrecorder.teacherserver.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.classrecorder.teacherserver.modules.youtube.YoutubeApi;
import com.classrecorder.teacherserver.modules.youtube.YoutubeOutputObserver;
import com.classrecorder.teacherserver.modules.youtube.YoutubeVideoInfo;
import com.classrecorder.teacherserver.modules.youtube.YoutubeApi.YoutubeUploaderState;
import com.classrecorder.teacherserver.server.entities.Course;
import com.classrecorder.teacherserver.server.entities.YoutubeVideo;
import com.classrecorder.teacherserver.server.properties.YoutubeApiProperties;
import com.classrecorder.teacherserver.server.repository.CourseRepository;
import com.classrecorder.teacherserver.server.repository.VideoRepository;
import com.google.api.services.youtube.model.Video;

@Service
public class YoutubeService {
	
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    CourseRepository courseRepository;
    
    private YoutubeApi youtubeApi;

    private final String YOUTUBE_LINK_BASE = "https://youtube.com/watch?v=";

	
	public YoutubeService(YoutubeApiProperties properties) {
		this.youtubeApi = new YoutubeApi(properties);
    }
    
    public void addObserver(YoutubeOutputObserver observer) {
        this.youtubeApi.addObserver(observer);
    }

    public YoutubeUploaderState getState() {
        return this.youtubeApi.getState();
    }

    public double getProgress() {
        return this.youtubeApi.getProgress();
    }
	
    @Async
    public CompletableFuture<Video> uploadVideo(YoutubeVideoInfo ytVideoInfo, long courseId) throws Exception {
        Video video = this.youtubeApi.uploadVideo(ytVideoInfo);
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(!optionalCourse.isPresent()) {
            throw new Exception("Course not found");
        }
        Course course = optionalCourse.get();
        YoutubeVideo youtubeVideo = new YoutubeVideo();
        youtubeVideo.setCourse(course);
        youtubeVideo.setDescription(ytVideoInfo.getDescription());
        youtubeVideo.setTags(ytVideoInfo.getTags());
        youtubeVideo.setTitle(ytVideoInfo.getVideoTitle());
        youtubeVideo.setLink(YOUTUBE_LINK_BASE + video.getId());
        youtubeVideo.setYoutubeId(video.getId());
        youtubeVideo.setImageLink(video.getSnippet().getThumbnails().get("high").getUrl());
        videoRepository.save(youtubeVideo);
        return CompletableFuture.completedFuture(video);
    }

    public Video updateVideo(String youtubeId, YoutubeVideoInfo youtubeVideoInfo) throws Exception {
        return this.youtubeApi.updateVideo(youtubeId, youtubeVideoInfo);
    }

    public boolean deleteVideo(String youtubeId) {
        return this.youtubeApi.deleteVideo(youtubeId);
    }

}
