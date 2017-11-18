package com.classrecorder.teacherserver.modules.ffmpegwrapper.video;

import java.util.ArrayList;

public class VideoCutInfo {

	private ArrayList<Cut> cuts;

	public VideoCutInfo() {
		this.cuts = new ArrayList<>();
	}
	
	public VideoCutInfo(ArrayList<Cut> cuts) {
		this.cuts = cuts;
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
