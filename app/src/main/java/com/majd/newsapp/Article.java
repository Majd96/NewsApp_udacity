package com.majd.newsapp;


/**
 * Created by majd on 3/21/18.
 */

public class Article {
    private String title;
    private String sectionName;
    private String authorName;
    private String publishedDate;
    private String url;

    public Article(String title, String sectionName, String authorName, String publishedDate, String url) {
        this.title = title;
        this.sectionName = sectionName;
        this.publishedDate = publishedDate;
        this.url = url;
        this.authorName = authorName;

    }


    public String getTitle() {
        return title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getUrl() {
        return url;
    }


}
