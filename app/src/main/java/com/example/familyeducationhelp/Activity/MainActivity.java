package com.example.familyeducationhelp.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.familyeducationhelp.ClassList.BottomNavigationBar;
import com.example.familyeducationhelp.Fragment.Home;
import com.example.familyeducationhelp.Fragment.Mine;
import com.example.familyeducationhelp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用了singleTask，使得每次返回MainActivity时，MainActivity都保持着跳转前的状态
 */
public class MainActivity extends BaseActivity {
    private int button_homePage = 0;
    private int button_add = 0;
    private int button_myself = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        judgePermission();
        addStatusViewWithColor(this, Color.parseColor("#39C2D0"));
        replaceFragment(new Home());
        //底部导航栏
        BottomNavigationBar bottomView = findViewById(R.id.BottomLayout);
        //底部按钮的信息
        bottomView.setMenu(R.menu.navigation_menu);
        //设置中间按钮可浮动
        bottomView.setFloatingEnable(true);
        //设置导航栏的监听事件
        bottomView.setOnItemSelectedListener(new BottomNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomNavigationBar.Item item, int position) {
                if (position == 0) {
                    if(button_homePage == 0){
                        button_add = 0;
                        button_myself = 0;
                        button_homePage++;
                        loadFragment(button_homePage,button_add,button_myself);
                    }
                } else if (position == 1) {
                    if(button_add == 0){
                        button_homePage = 0;
                        button_myself = 0;
                        button_add++;
                        loadFragment(button_homePage,button_add,button_myself);
                    }
                } else if (position == 2) {
                    if(button_myself == 0){
                        button_homePage = 0;
                        button_add = 0;
                        button_myself++;
                        loadFragment(button_homePage,button_add,button_myself);
                    }
                }
            }

        });
    }
    //6.0之后要动态获取权限，重要！！！
    protected void judgePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            // 检查该权限是否已经获取
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            List<String> mPermissionList = new ArrayList<>();
            mPermissionList.clear();
            for (String power : permissions
            ) {
                if (ContextCompat.checkSelfPermission(this,power) != PackageManager.PERMISSION_GRANTED){
                    mPermissionList.add(power);
                }
            }
            if (mPermissionList.size() >0){
                ActivityCompat.requestPermissions(this, permissions, 100);
            }
        }else{
            //做任何需要满足所有权限才能做的事情
        }
    }
    private void loadFragment(int button_homePage, int button_add, int button_myself) {
        if(button_homePage != 0){
            replaceFragment(new Home());
        }else if(button_add != 0){
            //replaceFragment(new release);
        }else if(button_myself != 0){
            replaceFragment(new Mine());
        }
    }
    //Fragment的更换
    private void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.on_fragment, fragment);
        transaction.commit();
    }
}
