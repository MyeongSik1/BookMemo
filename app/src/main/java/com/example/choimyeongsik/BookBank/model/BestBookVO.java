package com.example.choimyeongsik.BookBank.model;
//
public class BestBookVO {
    private String best_isbn13;
    private String best_bookimageURL;
    public BestBookVO() {

    }
    public BestBookVO(String best_isbn13, String best_bookimageURL) {
        this.best_bookimageURL = best_bookimageURL;
        this.best_isbn13 = best_isbn13;
    }

    public String getBest_isbn13() {
        return best_isbn13;
    }

    public void setBest_isbn13(String best_isbn13) {
        this.best_isbn13 = best_isbn13;
    }

    public String getBest_bookimageURL() {
        return best_bookimageURL;
    }

    public void setBest_bookimageURL(String best_bookimageURL) {
        this.best_bookimageURL = best_bookimageURL;
    }
}


