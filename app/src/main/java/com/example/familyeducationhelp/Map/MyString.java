package com.example.familyeducationhelp.Map;

import androidx.annotation.NonNull;

public class MyString {
    private String str = null;

    public MyString() {

    }

    public MyString(String str) {
        this.str = str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @NonNull
    @Override
    public String toString() {
        return this.str;
    }
}
