package com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions;

public class FfmpegIsNotRecException extends FfmpegException {

	private static final long serialVersionUID = 1L;

	public FfmpegIsNotRecException(){
	}

	public FfmpegIsNotRecException(String message){
		super(message);
	}


}
