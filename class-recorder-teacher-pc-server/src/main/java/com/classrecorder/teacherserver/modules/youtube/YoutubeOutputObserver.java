package com.classrecorder.teacherserver.modules.youtube;

import java.io.IOException;

public interface YoutubeOutputObserver {
    public void update(String outputMessage) throws IOException;
}