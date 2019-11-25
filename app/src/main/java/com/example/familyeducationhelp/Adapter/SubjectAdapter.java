package com.example.familyeducationhelp.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.familyeducationhelp.R;

public class SubjectAdapter extends BaseAdapter {
    private Context mContext;
    private String[] changeSubject = {"语文","数学","英语","物理","化学","生物","政治","历史","地理"};
    private int changePosition;
    public SubjectAdapter(Context context,int position) {
        mContext = context;
        changePosition = position;
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
        TextView iconSubject;
        if (view == null){
            iconSubject = new TextView(mContext);
            iconSubject.setGravity(Gravity.CENTER);
            switch (i){
                case 0:case 2:case 6:case 7:case 8:
                    if (changePosition == i){
                        iconSubject.setBackgroundResource(R.drawable.ellipse_yellow_background_shape);
                    }else {
                        iconSubject.setBackgroundResource(R.drawable.ellipse_shallow_cephg_background_shape);
                    }
                    break;
                case 1:case 3:case 4:case 5:
                    if (changePosition == i){
                        iconSubject.setBackgroundResource(R.drawable.ellipse_blue_background_shape);
                    }else {
                        iconSubject.setBackgroundResource(R.drawable.ellipse_shallow_mpcb_background_shape);
                    }
                    break;
                default:
                    break;
            }
            iconSubject.setTextSize(16);
            iconSubject.setPadding(50,25,50,25);
            iconSubject.setLayoutParams(new GridView.LayoutParams(252,130));
        } else {
            iconSubject = (TextView) view;
        }
        iconSubject.setText(changeSubject[i]);
        iconSubject.setTag(i);
        return iconSubject;
    }
}
