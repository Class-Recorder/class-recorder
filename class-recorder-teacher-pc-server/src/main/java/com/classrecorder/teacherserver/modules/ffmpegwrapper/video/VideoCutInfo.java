package com.classrecorder.teacherserver.modules.ffmpegwrapper.video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoCutInfo {

	private List<Cut> cuts;

	public VideoCutInfo() {
		this.cuts = new ArrayList<>();
	}
	
	public VideoCutInfo(List<Cut> cuts2) {
		this.cuts = cuts2;
	}

	public List<Cut> getCuts() {
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
	
	public static VideoCutInfo createDefaultCutInfo() {
		Cut cut = new Cut();
		return new VideoCutInfo(Arrays.asList(cut));
	}

}
