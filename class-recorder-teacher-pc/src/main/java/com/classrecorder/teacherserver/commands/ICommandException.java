package com.classrecorder.teacherserver.commands;

public class ICommandException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ICommandException(){
	}

	public ICommandException(String message){
		super(message);
	}

	public ICommandException(Throwable cause){
		super(cause);
	}

	public ICommandException(String message, Throwable cause){
		super(message, cause);
	}

	public ICommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
