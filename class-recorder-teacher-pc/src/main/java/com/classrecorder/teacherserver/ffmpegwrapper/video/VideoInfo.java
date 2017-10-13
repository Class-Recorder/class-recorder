package com.classrecorder.teacherserver.ffmpegwrapper.video;

import java.util.ArrayList;

public class VideoInfo {

	private String videoName;
	private ArrayList<Cut> cuts;

	public VideoInfo(String videoName, ArrayList<Cut> cuts) {
		this.videoName = videoName;
		this.cuts = cuts;
	}
	
	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public ArrayList<Cut> getCuts() {
		return cuts;
	}

	public void setCuts(ArrayList<Cut> cuts) {
		this.cuts = cuts;
	}

	
	public boolean addCut(Cut cut) {
		return this.cuts.add(cut);
	}
	
	public boolean deleteCut(Cut cut) {
		return this.cuts.remove(cut);
	}

}
