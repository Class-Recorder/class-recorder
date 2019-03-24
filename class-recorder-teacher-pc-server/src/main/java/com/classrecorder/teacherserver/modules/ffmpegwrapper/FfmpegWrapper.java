package com.classrecorder.teacherserver.modules.ffmpegwrapper;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.FfmpegArgumentsException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.FfmpegException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.FfmpegFrameRateException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.FfmpegIsNotRecException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.FfmpegIsRecException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.FfmpegWorkingException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandFileExistException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandFileNotExistException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandNoVideosCutException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandNotCutsException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;

/**
 * This class consist of a service capable of capture video and audio
 * from desktop and join audio and video captured separately
 * 
 * @author Carlos Ruiz Ballesteros 
 *
 */
public class FfmpegWrapper {


	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/*
	* Operating system
	*/
	private String os;

	/*
	 * Arguments used with ffmpeg to record a video
	 */
	private int screenWidth;
	private int screenHeight;
	private FfmpegContainerFormat videoContainerFormat;
	private int framerate;
	private String videoName;
	private String directory;
	private List<FfmpegOutputObserver> observers;
	private String x11device;

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
	public FfmpegWrapper(Path ffmpegOutput, String x11device) throws OperationNotSupportedException, IOException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.os = System.getProperty("os.name");
		log.info("Operaiting system: " + this.os);
		
		this.screenWidth = new Double(screenSize.getWidth()).intValue();
		this.screenHeight = new Double(screenSize.getHeight()).intValue();
		this.x11device = x11device;
		this.videoContainerFormat = null;
		this.videoName = null;
		this.directory = null;
		this.recording = false;
		this.observers = new ArrayList<>();
		this.observers.add(new FfmpegWrapperLogger());
		
		if (this.os.equals("Linux")) {
	        this.ffmpegCommand = new ICommandLinux(ffmpegOutput, x11device);
        }
        else if(this.os.contains("Windows")) {
        	this.ffmpegCommand = new ICommandWindows();
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
	
	public void setObservers(List<FfmpegOutputObserver> observers) {
	    this.observers = observers;
	}

	public void addObserver(FfmpegOutputObserver observer) {
		this.observers.add(observer);
	}
	
	public Process getProcess() {
		return this.process;
	}
	
	
	private void checkFormat() throws FfmpegException {
		if(videoContainerFormat == null || videoName == null || directory == null) {
			throw new FfmpegArgumentsException("Arguments are not set properly");
		}
		if(framerate <= 0) {
			throw new FfmpegFrameRateException("Framerate should be greater than 0");
		}
	}
	
	public Process startRecordingVideoAndAudio() throws IOException, ICommandException, FfmpegException {
		checkFormat();
		try {
			process = ffmpegCommand.executeFfmpegVideoAndSound(screenWidth, screenHeight, framerate, directory, videoName, videoContainerFormat);
			writeLastOutput(false);
		} catch(ICommandFileExistException exception) {
			process = null;
			throw exception;
		} catch(Exception e) {
			process = null;
			e.printStackTrace();
		}
		
		log.info("Recording video and audio: " + videoName);
		recording = true;
		return process;
	}
	
	public Process stopRecording() throws IOException, FfmpegException, ICommandException {
		if(!recording) {
			throw new FfmpegIsNotRecException("Ffmpeg is not recording");
		}
		if (os.contains("Windows")) {
			OutputStream ostream = process.getOutputStream();
			ostream.write("q\n".getBytes());
			ostream.flush();
		} else {
			process.destroy();
		}
		process.destroy();
		process = null;
		recording = false;
		log.info("Video saved: " + videoName);
        String videoDirName = directory + "/" + this.videoName + "." + videoContainerFormat;
        File thumbnailFile = new File(directory + "/" + this.videoName + ".jpg");
        int tries = 0;
        while(!thumbnailFile.exists() && tries < 10) {
            tries++;
            ffmpegCommand.createThumbnail(videoDirName, videoName, directory);
            try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
		return process;
	}
	
	public Process mergeAudioAndVideo(String dirAudioOri, String dirVideoOri) throws FfmpegException, IOException, ICommandException {
		if(recording) {
			throw new FfmpegIsRecException("Ffmpeg is recording");
		}
		if(process != null) {
			throw new FfmpegWorkingException("Ffmpeg is working");
		}
		try {
            System.out.println(dirAudioOri);
			process = ffmpegCommand.executeFfmpegMergeVideoAudio(dirAudioOri, dirVideoOri, directory, videoName, videoContainerFormat );
			writeLastOutput(true);
		} catch(ICommandFileExistException | ICommandFileNotExistException exception) {
			process = null;
			throw exception;
		} catch(Exception e) {
			process = null;
			e.printStackTrace();
		}
		
		log.info("Merging audio and video");
		return process;
	}

	public Process cutVideo(String dirVideoToCut, VideoCutInfo videoInfo, String directoryCutVideos) 
			throws FfmpegException, ICommandException, IOException {
		
		if(recording) {
			throw new FfmpegIsRecException("Ffmpeg is recording");
		}
		if(process != null) {
			throw new FfmpegWorkingException("Ffmpeg is working");
		}
		String videoContainerStr = "";
		int index = dirVideoToCut.lastIndexOf('.');
		if (!(index > 0)) {
			throw new FfmpegException("Not valid video to cut");
		}
		videoContainerStr = dirVideoToCut.substring(index+1);
		FfmpegContainerFormat videoContainerFormat = FfmpegContainerFormat.valueOf(videoContainerStr);
		// If execution fail, process must be null
		try {
			process = ffmpegCommand.executeFfmpegCutVideo(dirVideoToCut, directoryCutVideos, videoInfo, videoContainerFormat);
			writeLastOutput(true);
		} catch(ICommandFileNotExistException | ICommandNotCutsException exception) {
			process = null;
			throw exception;
		} catch(Exception e) {
			process = null;
			e.printStackTrace();
		}
		log.info("Cutting video");
		return process;
	}
	
	public Process mergeVideos(String directoryVideos) throws FfmpegException, ICommandException, IOException {
		if(recording) {
			throw new FfmpegIsRecException("Ffmpeg is recording");
		}
		try {
			process = ffmpegCommand.executeMergeVideos(videoContainerFormat, videoName, directory, directoryVideos);
			writeLastOutput(true);
		} catch(ICommandFileExistException | ICommandNoVideosCutException exception) {
			process = null;
			throw exception;
		} catch(Exception e) {
			process = null;
			e.printStackTrace();
		}
		
		log.info("Merging videos");
		return process;
	}
	
	public Process createThumbnail(String name, String imageName, String directory) throws FfmpegException, ICommandException, IOException {
		if(recording) {
			throw new FfmpegIsRecException("Ffmpeg is recording");
		}
		try {
			process = ffmpegCommand.createThumbnail(name, imageName, directory);
			writeLastOutput(true);
		} catch (ICommandFileNotExistException exception) {
			process = null;
			throw exception;
		} catch(Exception e) {
			process = null;
			e.printStackTrace();
		}
		log.info("Created Thumbnail");
		return process;
	}

	
	/**
	 * Log information about arguments
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void printInfo() throws IOException, InterruptedException {
		log.info("Screen info -- Width: " + screenWidth + " -- Heigth: " + screenHeight);
		log.info("Container Format info -- Video: " + videoContainerFormat);
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
	
	private void writeLastOutput(boolean forgetProcess) {
		InputStream ins = process.getErrorStream();
		Thread thread = new Thread() {
			public void run() {
		        BufferedReader buff = new BufferedReader(new InputStreamReader(ins));
		        String lineBuffer;
				try {
					lineBuffer = buff.readLine();
					while(lineBuffer !=null) {
						for(FfmpegOutputObserver o: observers) {
							try {
								o.update(lineBuffer);
							}
							catch (Exception e) {
								log.error(o.getClass().getName() + "has failed while receiving buffer process");
								log.error(e.getMessage());
							}
						}
			        	lineBuffer = buff.readLine();
			        }
					for(FfmpegOutputObserver o: observers) {
						//this end message is used to know where a process has ended
						//and it communicate it to the observers
						try {
							o.update("end");
						}
						catch(Exception e	) {
							log.error(o.getClass().getName() + "has failed while receiving buffer process");
							log.error(e.getMessage());
						}
						
					}
					if(forgetProcess) {
						process = null;
					}
				} catch (Exception e) {
					if(forgetProcess) {
						process = null;
					}
				}
		    }
		};
		thread.start();
	}

}
