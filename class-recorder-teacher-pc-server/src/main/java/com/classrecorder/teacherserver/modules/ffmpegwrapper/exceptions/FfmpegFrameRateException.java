package com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions;

public class FfmpegFrameRateException extends FfmpegException {

	private static final long serialVersionUID = 1L;
	
	public FfmpegFrameRateException(){
	}

	public FfmpegFrameRateException(String message){
		super(message);
	}

}
