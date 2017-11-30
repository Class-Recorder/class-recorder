package com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions;

public class ICommandNoVideosCutException extends ICommandException {
	
	private static final long serialVersionUID = 1L;

	public ICommandNoVideosCutException() { 
		
	}
	
	public ICommandNoVideosCutException(String message) {
		super(message);
	}

}
