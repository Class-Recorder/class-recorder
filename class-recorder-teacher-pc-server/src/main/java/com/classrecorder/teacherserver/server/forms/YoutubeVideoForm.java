package com.classrecorder.teacherserver.server.forms;

public class YoutubeVideoForm {

	private String videoTitle;
	private String description;
	private String tags;
	private boolean privateStatus;

    public YoutubeVideoForm() {
    }

    public String getVideoTitle() {
        return this.videoTitle;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTags() {
        return this.tags;
    }

    public boolean getPrivateStatus() {
        return this.privateStatus;
    }

    @Override
    public String toString() {
        return "{" +
            " videoTitle='" + getVideoTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", tags='" + getTags() + "'" +
            ", privateStatus='" + getPrivateStatus() + "'" +
            "}";
    }

}