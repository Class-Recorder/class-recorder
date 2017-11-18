 package com.classrecorder.teacherserver.server.websockets;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.Cut;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;
import com.classrecorder.teacherserver.server.services.FfmpegService;
import com.classrecorder.teacherserver.util.TimeCounter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Component
public class RecordWebSocketHandler extends TextWebSocketHandler {
	
	private final String PATH_VIDEOS_AND_CUTS = "videos";
	
	private final Logger log = LoggerFactory.getLogger(RecordWebSocketHandler.class);
	List<WebSocketSession> sessions = new ArrayList<>();
	
	//Recording Variables
	private TimeCounter timeCounter = new TimeCounter();
	private boolean onPause = false;
	private boolean mobileRecording;
	private ArrayList<Cut> cuts = new ArrayList<>();
	private String previousTime;
	private String actualTime;
	private String videoName;
	
	@Autowired
	FfmpegService ffmpegService;
	
	private void setConfigurationFfmpeg(WebSocketRecordMessage messageObject) {
		ffmpegService.setDirectory(PATH_VIDEOS_AND_CUTS);
		ffmpegService.setContainerVideoFormat(messageObject.getFfmpegContainerFormat());
		ffmpegService.setAudioFormat(messageObject.getFfmpegAudioFormat());
		ffmpegService.setVideoFormat(messageObject.getFfmpegVideoFormat());
		ffmpegService.setFrameRate(messageObject.getFrameRate());
		ffmpegService.setVideoName(messageObject.getVideoName());
		videoName = messageObject.getVideoName();
	}
	
	private TextMessage recordVideoAndAudio(WebSocketRecordMessage messageObject) throws Exception {
		if(ffmpegService.isFfmpegWorking()) {
			return new TextMessage(ConsInfoMsg.CANT_RECORD);
		}
		setConfigurationFfmpeg(messageObject);
		timeCounter.restart();
		actualTime = timeCounter.getTimeCounter();
		ffmpegService.startRecordingVideoAndAudio();
		return new TextMessage(ConsMsg.RECORDING);
	}
	
	private TextMessage recordVideo(WebSocketRecordMessage messageObject) throws Exception {
		if(ffmpegService.isFfmpegWorking()) {
			return new TextMessage(ConsInfoMsg.CANT_RECORD);
		}
		setConfigurationFfmpeg(messageObject);
		timeCounter.restart();
		actualTime = timeCounter.getTimeCounter();
		ffmpegService.startRecordingVideo();
		mobileRecording = true;
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
		//Save cut info file
		VideoCutInfo cutInfo = new VideoCutInfo(cuts);
		Writer writer = new FileWriter(PATH_VIDEOS_AND_CUTS + "/" + videoName + ".json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(cutInfo, writer);
		writer.close();
		//Save cut info file
		
		if(mobileRecording) {
			mobileRecording = false;
			return new TextMessage(ConsMsg.MOBILE_REC_STOPPED);
		}
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
	
	private void sendMessageToAllSenders(TextMessage message) throws IOException {
		for(WebSocketSession s: sessions) {
			s.sendMessage(message);
		}
	}
	
	@Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info(ConsInfoMsg.CONNECTION_OK);
        sessions.add(session);
    }
	
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		Gson gson = new Gson();
        WebSocketRecordMessage messageObject = gson.fromJson(message.getPayload(), WebSocketRecordMessage.class);
		String action = messageObject.getAction();
		TextMessage messageToSend;
		switch(action) {
			case ConsMsg.REC_VID_AUD:
				messageToSend = recordVideoAndAudio(messageObject);
				sendMessageToAllSenders(messageToSend);
				break;
			case ConsMsg.STOP: 
				messageToSend = stopRecording();
				sendMessageToAllSenders(messageToSend);
				break;
			case ConsMsg.PAUSE:
				messageToSend = pauseRecording();
				sendMessageToAllSenders(messageToSend);
				break;
			case ConsMsg.CONTINUE:
				messageToSend = continueRecording();
				sendMessageToAllSenders(messageToSend);
				break;
			case ConsMsg.REC_VID:
				messageToSend = recordVideo(messageObject);
				sendMessageToAllSenders(messageToSend);
			}
    	}
}
