package com.classrecorder.teacherserver.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.FfmpegOutputObserver;

public class FfmpegOutputFileWriter implements FfmpegOutputObserver{
	
	FileWriter fileWriter;
	
	public FfmpegOutputFileWriter(File file) throws IOException {
		fileWriter = new FileWriter(file);
	}
	
	public void setFileWriter(File file) throws IOException {
		this.fileWriter = new FileWriter(file);
	}
	
	public void close() throws IOException {
		this.fileWriter.close();
	}
	
	@Override
	public void update(String outputMessage) throws Exception {
		if(outputMessage.equals("end")) {
			this.fileWriter.close();
		}
		fileWriter.write(outputMessage + "\n");
	}

}
