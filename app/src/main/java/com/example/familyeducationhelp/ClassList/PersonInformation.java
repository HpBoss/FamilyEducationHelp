package com.example.familyeducationhelp.ClassList;

public class PersonInformation {
    private String person_name;
    private int image;
    private String grade;
    private String time;
    private String site;
    private String price;

    public PersonInformation(String person_name, int image, String grade, String time, String site, String price) {
        this.person_name = person_name;
        this.image = image;
        this.grade = grade;
        this.time = time;
        this.site = site;
        this.price = price;
    }

    public String getPerson_name() {
        return person_name;
    }

    public int getImage() {
        return image;
    }

    public String getGrade() {
        return grade;
    }

    public String getTime() {
        return time;
    }

    public String getSite() {
        return site;
    }

    public String getPrice() {
        return price;
    }
}
