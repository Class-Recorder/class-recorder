package com.classrecorder.teacherserver.util;

import java.util.ArrayList;

public class TimeCounterPause {

    private long startTime;
    private ArrayList<Integer> secondsRecordList = new ArrayList<>();
    private boolean paused;

    public TimeCounterPause() {
        startTime = System.currentTimeMillis();
    }

    public String getTimerCounter() {
        int totalSeconds = 0;
        for(Integer seconds: secondsRecordList) {
            totalSeconds += seconds;
        }
        if(paused) {
            return TimeCounter.formatSeconds(totalSeconds);
        }
        long nowMillis = System.currentTimeMillis();
        int actualSeconds = (int)((nowMillis - this.startTime) / 1000);
        return TimeCounter.formatSeconds(totalSeconds + actualSeconds);
    }

    public void pauseTimer() {
        long nowMillis = System.currentTimeMillis();
        int actualSeconds = (int)((nowMillis - this.startTime) / 1000);
        secondsRecordList.add(actualSeconds);
        paused = true;
    }

    public void continueTimer() {
        this.paused = false;
        startTime = System.currentTimeMillis();
    }

    public void restart() {
        startTime = System.currentTimeMillis();
        secondsRecordList.clear();
    }
}
