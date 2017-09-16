package com.classrecorder.teacherserver.commands;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.classrecorder.teacherserver.services.FfmpegAudioFormat;
import com.classrecorder.teacherserver.services.FfmpegFormat;
import com.classrecorder.teacherserver.services.FfmpegVideoFormat;

public class ICommandLinux implements ICommand{
	
	public ICommandLinux() {
		
	}
	
	@Override
	public Process executeFfmpegVideoAndSound(int screenWidth, int screenHeight, FfmpegVideoFormat vFormat,
			FfmpegAudioFormat aFormat, int frameRate, String name, String directory) throws IOException, ICommandException {
		
		checkDirectory(directory);
		checkFile(name, vFormat, directory, false);
		
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
		checkFile(name, vFormat, directory, false);
		
		StringBuilder command = new StringBuilder();
		command.append("ffmpeg")
			.append(" -video_size ").append(screenWidth).append("x").append(screenHeight)
			.append(" -framerate ").append(frameRate)
			.append(" -f x11grab -i :0 ")
			.append(directory).append("/").append(name).append(".").append(vFormat);
		
		System.out.println(command.toString());
		
		return Runtime.getRuntime().exec(command.toString());
	}
	
	@Override
	public Process executeFfmpegMergeVideoAudio(FfmpegVideoFormat vFormatVideoToMerge, FfmpegAudioFormat aFormatAudioToMerge, 
			FfmpegVideoFormat vFormatNewVideo, FfmpegAudioFormat aFormatNewVideo, String audioToMerge, 
			String videoToMerge, String newVideo, String directory) throws IOException, ICommandException {
		
		checkDirectory(directory);
		checkFile(newVideo, vFormatNewVideo, directory, false);
		checkFile(videoToMerge, vFormatVideoToMerge, directory, true);
		checkFile(audioToMerge, aFormatAudioToMerge, directory, true);
		
		
		StringBuilder command = new StringBuilder();
		command.append("ffmpeg")
			.append(" -i ").append(directory).append("/").append(videoToMerge).append(".").append(vFormatVideoToMerge)
			.append(" -i ").append(directory).append("/").append(audioToMerge).append(".").append(aFormatAudioToMerge)
			.append(" -c:v copy -c:a ").append(aFormatNewVideo)
			.append(" -strict experimental -shortest ")
			.append(directory).append("/").append(newVideo).append(".").append(vFormatNewVideo);
		
		System.out.println(command.toString());
		
		return Runtime.getRuntime().exec(command.toString());
	}
	
	@Override
	public Process executeFfmpegCutVideo(FfmpegVideoFormat vFormat, VideoInfo videoInfo, String videoToCut, String directory, String directoryCutVideos) throws ICommandException, IOException {
		
		checkDirectory(directory);
		checkFile(videoToCut, vFormat, directory, true);
		checkDirectory(directoryCutVideos);
		
		StringBuilder command = new StringBuilder();
		command.append("ffmpeg")
			.append(" -i ").append(directory).append("/").append(videoToCut).append(".").append(vFormat)
			.append(" -vcodec copy -acodec copy");
		
		int index = 0;
		ArrayList<Cut> cuts = videoInfo.getCuts();
		if(cuts.size() == 0) {
			throw new ICommandException("There's no cuts on VideoInfo");
		}
		PrintWriter writer = new PrintWriter(directoryCutVideos + "/files.txt", "UTF-8");
		for(Cut cut: cuts) {
			command.append(" -ss ").append(cut.getStart()).append(" -to ").append(cut.getEnd()).append(" ")
				.append(directoryCutVideos).append("/").append("out").append(index).append(".").append(vFormat);
			writer.println("file 'out" + index + "." + vFormat + "'");
			index++;
		}
		writer.close();
		System.out.println(command.toString());
		return Runtime.getRuntime().exec(command.toString());
	}
	
	@Override 
	public Process executeMergeVideos(FfmpegVideoFormat vFormat, String newVideo, String directory, String fileStrVideos, String directoryVideos) throws ICommandException, IOException{
		
		checkDirectory(directory);
		checkFile(newVideo, vFormat, directory, false);
		checkDirectory(directoryVideos);
		
		File tempFile = new File(directoryVideos);
		String[] files = tempFile.list();
		if(files.length == 0) {
			throw new ICommandException("You should cut a video before using executeFfmpegCutVideo");
		}
		StringBuilder command = new StringBuilder();
		command.append("ffmpeg")
			.append(" -f concat -i ").append(fileStrVideos)
			.append(" -c copy ").append(directory).append("/").append(newVideo).append(".").append(vFormat);
		
		System.out.println(command.toString());
		
		return Runtime.getRuntime().exec(command.toString());
	}

	private void checkDirectory(String directory) {
		
		File directoryFile = new File(directory);
		if (!directoryFile.exists()) {
			directoryFile.mkdir();
		}
	
	}
	
	private void checkFile(String name, FfmpegFormat format, String directory, boolean checkExist) throws ICommandException  {
		File checkFile = new File(directory + "/" + name + "." + format);
		if(checkExist) {
			if(!checkFile.exists()) {
				throw new ICommandException("The file: " + name + "." + format + ", doesn't exists");
			}
		}
		else {
			if(checkFile.exists()) {
				throw new ICommandException("The file: " + name + "." + format + ", actually exist");
			}
		}
	}
	
}
