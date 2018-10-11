package com.swdave.popular_movies_app_final.model;


public class TrailerResults {

    private final static String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private final static String BASE_YOUTUBE_IMG = "http://img.youtube.com/vi/";

    private String key;
    private String name;

    public TrailerResults(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getYoutubeUrl() {
        return BASE_YOUTUBE_URL + key;
    }

    public String getYoutubeImg() {
        return BASE_YOUTUBE_IMG + key + "/0.jpg";
    }

    public String getName() {
        return name;
    }


}
