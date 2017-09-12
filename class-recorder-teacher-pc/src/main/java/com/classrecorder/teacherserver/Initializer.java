package com.classrecorder.teacherserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import com.classrecorder.teacherserver.services.FfmpegAudioFormat;
import com.classrecorder.teacherserver.services.FfmpegService;
import com.classrecorder.teacherserver.services.FfmpegVideoFormat;

@Controller
public class Initializer implements CommandLineRunner{
	
	@Autowired
	private FfmpegService ffmpeg;
	
	@Override
	public void run(String... args) throws Exception {
		ffmpeg.setAudioFormat(FfmpegAudioFormat.libvorbis)
			.setFrameRate(25)
			.setVideoFormat(FfmpegVideoFormat.mkv)
			.setVideoName("test_java_mkv");
		
		ffmpeg.startRecordingVideo();
		Thread.sleep(20000);
		ffmpeg.stopRecording();
	}

}
