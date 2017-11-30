package com.classrecorder.teacherserver.server.services;

import java.io.IOException;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.springframework.stereotype.Service;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.FfmpegOutputObserver;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.FfmpegWrapper;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.FfmpegException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegAudioFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegVideoFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;

@Service
public class FfmpegService {
	
	private FfmpegWrapper ffmpegWrapper;
	
	public FfmpegService() throws OperationNotSupportedException {
		this.ffmpegWrapper = new FfmpegWrapper();
	}
	
	public FfmpegService setContainerVideoFormat(FfmpegContainerFormat videoFormat) {
		ffmpegWrapper.setVideoContainerFormat(videoFormat);
		return this;
	}
	
	public FfmpegService setAudioFormat(FfmpegAudioFormat audioFormat) {
		ffmpegWrapper.setAudioFormat(audioFormat);
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
	
	public FfmpegService setVideoFormat(FfmpegVideoFormat videoFormat) {
		ffmpegWrapper.setVideoFormat(videoFormat);
		return this;
	}
	
	public FfmpegService setObservers(List<FfmpegOutputObserver> observers) {
		ffmpegWrapper.setObservers(observers);
		return this;
	}
	
	public Process getProcess() {
		return ffmpegWrapper.getProcess();
	}
	
	public Process startRecordingVideoAndAudio() throws IOException, FfmpegException, ICommandException {
		return ffmpegWrapper.startRecordingVideoAndAudio();
	}
	
	public Process startRecordingVideo() throws IOException, FfmpegException, ICommandException {
		return ffmpegWrapper.startRecordingVideo();
	}
	
	public Process stopRecording() throws IOException, FfmpegException, ICommandException {
		return ffmpegWrapper.stopRecording();
	}
	
	public Process mergeAudioAndVideo(String audioNameOrigin, FfmpegAudioFormat aFormatOrigin, String videoNameOrigin, FfmpegContainerFormat vFormatOrigin) throws FfmpegException, IOException, ICommandException {
		return ffmpegWrapper.mergeAudioAndVideo(audioNameOrigin, aFormatOrigin, videoNameOrigin, vFormatOrigin);
	}
	
	public Process cutVideo(VideoCutInfo videoInfo, String videoToCut, String directoryCutVideos) throws FfmpegException, ICommandException, IOException {
		return ffmpegWrapper.cutVideo(videoInfo, videoToCut, directoryCutVideos);
	}
	
	public Process mergeVideos(String directoryVideos) throws FfmpegException, ICommandException, IOException {
		return ffmpegWrapper.mergeVideos(directoryVideos);
	}
	
	public Process createThumbnail(String name, String directory) throws FfmpegException, ICommandException, IOException {
		return ffmpegWrapper.createThumbnail(name, directory);
	}
	
	public boolean isFfmpegWorking() {
		return ffmpegWrapper.isFfmpegWorking();
	}
	
	public void printInfo() throws IOException, InterruptedException {
		ffmpegWrapper.printInfo();
	}
}
