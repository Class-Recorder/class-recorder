package com.classrecorder.teacherserver.modules.ffmpegwrapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandFileExistException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandFileNotExistException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandNoVideosCutException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandNotCutsException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.Cut;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains all the different commands used for ffmpeg in Linux.
 *
 * Ffmpeg record commands from docker container runs with the option "-r" instead of "-framerate"
 * because records does not work with xserver with "-framerate" options. But docker is only used in
 * development and ci environments
 *
 *
 * @author Carlos Ruiz Ballesteros
 *
 */
class ICommandLinux implements ICommand{

	private static final Logger log = LoggerFactory.getLogger(ICommandLinux.class);

	/**
	 * Screen device used for recording.
	 */
	private String x11device;

	/*
	* Location of ffmpegDirectory. This is necessary because the program is distributed with a
	* version of ffmpeg binary
	*/
	private String ffmpegDirectory;

	/*
	* Variable that represents if the application is running from a docker container
	*/
	private boolean fromDocker;

	public ICommandLinux(Path outputLog, String x11device, String ffmpegDirectory) {
		this.x11device = x11device;
		this.ffmpegDirectory = ffmpegDirectory;
		this.fromDocker = isRunningInsideDocker();
	}

    @Override
    public Process executeFfmpegVideoAndSound(int screenWidth, int screenHeight, int frameRate,
                                              String directory, String name, FfmpegContainerFormat cFormat) throws IOException, ICommandException {

        checkDirectory(directory);
		checkFile(name, cFormat, directory, false);
		String frameRateArgument = this.fromDocker ? "-r" : "-framerate";
		List<String> command = new ArrayList<>();
		if(this.isRunningInTravis()) {
			command.addAll(Arrays.asList(this.ffmpegDirectory, "-s", "640x480", "-f", "rawvideo", "-pix_fmt", "rgb24", "-r", "25"));
			command.addAll(Arrays.asList("-i", "/dev/zero"));
		} else {
			command.addAll(Arrays.asList(this.ffmpegDirectory, "-f", "x11grab", frameRateArgument, Integer.toString(frameRate)));
			command.addAll(Arrays.asList("-s", screenWidth + "x" + screenHeight, "-i", x11device));
			if(cFormat.equals(FfmpegContainerFormat.mkv))  {
				command.addAll(Arrays.asList("-vcodec", "h264", "-thread_queue_size", "20480", "-f", "alsa", "-i", "default", "-pix_fmt", "yuv420p"));
				command.addAll(Arrays.asList("-acodec", "mp3", "-preset", "ultrafast", "-crf", "30"));
			}
			if(cFormat.equals(FfmpegContainerFormat.mp4)) {
				command.addAll(Arrays.asList("-vcodec", "h264", "-thread_queue_size", "20480", "-f", "alsa", "-i", "default", "-pix_fmt", "yuv420p"));
				command.addAll(Arrays.asList("-acodec", "aac", "-strict", "-2", "-preset", "ultrafast", "-crf", "30"));
			}
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

	/**
	 * Checks if the application is running in docker.
	 * @return true if is running in docker container false in other cases.
	 */
	public static Boolean isRunningInsideDocker() {
        try (Stream <String> stream =
            Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker"));
        } catch (IOException e) {
            return false;
        }
	}

	private Boolean isRunningInTravis() {
		return System.getenv("TRAVIS") != null;
	}

}
