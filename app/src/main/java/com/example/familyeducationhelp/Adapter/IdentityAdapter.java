package com.example.familyeducationhelp.Adapter;

import com.example.familyeducationhelp.widget.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IdentityAdapter extends WheelView.WheelAdapter {
    private String[] userIdentity = {"学生","老师"};
    public List<String> identityList;
    public IdentityAdapter(){
        identityList = new ArrayList<>();
        identityList = Arrays.asList(userIdentity);
    }

    @Override
    protected int getItemCount() {
        return identityList.size();
    }

    @Override
    protected String getItem(int index) {
        return identityList.get(index);
    }
}
