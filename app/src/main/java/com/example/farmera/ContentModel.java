package com.example.farmera;


public class ContentModel {
    private String blog;
    private String video;

    public ContentModel() {
        // Default constructor required for Firebase
    }

    public ContentModel(String blog, String video) {
        this.blog = blog;
        this.video = video;
    }

    public String getBlog() {
        return blog;
    }

    public String getVideo() {
        return video;
    }
}