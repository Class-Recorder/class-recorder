package com.classrecorder.teacherserver.modules.ffmpegwrapper;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegAudioFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegVideoFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;

/**
 * This class consist of a service capable of capture video and audio
 * from desktop and join audio and video captured separately
 * 
 * @author Carlos Ruiz Ballesteros 
 * 
 *
 */
public class FfmpegWrapper {


	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final String NAME_FILE_OUTPUT = "lastOutput.txt";
	
	/*
	 * Arguments used with ffmpeg to record a video
	 */
	private int screenWidth;
	private int screenHeight;
	private FfmpegContainerFormat videoContainerFormat;
	private FfmpegAudioFormat audioFormat;
	private FfmpegVideoFormat videoFormat;
	private int framerate;
	private String videoName;
	private String directory;
	
	/*
	 * Process variables
	 */
	private Process process;
	private boolean recording;
	
	/*
	 * Command to be executed
	 */
	private ICommand ffmpegCommand;
	
	/**
	 * FfmpegService constructor
	 * @throws OperationNotSupportedException 
	 */
	public FfmpegWrapper() throws OperationNotSupportedException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		String so = System.getProperty("os.name");
		
		this.screenWidth = new Double(screenSize.getWidth()).intValue();
		this.screenHeight = new Double(screenSize.getHeight()).intValue();
		this.videoContainerFormat = null;
		this.audioFormat = null;
		this.videoFormat = null;
		this.videoName = null;
		this.directory = null;
		this.recording = false;
		
		if (so.equals("Linux")) {
	        this.ffmpegCommand = new ICommandLinux();	
        }
        else {
        	throw new OperationNotSupportedException("OS not supported");
        }
	}
	
	/**
	 * Set a video format to record a video
	 * @param videoFormat the video format to be used
	 * @return FfmpegService object
	 */
	public FfmpegWrapper setVideoContainerFormat(FfmpegContainerFormat videoFormat) {
		this.videoContainerFormat = videoFormat;
		return this;
	}
	
	/**
	 * Set an audio format to record the video
	 * @param audioFormat
	 * @return FfmpegService object
	 */
	public FfmpegWrapper setAudioFormat(FfmpegAudioFormat audioFormat) {
		this.audioFormat = audioFormat;
		return this;
	}
	
	/**
	 * Set the frame rate of the video
	 * @param frameRate integer that represents the frame rate
	 * @return
	 */
	public FfmpegWrapper setFrameRate(int frameRate) {
		this.framerate = frameRate;
		return this;
	}
	
	public FfmpegWrapper setVideoName(String videoName) {
		this.videoName = videoName;
		return this;
	}
	
	public FfmpegWrapper setDirectory(String directory) {
		this.directory = directory;
		return this;
	}

	public FfmpegWrapper setVideoFormat(FfmpegVideoFormat videoFormat) {
		this.videoFormat = videoFormat;
		return this;
	}
	
	
	private void checkFormat() throws FfmpegException {
		if(videoContainerFormat == null || audioFormat == null || videoName == null || directory == null || videoFormat == null) {
			throw new FfmpegException("Arguments are not set properly");
		}
		if(framerate <= 0) {
			throw new FfmpegException("Framerate should be greater than 0");
		}
	}
	
	public Process startRecordingVideoAndAudio() throws IOException, FfmpegException, ICommandException {
		checkFormat();
		process = ffmpegCommand.executeFfmpegVideoAndSound(screenWidth, screenHeight, videoContainerFormat, audioFormat, videoFormat, framerate, videoName, directory);
		writeLastOutput();
		log.info("Recording video and audio: " + videoName);
		recording = true;
		return process;
	}
	
	public Process startRecordingVideo() throws IOException, FfmpegException, ICommandException{
		checkFormat();
		process = this.ffmpegCommand.executeFfmpegVideo(screenWidth, screenHeight, videoContainerFormat, videoFormat, framerate, videoName, directory);
		writeLastOutput();
		log.info("Recording video: " + videoName);
		recording = true;
		return process;
	}
	
	public Process stopRecording() throws IOException, FfmpegException {
		if(!recording) {
			throw new FfmpegException("Ffmpeg is not recording");
		}
		process.destroy();
		recording = false;
		log.info("Video saved: " + videoName);
		return process;
	}
	
	public Process mergeAudioAndVideo(String audioNameOrigin, FfmpegAudioFormat aFormatOrigin, String videoNameOrigin, FfmpegContainerFormat cFormatOrigin) throws FfmpegException, IOException, ICommandException {
		if(recording) {
			throw new FfmpegException("Ffmpeg is recording");
		}
		process = ffmpegCommand.executeFfmpegMergeVideoAudio(cFormatOrigin, aFormatOrigin, videoContainerFormat, audioFormat, videoFormat, audioNameOrigin, videoNameOrigin, videoName, directory);
		writeLastOutput();
		log.info("Merging audio and video");
		return process;
	}

	public Process cutVideo(VideoCutInfo videoInfo, String videoToCut, String directoryCutVideos) throws FfmpegException, ICommandException, IOException {
		if(recording) {
			throw new FfmpegException("Ffmpeg is recording");
		}
		process = ffmpegCommand.executeFfmpegCutVideo(videoContainerFormat, videoInfo, videoToCut, directory, directoryCutVideos);
		writeLastOutput();
		log.info("Cutting and merging video");
		return process;
	}
	
	public Process mergeVideos(String fileStrVideos, String directoryVideos) throws FfmpegException, ICommandException, IOException {
		if(recording) {
			throw new FfmpegException("Ffmpeg is recording");
		}
		process = ffmpegCommand.executeMergeVideos(videoContainerFormat, videoName, directory, fileStrVideos, directoryVideos);
		writeLastOutput();
		log.info("Merging videos");
		return process;
	}

	
	/**
	 * Log information about arguments
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void printInfo() throws IOException, InterruptedException {
		log.info("Screen info -- Width: " + screenWidth + " -- Heigth: " + screenHeight);
		log.info("Container Format info -- Video: " + videoContainerFormat + " -- Audio: " + audioFormat);
		log.info("Frames info -- Frames: " + framerate);
		log.info("File   info -- File " + videoName);
	}
	
	public boolean isFfmpegWorking() {
		if(process == null) {
			return false;
		}
		else {
			return process.isAlive();
		}
	}
	
	private void writeLastOutput() {
		InputStream ins = process.getErrorStream();
		Thread thread = new Thread() {
			public void run() {
		        BufferedReader buff = new BufferedReader(new InputStreamReader(ins));
		        String lineBuffer;
				try {
					PrintWriter writer = new PrintWriter(directory + "/" + NAME_FILE_OUTPUT);
					lineBuffer = buff.readLine();
					while(lineBuffer !=null) {
						writer.println(lineBuffer);
			        	lineBuffer = buff.readLine();
			        }
					writer.close();
				} catch (IOException e) {
				}
		    }
		};
		thread.start();
	}
}
