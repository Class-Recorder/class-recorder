package com.classrecorder.teacherserver.server;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ClassRecProperties {
	
	public static final Path videosFolder = Paths.get(System.getProperty("user.dir"), "videos");
	public static final Path tempFolder = Paths.get(System.getProperty("user.dir"), "temp");
	public static final Path outputFfmpeg = Paths.get(System.getProperty("user.dir"), "output_ffmpeg");

}
