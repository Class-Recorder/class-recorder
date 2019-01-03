package com.classrecorder.teacherserver.server.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.classrecorder.teacherserver.modules.youtube.YoutubeVideoInfo;
import com.classrecorder.teacherserver.server.ClassRecProperties;
import com.classrecorder.teacherserver.server.entities.Course;
import com.classrecorder.teacherserver.server.entities.YoutubeVideo;
import com.classrecorder.teacherserver.server.forms.YoutubeUpdateForm;
import com.classrecorder.teacherserver.server.forms.YoutubeVideoForm;
import com.classrecorder.teacherserver.server.repository.CourseRepository;
import com.classrecorder.teacherserver.server.repository.VideoRepository;
import com.classrecorder.teacherserver.server.services.YoutubeService;
import com.classrecorder.teacherserver.server.websockets.youtube.WebSocketYoutubeUpload;
import com.classrecorder.teacherserver.util.LocalVideosReader;
import com.fasterxml.jackson.annotation.JsonView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YoutubeController {

    private interface YoutubeAllVideoInfo extends YoutubeVideo.Basic, YoutubeVideo.CourseInfo {}

    private interface YoutubeBasicInfo extends YoutubeVideo.Basic {};

    private final String REQUEST_FILE_API_URL = "/api/uploadVideo/";
    private final Path videosFolder = ClassRecProperties.videosFolder;

    @Autowired
    private YoutubeService youtubeService;

    @Autowired
    private WebSocketYoutubeUpload wsYoutubeProgress;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostConstruct
	public void init() {
		this.youtubeService.addObserver(wsYoutubeProgress);
	}

    @RequestMapping(value= "/api/getStateYoutubeUpload", method=RequestMethod.GET)
    public ResponseEntity<?> getStateYoutubeUploader() throws Exception{
        return new ResponseEntity<>(youtubeService.getState().toString(), HttpStatus.OK);
    }

    @RequestMapping(value= "/api/getYoutubeUploadProgress", method=RequestMethod.GET)
    public ResponseEntity<?> getYoutubeUploadProgress() throws Exception {
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
        youtubeService.uploadVideo(youtubeVideo, courseId);
        return new ResponseEntity<>(youtubeVideo, HttpStatus.OK);
    }

    @JsonView(YoutubeBasicInfo.class)
    @RequestMapping(value="/api/getVideoInfo/{videoId}")
    public ResponseEntity<?> getYoutubeVideoInfo(@PathVariable long videoId) throws Exception {
        Optional<YoutubeVideo> optionalYoutubeVideo = this.videoRepository.findById(videoId);
        if(!optionalYoutubeVideo.isPresent()) {
            return new ResponseEntity<>("Youtube Video Not Found", HttpStatus.NOT_FOUND);
        }
        YoutubeVideo youtubeVideo = optionalYoutubeVideo.get();
        return new ResponseEntity<>(youtubeVideo, HttpStatus.OK);
    }

    @JsonView(YoutubeBasicInfo.class)
    @RequestMapping(value="/api/getUploadedVideos/{idCourse}/", method=RequestMethod.GET)
    public ResponseEntity<?> getUploadedVideos(@RequestParam(defaultValue="0") int page, @PathVariable long idCourse) throws Exception {
        Optional<Course> optionalCourse = courseRepository.findById(idCourse);
        if(!optionalCourse.isPresent()) {
            return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
        }
        Course course = optionalCourse.get();
        Page<YoutubeVideo> pageResult = this.videoRepository.findByCourse(course, PageRequest.of(page, 9));
        return new ResponseEntity<>(pageResult.getContent(), HttpStatus.OK);
    }

    @JsonView(YoutubeBasicInfo.class)
    @RequestMapping(value="/api/updateVideoInfo/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateVideoInfo(@RequestBody YoutubeUpdateForm youtubeVideoForm, @PathVariable long id) throws Exception {
        try {
            YoutubeVideoInfo youtubeVideoInfo = new YoutubeVideoInfo();
            Optional<YoutubeVideo> optionalVideoById = videoRepository.findById(id);
            if(!optionalVideoById.isPresent()) {
                return new ResponseEntity<>("Video not found", HttpStatus.NOT_FOUND);
            }
            YoutubeVideo videoById = optionalVideoById.get();
            videoById.setTitle(youtubeVideoForm.getVideoTitle());
            System.out.println(youtubeVideoForm.getVideoTitle());
            videoById.setDescription(youtubeVideoForm.getDescription());
            System.out.println(youtubeVideoForm.getDescription());
            videoById.getTags().clear();
            videoById.getTags().addAll(Arrays.asList(youtubeVideoForm.getTags().split(",\\s*")));
            youtubeVideoInfo.setVideoTitle(videoById.getTitle());
            youtubeVideoInfo.setDescription(videoById.getDescription());
            youtubeVideoInfo.setTags(videoById.getTags());
            this.youtubeService.updateVideo(videoById.getYoutubeId(), youtubeVideoInfo);
            this.videoRepository.save(videoById);
            return new ResponseEntity<>(videoById, HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Can't update the video", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(YoutubeBasicInfo.class)
    @RequestMapping(value="/api/deleteVideo/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteVideo(@PathVariable long id) throws Exception {
        if (!videoRepository.existsById(id)) {
            return new ResponseEntity<>("Video doesn't exist", HttpStatus.NOT_FOUND);
        }
        Optional<YoutubeVideo> optionalVideo = videoRepository.findById(id);
        if(!optionalVideo.isPresent()) {
            return new ResponseEntity<>("Youtube video not found", HttpStatus.NOT_FOUND);
        }
        YoutubeVideo video = optionalVideo.get();
        Course course = video.getCourse();
        course.getVideos().remove(video);
        video.setCourse(null);
        String youtubeId = video.getYoutubeId();
        this.youtubeService.deleteVideo(youtubeId);
        courseRepository.save(course);
        videoRepository.delete(video);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @RequestMapping(value="/api/getYoutubeOAuthUrl", method=RequestMethod.GET)
    public ResponseEntity<?> getYoutubeOAuthUrl() throws Exception {
        return new ResponseEntity<>(this.youtubeService.getoAuthUrl(), HttpStatus.OK);
    }

}