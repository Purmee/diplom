package com.example.punny.test;

/**
 * Created by PuNNy on 4/13/2018.
 */

public class Lesson {

    private final String title;
    private final String news;
    private final String info;
    private final int imageResource;

    public Lesson(String title, String news, String info, int imageResource) {
        this.title = title;
        this.news = news;
        this.info = info;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public String getNews() {
        return news;
    }

    public int getImageResource() {
        return imageResource;
    }
}
