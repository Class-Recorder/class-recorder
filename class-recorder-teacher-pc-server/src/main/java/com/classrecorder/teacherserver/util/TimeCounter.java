package com.classrecorder.teacherserver.util;

public class TimeCounter {
	
	private long startTime;
	
	public TimeCounter() {
		startTime = System.currentTimeMillis();
	}
	
	/*
	 * Get Time passed from creation on format HH:mm:ss
	 */
	public String getTimeCounter() {
		long nowMillis = System.currentTimeMillis();
		int seconds = (int)((nowMillis - this.startTime) / 1000);
		return formatSeconds(seconds);
	}
	
	public void restart() {
		startTime = System.currentTimeMillis();
	}
	
	private static String formatSeconds(int timeInSeconds)
	{
	    int hours = timeInSeconds / 3600;
	    int secondsLeft = timeInSeconds - hours * 3600;
	    int minutes = secondsLeft / 60;
	    int seconds = secondsLeft - minutes * 60;

	    String formattedTime = "";
	    if (hours < 10)
	        formattedTime += "0";
	    formattedTime += hours + ":";

	    if (minutes < 10)
	        formattedTime += "0";
	    formattedTime += minutes + ":";

	    if (seconds < 10)
	        formattedTime += "0";
	    formattedTime += seconds ;

	    return formattedTime;
	}

}
