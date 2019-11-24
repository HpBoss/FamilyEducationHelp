package com.example.familyeducationhelp.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.familyeducationhelp.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcom);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("count", 0); // 存在则打开它，否则创建新的Preferences
                int count = preferences.getInt("count", 0); // 取出数据
                if (count == 0) {
                    Intent intent_identity = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent_identity);
                    finish();
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                overridePendingTransition(R.anim.translate_right_in, R.anim.translate_left_out);
            }
        }, 3000);//延迟3s

    }


}
