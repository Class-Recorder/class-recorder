package com.classrecorder.teacherserver;

import javax.naming.OperationNotSupportedException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.classrecorder.teacherserver.services.FfmpegService;

@Configuration
public class AppConf {

	@Bean
	public FfmpegService ffmpegService() throws OperationNotSupportedException {
		return new FfmpegService();
	}
}
