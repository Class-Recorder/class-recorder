package com.classrecorder.teacherserver.server.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.server.ClassRecProperties;
import com.classrecorder.teacherserver.server.services.FfmpegService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AudioController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FfmpegService ffmpegService;

    @Autowired
    private ClassRecProperties classRecProperties;

    public AudioController() {
    }

    @RequestMapping(value="/api/uploadFile/{containerFormat}/{videoName}", method=RequestMethod.POST)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String videoName, @PathVariable String containerFormat) {
        if(file.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.CONFLICT);
        }

        try {
            byte[] bytes = file.getBytes();

            if(!classRecProperties.getTempFolder().toFile().exists()) {
                classRecProperties.getTempFolder().toFile().mkdir();
            }
            Path audioFile = classRecProperties.getTempFolder().resolve(file.getOriginalFilename());
            Files.write(audioFile, bytes);

            Path videoFile = classRecProperties.getVideosFolder().resolve(videoName + "." + containerFormat);
            String videoDir = videoFile.toString();
            String audioDir = audioFile.toString();
            String tempVideoName = videoName + "_merged";
            ffmpegService.setVideoName(tempVideoName);
            ffmpegService.setContainerVideoFormat(FfmpegContainerFormat.valueOf(containerFormat));
            ffmpegService.setDirectory(classRecProperties.getTempFolder().toString());
            Process process = ffmpegService.mergeAudioAndVideo(audioDir, videoDir);
            process.waitFor();
            //Rename merged video
            File oldFile = new File(videoFile.toString());
            oldFile.delete();
            Path tempPath = classRecProperties.getTempFolder().resolve(tempVideoName + "." + containerFormat);
            Files.move(tempPath, videoFile, StandardCopyOption.REPLACE_EXISTING);

            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error saving file", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}