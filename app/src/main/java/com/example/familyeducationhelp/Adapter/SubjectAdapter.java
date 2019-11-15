package com.example.familyeducationhelp.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class SubjectAdapter extends BaseAdapter {
    private Context mContext;
    private int[] changeSubject;
    public SubjectAdapter(Context context,int[] changeSubject) {
        mContext = context;
        this.changeSubject = changeSubject;
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView iconSubject;
        if (view == null){
            iconSubject = new ImageView(mContext);
            iconSubject.setLayoutParams(new GridView.LayoutParams(252,120));
            iconSubject.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            iconSubject = (ImageView) view;
        }
        iconSubject.setImageResource(changeSubject[i]);
        iconSubject.setTag(i);
        return iconSubject;
    }
}
