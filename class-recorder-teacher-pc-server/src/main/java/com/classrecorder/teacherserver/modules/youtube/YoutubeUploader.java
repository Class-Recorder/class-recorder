package com.classrecorder.teacherserver.modules.youtube;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.classrecorder.teacherserver.server.properties.YoutubeApiProperties;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class YoutubeUploader {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	//Objects needed to use de Youtube API
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static YouTube youtube;
	private static String VIDEO_FILE_FORMAT = "video/*";
	private List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");

	private YoutubeApiProperties properties;
	private List<YoutubeOutputObserver> observers = new ArrayList<>();
	
	public YoutubeUploader(YoutubeApiProperties youtubeProperties) {
		this.properties = youtubeProperties;
	}
	
	private Credential authorize() throws Exception {

		InputStream convertedJsonYtSecrets = ytPropertiesToJsonValidFormat(properties);
		// Load client secrets.
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, convertedJsonYtSecrets);

	    // Checks that the defaults have been replaced (Default = "Enter X here").
	    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
	        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
	        
	    	log.error("Set Client ID and Secret from https://code.google.com/apis/console/?api=youtube"
			  + "to environment variables or in your application.properties");
			log.error("Example:");
			log.error("export YOUTUBE_CLIENT_ID=<your_client_id && export YOUTUBE_SECRET=<your_client_secret>");
	    	
	      throw new Exception("Youtube Api keys are not set correctly");
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

		log.info("Youtube api authorized");
	    // Authorize.
	    return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
	}

	private InputStream ytPropertiesToJsonValidFormat(YoutubeApiProperties properties) {
		Gson gson = new Gson();
		JsonObject jsonClientSecrets = new JsonObject();
		jsonClientSecrets.add("installed", new JsonObject());
		jsonClientSecrets.get("installed").getAsJsonObject().addProperty("client_id", properties.getClientId());
		jsonClientSecrets.get("installed").getAsJsonObject().addProperty("client_secret", properties.getClientSecret());
		return new ByteArrayInputStream(gson.toJson(jsonClientSecrets).getBytes());
	}

	public void addObserver(YoutubeOutputObserver observer) {
		this.addObserver(observer);
	}

	public void notifyObservers(String outputMessage) {
		for(YoutubeOutputObserver observer: observers) {
			observer.update(outputMessage);
		}
	}
	
	public void uploadVideo(YoutubeVideoInfo ytVideoInfo) throws Exception {
		Credential credential = authorize();
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
	              notifyObservers("Initiation Started");
	              break;
	            case INITIATION_COMPLETE:
	              log.info("Initiation Completed");
	              notifyObservers("Initiazion Completed");
	              break;
	            case MEDIA_IN_PROGRESS:
	              log.info("Upload in progress");
	              log.info("Upload percentage: " + uploader.getProgress());
	              notifyObservers("Upload percentage: " + uploader.getProgress());
	              break;
	            case MEDIA_COMPLETE:
	              log.info("Upload Completed!");
	              notifyObservers("Upload Completed!");
	              break;
	            case NOT_STARTED:
	              log.info("Upload Not Started!");
	              notifyObservers("Upload Not Started!");
	              break;
	          }
	        }
	      };
	      uploader.setProgressListener(progressListener);

	      videoInsert.execute();
	}
}
