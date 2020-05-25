package com.example.familyeducationhelp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.familyeducationhelp.R;

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
