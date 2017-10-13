package com.classrecorder.teacherserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import com.classrecorder.teacherserver.ffmpegwrapper.formats.FfmpegAudioFormat;
import com.classrecorder.teacherserver.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.ffmpegwrapper.formats.FfmpegVideoFormat;
import com.classrecorder.teacherserver.services.FfmpegService;
import com.classrecorder.teacherserver.services.YoutubeService;
import com.classrecorder.teacherserver.youtube.YoutubeVideoInfo;

@Controller
public class Initializer implements CommandLineRunner {
	
	@Autowired
	private FfmpegService ffmpeg;
	
	@Autowired
	private YoutubeService youtubeService;
	
	boolean recording;
	
	
	@Override
	public void run(String... args) throws Exception {
		
		//final String tempVideos = "temp";
		//final String fileVideos = tempVideos + "/files.txt";
		recording = false;
		
		ffmpeg.setAudioFormat(FfmpegAudioFormat.libvorbis)
			.setVideoFormat(FfmpegVideoFormat.libx264)
			.setContainerVideoFormat(FfmpegContainerFormat.mkv)
			.setFrameRate(60)
			.setDirectory("videos")
			.setVideoName("2_actividados_contenido");
		
		
		YoutubeVideoInfo videoInfo = new YoutubeVideoInfo();
		videoInfo.setVideoTitle("Actividados - Añadir, modificar o eliminar contenido | Resumen de la página");
		videoInfo.setDescription("Añadir, modificar o eliminar contenido");
		videoInfo.setPrivacyStatus("unlisted");
		videoInfo.setVideoPath("videos/2_actividados_contenido.mkv");
		
		ffmpeg.startRecordingVideoAndAudio();
	
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line = "";

	   while (!line.equalsIgnoreCase("stop")) {
	       line = in.readLine();
	   }
	   System.out.println("Stopped");
	   in.close();
	   ffmpeg.stopRecording().waitFor();
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
