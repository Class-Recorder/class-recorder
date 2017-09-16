package com.classrecorder.teacherserver;

import java.io.File;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import com.classrecorder.teacherserver.commands.Cut;
import com.classrecorder.teacherserver.commands.VideoInfo;
import com.classrecorder.teacherserver.services.FfmpegAudioFormat;
import com.classrecorder.teacherserver.services.FfmpegService;
import com.classrecorder.teacherserver.services.FfmpegVideoFormat;

@Controller
public class Initializer implements CommandLineRunner{
	
	@Autowired
	private FfmpegService ffmpeg;
	
	@Override
	public void run(String... args) throws Exception {
		
		final String tempVideos = "temp";
		final String fileVideos = tempVideos + "/files.txt";
		
		ffmpeg.setAudioFormat(FfmpegAudioFormat.libvorbis)
			.setFrameRate(25)
			.setVideoFormat(FfmpegVideoFormat.mkv)
			.setDirectory("videos")
			.setVideoName("output");
		
		ArrayList<Cut> cuts = new ArrayList<>();
		Cut cut1 = new Cut("00:00:00", "00:00:10");
		cuts.add(cut1);
		Cut cut2 = new Cut("00:00:30", "00:00:35");
		cuts.add(cut2);
		//Cut cut3 = new Cut("00:00:40", "00:00:45");
		//cuts.add(cut3);
		VideoInfo videoInfo = new VideoInfo("VideoTest", cuts);
		
		Process cutVideos = ffmpeg.cutVideo(videoInfo, "test", tempVideos);
		cutVideos.waitFor();
		
		Process mergeVideos = ffmpeg.mergeVideos(fileVideos, tempVideos);
		mergeVideos.waitFor();
		
		File tempFolder = new File(tempVideos);
		while(mergeVideos.isAlive()) {}
		for (File file: tempFolder.listFiles()) if (!file.isDirectory()) file.delete();
	}
}
