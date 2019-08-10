package com.example.familyeducationhelp.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {
    private List<Integer> pictures = null;
    @Override
    public int getCount() {
        if (pictures != null) {
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position%pictures.size();
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageResource(pictures.get(realPosition));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);//让图片填满imageview，保持比例不变
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    public void setData(List<Integer> pictures) {
        this.pictures = pictures;
    }
    public int getDataRealSize(){
        if (pictures != null) {
            return pictures.size();
        }
            return 0;
    }

}
