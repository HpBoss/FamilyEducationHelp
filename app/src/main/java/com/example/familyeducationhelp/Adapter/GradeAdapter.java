package com.example.familyeducationhelp.Adapter;

import com.example.familyeducationhelp.widget.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GradeAdapter extends WheelView.WheelAdapter {
    private String[][] userGrade = {{"一年级","二年级","三年级","四年级","五年级","六年级",
            "初一","初二","初三","高一","高二","高三"},{""}};
    public List<String> gradeList;
    public GradeAdapter(){

    }
    public GradeAdapter(int index){
        gradeList = new ArrayList<>();
        gradeList = Arrays.asList(userGrade[index]);
    }
    @Override
    protected int getItemCount() {
        return gradeList.size();
    }

    @Override
    protected String getItem(int index) {
        return gradeList.get(index);
    }
}
