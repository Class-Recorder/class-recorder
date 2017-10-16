package com.classrecorder.teacherserver.server.websockets;

public class ConsMsg {
	
	//Received
	public static final String COMPUTER_DEVICE = "computer"; 
	public static final String MOBILE_DEVICE = "mobile";
	public static final String REC_VID_AUD = "recordVideoAndAudio";
	public static final String REC_VID = "recordVideo";
	public static final String STOP = "stop";
	public static final String PAUSE = "pause";
	public static final String CONTINUE = "continue";
	
	//Sended
	public static final String RECORDING = "Recording";
	public static final String STOPPED = "Stopped";
	public static final String MOBILE_REC_STOPPED = "Mobile recording stopped";
}
