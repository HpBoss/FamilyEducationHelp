package com.example.familyeducationhelp.ClassList;


import android.graphics.Bitmap;

import org.litepal.crud.LitePalSupport;

public class MyAccountInformation extends LitePalSupport {
    private String mAccountName;
    private Bitmap mAccountBitmap;
    private String mAccountItemContent;
    private int mAccountNextImage;
    private int mAccountImage;

    public MyAccountInformation(){

    }

    public MyAccountInformation(String mAccountName, Bitmap mAccountBitmap, String mAccountContent, int mAccountImage) {
        this.mAccountName = mAccountName;
        this.mAccountBitmap = mAccountBitmap;
        this.mAccountItemContent = mAccountContent;
        this.mAccountNextImage = mAccountImage;
    }

    public MyAccountInformation(String name, String content, int image, int nextImage) {
        this.mAccountName = name;
        this.mAccountItemContent = content;
        this.mAccountNextImage = nextImage;
        this.mAccountImage = image;
    }

    public String getAccountName() {
        return mAccountName;
    }

    public void setAccountName(String accountName) {
        mAccountName = accountName;
    }

    public Bitmap getAccountBitmap() {
        return mAccountBitmap;
    }

    public void setAccountBitmap(Bitmap accountBitmap) {
        mAccountBitmap = accountBitmap;
    }

    public String getAccountItemContent() {
        return mAccountItemContent;
    }

    public void setAccountItemContent(String accountItemContent) {
        mAccountItemContent = accountItemContent;
    }

    public int getAccountNextImage() {
        return mAccountNextImage;
    }

    public void setAccountNextImage(int accountNextImage) {
        mAccountNextImage = accountNextImage;
    }

    public int getAccountImage() {
        return mAccountImage;
    }

    public void setAccountImage(int accountImage) {
        mAccountImage = accountImage;
    }
}
