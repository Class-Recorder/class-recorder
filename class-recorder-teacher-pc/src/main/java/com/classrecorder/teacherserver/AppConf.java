package com.classrecorder.teacherserver;

import javax.naming.OperationNotSupportedException;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.classrecorder.teacherserver.services.FfmpegService;
import com.classrecorder.teacherserver.services.YoutubeService;

@Configuration
public class AppConf {

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public FfmpegService ffmpegService() throws OperationNotSupportedException {
		return new FfmpegService();
	}
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public YoutubeService youtubeService() {
		return new YoutubeService();
	}
}
