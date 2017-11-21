package com.classrecorder.teacherserver.server.websockets.processinfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.classrecorder.teacherserver.server.services.FfmpegService;
import com.classrecorder.teacherserver.server.websockets.record.WebSocketRecordHandler;

@Component
public class WebSocketProcessHandler extends TextWebSocketHandler {
	
	
	private static class ConsMsgSended {
		public static final String CONNECTION_OK = "Connection established";
	}
	
	private final Logger log = LoggerFactory.getLogger(WebSocketRecordHandler.class);
	
	@Autowired
	private FfmpegService ffmpegService;

	public WebSocketSession session;
	
	public void sendProcessOutput() throws IOException {
		try {
			InputStream ins = ffmpegService.getProcess().getErrorStream();
			BufferedReader buff = new BufferedReader(new InputStreamReader(ins));
	        String lineBuffer;
			lineBuffer = buff.readLine();
			while(lineBuffer !=null) {
	        	lineBuffer = buff.readLine();
	        	
	        	if(lineBuffer != null) {
	        		this.session.sendMessage(new TextMessage(lineBuffer));
	        	}
	        }
			this.session.sendMessage(new TextMessage("end"));
		} catch (IOException e) {
			this.session.sendMessage(new TextMessage("error"));
		}
	}
	
	@Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info(ConsMsgSended.CONNECTION_OK);
        this.session = session;
    }
	
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			
	}
}
