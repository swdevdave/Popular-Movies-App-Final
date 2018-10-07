package com.swdave.popular_movies_app_final.model;

public class TrailerResults {

    // address to videos
    // https://www.youtube.com/watch?v=6ZfuNTqbHE8

    // address to images
    // https://img.youtube.com/vi/6ZfuNTqbHE8/1.jpg

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

    public String getBaseYoutubeUrl(){
        return BASE_YOUTUBE_URL + key;
    }

    public String getBaseYoutubeImg() {
        return BASE_YOUTUBE_IMG + key + "/0.jpg";
    }

    public String getName() {
        return name;
    }



}
