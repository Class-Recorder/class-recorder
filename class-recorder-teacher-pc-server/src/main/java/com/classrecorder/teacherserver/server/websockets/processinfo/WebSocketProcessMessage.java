package com.classrecorder.teacherserver.server.websockets.processinfo;

public class WebSocketProcessMessage {
	
	private String action;
	private String processOutput;

	public WebSocketProcessMessage(String action, String processOutput) {
		this.action = action;
		this.processOutput = processOutput;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getProcessOutput() {
		return processOutput;
	}

	public void setProcessOutput(String processOutput) {
		this.processOutput = processOutput;
	}
	
	
	
	

}
