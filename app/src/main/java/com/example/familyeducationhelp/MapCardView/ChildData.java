package com.example.familyeducationhelp.MapCardView;

import com.example.familyeducationhelp.Map.MyString;

public class ChildData {
    private String startTime;//开始时间
    private String frequency;//频率
    private String otherRequest;//其它要求
    private String location;//位置信息
    private MyString distance;//离自己的距离

    public ChildData(String startTime, String frequency, String otherRequest, String location, MyString distance) {
        this.startTime = startTime;
        this.frequency = frequency;
        this.otherRequest = otherRequest;
        this.location = location;
        this.distance = distance;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getOtherRequest() {
        return otherRequest;
    }

    public void setOtherRequest(String otherRequest) {
        this.otherRequest = otherRequest;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MyString getDistance() {
        return distance;
    }

    public void setDistance(MyString distance) {
        this.distance = distance;
    }
}
