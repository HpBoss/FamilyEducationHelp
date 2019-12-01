package com.example.familyeducationhelp.mapCardView;

import java.util.ArrayList;

public class FatherData {
    private int avatar;//头像
    private String username;//用户名
    private int sex;//性别
    private String grade;//年级
    private String subject;//科目
    private String price;//上课一小时的价格
    private ArrayList<ChildData> list;

    public FatherData(int avatar, String username, int sex, String grade, String subject, String price) {
        this.avatar = avatar;
        this.username = username;
        this.sex = sex;
        this.grade = grade;
        this.subject = subject;
        this.price = price;
    }

    public ArrayList<ChildData> getList() {
        return list;
    }

    public void setList(ArrayList<ChildData> list) {
        this.list = list;
    }

    public int getAvater() {
        return avatar;
    }

    public void setAvater(int avater) {
        this.avatar = avater;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

