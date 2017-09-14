package com.classrecorder.teacherserver.services;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.classrecorder.teacherserver.commands.ICommand;
import com.classrecorder.teacherserver.commands.ICommandException;
import com.classrecorder.teacherserver.commands.ICommandLinux;

/**
 * This class consist of a service capable of capture video and audio
 * from desktop and join audio and video captured separately
 * 
 * @author Carlos Ruiz Ballesteros 
 * 
 *
 */
@Service
public class FfmpegService {


	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/*
	 * Arguments used with ffmpeg to record a video
	 */
	private int screenWidth;
	private int screenHeight;
	private FfmpegVideoFormat videoFormat;
	private FfmpegAudioFormat audioFormat;
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
	public FfmpegService() throws OperationNotSupportedException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		String so = System.getProperty("os.name");
		
		this.screenWidth = new Double(screenSize.getWidth()).intValue();
		this.screenHeight = new Double(screenSize.getHeight()).intValue();
		this.videoFormat = null;
		this.audioFormat = null;
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
	public FfmpegService setVideoFormat(FfmpegVideoFormat videoFormat) {
		this.videoFormat = videoFormat;
		return this;
	}
	
	/**
	 * Set an audio format to record the video
	 * @param audioFormat
	 * @return FfmpegService object
	 */
	public FfmpegService setAudioFormat(FfmpegAudioFormat audioFormat) {
		this.audioFormat = audioFormat;
		return this;
	}
	
	/**
	 * Set the frame rate of the video
	 * @param frameRate integer that represents the frame rate
	 * @return
	 */
	public FfmpegService setFrameRate(int frameRate) {
		this.framerate = frameRate;
		return this;
	}
	
	public FfmpegService setVideoName(String videoName) {
		this.videoName = videoName;
		return this;
	}
	
	public FfmpegService setDirectory(String directory) {
		this.directory = directory;
		return this;
	}
	
	
	private void checkFormat() throws FfmpegException {
		if(videoFormat == null && audioFormat == null && videoName == null && directory == null) {
			throw new FfmpegException("Arguments are not set properly");
		}
		if(framerate <= 0) {
			throw new FfmpegException("Framerate should be greater than 0");
		}
	}
	
	public void startRecordingVideoAndAudio() throws IOException, FfmpegException, ICommandException {
		checkFormat();
		process = ffmpegCommand.executeFfmpegVideoAndSound(screenWidth, screenHeight, videoFormat, audioFormat, framerate, videoName, directory);
		log.info("Recording video and audio: " + videoName);
		recording = true;
	}
	
	public void startRecordingVideo() throws IOException, FfmpegException, ICommandException{
		checkFormat();
		process = this.ffmpegCommand.executeFfmpegVideo(screenWidth, screenHeight, videoFormat, framerate, videoName, directory);
		log.info("Recording video: " + videoName);
		recording = true;
	}
	
	public void stopRecording() throws IOException, FfmpegException {
		if(!recording) {
			throw new FfmpegException("Ffmpeg is not recording");
		}
		process.destroy();
		recording = false;
		log.info("Video saved: " + videoName);
	}
	
	public void mergeAudioAndVideo(String audioNameOrigin, FfmpegAudioFormat aFormatOrigin, String videoNameOrigin, FfmpegVideoFormat vFormatOrigin) throws FfmpegException, IOException, ICommandException {
		if(recording) {
			throw new FfmpegException("Ffmpeg is recording");
		}
		process = ffmpegCommand.executeFfmpegMergeVideoAudio(vFormatOrigin, aFormatOrigin, videoFormat, audioFormat, audioNameOrigin, videoNameOrigin, videoName, directory);
		log.info("Merging audio and video");
	}

	
	/**
	 * Log information about arguments
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void printInfo() throws IOException, InterruptedException {
		log.info("Screen info -- Width: " + screenWidth + " -- Heigth: " + screenHeight);
		log.info("Format info -- Video: " + videoFormat + " -- Audio: " + audioFormat);
		log.info("Frames info -- Frames: " + framerate);
		log.info("File   info -- File " + videoName);
	}
}
