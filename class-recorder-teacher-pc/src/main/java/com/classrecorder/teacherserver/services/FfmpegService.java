package com.classrecorder.teacherserver.services;

import java.io.IOException;

import javax.naming.OperationNotSupportedException;

import org.springframework.stereotype.Service;

import com.classrecorder.teacherserver.ffmpegwrapper.FfmpegAudioFormat;
import com.classrecorder.teacherserver.ffmpegwrapper.FfmpegException;
import com.classrecorder.teacherserver.ffmpegwrapper.FfmpegVideoFormat;
import com.classrecorder.teacherserver.ffmpegwrapper.FfmpegWrapper;
import com.classrecorder.teacherserver.ffmpegwrapper.ICommandException;
import com.classrecorder.teacherserver.ffmpegwrapper.VideoInfo;

@Service
public class FfmpegService {
	
	private FfmpegWrapper ffmpegWrapper;
	
	public FfmpegService() throws OperationNotSupportedException {
		this.ffmpegWrapper = new FfmpegWrapper();
	}
	
	public FfmpegService setVideoFormat(FfmpegVideoFormat videoFormat) {
		ffmpegWrapper.setVideoFormat(videoFormat);
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
	
	public Process startRecordingVideoAndAudio() throws IOException, FfmpegException, ICommandException {
		return ffmpegWrapper.startRecordingVideoAndAudio();
	}
	
	public Process startRecordingVideo() throws IOException, FfmpegException, ICommandException {
		return ffmpegWrapper.startRecordingVideo();
	}
	
	public Process stopRecording() throws IOException, FfmpegException {
		return ffmpegWrapper.stopRecording();
	}
	
	public Process mergeAudioAndVideo(String audioNameOrigin, FfmpegAudioFormat aFormatOrigin, String videoNameOrigin, FfmpegVideoFormat vFormatOrigin) throws FfmpegException, IOException, ICommandException {
		return ffmpegWrapper.mergeAudioAndVideo(audioNameOrigin, aFormatOrigin, videoNameOrigin, vFormatOrigin);
	}
	
	public Process cutVideo(VideoInfo videoInfo, String videoToCut, String directoryCutVideos) throws FfmpegException, ICommandException, IOException {
		return ffmpegWrapper.cutVideo(videoInfo, videoToCut, directoryCutVideos);
	}
	
	public Process mergeVideos(String fileStrVideos, String directoryVideos) throws FfmpegException, ICommandException, IOException {
		return ffmpegWrapper.mergeVideos(fileStrVideos, directoryVideos);
	}
	
	public void printInfo() throws IOException, InterruptedException {
		ffmpegWrapper.printInfo();
	}
}
