package com.classrecorder.teacherserver.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classrecorder.teacherserver.modules.youtube.YoutubeUploader;
import com.classrecorder.teacherserver.modules.youtube.YoutubeVideoInfo;
import com.classrecorder.teacherserver.server.properties.YoutubeApiProperties;

@Service
public class YoutubeService {
	
	private YoutubeUploader youtubeUploader;

	
	public YoutubeService(YoutubeApiProperties properties) {
		this.youtubeUploader = new YoutubeUploader(properties);
	}
	
	public void uploadVideo(YoutubeVideoInfo ytVideoInfo) throws Exception {
		youtubeUploader.uploadVideo(ytVideoInfo);	
	}

}
