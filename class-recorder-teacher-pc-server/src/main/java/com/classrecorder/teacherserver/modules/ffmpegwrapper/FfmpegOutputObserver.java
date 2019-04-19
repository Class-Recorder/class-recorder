package com.classrecorder.teacherserver.modules.ffmpegwrapper;

public interface FfmpegOutputObserver {

	void update(String outputMessage) throws Exception;

}
