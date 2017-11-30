package com.classrecorder.teacherserver.modules.ffmpegwrapper;

import java.io.IOException;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandFileExistException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandFileNotExistException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegAudioFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegVideoFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;

interface ICommand {
	
	Process executeFfmpegVideoAndSound(int screenWidth, int screenHeight, FfmpegContainerFormat cFormat, 
			FfmpegAudioFormat aFormat, FfmpegVideoFormat vFormat, int frameRate, String name, String directory) throws IOException, ICommandException;
	
	Process executeFfmpegVideo(int screenWidth, int screenHeight, FfmpegContainerFormat cFormat, FfmpegVideoFormat vFormat,
			int frameRate, String name, String directory) throws IOException, ICommandException;
	
	Process executeFfmpegMergeVideoAudio(FfmpegContainerFormat cFormatVideoToMerge, FfmpegAudioFormat aFormatAudioToMerge,
			FfmpegContainerFormat cFormatNewVideo, FfmpegAudioFormat aFormatNewVideo, FfmpegVideoFormat vFormatNewVideo, String audioToMerge, 
			String videoToMerge, String newVideo, String directory) throws IOException, ICommandException;

	Process executeFfmpegCutVideo(FfmpegContainerFormat cFormat, VideoCutInfo videoInfo, String videoToCut, String directory, 
			 String directoryCutVideos) throws ICommandException, IOException;

	Process executeMergeVideos(FfmpegContainerFormat cFormat, String newVideo, String directory,
			String directoryVideos) throws ICommandException, IOException;
	
	Process createThumbnail(String name, String directory) throws ICommandException, IOException;
}
