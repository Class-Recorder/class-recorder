package com.classrecorder.teacherserver.services;

import com.classrecorder.teacherserver.youtube.YoutubeUploader;
import com.classrecorder.teacherserver.youtube.YoutubeVideoInfo;

public class YoutubeService {
	
	private YoutubeUploader youtubeUploader;
	
	public YoutubeService() {
		this.youtubeUploader = new YoutubeUploader();
	}
	
	public void uploadVideo(YoutubeVideoInfo ytVideoInfo) throws Exception {
		youtubeUploader.uploadVideo(ytVideoInfo);	
	}

}
