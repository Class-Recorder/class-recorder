package com.classrecorder.teacherserver.server.websockets.record;

public class WebSocketRecordMessageServer {

    private String message;
    private boolean isError;

    public WebSocketRecordMessageServer(String message, boolean isError) {
        this.message = message;
        this.isError = isError;
    }

	public String getMessage() {
		return message;
	}

	public boolean isError() {
		return isError;
    }
    
	public void setIsError(boolean isError) {
		this.isError = isError;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}