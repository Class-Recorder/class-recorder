package com.classrecorder.teacherserver.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classrecorder.teacherserver.server.services.FfmpegService;

@RestController
public class TestController {
	
	@Autowired
	FfmpegService ffmpegService;
	
	@RequestMapping("/api/isRecording")
	public ResponseEntity<Boolean> isRecording() throws Exception{
		ffmpegService.printInfo();
		return new ResponseEntity<Boolean>(ffmpegService.isFfmpegWorking(), HttpStatus.OK);
	}

}
