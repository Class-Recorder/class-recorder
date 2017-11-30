package com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions;

public class FfmpegException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public FfmpegException(){
	}

	public FfmpegException(String message){
		super(message);
	}

}
