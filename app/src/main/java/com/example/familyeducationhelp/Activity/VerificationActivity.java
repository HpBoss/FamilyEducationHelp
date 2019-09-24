package com.example.familyeducationhelp.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyeducationhelp.ClassList.VerificationCode;
import com.example.familyeducationhelp.R;

import cn.leancloud.sms.AVSMS;
import cn.leancloud.types.AVNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class VerificationActivity extends BaseActivity implements View.OnClickListener{
    private VerificationCode verificationCode;
    private EditText editText;
    private TextView textCaptcha;
    private String verifyCode ;
    private String mobilePhone;
    private ImageView iv_rt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_verification);
        addStatusViewWithColor(this, Color.parseColor("#39C2D0"));
        //实例化editText、textView、获取焦点、打开软键盘
        initWindows();
        //当一转到这个活动，就启动——倒计时
        countTimer.start();
        //左上角返回按钮
        iv_rt = findViewById(R.id.iv_rt);
        iv_rt.setOnClickListener(this);
        //监听验证码的输入情况并进行判断
        verificationCode = findViewById(R.id.verify_code_view);
        verificationCode.setInputCompleteListener(new VerificationCode.InputCompleteListener() {
            @Override
            public void inputComplete() {
                Intent intent = getIntent();
                verifyCode = editText.getText().toString();
                mobilePhone = intent.getStringExtra("mobilePhone");
                AVSMS.verifySMSCodeInBackground(verifyCode,mobilePhone).subscribe(new Observer<AVNull>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(AVNull avNull) {
                       skip();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        warning();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
            }
            @Override
            public void invalidContent() {
            }
        });
    }
    private void warning(){
        Toast.makeText(this,verifyCode+" "+mobilePhone,Toast.LENGTH_LONG).show();
    }
    private void skip(){
        Toast.makeText(VerificationActivity.this,"登录成功",Toast.LENGTH_LONG).show();
        //验证输入完成之后软键盘自动收起
        InputMethodManager imm = (InputMethodManager) VerificationActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        //跳转到主界面
        Intent intent = new Intent(VerificationActivity.this, IdentityActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    private void initWindows() {
        textCaptcha = findViewById(R.id.textCaptcha);
        textCaptcha.setOnClickListener(this);
        editText = findViewById(R.id.edit_text_view);
        //为编辑框获取焦点
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        //打开软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textCaptcha:
                countTimer.start();
                break;
            case R.id.iv_rt:
                Intent intent = new Intent(this, loginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                break;
            default:
                break;
        }
    }

    //为内部类实例化一个对象
    CountTimer countTimer = new CountTimer(60000, 1000);

    //新建一个CountTimer继承CountDownTimer,并使用他的onTick和onFinish方法
    public class CountTimer extends CountDownTimer {
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int time = (int)(Math.round((double)millisUntilFinished/1000)-1);
            String s_time = Integer.toString(time);
            String resend = "s后重新发送";
            String inform = s_time+resend;
            textCaptcha.setText(inform);
            //当倒计时的时候，不可点击
            textCaptcha.setClickable(false);
            textCaptcha.setTextSize(16);
        }

        @Override
        public void onFinish() {
            Log.d("MainActivity","倒计时完成");
            textCaptcha.setTextSize(16);
            textCaptcha.setText("重新发送");
            textCaptcha.setClickable(true);
        }
    }
}
