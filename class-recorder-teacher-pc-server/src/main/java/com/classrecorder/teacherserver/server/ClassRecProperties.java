package com.classrecorder.teacherserver.server;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ClassRecProperties {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${type_app:jar}")
	private String typeApp;
	private Path videosFolder;
	private Path tempFolder;
	private Path outputFfmpeg;

	public ClassRecProperties() {
	}

	public String getTypeApp() {
		return this.typeApp;
	}

	public Path getVideosFolder() {
		return this.videosFolder;
	}


	public Path getTempFolder() {
		return this.tempFolder;
	}


	public Path getOutputFfmpeg() {
		return this.outputFfmpeg;
	}

	@Value("${videos_folder:}")
	public void setVideosFolder(String videosFolder) {
		if (videosFolder.length() == 0) {
			this.videosFolder = Paths.get(System.getProperty("user.dir"), "videos");
		} else {
			this.videosFolder = Paths.get(videosFolder);
		}
		log.info("Videos directory: " + this.videosFolder.toString());
	}

	@Value("${temp_folder:}")
	public void setTempFolder(String tempFolder) {
		if (tempFolder.length() == 0) {
			this.tempFolder = Paths.get(System.getProperty("user.dir"), "temp");
		} else {
			this.tempFolder = Paths.get(tempFolder);
		}
		log.info("Videos directory: " + this.tempFolder.toString());
	}

	@Value("${output_ffmpeg:}")
	public void setOutputFfmpeg(String outputFolder) {
		if (outputFolder.length() == 0) {
			this.outputFfmpeg = Paths.get(System.getProperty("user.dir"), "temp");
		} else {
			this.outputFfmpeg = Paths.get(outputFolder);
		}
		log.info("Videos directory: " + this.outputFfmpeg.toString());
	}

	@Override
	public String toString() {
		return "{" +
			" typeApp='" + getTypeApp() + "'" +
			", videosFolder='" + getVideosFolder() + "'" +
			", tempFolder='" + getTempFolder() + "'" +
			", outputFfmpeg='" + getOutputFfmpeg() + "'" +
			"}";
	}
}
