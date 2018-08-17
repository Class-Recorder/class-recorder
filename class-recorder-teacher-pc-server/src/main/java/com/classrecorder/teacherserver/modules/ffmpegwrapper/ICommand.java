package com.classrecorder.teacherserver.modules.ffmpegwrapper;

import java.io.IOException;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;

interface ICommand {
	
	Process executeFfmpegVideoAndSound(int screenWidth, int screenHeight, int frameRate, 
			String directory, String name, FfmpegContainerFormat cFormat) throws IOException, ICommandException;
	
	Process executeFfmpegVideo(int screenWidth, int screenHeight, int frameRate,
			String directory, String name, FfmpegContainerFormat cFormat) throws IOException, ICommandException;
	
	Process executeFfmpegMergeVideoAudio(String dirAudioToMerge,  String dirVideoToMerge, 
			 String directory, String newVideo, FfmpegContainerFormat cFormatNewVideo) throws IOException, ICommandException;

	Process executeFfmpegCutVideo(String dirVideoToCut, String directoryCutVideos,
			VideoCutInfo videoInfo, FfmpegContainerFormat cFormat) throws ICommandException, IOException;

	Process executeMergeVideos(FfmpegContainerFormat cFormat, String newVideo, String directory,
			String directoryVideos) throws ICommandException, IOException;
	
	Process createThumbnail(String name, String imageName, String directory) throws ICommandException, IOException;
}
