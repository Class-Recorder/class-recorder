package com.classrecorder.teacherserver.server;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.naming.OperationNotSupportedException;

import com.classrecorder.teacherserver.modules.youtube.com.classrecorder.teacherserver.modules.youtube.exceptions.YoutubeApiException;
import com.classrecorder.teacherserver.server.properties.YoutubeApiProperties;
import com.classrecorder.teacherserver.server.services.FfmpegService;
import com.classrecorder.teacherserver.server.services.YoutubeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;


@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(YoutubeApiProperties.class)
public class ClassrecorderApplication {

    @Autowired
    private YoutubeApiProperties properties;

    @Autowired
    private ClassRecProperties classRecProperties;

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ClassrecorderApplication.class);

        //With headless = false, we can get the width and height from the screen
        builder.headless(false);
        builder.run(args);

    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public YoutubeService youtubeService() throws YoutubeApiException {
        System.out.println(properties);
        return new YoutubeService(properties);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public FfmpegService ffmpegService() throws OperationNotSupportedException, IOException {
        return new FfmpegService(classRecProperties.getOutputFfmpeg(), System.getenv("DISPLAY"), classRecProperties.getFfmpegDirectory());
    }


    @Service
    static class Startup implements CommandLineRunner {

        @Autowired
        private YoutubeApiProperties properties;

        @Autowired
        private YoutubeService youtubeService;

        @Override
        public void run(String... strings) throws Exception {

        }
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("YoutubeUpload-");
        executor.initialize();
        return executor;
    }
}
