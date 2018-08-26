package com.classrecorder.teacherserver.server.forms;

public class YoutubeUpdateForm {
    
    private String videoTitle;

    private String description;

    private String tags;

    public YoutubeUpdateForm(String videoTitle, String description, String tags) {
        this.videoTitle = videoTitle;
        this.description = description;
        this.tags = tags;
    }

    public YoutubeUpdateForm() {
    }

    public String getVideoTitle() {
        return this.videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "{" +
            " videoTitle='" + getVideoTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", tags='" + getTags() + "'" +
            "}";
    }

}