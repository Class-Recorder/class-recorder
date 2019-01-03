package com.classrecorder.teacherserver.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("youtube")
public class YoutubeApiProperties {

    private String clientId;
    private String clientSecret;
    private int callbackRedirectPort;
    

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
    }

	public int getCallbackRedirectPort() {
		return callbackRedirectPort;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setCallbackRedirectPort(int callbackRedirectPort) {
		this.callbackRedirectPort = callbackRedirectPort;
	}
}