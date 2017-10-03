package com.classrecorder.teacherserver.ffmpegwrapper;

import java.io.IOException;

interface ICommand {
	
	Process executeFfmpegVideoAndSound(int screenWidth, int screenHeight, FfmpegVideoFormat vFormat, 
			FfmpegAudioFormat aFormat, int frameRate, String name, String directory) throws IOException, ICommandException;
	
	Process executeFfmpegVideo(int screenWidth, int screenHeight, FfmpegVideoFormat vFormat, 
			int frameRate, String name, String directory) throws IOException, ICommandException;
	
	Process executeFfmpegMergeVideoAudio(FfmpegVideoFormat vFormatVideoToMerge, FfmpegAudioFormat aFormatAudioToMerge, 
			FfmpegVideoFormat vFormatNewVideo, FfmpegAudioFormat aFormatNewVideo, String audioToMerge, 
			String videoToMerge, String newVideo, String directory) throws IOException, ICommandException;

	Process executeFfmpegCutVideo(FfmpegVideoFormat vFormat, VideoInfo videoInfo, String videoToCut, String directory, 
			 String directoryCutVideos) throws ICommandException, IOException;

	Process executeMergeVideos(FfmpegVideoFormat vFormat, String newVideo, String directory, 
			String fileStrVideos, String directoryVideos) throws ICommandException, IOException;
}
