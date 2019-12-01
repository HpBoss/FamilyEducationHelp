package com.example.familyeducationhelp.classList;

public class ReleaseInformation {
    private String litleTitle;
    private String information;
    private String subject;

    public ReleaseInformation(String litleTitle, String information, String subject) {
        this.litleTitle = litleTitle;
        this.information = information;
        this.subject = subject;
    }

    public String getLitleTitle() {
        return litleTitle;
    }

    public void setLitleTitle(String litleTitle) {
        this.litleTitle = litleTitle;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
