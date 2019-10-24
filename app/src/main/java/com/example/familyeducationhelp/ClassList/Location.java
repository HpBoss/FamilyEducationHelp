package com.example.familyeducationhelp.ClassList;

import android.widget.TextView;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

public class Location extends BDAbstractLocationListener {
    private String oldLocation = null;
    private TextView mTextView;

    public Location(TextView textView) {
        this.mTextView = textView;
    }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        //因为这个函数会实时返回定位信息，我们只需要检测当位置发生变化的情况
        String newLocation = bdLocation.getLocationDescribe();
        if (!newLocation.equals(oldLocation)){
            int count = bdLocation.getLocationDescribe().length();
            mTextView.setText(bdLocation.getLocationDescribe().substring(1, count -2));
            oldLocation = newLocation;
        }
    }
}
