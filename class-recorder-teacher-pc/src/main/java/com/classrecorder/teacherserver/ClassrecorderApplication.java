package com.classrecorder.teacherserver;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
@EnableAutoConfiguration
public class ClassrecorderApplication {
	
	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ClassrecorderApplication.class);
		//With headless = false, we can get the width and height from the screen
		builder.headless(false);
		builder.run(args);
	}
}
