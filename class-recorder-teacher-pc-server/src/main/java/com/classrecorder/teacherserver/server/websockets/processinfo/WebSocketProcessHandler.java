package com.classrecorder.teacherserver.server.websockets.processinfo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.FfmpegOutputObserver;
import com.classrecorder.teacherserver.server.websockets.record.WebSocketRecordHandler;

@Component
public class WebSocketProcessHandler extends TextWebSocketHandler implements FfmpegOutputObserver {
	
	
	private static class ConsMsgSended {
		public static final String CONNECTION_OK = "Connection established";
	}
	
	private final Logger log = LoggerFactory.getLogger(WebSocketRecordHandler.class);
	
	public WebSocketSession session;
	
	@Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info(ConsMsgSended.CONNECTION_OK);
        this.session = session;
    }
	
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			
	}

	@Override
	public void update(String outputMessage) throws IOException {
		try {
			log.info(outputMessage);
			this.session.sendMessage(new TextMessage(outputMessage));
		} catch(IOException exc) {
			this.session.sendMessage(new TextMessage("error"));
		}
		
	}
}
