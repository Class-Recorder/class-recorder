 package com.classrecorder.teacherserver.server.websockets.record;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.FfmpegException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.ICommandFileExistException;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.Cut;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;
import com.classrecorder.teacherserver.server.ClassRecProperties;
import com.classrecorder.teacherserver.server.services.FfmpegService;
import com.classrecorder.teacherserver.util.TimeCounter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



@Component
public class WebSocketRecordHandler extends TextWebSocketHandler {
	
	private static class ConsMsg {	
		//Received
		public static final String REC_VID_AUD = "recordVideoAndAudio";
		public static final String REC_VID = "recordVideo";
		public static final String STOP = "stop";
		public static final String PAUSE = "pause";
		public static final String CONTINUE = "continue";
		
		//Sended
		public static final String RECORDING = "Recording";
		public static final String STOPPED = "Stopped";
		public static final String PAUSED = "Paused";
		public static final String MOBILE_REC_STOPPED = "Mobile recording stopped";
		public static final String CONNECTION_OK = "Connection established";
		public static final String CANT_RECORD = "Can't Record, ffmpeg is working";
		public static final String CANT_STOP = "Can't Stop, ffmpeg is not working";
		public static final String IS_NOT_RECORDING = "Computer is not recording";
		public static final String IS_PAUSED = "Record is paused";
		public static final String IS_NOT_PAUSED = "Video is not paused";
		
		//Exception Messages
		public static final String IO_EXCEPTION = "Disk I/O error has occurred";
		public static final String FFMPEG_EXCEPTION = "Ffmpeg exception has occurred";
		public static final String ICOMMAND_EXCEPTION = "Exception calling ffmpeg";
		public static final String VIDEO_EXISTS_EXCEPTION = "Video actually exists";
		public static final String IO_EXCEPTION_CUTS = "Disk I/O error has ocurred while writing cuts";
	}
	
	private final Path videosFolder = ClassRecProperties.videosFolder;
	private final Path outputFolder = ClassRecProperties.tempFolder;
	
	private final Logger log = LoggerFactory.getLogger(WebSocketRecordHandler.class);
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
	private FfmpegService ffmpegService;
	
	private void setConfigurationFfmpeg(WebSocketRecordMessageClient messageObject) {
		ffmpegService.setDirectory(videosFolder.toString())
			.setContainerVideoFormat(messageObject.getFfmpegContainerFormat())
			.setFrameRate(messageObject.getFrameRate())
			.setVideoName(messageObject.getVideoName());
		videoName = messageObject.getVideoName();
		
	}
	
	private WebSocketRecordMessageServer recordVideoAndAudio(WebSocketRecordMessageClient messageObject) {
		if(ffmpegService.isFfmpegWorking()) {
			return new WebSocketRecordMessageServer(ConsMsg.CANT_RECORD, true);
		}
		setConfigurationFfmpeg(messageObject);
		timeCounter.restart();
		actualTime = timeCounter.getTimeCounter();
		try {
			ffmpegService.startRecordingVideoAndAudio();
			return new WebSocketRecordMessageServer(ConsMsg.RECORDING, false);
		} catch (IOException e) {
			return new WebSocketRecordMessageServer(ConsMsg.IO_EXCEPTION + " " + e.getMessage(), true);
		} catch (FfmpegException e) {
			return new WebSocketRecordMessageServer(ConsMsg.FFMPEG_EXCEPTION + " " + e.getMessage(), true);
		} catch (ICommandFileExistException e) {
			return new WebSocketRecordMessageServer(ConsMsg.VIDEO_EXISTS_EXCEPTION, true);
		} catch (ICommandException e) {
			return new WebSocketRecordMessageServer(ConsMsg.ICOMMAND_EXCEPTION, true);
		}
	}
	
	private WebSocketRecordMessageServer recordVideo(WebSocketRecordMessageClient messageObject) {
		if(ffmpegService.isFfmpegWorking()) {
			return new WebSocketRecordMessageServer(ConsMsg.CANT_RECORD, true);
		}
		setConfigurationFfmpeg(messageObject);
		timeCounter.restart();
		actualTime = timeCounter.getTimeCounter();
		mobileRecording = true;
		try {
			ffmpegService.startRecordingVideo();
			return new WebSocketRecordMessageServer(ConsMsg.RECORDING, false);	
		} catch (IOException e) {
			return new WebSocketRecordMessageServer(ConsMsg.IO_EXCEPTION + " " + e.getMessage(), true);
		} catch (FfmpegException e) {
			return new WebSocketRecordMessageServer(ConsMsg.FFMPEG_EXCEPTION + " " + e.getMessage(), true);
		} catch (ICommandFileExistException e) {
			return new WebSocketRecordMessageServer(ConsMsg.VIDEO_EXISTS_EXCEPTION, true);
		} catch (ICommandException e) {
			return new WebSocketRecordMessageServer(ConsMsg.ICOMMAND_EXCEPTION, true);
		}
	}
	
	private WebSocketRecordMessageServer stopRecording() {
		if(!ffmpegService.isFfmpegWorking()) {
			return new WebSocketRecordMessageServer(ConsMsg.CANT_STOP, true);
		}
		if(!onPause) {
			previousTime = actualTime;
			actualTime = timeCounter.getTimeCounter();
			Cut newCut = new Cut(previousTime, actualTime);
			System.out.println(newCut.toString());
			cuts.add(newCut);
        }
		try {
			ffmpegService.stopRecording();
		} catch (IOException e) {
			return new WebSocketRecordMessageServer(ConsMsg.IO_EXCEPTION + " " + e.getMessage(), true);
		} catch (FfmpegException e) {
			return new WebSocketRecordMessageServer(ConsMsg.FFMPEG_EXCEPTION + " " + e.getMessage(), true);
		} catch (ICommandException e) {
			return new WebSocketRecordMessageServer(ConsMsg.ICOMMAND_EXCEPTION, true);
		}
		onPause = false;
		//Save cut info file
		VideoCutInfo cutInfo = new VideoCutInfo(cuts);
		Writer writer;
		try {
			writer = new FileWriter(videosFolder.toString() + "/" + videoName + ".json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(cutInfo, writer);
			writer.close();
			//Save cut info file
			cuts = new ArrayList<>();
			if(mobileRecording) {
				mobileRecording = false;
				return new WebSocketRecordMessageServer(ConsMsg.MOBILE_REC_STOPPED, false);
			}
			return new WebSocketRecordMessageServer(ConsMsg.STOPPED, false);
		} catch (IOException e) {
			return new WebSocketRecordMessageServer(ConsMsg.IO_EXCEPTION_CUTS, true);
		}
	}
	
	private WebSocketRecordMessageServer pauseRecording() {
		if(!ffmpegService.isFfmpegWorking()) {
			return new WebSocketRecordMessageServer(ConsMsg.IS_NOT_RECORDING, true);
		}
		previousTime = actualTime;
		actualTime = timeCounter.getTimeCounter();
		Cut newCut = new Cut(previousTime, actualTime);
		log.info(newCut.toString());
		cuts.add(newCut);
		onPause = true;
		return new WebSocketRecordMessageServer(ConsMsg.PAUSED, false);
	}
	
	private WebSocketRecordMessageServer continueRecording() {
		if(!onPause || !ffmpegService.isFfmpegWorking()) {
			return new WebSocketRecordMessageServer(ConsMsg.IS_NOT_PAUSED, true);
		}
		actualTime = timeCounter.getTimeCounter();
		onPause = false;
		return new WebSocketRecordMessageServer(ConsMsg.RECORDING, false);
	}
	
	public boolean isStopped() {
		return !onPause && !ffmpegService.isFfmpegWorking();
	}
	
	public boolean isPaused() {
		return onPause && ffmpegService.isFfmpegWorking();
	}
	
	public boolean isRecording() {
		return !onPause && ffmpegService.isFfmpegWorking();
	}
	
	private void sendMessageToAllSenders(TextMessage message) throws IOException {
		for(WebSocketSession s: sessions) {
			if(s.isOpen()) {
				s.sendMessage(message);
			}
			
		}
	}
	
	@Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
		Gson gson = new Gson();
		log.info(ConsMsg.CONNECTION_OK);
		sessions.add(session);
		String messageToSend = gson.toJson(new WebSocketRecordMessageServer(ConsMsg.CONNECTION_OK, false), WebSocketRecordMessageServer.class);
        session.sendMessage(new TextMessage(messageToSend));
    }
	
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
		Gson gson = new Gson();
        WebSocketRecordMessageClient messageObjectClient = gson.fromJson(message.getPayload(), WebSocketRecordMessageClient.class);
		WebSocketRecordMessageServer messageObjectServer;
		TextMessage messageToSend;
		String action = messageObjectClient.getAction();
		switch(action) {
			case ConsMsg.REC_VID_AUD:
				messageObjectServer = recordVideoAndAudio(messageObjectClient);
				messageToSend = new TextMessage(gson.toJson(messageObjectServer, WebSocketRecordMessageServer.class));
				sendMessageToAllSenders(messageToSend);
				break;
			case ConsMsg.STOP: 
				messageObjectServer = stopRecording();
				messageToSend = new TextMessage(gson.toJson(messageObjectServer, WebSocketRecordMessageServer.class));
				sendMessageToAllSenders(messageToSend);
				break;
			case ConsMsg.PAUSE:
				messageObjectServer = pauseRecording();
				messageToSend = new TextMessage(gson.toJson(messageObjectServer, WebSocketRecordMessageServer.class));
				sendMessageToAllSenders(messageToSend);
				break;
			case ConsMsg.CONTINUE:
				messageObjectServer = continueRecording();
				messageToSend = new TextMessage(gson.toJson(messageObjectServer, WebSocketRecordMessageServer.class));
				sendMessageToAllSenders(messageToSend);
				break;
			case ConsMsg.REC_VID:
				messageObjectServer = recordVideo(messageObjectClient);
				messageToSend = new TextMessage(gson.toJson(messageObjectServer, WebSocketRecordMessageServer.class));
				sendMessageToAllSenders(messageToSend);
				break;
			}
    	}
}
