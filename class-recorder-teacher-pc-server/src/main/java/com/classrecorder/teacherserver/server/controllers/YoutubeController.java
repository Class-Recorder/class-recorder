package com.classrecorder.teacherserver.server.controllers;

import com.classrecorder.teacherserver.server.services.YoutubeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YoutubeController {

    private static final String REQUEST_FILE_API_URL = "/api/uploadVideo/";

    @Autowired
    private YoutubeService youtube;

    //@RequestMapping(REQUEST_FILE_API_URL + "{fileName:.+}")

}