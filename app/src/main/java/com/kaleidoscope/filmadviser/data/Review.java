package com.kaleidoscope.filmadviser.data;

public class Review {
    private String author;
    private String content;
    private String date;
    private String type;

    public Review(String author, String content, String date, String type) {
        this.author = author;
        this.content = content;
        this.date = date;
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }
}
