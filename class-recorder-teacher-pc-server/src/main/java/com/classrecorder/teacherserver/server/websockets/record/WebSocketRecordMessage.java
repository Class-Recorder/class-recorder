package com.classrecorder.teacherserver.server.websockets.record;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;

public class WebSocketRecordMessage {
	
	private String action;
	private FfmpegContainerFormat ffmpegContainerFormat;
	private int frameRate;
	private String videoName;
	
	public WebSocketRecordMessage() {}

	public WebSocketRecordMessage(String action, FfmpegContainerFormat ffmpegContainerFormat, int frameRate, 
			String videoName) {
		this.action = action;
		this.ffmpegContainerFormat = ffmpegContainerFormat;
		this.frameRate = frameRate;
		this.videoName = videoName;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	public FfmpegContainerFormat getFfmpegContainerFormat() {
		return ffmpegContainerFormat;
	}
	public void setFfmpegContainerFormat(FfmpegContainerFormat ffmpegContainerFormat) {
		this.ffmpegContainerFormat = ffmpegContainerFormat;
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

	@Override
	public String toString() {
		return "WebSocketRecordMessage{" +
				"action='" + action + '\'' +
				", ffmpegContainerFormat=" + ffmpegContainerFormat +
				", frameRate=" + frameRate +
				", videoName='" + videoName + '\'' +
				'}';
	}
}
