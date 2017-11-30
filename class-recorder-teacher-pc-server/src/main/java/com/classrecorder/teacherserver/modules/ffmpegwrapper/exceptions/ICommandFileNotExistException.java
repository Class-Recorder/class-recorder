package com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions;

public class ICommandFileNotExistException extends ICommandException{
	
	static final long serialVersionUID = 1L;

	public ICommandFileNotExistException(){
	}

	public ICommandFileNotExistException(String message){
		super(message);
	}
	
}
