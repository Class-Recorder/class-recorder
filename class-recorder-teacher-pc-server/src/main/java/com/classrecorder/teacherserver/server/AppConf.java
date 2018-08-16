package com.classrecorder.teacherserver.server;

import java.nio.file.Paths;

import javax.naming.OperationNotSupportedException;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import com.classrecorder.teacherserver.server.services.FfmpegService;
import com.classrecorder.teacherserver.server.services.YoutubeService;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConf {

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public FfmpegService ffmpegService() throws OperationNotSupportedException {
		return new FfmpegService(ClassRecProperties.outputFfmpeg, System.getenv("DISPLAY"));
	}
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public YoutubeService youtubeService() {
		return new YoutubeService();
	}
	
	@Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }

    @Bean
    public WebMvcConfigurerAdapter forwardToIndex() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                // redirect to angular app
                registry.addViewController("/").setViewName(
                        "forward:/index.html");
            }
        };
    }
}
