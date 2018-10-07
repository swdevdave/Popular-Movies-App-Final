package com.swdave.popular_movies_app_final.model;


public class ReviewResults {

    private String author;

    private String content;


    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public ReviewResults(String author, String content) {
        this.author = author;
        this.content = content;
    }
}
