package com.classrecorder.teacherserver.server.services;

import org.springframework.stereotype.Service;

import com.classrecorder.teacherserver.modules.youtube.YoutubeUploader;
import com.classrecorder.teacherserver.modules.youtube.YoutubeVideoInfo;

@Service
public class YoutubeService {
	
	private YoutubeUploader youtubeUploader;
	
	public YoutubeService() {
		this.youtubeUploader = new YoutubeUploader();
	}
	
	public void uploadVideo(YoutubeVideoInfo ytVideoInfo) throws Exception {
		youtubeUploader.uploadVideo(ytVideoInfo);	
	}

}
