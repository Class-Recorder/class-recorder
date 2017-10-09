package com.classrecorder.teacherserver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import com.classrecorder.teacherserver.ffmpegwrapper.FfmpegAudioFormat;
import com.classrecorder.teacherserver.ffmpegwrapper.FfmpegVideoFormat;
import com.classrecorder.teacherserver.services.FfmpegService;
import com.classrecorder.teacherserver.services.YoutubeService;
import com.classrecorder.teacherserver.youtube.YoutubeVideoInfo;
import com.google.common.collect.Lists;

@Controller
public class Initializer implements CommandLineRunner{
	
	@Autowired
	private FfmpegService ffmpeg;
	
	@Autowired
	private YoutubeService youtubeService;
	
	@Override
	public void run(String... args) throws Exception {
		
		final String tempVideos = "temp";
		final String fileVideos = tempVideos + "/files.txt";
		
		ffmpeg.setAudioFormat(FfmpegAudioFormat.libvorbis)
			.setFrameRate(25)
			.setVideoFormat(FfmpegVideoFormat.mkv)
			.setDirectory("videos")
			.setVideoName("test_youtube2");
		
		ffmpeg.startRecordingVideoAndAudio();
		Thread.sleep(60000);
		ffmpeg.stopRecording();
		
		YoutubeVideoInfo videoInfo = new YoutubeVideoInfo();
		videoInfo.setVideoTitle("Video de prueba");
		videoInfo.setDescription("Este es un video de prueba de la aplicación Class Recorder");
		videoInfo.setPrivacyStatus("unlisted");
		videoInfo.setTags(Lists.newArrayList("Class recorder", "class", "recorder", "test"));
		videoInfo.setVideoPath("videos/test_youtube.mkv");
		youtubeService.uploadVideo(videoInfo);
		
		
		
//		ArrayList<Cut> cuts = new ArrayList<>();
//		Cut cut1 = new Cut("00:00:00", "00:00:10");
//		cuts.add(cut1);
//		Cut cut2 = new Cut("00:00:30", "00:00:35");
//		cuts.add(cut2);
//		Cut cut3 = new Cut("00:00:40", "00:00:45");
//		cuts.add(cut3);
//		VideoInfo videoInfo = new VideoInfo("VideoTest", cuts);
//		
//		Process cutVideos = ffmpeg.cutVideo(videoInfo, "test", tempVideos);
//		cutVideos.waitFor();
//		
//		Process mergeVideos = ffmpeg.mergeVideos(fileVideos, tempVideos);
//		mergeVideos.waitFor();
//		
//		File tempFolder = new File(tempVideos);
//		while(mergeVideos.isAlive()) {}
//		for (File file: tempFolder.listFiles()) if (!file.isDirectory()) file.delete();
	}
}
