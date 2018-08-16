package com.classrecorder.teacherserver.modules.ffmpegwrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FfmpegWrapperLogger implements FfmpegOutputObserver {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public FfmpegWrapperLogger() {
    }

    @Override
    public void update(String outputMessage) throws Exception {
        log.info(outputMessage);
    }
}
