package com.classrecorder.teacherserver.util;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.classrecorder.teacherserver.server.entities.local.LocalVideo;

public class LocalVideosReader {
	

	public static List<LocalVideo> readListLocalVideos(String requestFileUrl, File directory) {
		List<LocalVideo> localVideos = new ArrayList<>();
		File[] listFiles = directory.listFiles();
		Arrays.sort(listFiles);
		String currentFile = null;
		LocalVideo currentLocalVideo = null;
		for(int i = 0; i < listFiles.length; i++) {
            if(listFiles[i].isDirectory()) {
                continue;
            }
            if(currentLocalVideo == null) {
                currentFile = listFiles[i].getName();
                currentLocalVideo = new LocalVideo();
            }
            else {
                String newFile = listFiles[i].getName();
                String currentFileWoutExt = currentFile.substring(0, currentFile.lastIndexOf('.'));
                String newFileWoutExt = newFile.substring(0, newFile.lastIndexOf('.'));
                if(!currentFileWoutExt.equals(newFileWoutExt)) {
                    if(isValidLocalVideo(currentLocalVideo)) {
                        localVideos.add(currentLocalVideo);
                    }
                    currentLocalVideo = new LocalVideo();
                }
                currentFile = listFiles[i].getName();
            }
			assignContentTypeToDifferentFiles(requestFileUrl, currentFile, currentLocalVideo);
			//Add last element
			if(i == listFiles.length-1 && isValidLocalVideo(currentLocalVideo)) {
				localVideos.add(currentLocalVideo);
			}
		}
		return localVideos;
	}
	

	private static void assignContentTypeToDifferentFiles(String requestFileUrl, String currentFile, LocalVideo currentLocalVideo) {
		//Add properties to the class
		if(currentFile.endsWith(".mkv") || currentFile.endsWith(".webm")) {
			currentLocalVideo.setUrlApiLocalVideo(requestFileUrl + currentFile);
			currentLocalVideo.setVideoName(currentFile);
		}
		else if (currentFile.endsWith(".json")) {
			currentLocalVideo.setUrlApiLocalCuts(requestFileUrl + currentFile);
		}
		else if (currentFile.endsWith("jpg") || (currentFile.endsWith("jpg"))) {
			currentLocalVideo.setUrlApiLocalThumb(requestFileUrl + currentFile);
		}
	}

	public static void assignContentTypeToDifferentFiles(HttpServletResponse res, Path file) {
		if(file.toString().endsWith(".mkv")) {
			res.setContentType("video/webm");
		}
		if(file.toString().endsWith(".mp4")) {
			res.setContentType("video/mp4");
		}
		if(file.toString().endsWith(".json")) {
			res.setContentType("application/json");
		}
		if(file.toString().endsWith(".jpg")) {
			res.setContentType("image/jpeg");
		}
	}
	
	public static boolean isVideo(Path file) {
		if(file.toString().endsWith(".mkv") || (file.toString().endsWith(".mp4"))) {
			return true;
		}
		return false;
	}

	private static boolean isValidLocalVideo(LocalVideo localVideo) {
		return localVideo.getVideoName() != null && localVideo.getUrlApiLocalCuts() != null &&
				localVideo.getUrlApiLocalThumb() != null && localVideo.getUrlApiLocalVideo() != null;
	}

}
