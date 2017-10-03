package com.classrecorder.teacherserver.ffmpegwrapper;

public class ICommandException extends Exception{
	
	static final long serialVersionUID = 1L;

	ICommandException(){
	}

	ICommandException(String message){
		super(message);
	}

	ICommandException(Throwable cause){
		super(cause);
	}

	ICommandException(String message, Throwable cause){
		super(message, cause);
	}

	ICommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
