package com.example.familyeducationhelp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.familyeducationhelp.R;

import java.sql.DatabaseMetaData;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(this, getResources().getColor(R.color.colorMainBlue));
    }

    /**
     * 设置沉浸式界面
     * 根据Android版本的不同，作出相应设置
     */
    public void setImmersive() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 活动页面从小到大的渐变动画
     */
    public void alphaAnimation_smallToBig() {
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    /**
     * 活动页面从右至左平移滑动动画
     */
    public void translatingAnimation_rightToLeft() {
        overridePendingTransition(R.anim.translate_right_in, R.anim.translate_left_out);
    }

    /**
     * 活动页面从左至右平移滑动动画
     */
    public void translatingAnimation_leftToRight() {
        overridePendingTransition(R.anim.translate_left_in, R.anim.translate_right_out);
    }

    /**
     * @param start 起始活动
     * @param end   结束活动类
     */
    public void activityJump(Activity start, Class end) {
        Intent intent = new Intent(start, end);
        startActivity(intent);
        finish();
    }

    /**
     * @return 返回布尔值
     */
    public boolean isLoginSuccess() {
        SharedPreferences preferences = getSharedPreferences("SHARE_APP_LOGIN", Context.MODE_PRIVATE);
        return preferences.getBoolean("LOGIN_SUCCESS", false);
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity 活动
     * @param color    颜色值
     */
    protected void setStatusBar(Activity activity, int color) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0 以上直接设置状态栏颜色
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
            // 去掉系统状态栏下的windowContentOverlay
            View v = window.findViewById(android.R.id.content);
            if (v != null) {
                v.setForeground(null);
            }
            if (toGrey(color) > 225) {//判定该状态栏颜色条件下，是否要将状态栏图标切换成灰色
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 以上直接设置状态栏颜色
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
        }
    }

    /**
     * 把颜色转换成灰度值。
     * 代码来自 Flyme 示例代码
     */
    public static int toGrey(@ColorInt int color) {
        int blue = Color.blue(color);
        int green = Color.green(color);
        int red = Color.red(color);
        return (red * 38 + green * 75 + blue * 15) >> 7;
    }

    /**
     * 重写回退键监听，添加返回活动切换动画
     */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.translate_left_in, R.anim.translate_right_out);
    }

    /**
     * 键盘焦点监听，焦点消失键盘收起
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();//这时能获取到焦点的View就是EditText
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            //只要点击了EditText周围的空白处，就返回true
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    /**
     * 利用反射获取状态栏高度
     *
     * @return statusBar的height
     */
    public static int getStatusBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
