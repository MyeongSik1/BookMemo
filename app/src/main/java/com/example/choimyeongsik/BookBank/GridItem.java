package com.example.choimyeongsik.BookBank;

public class GridItem {
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public GridItem(byte[] image) {
        this.image = image;
    }
}
