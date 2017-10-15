 package com.classrecorder.teacherserver.server.websockets;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.Cut;
import com.classrecorder.teacherserver.server.services.FfmpegService;
import com.classrecorder.teacherserver.util.TimeCounter;
import com.google.gson.Gson;

@Component
public class RecordPCHandler extends TextWebSocketHandler {
	
	
	private static final Logger log = LoggerFactory.getLogger(RecordPCHandler.class);
	WebSocketSession sessionComputer;
	WebSocketSession sessionMobile;
	
	//Recording Variables
	private TimeCounter timeCounter = new TimeCounter();
	private boolean onPause = false;
	private ArrayList<Cut> cuts = new ArrayList<>();
	private String previousTime;
	private String actualTime;
	
	@Autowired
	FfmpegService ffmpegService;

	/**
	 * Check the device connecting to our server
	 * @param session
	 * @param message
	 * @return true if conection is stablished correctly.
	 * @throws IOException
	 */
	private void checkDevice(WebSocketSession session, TextMessage message) throws IOException {
		if(message.getPayload().equals(ConsMsg.COMPUTER_DEVICE)) {
			this.sessionComputer = session;
			log.info(ConsInfoMsg.COMPUTER_OK + "| Local Ip " + session.getLocalAddress());
			session.sendMessage(new TextMessage(ConsInfoMsg.COMPUTER_OK));
		}
		else if(sessionComputer != null && message.getPayload().equals(ConsMsg.MOBILE_DEVICE)) {
			this.sessionMobile = session;
			log.info(ConsInfoMsg.MOBILE_OK + "| Local Ip " + session.getLocalAddress());
			session.sendMessage(new TextMessage(ConsInfoMsg.MOBILE_OK));
		}
	}
	
	private void setConfigurationFfmpeg(WebSocketRecordMessage messageObject) {
		ffmpegService.setDirectory(messageObject.getDirectory());
		ffmpegService.setContainerVideoFormat(messageObject.getFfmpegContainerFormat());
		ffmpegService.setAudioFormat(messageObject.getFfmpegAudioFormat());
		ffmpegService.setVideoFormat(messageObject.getFfmpegVideoFormat());
		ffmpegService.setFrameRate(messageObject.getFrameRate());
		ffmpegService.setVideoName(messageObject.getVideoName());
	}
	
	@Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info(ConsInfoMsg.CONNECTION_OK);
    }
	
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		checkDevice(session, message);
		if(!message.getPayload().equals(ConsMsg.COMPUTER_DEVICE) || !message.getPayload().equals(ConsMsg.MOBILE_DEVICE)) {
			Gson gson = new Gson();
	        WebSocketRecordMessage messageObject = gson.fromJson(message.getPayload(), WebSocketRecordMessage.class);
			String action = messageObject.getAction();
			TextMessage messageToSend;
			switch(action) {
				case ConsMsg.REC_VID_AUD:
					messageToSend = recordVideoAndAudio(messageObject);
					session.sendMessage(messageToSend);
					break;
				case ConsMsg.STOP: 
					messageToSend = stopRecording();
					session.sendMessage(messageToSend);
					break;
				case ConsMsg.PAUSE:
					messageToSend = pauseRecording();
					session.sendMessage(messageToSend);
					break;
				case ConsMsg.CONTINUE:
					messageToSend = continueRecording();
					session.sendMessage(messageToSend);
				}
	    	}
		}
	
	private TextMessage recordVideoAndAudio(WebSocketRecordMessage messageObject) throws Exception {
		if(ffmpegService.isFfmpegWorking()) {
			return new TextMessage(ConsInfoMsg.CANT_RECORD);
		}
		setConfigurationFfmpeg(messageObject);
		timeCounter.restart();
		actualTime = timeCounter.getTimeCounter();
		ffmpegService.startRecordingVideo();
		return new TextMessage(ConsMsg.RECORDING);
	}
	
	private TextMessage stopRecording() throws Exception {
		if(!ffmpegService.isFfmpegWorking()) {
			return new TextMessage(ConsInfoMsg.CANT_STOP);
		}
		if(!onPause) {
			previousTime = actualTime;
			actualTime = timeCounter.getTimeCounter();
			Cut newCut = new Cut(previousTime, actualTime);
			System.out.println(newCut.toString());
			cuts.add(newCut);
		}
		ffmpegService.stopRecording();
		onPause = false;
		//TODO write File with cuts and remove array of cuts
		return new TextMessage(ConsMsg.STOPPED);
	}
	
	private TextMessage pauseRecording() {
		if(!ffmpegService.isFfmpegWorking()) {
			return new TextMessage(ConsInfoMsg.IS_NOT_RECORDING);
		}
		previousTime = actualTime;
		actualTime = timeCounter.getTimeCounter();
		Cut newCut = new Cut(previousTime, actualTime);
		System.out.println(newCut);
		cuts.add(newCut);
		onPause = true;
		return new TextMessage(ConsInfoMsg.IS_PAUSED);
	}
	
	private TextMessage continueRecording() {
		if(!onPause || !ffmpegService.isFfmpegWorking()) {
			return new TextMessage(ConsInfoMsg.IS_NOT_PAUSED);
		}
		actualTime = timeCounter.getTimeCounter();
		onPause = false;
		return new TextMessage(ConsMsg.RECORDING);
	}
}
