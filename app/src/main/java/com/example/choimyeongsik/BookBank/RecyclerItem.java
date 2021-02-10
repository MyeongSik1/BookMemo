package com.example.choimyeongsik.BookBank;

public class RecyclerItem {
    private String title;
    private String pirce;
    private String authors;
    private String publisher;
    private String thumbnail;


    public RecyclerItem(String title, String pirce, String authors, String publisher, String thumbnail, String isbn) {
        this.title = title;
        this.pirce = pirce;
        this.authors = authors;
        this.publisher = publisher;
        this.thumbnail = thumbnail;




    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPirce() {
        return pirce;
    }

    public void setPirce(String pirce) {
        this.pirce = pirce;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }



}
