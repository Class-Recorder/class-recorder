package com.classrecorder.teacherserver.modules.youtube.com.classrecorder.teacherserver.modules.youtube.exceptions;

public class YoutubeApiException extends Exception {

    private static final long serialVersionUID = 1L;

    public YoutubeApiException(){
    }

    public YoutubeApiException(String message){
        super(message);
    }

}

