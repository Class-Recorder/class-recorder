package com.classrecorder.teacherserver.ffmpegwrapper;

import java.io.IOException;

import com.classrecorder.teacherserver.ffmpegwrapper.formats.FfmpegAudioFormat;
import com.classrecorder.teacherserver.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.ffmpegwrapper.formats.FfmpegVideoFormat;
import com.classrecorder.teacherserver.ffmpegwrapper.video.VideoInfo;

interface ICommand {
	
	Process executeFfmpegVideoAndSound(int screenWidth, int screenHeight, FfmpegContainerFormat cFormat, 
			FfmpegAudioFormat aFormat, FfmpegVideoFormat vFormat, int frameRate, String name, String directory) throws IOException, ICommandException;
	
	Process executeFfmpegVideo(int screenWidth, int screenHeight, FfmpegContainerFormat cFormat, FfmpegVideoFormat vFormat,
			int frameRate, String name, String directory) throws IOException, ICommandException;
	
	Process executeFfmpegMergeVideoAudio(FfmpegContainerFormat cFormatVideoToMerge, FfmpegAudioFormat aFormatAudioToMerge,
			FfmpegContainerFormat cFormatNewVideo, FfmpegAudioFormat aFormatNewVideo, FfmpegVideoFormat vFormatNewVideo, String audioToMerge, 
			String videoToMerge, String newVideo, String directory) throws IOException, ICommandException;

	Process executeFfmpegCutVideo(FfmpegContainerFormat cFormat, VideoInfo videoInfo, String videoToCut, String directory, 
			 String directoryCutVideos) throws ICommandException, IOException;

	Process executeMergeVideos(FfmpegContainerFormat cFormat, String newVideo, String directory, 
			String fileStrVideos, String directoryVideos) throws ICommandException, IOException;
}
