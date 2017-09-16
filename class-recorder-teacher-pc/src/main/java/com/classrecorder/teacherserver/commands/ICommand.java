package com.classrecorder.teacherserver.commands;

import java.io.IOException;

import com.classrecorder.teacherserver.services.FfmpegAudioFormat;
import com.classrecorder.teacherserver.services.FfmpegVideoFormat;

public interface ICommand {
	
	public Process executeFfmpegVideoAndSound(int screenWidth, int screenHeight, FfmpegVideoFormat vFormat, 
			FfmpegAudioFormat aFormat, int frameRate, String name, String directory) throws IOException, ICommandException;
	
	public Process executeFfmpegVideo(int screenWidth, int screenHeight, FfmpegVideoFormat vFormat, 
			int frameRate, String name, String directory) throws IOException, ICommandException;
	
	public Process executeFfmpegMergeVideoAudio(FfmpegVideoFormat vFormatVideoToMerge, FfmpegAudioFormat aFormatAudioToMerge, 
			FfmpegVideoFormat vFormatNewVideo, FfmpegAudioFormat aFormatNewVideo, String audioToMerge, 
			String videoToMerge, String newVideo, String directory) throws IOException, ICommandException;

	public Process executeFfmpegCutVideo(FfmpegVideoFormat vFormat, VideoInfo videoInfo, String videoToCut, String directory, 
			 String directoryCutVideos) throws ICommandException, IOException;

	public Process executeMergeVideos(FfmpegVideoFormat vFormat, String newVideo, String directory, 
			String fileStrVideos, String directoryVideos) throws ICommandException, IOException;
}
