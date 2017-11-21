package com.classrecorder.teacherserver.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.classrecorder.teacherserver.server.websockets.processinfo.WebSocketProcessHandler;
import com.classrecorder.teacherserver.server.websockets.record.WebSocketRecordHandler;

@Configuration
@EnableWebSocket
@EnableScheduling
public class WebSocketConfig implements WebSocketConfigurer {
    
	@Autowired
    WebSocketRecordHandler recordPCHandler;
	
	@Autowired
	WebSocketProcessHandler processHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(recordPCHandler, "/recordpc").setAllowedOrigins("http://localhost:4200");
        registry.addHandler(processHandler, "/process/info").setAllowedOrigins("http://localhost:4200");
    }
    
    

}