package com.classrecorder.teacherserver.ffmpegwrapper;

public class FfmpegException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public FfmpegException(){
	}

	public FfmpegException(String message){
		super(message);
	}

	public FfmpegException(Throwable cause){
		super(cause);
	}

	public FfmpegException(String message, Throwable cause){
		super(message, cause);
	}

	public FfmpegException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
