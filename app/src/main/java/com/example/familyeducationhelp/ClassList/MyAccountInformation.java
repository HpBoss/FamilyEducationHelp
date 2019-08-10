package com.example.familyeducationhelp.ClassList;

import android.graphics.Bitmap;

public class MyAccountInformation {
    private String name;
    private Bitmap bitmap;
    private String content;
    private int next;
    private int image;

    public MyAccountInformation(String name, Bitmap bitmap, String content, int next) {
        this.name = name;
        this.bitmap = bitmap;
        this.content = content;
        this.next = next;
    }

    public MyAccountInformation(String name, String content, int image, int next) {
        this.name = name;
        this.content = content;
        this.next = next;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
