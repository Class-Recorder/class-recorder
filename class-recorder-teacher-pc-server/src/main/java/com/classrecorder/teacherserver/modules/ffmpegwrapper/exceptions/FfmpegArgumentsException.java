package com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions;

public class FfmpegArgumentsException extends FfmpegException {

	private static final long serialVersionUID = 1L;
	
	public FfmpegArgumentsException(){
	}

	public FfmpegArgumentsException(String message){
		super(message);
	}

}
