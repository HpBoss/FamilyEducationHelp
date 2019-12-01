package com.example.familyeducationhelp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.tu.loadingdialog.LoadingDailog;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.familyeducationhelp.R;
import com.example.familyeducationhelp.classList.PasswordTextOnListener;
import com.example.familyeducationhelp.classList.PhoneTextOnListener;
import com.example.familyeducationhelp.classList.SendMessageRequest;
import com.example.familyeducationhelp.database.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText input_phoneN,input_login_key;
    private Button bt_getVerify;
    private TextView bottom_hint,password_login,forget_password;
    private ImageView image_delete_phone,bottom_line,image_delete_password,image_display;
    private String content = "";
    private boolean isKey = true;
    private boolean isDisplay = true;
    private PhoneTextOnListener phoneTextOnListener;
    private boolean isCompletePrint = false;
    private LoadingDailog.Builder loadBuilder;
    private LoadingDailog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBar(this,true,true);
        judgePermission();
        initLocationOption();
        initView();
        setPhoneListener();
        setPasswordListener();
    }

    private void setPhoneListener() {
        phoneTextOnListener = new PhoneTextOnListener(input_phoneN,image_delete_phone);
        input_phoneN.addTextChangedListener(phoneTextOnListener);
        phoneTextOnListener.setIsNotEmpty(new PhoneTextOnListener.isNotEmpty() {
            @Override
            public void setBackgroundLight(String str) {
                content = str;
                bt_getVerify.setBackgroundResource(R.drawable.rectangle_button_light);
                isCompletePrint = true;
            }
            @Override
            public void setBackgroundDark() {
                bt_getVerify.setBackgroundResource(R.drawable.rectangle_button_dark);

            }
        });
    }

    private void initLocationOption() {
        //定位初始化
        LocationClient locationClient = new LocationClient(getApplicationContext());
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("gcj02"); // 设置坐标类型
        option.setScanSpan(1000);//必须大于一秒（1000毫秒）
        //设置locationClientOption
        locationClient.setLocOption(option);
        //注册LocationListener监听器
        locationClient.registerLocationListener(listen);
        //开启地图定位图层
        locationClient.start();
    }
    BDAbstractLocationListener listen = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            int count = bdLocation.getLocationDescribe().length();
//            Log.d("location:",bdLocation.getLocationDescribe());
            SharedPreferences.Editor editor = getSharedPreferences("address",MODE_PRIVATE).edit();
            editor.putString("locationInformation",bdLocation.getLocationDescribe().substring(1,count-2));
            editor.apply();
            //159 0839 2578
        }
    };
    //6.0之后要动态获取权限，重要！！！
    protected void judgePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            // 检查该权限是否已经获取
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            String[] permissions = new String[]{Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
    private void initView() {
        image_display = findViewById(R.id.display_password);
        image_display.setOnClickListener(this);
        password_login = findViewById(R.id.password_login);
        password_login.setOnClickListener(this);
        forget_password = findViewById(R.id.forget_password);
        input_phoneN = findViewById(R.id.input_phone);
        TextView password_login = findViewById(R.id.password_login);
        password_login.setOnClickListener(this);
        bt_getVerify = findViewById(R.id.get_verify);
        bt_getVerify.setOnClickListener(this);
        image_delete_phone = findViewById(R.id.image_delete1);
        image_delete_phone.setOnClickListener(this);
        image_delete_password = findViewById(R.id.image_delete2);
        image_delete_password.setOnClickListener(this);
        input_login_key = findViewById(R.id.input_login_key);
        bottom_line = findViewById(R.id.bottom_line);
        bottom_hint = findViewById(R.id.bottom_hint);
        loadBuilder = new LoadingDailog.Builder(this);
    }

    private void setPasswordListener() {
        PasswordTextOnListener passwordTextOnListener = new PasswordTextOnListener();
        input_login_key.addTextChangedListener(passwordTextOnListener);
        passwordTextOnListener.setIsExist(new PasswordTextOnListener.isExist() {
            @Override
            public void setAppear() {
                image_delete_password.setVisibility(View.VISIBLE);
                if (isCompletePrint){
                    bt_getVerify.setBackgroundResource(R.drawable.rectangle_button_light);
                }
            }
            @Override
            public void setDisappear() {
                image_delete_password.setVisibility(View.GONE);
                bt_getVerify.setBackgroundResource(R.drawable.rectangle_button_dark);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.password_login://点击密码登录按钮
                //选择登录形式（验证码/密码）
                if (isKey){
                    input_login_key.setVisibility(View.VISIBLE);
                    bottom_line.setVisibility(View.VISIBLE);
                    bottom_hint.setVisibility(View.INVISIBLE);
                    password_login.setText("验证码登录");
                    forget_password.setVisibility(View.VISIBLE);
                    image_display.setVisibility(View.VISIBLE);
                    input_login_key.setVisibility(View.VISIBLE);
                    if (isCompletePrint){
                        bt_getVerify.setBackgroundResource(R.drawable.rectangle_button_dark);
                    }
                    isKey = false;
                }else {
                    input_login_key.setVisibility(View.GONE);
                    bottom_line.setVisibility(View.GONE);
                    bottom_hint.setVisibility(View.VISIBLE);
                    password_login.setText("密码登录");
                    forget_password.setVisibility(View.GONE);
                    input_login_key.setVisibility(View.GONE);
                    image_delete_password.setVisibility(View.GONE);
                    image_display.setVisibility(View.GONE);
                    if (isCompletePrint){
                        bt_getVerify.setBackgroundResource(R.drawable.rectangle_button_light);
                    }
                    isKey = true;
                }
                break;
            case R.id.get_verify://点击获取验证码按钮
                if (isCompletePrint && isKey){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (DBUtils.registerUser(content)){
                                Log.d("Register","成功！");
                            }
                        }
                    }).start();
                    SendMessageRequest messageRequest = new SendMessageRequest(content);
                    loadingLayout();
                    messageRequest.sendRequest();
                    messageRequest.setIsSendMessage(new SendMessageRequest.SendMessage() {
                        @Override
                        public void SendSucceed() {
                            print_succeed();
                        }
                        @Override
                        public void SendFalse(Throwable throwable) {
                            print_failed(throwable);
                        }
                    });
                }else {
                    Toast.makeText(LoginActivity.this,"没有成功输入",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.image_delete1://点击清空手机号按钮
                input_phoneN.setText("");//清空手机号码输入框
                phoneTextOnListener.setsLastLength(0);
                image_delete_phone.setVisibility(View.GONE);
                bt_getVerify.setBackgroundResource(R.drawable.rectangle_button_dark);//点击右侧删除按钮后，获取验证码按钮背景变暗
                break;
            case R.id.image_delete2://点击清空密码按钮
                input_login_key.setText("");//清空密码输入框
                image_delete_password.setVisibility(View.GONE);
                break;
            case R.id.display_password://点击显示密码
                if (isDisplay){
                    input_login_key.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    image_display.setImageResource(R.drawable.display_password);
                    isDisplay = false;
                }else {
                    input_login_key.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);//必须使用这两种类型
                    image_display.setImageResource(R.drawable.hide_password);
                    isDisplay = true;
                }
                break;
        }
    }

    private void loadingLayout(){
        loadBuilder.setMessage("发送中...")
                .setCancelable(true)
                .setCancelOutside(true);
        dialog=loadBuilder.create();
        dialog.show();
    }
    private void print_succeed(){
        dialog.dismiss();
        Intent intent = new Intent(this, VerificationActivity.class);
        intent.putExtra("mobilePhone",content);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }
    private void print_failed(Throwable throwable){
        Toast.makeText(this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
    }

}
