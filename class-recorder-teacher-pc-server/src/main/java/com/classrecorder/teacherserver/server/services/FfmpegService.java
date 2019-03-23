package com.classrecorder.teacherserver.server.services;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.springframework.stereotype.Service;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.FfmpegOutputObserver;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.FfmpegWrapper;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.FfmpegException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;

@Service
public class FfmpegService {
	
	private FfmpegWrapper ffmpegWrapper;
	
	public FfmpegService(Path ffmpegOutput, String x11device) throws OperationNotSupportedException, IOException {
		this.ffmpegWrapper = new FfmpegWrapper(ffmpegOutput, x11device);
	}
	
	public FfmpegService setContainerVideoFormat(FfmpegContainerFormat videoFormat) {
		ffmpegWrapper.setVideoContainerFormat(videoFormat);
		return this;
	}
	
	public FfmpegService setFrameRate(int frameRate) {
		ffmpegWrapper.setFrameRate(frameRate);
		return this;
	}
	
	public FfmpegService setVideoName(String videoName) {
		ffmpegWrapper.setVideoName(videoName);
		return this;
	}
	
	public FfmpegService setDirectory(String directory) {
		ffmpegWrapper.setDirectory(directory);
		return this;
	}
	
	public FfmpegService setObservers(List<FfmpegOutputObserver> observers) {
		ffmpegWrapper.setObservers(observers);
		return this;
	}

	public FfmpegService addObserver(FfmpegOutputObserver observer) {
		ffmpegWrapper.addObserver(observer);
		return this;
	}
	
	public Process getProcess() {
		return ffmpegWrapper.getProcess();
	}
	
	public Process startRecordingVideoAndAudio() throws IOException, FfmpegException, ICommandException {
		return ffmpegWrapper.startRecordingVideoAndAudio();
	}
	
	public Process stopRecording() throws IOException, FfmpegException, ICommandException {
		return ffmpegWrapper.stopRecording();
	}
	
	public Process mergeAudioAndVideo(String dirAudioOri, String dirVideoOri) throws FfmpegException, IOException, ICommandException {
		return ffmpegWrapper.mergeAudioAndVideo(dirAudioOri, dirVideoOri);
	}
	
	public Process cutVideo(String dirVideoToCut, VideoCutInfo videoInfo, String directoryCutVideos) throws FfmpegException, ICommandException, IOException {
		return ffmpegWrapper.cutVideo(dirVideoToCut, videoInfo, directoryCutVideos);
	}
	
	public Process mergeVideos(String directoryVideos) throws FfmpegException, ICommandException, IOException {
		return ffmpegWrapper.mergeVideos(directoryVideos);
	}
	
	public Process createThumbnail(String dirVideo, String imageName, String directory) throws FfmpegException, ICommandException, IOException {
		return ffmpegWrapper.createThumbnail(dirVideo, imageName, directory);
	}
	
	public boolean isFfmpegWorking() {
		return ffmpegWrapper.isFfmpegWorking();
	}
	
	public void printInfo() throws IOException, InterruptedException {
		ffmpegWrapper.printInfo();
	}
}
