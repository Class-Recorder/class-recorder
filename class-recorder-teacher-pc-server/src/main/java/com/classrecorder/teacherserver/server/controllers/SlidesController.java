package com.classrecorder.teacherserver.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.awt.event.KeyEvent;

@RestController
public class SlidesController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/api/leftSlides")
    public ResponseEntity<?> leftSlides() throws Exception {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @RequestMapping("/api/rightSlides")
    public ResponseEntity<?> rightSlides() throws Exception {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
