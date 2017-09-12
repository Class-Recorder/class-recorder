package com.classrecorder.teacherserver.services;

import java.io.File;
import java.io.IOException;

import com.classrecorder.teacherserver.exceptions.ICommandException;

public class ICommandLinux implements ICommand{
	
	public ICommandLinux() {
		
	}
	
	@Override
	public Process executeFfmpegVideoAndSound(int screenWidth, int screenHeight, FfmpegVideoFormat vFormat,
			FfmpegAudioFormat aFormat, int frameRate, String name, String directory) throws IOException, ICommandException {
		
		checkDirectory(directory);
		checkFile(name, vFormat, directory);
		
		StringBuilder command = new StringBuilder();
		command.append("ffmpeg")
			.append(" -video_size ").append(screenWidth).append("x").append(screenHeight)
			.append(" -framerate ").append(frameRate)
			.append(" -f x11grab -i :0 -f alsa -i default")
			.append(" -acodec ").append(aFormat).append(" ")
			.append(directory).append("/").append(name).append(".").append(vFormat);
		
		System.out.println(command.toString());
	
		return Runtime.getRuntime().exec(command.toString()); 
		
	}
	
	@Override
	public Process executeFfmpegVideo(int screenWidth, int screenHeight, FfmpegVideoFormat vFormat, int frameRate,
			String name, String directory) throws IOException, ICommandException {
		
		checkDirectory(directory);
		checkFile(name, vFormat, directory);
		
		StringBuilder command = new StringBuilder();
		command.append("ffmpeg")
			.append(" -video_size ").append(screenWidth).append("x").append(screenHeight)
			.append(" -framerate ").append(frameRate)
			.append(" -f x11grab -i :0 ")
			.append(directory).append("/").append(name).append(".").append(vFormat);
		
		System.out.println(command.toString());
		
		return Runtime.getRuntime().exec(command.toString());
	}
	
	public void checkDirectory(String directory) {
		
		File directoryFile = new File(directory);
		if (!directoryFile.exists()) {
			directoryFile.mkdir();
		}
	
	}
	
	public void checkFile(String name, FfmpegVideoFormat vFormat, String directory) throws ICommandException {
		File checkFile = new File(directory + "/" + name + "." + vFormat);
		if(checkFile.exists()) {
			throw new ICommandException("The file: " + name + "." + vFormat + ", actually exists");
		}
	}
	
}
