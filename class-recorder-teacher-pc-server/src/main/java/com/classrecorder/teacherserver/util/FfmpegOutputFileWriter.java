package com.classrecorder.teacherserver.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.FfmpegOutputObserver;

public class FfmpegOutputFileWriter implements FfmpegOutputObserver{
	
	FileWriter fileWriter;
	File file;
	
	public FfmpegOutputFileWriter(File file) throws IOException {
		this.file = file;
		fileWriter = new FileWriter(file);
	}
	
	public void setFileWriter(File file) throws IOException {
		this.file = file;
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
		try {
			fileWriter.append(outputMessage + "\n");
		} catch(IOException e)  {
			this.fileWriter = new FileWriter(file);
			fileWriter.append(outputMessage + "\n");
		}
		
	}

}
