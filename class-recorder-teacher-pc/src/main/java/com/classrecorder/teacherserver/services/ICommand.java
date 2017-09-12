package com.classrecorder.teacherserver.services;

import java.io.IOException;

import com.classrecorder.teacherserver.exceptions.ICommandException;

public interface ICommand {
	
	public Process executeFfmpegVideoAndSound(int screenWidth, int screenHeight, FfmpegVideoFormat vFormat, 
			FfmpegAudioFormat aFormat, int frameRate, String name, String directory) throws IOException, ICommandException;
	
	public Process executeFfmpegVideo(int screenWidth, int screenHeight, FfmpegVideoFormat vFormat, 
			int frameRate, String name, String directory) throws IOException, ICommandException;
}
