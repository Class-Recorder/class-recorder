package com.classrecorder.teacherserver.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classrecorder.teacherserver.server.websockets.record.WebSocketRecordHandler;

@RestController
public class RecordStateController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	WebSocketRecordHandler wsRecordHandler;
	
	@RequestMapping("/api/recording/currentState")
	public ResponseEntity<?> currentRecordStatus() {
		if(wsRecordHandler.isRecording()) {
			return new ResponseEntity<>("Recording", HttpStatus.OK);
		}
		if(wsRecordHandler.isPaused()) {
			return new ResponseEntity<>("Paused", HttpStatus.OK);
		}
		if(wsRecordHandler.isStopped()) {
			return new ResponseEntity<>("Stopped", HttpStatus.OK);
		}
		return new ResponseEntity<>("Illegal Recording status", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
