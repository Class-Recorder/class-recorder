package com.classrecorder.teacherserver.server.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;
import com.classrecorder.teacherserver.server.entities.local.LocalVideo;
import com.classrecorder.teacherserver.server.services.FfmpegService;
import com.classrecorder.teacherserver.server.websockets.processinfo.WebSocketProcessHandler;
import com.classrecorder.teacherserver.util.LocalVideosReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

@RestController
public class LocalVideoController {
	
	private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "videos");
	private static final Path FILES_TEMP = Paths.get(System.getProperty("user.dir"), "temp");
	private static final String REQUEST_FILE_API_URL = "/api/videofiles/";
	private static final String REQUEST_LOCALVIDEO_API_URL = "/api/getLocalVideos/";
	
	@Autowired
	private FfmpegService ffmpegService;
	
	@Autowired 
	private WebSocketProcessHandler wsProcessHandler; 
	
	@RequestMapping(REQUEST_FILE_API_URL + "{fileName:.+}")
	public void handleFileDownload(@PathVariable String fileName, HttpServletResponse res)
			throws FileNotFoundException, IOException {
		
		Path file = FILES_FOLDER.resolve(fileName);
		
		if (Files.exists(file)) {
			LocalVideosReader.assignContentTypeToDifferentFiles(res, file);
			res.setContentLength((int) file.toFile().length());
			FileCopyUtils.copy(Files.newInputStream(file), res.getOutputStream());
		} else {
			res.sendError(404, "File" + fileName + "(" + file.toAbsolutePath() + ") does not exist");
		}
	}
	
	@RequestMapping(REQUEST_LOCALVIDEO_API_URL)
	public ResponseEntity<?> getLocalVideos() throws Exception{
		File directory = new File(FILES_FOLDER.toString());
		if(directory.listFiles().length == 0) {
			return new ResponseEntity<>("There's no videos on server", HttpStatus.NOT_FOUND);
		}
		List<LocalVideo> localVideos = LocalVideosReader.readListLocalVideos(REQUEST_FILE_API_URL, directory);
		return new ResponseEntity<>(localVideos, HttpStatus.OK);
	}
	
	@RequestMapping(REQUEST_LOCALVIDEO_API_URL + "{fileName:.+}")
	public ResponseEntity<?> getLocalVideos(@PathVariable String fileName) {
		File directory = new File(FILES_FOLDER.toString());
		if(directory.listFiles().length == 0) {
			return new ResponseEntity<>("There's no videos on server", HttpStatus.NOT_FOUND);
		}
		List<LocalVideo> localVideos = LocalVideosReader.readListLocalVideos(REQUEST_FILE_API_URL, directory);
		for(LocalVideo localVideo: localVideos) {
			if(localVideo.getVideoName().equals(fileName)) {
				return new ResponseEntity<>(localVideo, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Video not found", HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value = "/api/updateCutFileOf/{fileName:.+}", method=RequestMethod.POST)
	public ResponseEntity<?> postCutFile(@PathVariable String fileName, @RequestBody VideoCutInfo videoCut) throws IOException{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		File directory = new File(FILES_FOLDER.toString());
		if(directory.listFiles().length == 0) {
			return new ResponseEntity<>("There's no videos on server", HttpStatus.NOT_FOUND);
		}
		List<LocalVideo> localVideos = LocalVideosReader.readListLocalVideos(REQUEST_FILE_API_URL, directory);
		for(LocalVideo localVideo: localVideos) {
			if(localVideo.getVideoName().equals(fileName)) {
				String jsonFileName = fileName.substring(0, fileName.indexOf('.')).concat(".json");
				File jsonFile = new File(FILES_FOLDER.toString() + "/" + jsonFileName);
				FileWriter fileWriter = new FileWriter(jsonFile, false);
				gson.toJson(videoCut, fileWriter);
				fileWriter.close();
				return new ResponseEntity<>(true, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Error writing json file", HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value="/api/cutVideo/{fileName:.+}")
	public ResponseEntity<?> cutFile(@PathVariable String fileName) throws Exception {
		Gson gson = new Gson();
		File directory = new File(FILES_FOLDER.toString());
		if(directory.listFiles().length == 0) {
			return new ResponseEntity<>("There's no videos on server", HttpStatus.NOT_FOUND);
		}
		List<LocalVideo> localVideos = LocalVideosReader.readListLocalVideos(REQUEST_FILE_API_URL, directory);
		for(LocalVideo localVideo: localVideos) {
			if(localVideo.getVideoName().equals(fileName)) {
				File directoryTemp = new File(FILES_TEMP.toString());
				System.out.println(directoryTemp);
				String jsonFileStr = fileName.substring(0, fileName.indexOf('.')).concat(".json");
				System.out.println(jsonFileStr);
				if(!directoryTemp.exists()) {
					directoryTemp.mkdir();
				} else {
					FileUtils.cleanDirectory(directoryTemp);
				}
				JsonReader reader = new JsonReader(new FileReader(directory + "/" + jsonFileStr));
				VideoCutInfo cutInfo = gson.fromJson(reader, VideoCutInfo.class);
				ffmpegService.setDirectory(FILES_FOLDER.toString());
				Process p = ffmpegService.cutVideo(cutInfo, fileName, FILES_TEMP.toString());
				wsProcessHandler.sendProcessOutput();
				p.waitFor();
				return new ResponseEntity<>(true, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("An error has occoured", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
