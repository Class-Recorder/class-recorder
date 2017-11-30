package com.classrecorder.teacherserver.modules.ffmpegwrapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandFileExistException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandFileNotExistException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandNoVideosCutException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandNotCutsException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegAudioFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegVideoFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.Cut;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;
import com.classrecorder.teacherserver.server.security.LoginController;

class ICommandLinux implements ICommand{
	
	private static final Logger log = LoggerFactory.getLogger(ICommandLinux.class);
	
	public ICommandLinux() {
	
	}
	
	@Override
	public Process executeFfmpegVideoAndSound(int screenWidth, int screenHeight, FfmpegContainerFormat cFormat,
			FfmpegAudioFormat aFormat, FfmpegVideoFormat vFormat, int frameRate, String name, String directory) throws IOException, ICommandException {
		
		checkDirectory(directory);
		checkFile(name, cFormat, directory, false);
		
		List<String> command = new ArrayList<>();
		command.addAll(Arrays.asList("ffmpeg", "-video_size", screenWidth + "x" + screenHeight, "-r", Integer.toString(frameRate)));
		command.addAll(Arrays.asList("-f", "x11grab", "-i", ":0", "-f", "alsa", "-i", "default", "-acodec", aFormat.toString(), "-vcodec", vFormat.toString()));
		command.addAll(Arrays.asList(directory + "/" + name + "." + cFormat));
		logCommand(command);
		ProcessBuilder pb = new ProcessBuilder(command);
	
		return pb.start(); 
		
	}
	
	@Override
	public Process executeFfmpegVideo(int screenWidth, int screenHeight, FfmpegContainerFormat cFormat, FfmpegVideoFormat vFormat, int frameRate,
			String name, String directory) throws IOException, ICommandException {
		
		checkDirectory(directory);
		checkFile(name, cFormat, directory, false);
		
		List<String> command = new ArrayList<>();
		command.addAll(Arrays.asList("ffmpeg", "video_size", screenWidth + "x" + screenHeight, "-framerate", Integer.toString(frameRate)));
		command.addAll(Arrays.asList("-f", "x11grab", "-i", ":0", "-vcodec", vFormat.toString(), directory + "/" + name + "." + cFormat));
		logCommand(command);
		ProcessBuilder pb = new ProcessBuilder(command);
		return pb.start();
		}
	
	@Override
	public Process executeFfmpegMergeVideoAudio(FfmpegContainerFormat cFormatVideoToMerge, FfmpegAudioFormat aFormatAudioToMerge,
			FfmpegContainerFormat cFormatNewVideo, FfmpegAudioFormat aFormatNewVideo, FfmpegVideoFormat vFormatNewVideo, String audioToMerge, 
			String videoToMerge, String newVideo, String directory) throws IOException, ICommandException {
		
		checkDirectory(directory);
		checkFile(newVideo, vFormatNewVideo, directory, false);
		checkFile(videoToMerge, cFormatVideoToMerge, directory, true);
		checkFile(audioToMerge, aFormatAudioToMerge, directory, true);
		
		List<String> command = new ArrayList<>();
		command.addAll(Arrays.asList("ffmpeg", "-i", directory + "/" + videoToMerge + "." + cFormatVideoToMerge));
		command.addAll(Arrays.asList("-i", directory + "/" + audioToMerge + "." + aFormatAudioToMerge));
		command.addAll(Arrays.asList("-c:v", "copy", "-c:a", aFormatNewVideo.toString(), "-strict", "experimental", "-shortest"));
		command.addAll(Arrays.asList("-vcodec", vFormatNewVideo.toString(), directory + "/" + newVideo + "." + vFormatNewVideo));
		logCommand(command);
		ProcessBuilder pb = new ProcessBuilder(command); 
		return pb.start();
	}
	
	@Override
	public Process executeFfmpegCutVideo(FfmpegContainerFormat cFormat, VideoCutInfo videoCutInfo, String videoToCut, String directory, String directoryCutVideos) throws ICommandException, IOException {
		
		checkDirectory(directory);
		checkFile(videoToCut, cFormat, directory, true);
		checkDirectory(directoryCutVideos);
		System.out.println();
		
		List<String> command = new ArrayList<>();
		command.addAll(Arrays.asList("ffmpeg", "-i", directory + "/" + videoToCut + "." + cFormat, 
				"-vcodec", "copy", "-acodec", "copy"));
		
		int index = 0;
		List<Cut> cuts = videoCutInfo.getCuts();
		if(cuts.size() == 0) {
			throw new ICommandNotCutsException("There's no cuts on VideoInfo");
		}
		PrintWriter writer = new PrintWriter(directoryCutVideos + "/files.txt", "UTF-8");
		for(Cut cut: cuts) {
			command.addAll(Arrays.asList("-ss", cut.getStart(), "-to", cut.getEnd(), directoryCutVideos + "/out" + index + "." + cFormat));
			writer.println("file 'out" + index + "." + cFormat + "'");
			index++;
		}
		writer.close();
		ProcessBuilder pb = new ProcessBuilder(command);
		logCommand(command);	
		return pb.start();
	}
	
	@Override 
	public Process executeMergeVideos(FfmpegContainerFormat cFormat, String newVideo, String directory, String directoryVideos) throws ICommandException, IOException{
		
		checkDirectory(directory);
		checkFile(newVideo, cFormat, directory, false);
		checkDirectory(directoryVideos);
		
		File tempFile = new File(directoryVideos);
		String[] files = tempFile.list();
		if(files.length == 0) {
			throw new ICommandNoVideosCutException("You should cut a video before using executeFfmpegCutVideo");
		}
		List<String> command = new ArrayList<>();
		command.addAll(Arrays.asList("ffmpeg", "-f", "concat", "-i", directoryVideos + "/files.txt", "-c", "copy", directory + "/" + newVideo + "." + cFormat));
		logCommand(command);
		ProcessBuilder pb = new ProcessBuilder(command);
		
		return pb.start();
	}
	
	@Override
	public Process createThumbnail(String name, String directory) throws ICommandException, IOException {
		File file = new File(directory + "/" + name);
		checkDirectory(directory);
		if(!file.exists()) {
			throw new ICommandFileNotExistException("The file doesn't exists");
		}
		String nameWoutExt = name.substring(0, name.indexOf("."));
		String thumbnailDir = directory + "/" + nameWoutExt + ".jpg";
		List<String> command = new ArrayList<>();
		command.addAll(Arrays.asList("ffmpeg", "-ss", "0.5", "-i", file.getPath(), "-t", "1", "-s", "480x300", "-f", "image2", thumbnailDir));
		logCommand(command);
		ProcessBuilder pb = new ProcessBuilder(command); 
		return pb.start();
	}
	
	
	private void logCommand(List<String> command) {
		String commandLog = "";
		for(String c: command) {
			commandLog += c + " ";
		}
		log.info(commandLog);
	}
	

	private void checkDirectory(String directory) {
		
		File directoryFile = new File(directory);
		if (!directoryFile.exists()) {
			directoryFile.mkdir();
		}
	
	}
	
	private void checkFile(String name, FfmpegFormat format, String directory, boolean checkExist) throws ICommandFileExistException, ICommandFileNotExistException  {
		File checkFile = new File(directory + "/" + name + "." + format);
		if(checkExist) {
			if(!checkFile.exists()) {
				throw new ICommandFileNotExistException("The file: " + name + "." + format + ", doesn't exists");
			}
		}
		else {
			if(checkFile.exists()) {
				throw new ICommandFileExistException("The file: " + name + "." + format + ", actually exist");
			}
		}
	}
	
}
