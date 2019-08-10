package com.example.familyeducationhelp.ClassList;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller {

    private int mDuration = 1000; // 默认滑动速度 1500ms

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, AccelerateInterpolator interpolator) {
        super(context, (android.view.animation.Interpolator) interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    /**
     * set animation time
     *
     * @param time
     */
    public void setmDuration(int time) {
        mDuration = time;
    }

    /**
     * get current animation time
     *
     * @return
     */
    public int getmDuration() {
        return mDuration;
    }
}