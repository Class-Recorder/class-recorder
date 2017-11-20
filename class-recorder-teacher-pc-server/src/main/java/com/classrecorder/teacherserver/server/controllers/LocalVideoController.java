package com.classrecorder.teacherserver.server.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classrecorder.teacherserver.server.entities.local.LocalVideo;

@RestController
public class LocalVideoController {
	
	private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "videos");
	private static final String REQUEST_FILE_API_URL = "/api/videofiles/";
	
	@RequestMapping(REQUEST_FILE_API_URL + "{fileName:.+}")
	public void handleFileDownload(@PathVariable String fileName, HttpServletResponse res)
			throws FileNotFoundException, IOException {
		
		Path file = FILES_FOLDER.resolve(fileName);
		
		if (Files.exists(file)) {
			assignContentTypeToDifferentFiles(res, file);
			res.setContentLength((int) file.toFile().length());
			FileCopyUtils.copy(Files.newInputStream(file), res.getOutputStream());
		} else {
			res.sendError(404, "File" + fileName + "(" + file.toAbsolutePath() + ") does not exist");
		}
	}
	
	@RequestMapping("/api/getLocalVideos")
	public ResponseEntity<?> getLocalVideos() {
		File directory = new File(FILES_FOLDER.toString());
		if(directory.listFiles().length == 0) {
			new ResponseEntity<>("There's no videos", HttpStatus.NOT_FOUND);
		}
		List<LocalVideo> localVideos = new ArrayList<>();
		File[] listFiles = directory.listFiles();
		Arrays.sort(listFiles);
		String currentFile = listFiles[0].getName();
		LocalVideo currentLocalVideo = new LocalVideo();
		for(int i = 0; i < listFiles.length; i++) {
			
			//Checks if it's reading other file, and if it is, save the information to start with another video
			if(i > 0) {
				String newFile = listFiles[i].getName();
				String currentFileWoutExt = currentFile.substring(0, currentFile.lastIndexOf('.'));
				String newFileWoutExt = newFile.substring(0, newFile.lastIndexOf('.'));
				if(!currentFileWoutExt.equals(newFileWoutExt)) {
					localVideos.add(currentLocalVideo);
					currentLocalVideo = new LocalVideo();
				}
				currentFile = listFiles[i].getName();
			}
			
			//Add properties to the class
			if(currentFile.endsWith(".mkv") || currentFile.endsWith(".mp4")) {
				currentLocalVideo.setUrlApiLocalVideo(REQUEST_FILE_API_URL + currentFile);
				currentLocalVideo.setVideoName(currentFile);
			}
			else if (currentFile.endsWith(".json")) {
				currentLocalVideo.setUrlApiLocalCuts(REQUEST_FILE_API_URL + currentFile);
			}
			else if (currentFile.endsWith("jpg") || (currentFile.endsWith("jpg"))) {
				currentLocalVideo.setUrlApiLocalThumb(REQUEST_FILE_API_URL + currentFile);
			}
			
			//Add last element
			if(i == listFiles.length-1) {
				localVideos.add(currentLocalVideo);
			}
		}
		return new ResponseEntity<>(localVideos, HttpStatus.OK);
	}

	private void assignContentTypeToDifferentFiles(HttpServletResponse res, Path file) {
		if(file.toString().endsWith(".mkv")) {
			res.setContentType("video/webm");
		}
		if(file.toString().endsWith(".mp4")) {
			res.setContentType("video/mp4");
		}
		if(file.toString().endsWith(".json")) {
			res.setContentType("application/json");
		}
		if(file.toString().endsWith(".jpg")) {
			res.setContentType("image/jpeg");
		}
	}

}
