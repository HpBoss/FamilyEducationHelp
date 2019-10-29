package com.example.familyeducationhelp.Adapter;

import com.example.familyeducationhelp.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

public class SexAdapter extends WheelView.WheelAdapter {
    //性别List添加数据
    public List<String> sexList;
    public SexAdapter(){
        sexList = new ArrayList<>();
        sexList.add("男");
        sexList.add("女");
    }
    @Override
    public int getItemCount() {
        return sexList.size();
    }

    @Override
    public String getItem(int index) {
        return sexList.get(index);
    }
}