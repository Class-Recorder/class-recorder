package com.classrecorder.teacherserver.modules.ffmpegwrapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
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
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.Cut;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;

class ICommandLinux implements ICommand{

	private static final Logger log = LoggerFactory.getLogger(ICommandLinux.class);

	private String x11device;
	private String ffmpegDirectory;

	public ICommandLinux(Path outputLog, String x11device, String ffmpegDirectory) {
		this.x11device = x11device;
		this.ffmpegDirectory = ffmpegDirectory;
	}

    @Override
    public Process executeFfmpegVideoAndSound(int screenWidth, int screenHeight, int frameRate,
                                              String directory, String name, FfmpegContainerFormat cFormat) throws IOException, ICommandException {

        checkDirectory(directory);
        checkFile(name, cFormat, directory, false);

        List<String> command = new ArrayList<>();
        command.addAll(Arrays.asList(this.ffmpegDirectory, "-f", "x11grab", "-r", Integer.toString(frameRate)));
		command.addAll(Arrays.asList("-s", screenWidth + "x" + screenHeight, "-i", x11device));
		if(cFormat.equals(FfmpegContainerFormat.mkv))  {
			command.addAll(Arrays.asList("-vcodec", "h264", "-thread_queue_size", "20480", "-f", "alsa", "-i", "default", "-pix_fmt", "yuv420p"));
			command.addAll(Arrays.asList("-acodec", "mp3", "-preset", "ultrafast", "-crf", "30"));
		}
		if(cFormat.equals(FfmpegContainerFormat.mp4)) {
			command.addAll(Arrays.asList("-vcodec", "h264", "-thread_queue_size", "20480", "-f", "alsa", "-i", "default", "-pix_fmt", "yuv420p"));
			command.addAll(Arrays.asList("-acodec", "aac", "-strict", "-2", "-preset", "ultrafast", "-crf", "30"));
		}
        command.addAll(Arrays.asList(directory + "/" + name + "." + cFormat));
        logCommand(command);
        ProcessBuilder pb = new ProcessBuilder(command);

        return pb.start();

    }

	@Override
	public Process executeFfmpegMergeVideoAudio(String dirAudioToMerge,  String dirVideoToMerge,
			 String directory, String newVideo, FfmpegContainerFormat cFormatNewVideo) throws IOException, ICommandException {

		checkDirectory(directory);
		checkFile(newVideo, cFormatNewVideo, directory, false);
		checkFile(dirVideoToMerge, true);
		checkFile(dirAudioToMerge, true);

		List<String> command = new ArrayList<>();
		command.addAll(Arrays.asList(this.ffmpegDirectory, "-i", dirVideoToMerge));
		command.addAll(Arrays.asList("-i", dirAudioToMerge));
        command.addAll(Arrays.asList("-c:v", "copy"));
        if(cFormatNewVideo.equals(FfmpegContainerFormat.mkv)) {
            command.addAll(Arrays.asList("-c:a", "mp3", "-b:a", "32k", "-filter:a", "volume=1.5"));
        }
        if(cFormatNewVideo.equals(FfmpegContainerFormat.mp4)) {
            command.addAll(Arrays.asList("-c:a", "aac", "-b:a", "32k", "-strict", "-2", "-filter:a", "volume=1.5"));
        }
        command.addAll(Arrays.asList("-map", "0:v:0", "-map", "1:a:0"));
        command.addAll(Arrays.asList("-shortest", directory + "/" + newVideo + "." + cFormatNewVideo));
        logCommand(command);
		ProcessBuilder pb = new ProcessBuilder(command);
		return pb.start();
	}

	@Override
	public Process executeFfmpegCutVideo(String dirVideoToCut, String directoryCutVideos, VideoCutInfo videoCutInfo, FfmpegContainerFormat cFormat) throws ICommandException, IOException {

		checkFile(dirVideoToCut, true);
		checkDirectory(directoryCutVideos);
		System.out.println();

		List<String> command = new ArrayList<>();
		int index = 0;
		List<Cut> cuts = videoCutInfo.getCuts();
		if(cuts.size() == 0) {
			throw new ICommandNotCutsException("There's no cuts on VideoInfo");
		}
		PrintWriter writer = new PrintWriter(directoryCutVideos + "/files.txt", "UTF-8");
		command.addAll(Arrays.asList(this.ffmpegDirectory, "-i", dirVideoToCut, "-vcodec", "copy"));
		for(Cut cut: cuts) {
			String cuttedVideo = directoryCutVideos + "/out" + index + "." + cFormat;
			command.addAll(Arrays.asList("-acodec", "copy", "-ss", cut.getStart(), "-to", cut.getEnd(), cuttedVideo));
			writer.println("file '" + "out" + index + "." + cFormat + "'");
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
		command.addAll(Arrays.asList(this.ffmpegDirectory, "-f", "concat", "-i", directoryVideos + "/files.txt", "-c", "copy", directory + "/" + newVideo + "." + cFormat));
		logCommand(command);
		ProcessBuilder pb = new ProcessBuilder(command);

		return pb.start();
	}

	@Override
	public Process createThumbnail(String dirVideo, String imageName, String finalDirectory) throws ICommandException, IOException {
		File file = new File(dirVideo);
		checkDirectory(finalDirectory);
		if(!file.exists()) {
			throw new ICommandFileNotExistException("The file doesn't exists");
		}
		String thumbnailDir = finalDirectory + "/" + imageName + ".jpg";
        List<String> command = new ArrayList<>();
        command.addAll(Arrays.asList(this.ffmpegDirectory, "-i", file.getPath(), "-vf", "scale=640:360", "-ss", "00:00:01.000", "-vframes", "1", thumbnailDir, "-y"));
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

	private void checkFile(String dirFile, boolean checkExist) throws ICommandFileExistException, ICommandFileNotExistException {
		File checkFile = new File(dirFile);
		if(checkExist) {
			if(!checkFile.exists()) {
				throw new ICommandFileNotExistException("The file: " + dirFile +", doesn't exists");
			}
		}
		else {
			if(checkFile.exists()) {
				throw new ICommandFileExistException("The file: " + dirFile + ", actually exist");
			}
		}
	}

}
