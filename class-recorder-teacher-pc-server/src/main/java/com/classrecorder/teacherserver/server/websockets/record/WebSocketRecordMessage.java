package com.classrecorder.teacherserver.server.websockets;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegAudioFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegVideoFormat;

public class WebSocketRecordMessage {
	
	private String action;
	private FfmpegAudioFormat ffmpegAudioFormat;
	private FfmpegContainerFormat ffmpegContainerFormat;
	private FfmpegVideoFormat ffmpegVideoFormat;
	private int frameRate;
	private String videoName;
	
	public WebSocketRecordMessage() {}

	public WebSocketRecordMessage(String action, FfmpegAudioFormat ffmpegAudioFormat,
			FfmpegContainerFormat ffmpegContainerFormat, FfmpegVideoFormat ffmpegVideoFormat, int frameRate, 
			String videoName) {
		this.action = action;
		this.ffmpegAudioFormat = ffmpegAudioFormat;
		this.ffmpegContainerFormat = ffmpegContainerFormat;
		this.ffmpegVideoFormat = ffmpegVideoFormat;
		this.frameRate = frameRate;
		this.videoName = videoName;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public FfmpegAudioFormat getFfmpegAudioFormat() {
		return ffmpegAudioFormat;
	}
	public void setFfmpegAudioFormat(FfmpegAudioFormat ffmpegAudioFormat) {
		this.ffmpegAudioFormat = ffmpegAudioFormat;
	}
	public FfmpegContainerFormat getFfmpegContainerFormat() {
		return ffmpegContainerFormat;
	}
	public void setFfmpegContainerFormat(FfmpegContainerFormat ffmpegContainerFormat) {
		this.ffmpegContainerFormat = ffmpegContainerFormat;
	}
	public FfmpegVideoFormat getFfmpegVideoFormat() {
		return ffmpegVideoFormat;
	}
	public void setFfmpegVideoFormat(FfmpegVideoFormat ffmpegVideoFormat) {
		this.ffmpegVideoFormat = ffmpegVideoFormat;
	}
	public int getFrameRate() {
		return frameRate;
	}
	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	@Override
	public String toString() {
		return "WebSocketRecordPCMessage [action=" + action + ", ffmpegAudioFormat=" + ffmpegAudioFormat
				+ ", ffmpegContainerFormat=" + ffmpegContainerFormat + ", ffmpegVideoFormat=" + ffmpegVideoFormat
				+ ", frameRate=" + frameRate + ", videoName=" + videoName + "]";
	}
	
}
