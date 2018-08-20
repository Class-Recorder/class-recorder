package com.classrecorder.teacherserver.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("youtube")
public class YoutubeApiProperties {

    private String clientId;
    private String clientSecret;
    

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
    }
    

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}