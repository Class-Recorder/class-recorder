package com.classrecorder.teacherserver.modules.youtube;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.common.collect.Lists;

public class YoutubeUploader {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	//Objects needed to use de Youtube API
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static YouTube youtube;
	private static String VIDEO_FILE_FORMAT = "video/*";
	private List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");
	private static final String CONFIG_FILE_PATH = "configuration/youtube_client_secrets.json"; 
	
	public YoutubeUploader() {
	}
	
	private Credential authorize(List<String> scopes) throws Exception {

	    // Load client secrets.
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
	        JSON_FACTORY, new FileInputStream(new File(CONFIG_FILE_PATH)));

	    // Checks that the defaults have been replaced (Default = "Enter X here").
	    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
	        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
	        
	    	log.error("Enter Client ID and Secret from https://code.google.com/apis/console/?api=youtube"
	          + "into youtube-cmdline-uploadvideo-sample/src/main/resources/client_secrets.json");
	    	
	      throw new Exception(CONFIG_FILE_PATH + "is not valid");
	    }

	    // Set up file credential store.
	    FileCredentialStore credentialStore = new FileCredentialStore(
	        new File(System.getProperty("user.home"), ".credentials/youtube-api-uploadvideo.json"),
	        JSON_FACTORY);

	    // Set up authorization code flow.
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialStore(credentialStore)
	        .build();

	    // Build the local server and bind it to port 9000
	    LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(9000).build();

	    // Authorize.
	    return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
	}
	
	public void uploadVideo(YoutubeVideoInfo ytVideoInfo) throws Exception {
		Credential credential = authorize(scopes);
		youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();
		File videoFile = new File(ytVideoInfo.getVideoPath());
		Video videoMetadata = new Video();
		
		VideoStatus status = new VideoStatus();
		status.setPrivacyStatus(ytVideoInfo.getPrivacyStatus());
		videoMetadata.setStatus(status);
		
		VideoSnippet snippet = new VideoSnippet();
		snippet.setTitle(ytVideoInfo.getVideoTitle());
		snippet.setDescription(ytVideoInfo.getDescription());
		snippet.setTags(ytVideoInfo.getTags());
		
		videoMetadata.setSnippet(snippet);
		
		InputStreamContent mediaContent = new InputStreamContent(
				VIDEO_FILE_FORMAT, new BufferedInputStream(new FileInputStream(videoFile)));
		mediaContent.setLength(videoFile.length());
		
		YouTube.Videos.Insert videoInsert = youtube.videos()
		          .insert("snippet,statistics,status", videoMetadata, mediaContent);
		
		MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();
		uploader.setDirectUploadEnabled(false);
		
		MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
	        public void progressChanged(MediaHttpUploader uploader) throws IOException {
	          switch (uploader.getUploadState()) {
	            case INITIATION_STARTED:
	              log.info("Initiation Started");
	              // TODO Call websocket
	              break;
	            case INITIATION_COMPLETE:
	              log.info("Initiation Completed");
	              // TODO Call websocket
	              break;
	            case MEDIA_IN_PROGRESS:
	              log.info("Upload in progress");
	              log.info("Upload percentage: " + uploader.getProgress());
	              // TODO Call websocket
	              break;
	            case MEDIA_COMPLETE:
	              log.info("Upload Completed!");
	              // TODO Call websocket
	              break;
	            case NOT_STARTED:
	              log.info("Upload Not Started!");
	              // TODO Call websocket
	              break;
	          }
	        }
	      };
	      uploader.setProgressListener(progressListener);

	      videoInsert.execute();
	}
	
	@Override
	public YoutubeUploader clone() throws CloneNotSupportedException{
	    throw new CloneNotSupportedException("Youtube uploader can't be cloned");
	}
}
