package com.example.choimyeongsik.BookBank.model;

public class datalibraryVO {
   private String title;
    private String authors;
    private String isbn13;
    private String bookImageURL;

    public datalibraryVO(){

    }
    public datalibraryVO(String title, String authors, String isbn13, String bookImageURL) {
        this.title = title;
        this.authors = authors;
        this.isbn13 = isbn13;
        this.bookImageURL = bookImageURL;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getBookImageURL() {
        return bookImageURL;
    }

    public void setBookImageURL(String bookImageURL) {
        this.bookImageURL = bookImageURL;
    }



}
