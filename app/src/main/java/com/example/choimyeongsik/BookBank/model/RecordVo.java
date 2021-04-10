package com.example.choimyeongsik.BookBank.model;

//
public class RecordVo {

    private String content;
    private String name;
    private byte[] image;
    private String number;

    public RecordVo(String name, String content, byte[] image, String number) {
this.image=image;
        this.name = name;
        this.content = content;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }
    public void setName() {
        this.content = content;
    }


    public String getContent() {
        return content;
    }

    public void setContent() {
        this.content = content;
    }
}
