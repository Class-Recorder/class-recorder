package com.classrecorder.teacherserver.server.websockets.youtube;

import java.io.IOException;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.FfmpegOutputObserver;
import com.classrecorder.teacherserver.modules.youtube.YoutubeOutputObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketYoutubeUpload extends TextWebSocketHandler implements YoutubeOutputObserver {

    private static class ConsMsgSended {
		public static final String CONNECTION_OK = "Connection Youtube process established";
	}

    private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public WebSocketSession session;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        log.info(ConsMsgSended.CONNECTION_OK);
        this.session = session;
        this.session.sendMessage(new TextMessage(ConsMsgSended.CONNECTION_OK));
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