package com.classrecorder.teacherserver.server.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.FfmpegIsRecException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.FfmpegWorkingException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandFileExistException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandFileNotExistException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandNoVideosCutException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandNotCutsException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.Cut;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;
import com.classrecorder.teacherserver.server.ClassRecProperties;
import com.classrecorder.teacherserver.server.entities.local.LocalVideo;
import com.classrecorder.teacherserver.server.services.FfmpegService;
import com.classrecorder.teacherserver.server.services.MyResourceHttpRequestHandler;
import com.classrecorder.teacherserver.server.websockets.processinfo.WebSocketProcessHandler;
import com.classrecorder.teacherserver.util.LocalVideosReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

@RestController
public class LocalVideoController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final String REQUEST_FILE_API_URL = "/api/videofiles/";
	private static final String REQUEST_LOCALVIDEO_API_URL = "/api/getLocalVideos/";
	
	private Path videosFolder;
	private Path tempFolder;
	private Path outputFolder;
	
	
	@Autowired
	private FfmpegService ffmpegService;
	
	@Autowired 
	private WebSocketProcessHandler wsProcessHandler; 
	
	@Autowired
    private MyResourceHttpRequestHandler handler;
	
	public LocalVideoController() throws IOException{
		this.videosFolder = ClassRecProperties.videosFolder;
		this.tempFolder = ClassRecProperties.tempFolder;
		this.outputFolder = ClassRecProperties.outputFfmpeg;
	}
	
	
	@RequestMapping(REQUEST_FILE_API_URL + "{fileName:.+}")
	public void handleFileDownload(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException, ServletException {
		
		Path file = videosFolder.resolve(fileName);
		
		if (Files.exists(file)) {
			if(LocalVideosReader.isVideo(file)) {
				request.setAttribute(MyResourceHttpRequestHandler.ATTR_FILE, file.toFile());
				LocalVideosReader.assignContentTypeToDifferentFiles(response, file);
	            handler.handleRequest(request, response);
			}
			else {
				LocalVideosReader.assignContentTypeToDifferentFiles(response, file);
				response.setContentLength((int) file.toFile().length());
				FileCopyUtils.copy(Files.newInputStream(file), response.getOutputStream());
			}
		} else {
			response.sendError(404, "File" + fileName + "(" + file.toAbsolutePath() + ") does not exist");
		}
	}
	
	@RequestMapping(REQUEST_LOCALVIDEO_API_URL)
	public ResponseEntity<?> getLocalVideos() throws Exception{
		File directory = new File(videosFolder.toString());
		if(!directory.exists() || directory.listFiles().length == 0) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
		}
		List<LocalVideo> localVideos = LocalVideosReader.readListLocalVideos(REQUEST_FILE_API_URL, directory);
		return new ResponseEntity<>(localVideos, HttpStatus.OK);
	}
	
	@RequestMapping(REQUEST_LOCALVIDEO_API_URL + "{fileName:.+}")
	public ResponseEntity<?> getLocalVideos(@PathVariable String fileName) {
		File directory = new File(videosFolder.toString());
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
		File directory = new File(videosFolder.toString());
		if(directory.listFiles().length == 0) {
			return new ResponseEntity<>("There's no videos on server", HttpStatus.NOT_FOUND);
		}
		List<LocalVideo> localVideos = LocalVideosReader.readListLocalVideos(REQUEST_FILE_API_URL, directory);
		for(LocalVideo localVideo: localVideos) {
			if(localVideo.getVideoName().equals(fileName)) {
				String jsonFileName = fileName.substring(0, fileName.indexOf('.')).concat(".json");
				File jsonFile = new File(videosFolder.toString() + "/" + jsonFileName);
				FileWriter fileWriter = new FileWriter(jsonFile, false);
				gson.toJson(videoCut, fileWriter);
				fileWriter.close();
				return new ResponseEntity<>(true, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Error writing json file", HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value="/api/cutVideo/{fileName:.+}/{newVideo}/{containerVideo}")
	public ResponseEntity<?> cutFile(
			@PathVariable String fileName, 
			@PathVariable String newVideo, 
			@PathVariable String containerVideo) throws Exception {
		Gson gson = new Gson();
		File directory = new File(videosFolder.toString());
		File directoryTemp = new File(tempFolder.toString());
		if(!checkDirectories(directory, directoryTemp)) {
			return new ResponseEntity<>("There's no videos on server", HttpStatus.NOT_FOUND);
		}
		File newFileVideo = new File(videosFolder.toString() + "/" + newVideo + "."+ containerVideo);
		if(newFileVideo.exists()) {
			return new ResponseEntity<>("Video actually exists", HttpStatus.CONFLICT);
		}
		List<LocalVideo> localVideos = LocalVideosReader.readListLocalVideos(REQUEST_FILE_API_URL, directory);
		for(LocalVideo localVideo: localVideos) {
			if(localVideo.getVideoName().equals(fileName)) {
				String fileNameWoutExt = fileName.substring(0, fileName.indexOf('.'));
				String jsonFileStr = fileNameWoutExt.concat(".json");
				String videoToCut = directory + "/" + fileName;
				JsonReader reader = new JsonReader(new FileReader(directory + "/" + jsonFileStr));
				VideoCutInfo cutInfo = gson.fromJson(reader, VideoCutInfo.class);
				ffmpegService.setDirectory(videosFolder.toString());
				ffmpegService.setObservers(Arrays.asList(wsProcessHandler));
				try {
					ffmpegService.cutVideo(videoToCut, cutInfo, tempFolder.toString());
				} catch (FfmpegIsRecException | FfmpegWorkingException e) {
					return new ResponseEntity<>("Ffmpeg is working", HttpStatus.SERVICE_UNAVAILABLE);
				} catch (ICommandFileNotExistException e) {
					return new ResponseEntity<>("The file" + fileName + "doesn't exist", HttpStatus.BAD_REQUEST);
				} catch (ICommandNotCutsException e) {
					return new ResponseEntity<>("The video " + fileName + " has no cut file" , HttpStatus.CONFLICT);
				}
				return new ResponseEntity<>(true, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("An error has occoured", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value="/api/mergeVideo/{newVideo}/{containerVideo}", method=RequestMethod.POST)
	public ResponseEntity<?> mergeVideo(@PathVariable String newVideo, @PathVariable String containerVideo) throws Exception {
		Gson gson = new Gson();
		File directory = new File(videosFolder.toString());
		File directoryTemp = new File(tempFolder.toString());
		if(directoryTemp.listFiles().length == 0) {
			return new ResponseEntity<>("No video was cutted", HttpStatus.BAD_REQUEST);
		}
		ffmpegService.setDirectory(videosFolder.toString());
		ffmpegService.setContainerVideoFormat(FfmpegContainerFormat.valueOf(containerVideo));
		ffmpegService.setVideoName(newVideo);
		ffmpegService.setObservers(Arrays.asList(wsProcessHandler));
		
		VideoCutInfo videoCut = new VideoCutInfo(Arrays.asList(new Cut()));
		Writer writer = new FileWriter(videosFolder.toString() + "/" + newVideo + ".json");
		gson.toJson(videoCut, writer);
		writer.close();
		String thumbName = tempFolder.toString() + "/out1" + "." + containerVideo.toString();
		
		File jsonCutsFile = new File(videosFolder.toString() + "/" + newVideo + ".json");
		try {
			ffmpegService.createThumbnail(thumbName, newVideo, directory.toString()).waitFor();
			
		} catch(FfmpegIsRecException e) {
			jsonCutsFile.delete();
			return new ResponseEntity<>("Ffmpeg is working", HttpStatus.SERVICE_UNAVAILABLE);
		} catch(ICommandFileNotExistException e) {
			jsonCutsFile.delete();
			return new ResponseEntity<>("The file " + newVideo + " not exist", HttpStatus.CONFLICT);	
		}
		
		try {
			ffmpegService.mergeVideos(tempFolder.toString());
		}
		catch(FfmpegIsRecException e) {
			return new ResponseEntity<>("Ffmpeg is working", HttpStatus.SERVICE_UNAVAILABLE);
		} catch (ICommandFileExistException e) {
			return new ResponseEntity<>("The file" + newVideo + "exist", HttpStatus.CONFLICT);
		} catch (ICommandNoVideosCutException e) {
			return new ResponseEntity<>("There's no files to merge", HttpStatus.PRECONDITION_FAILED);
		}
		
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	

	private boolean checkDirectories(File directory, File directoryTemp) throws IOException {
		if(directory.listFiles().length == 0) {
			return false;
		}
		if(!outputFolder.toFile().exists()) {
			outputFolder.toFile().mkdir();
		}
		if(!directoryTemp.exists()) {
			directoryTemp.mkdir();
		} else {
			FileUtils.cleanDirectory(directoryTemp);
		}
		return true;
	}

}