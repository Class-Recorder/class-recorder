package com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions;

public class ICommandFileExistException extends ICommandException{
	
	static final long serialVersionUID = 1L;

	public ICommandFileExistException(){
	}

	public ICommandFileExistException(String message){
		super(message);
	}

}
