package com.classrecorder.teacherserver.server.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import com.classrecorder.teacherserver.modules.youtube.YoutubeVideoInfo;
import com.classrecorder.teacherserver.server.ClassRecProperties;
import com.classrecorder.teacherserver.server.forms.YoutubeVideoForm;
import com.classrecorder.teacherserver.server.services.YoutubeService;
import com.classrecorder.teacherserver.server.websockets.youtube.WebSocketYoutubeUpload;
import com.classrecorder.teacherserver.util.LocalVideosReader;
import com.google.api.services.youtube.model.Video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YoutubeController {

    private final String REQUEST_FILE_API_URL = "/api/uploadVideo/";
    private final Path videosFolder = ClassRecProperties.videosFolder;
    private CompletableFuture<Video> videoUploadingTask;

    @Autowired
    private YoutubeService youtubeService;

    @Autowired
    private WebSocketYoutubeUpload wsYoutubeProgress;

    @PostConstruct
	public void init() {
		this.youtubeService.addObserver(wsYoutubeProgress);
	}

    @RequestMapping(value = "/api/cancelUpload/", method=RequestMethod.GET)
    public ResponseEntity<?> cancelUpload() {
        if(videoUploadingTask != null && !videoUploadingTask.isDone()) {
            videoUploadingTask.cancel(true);
            videoUploadingTask = null;
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>("There is no process uploading.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value= "/api/getStateYoutubeUpload", method=RequestMethod.GET)
    public ResponseEntity<?> getStateYoutubeUploader() {
        return new ResponseEntity<>(youtubeService.getState().toString(), HttpStatus.OK);
    }

    @RequestMapping(value= "/api/getYoutubeUploadProgress", method=RequestMethod.GET)
    public ResponseEntity<?> getYoutubeUploadProgress() {
        return new ResponseEntity<>(youtubeService.getProgress(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/uploadVideo/{courseId}/{fileName}/{containerFormat}", method=RequestMethod.POST)
    public ResponseEntity<?> uploadVideo(@RequestBody YoutubeVideoForm formBody, @PathVariable long courseId, @PathVariable String fileName, @PathVariable String containerFormat) throws Exception {
        Path videoPath = videosFolder.resolve(fileName + "." + containerFormat);
        if(!Files.exists(videoPath) && LocalVideosReader.isVideo(videoPath)) {
            return new ResponseEntity<>("Error, video doesn't exists", HttpStatus.BAD_REQUEST);
        }
        YoutubeVideoInfo youtubeVideo = new YoutubeVideoInfo();
        youtubeVideo.setVideoPath(videoPath.toString());
        youtubeVideo.setVideoTitle(formBody.getVideoTitle());
        youtubeVideo.setPrivacyStatus(formBody.getPrivateStatus() ? "unlisted" : "public");
        youtubeVideo.setDescription(formBody.getDescription());
        youtubeVideo.setTags(Arrays.asList(formBody.getTags().split(",\\s*")));
        videoUploadingTask = youtubeService.uploadVideo(youtubeVideo, courseId);
        return new ResponseEntity<>(youtubeVideo, HttpStatus.OK);
    }

}