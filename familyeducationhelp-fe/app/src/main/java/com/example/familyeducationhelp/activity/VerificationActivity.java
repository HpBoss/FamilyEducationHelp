package com.example.familyeducationhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.example.familyeducationhelp.R;
import com.example.familyeducationhelp.classList.SendMessageRequest;
import com.example.familyeducationhelp.classList.VerificationCode;

import cn.leancloud.sms.AVSMS;
import cn.leancloud.types.AVNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class VerificationActivity extends BaseActivity implements View.OnClickListener {
    private EditText editText;
    private TextView textCaptcha, textError;
    private String verifyCode;
    private String mobilePhone;
    private LoadingDailog.Builder loadBuilder;
    private LoadingDailog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_verification);
        //实例化editText、textView、获取焦点、打开软键盘
        initWindows();
        //当一转到这个活动，就启动——倒计时
        countTimer.start();
        //左上角返回按钮
        ImageView iv_rt = findViewById(R.id.iv_rt);
        iv_rt.setOnClickListener(this);
        //监听验证码的输入情况并进行判断
        VerificationCode verificationCode = findViewById(R.id.verify_code_view);
        verificationCode.setInputCompleteListener(new VerificationCode.InputCompleteListener() {
            @Override
            public void inputComplete() {
                Intent intent = getIntent();
                verifyCode = editText.getText().toString();
                mobilePhone = intent.getStringExtra("mobilePhone");
                loadingLayout();
                AVSMS.verifySMSCodeInBackground(verifyCode, mobilePhone).subscribe(new Observer<AVNull>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(AVNull avNull) {
                        skip();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        warning(throwable);
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

    private void loadingLayout() {
        loadBuilder.setMessage("验证中...")
                .setCancelable(true)
                .setCancelOutside(true);
        dialog = loadBuilder.create();
        dialog.show();
    }

    private void warning(Throwable throwable) {
        dialog.dismiss();
        textError.setVisibility(View.VISIBLE);
        textError.setText(throwable.getMessage());
    }

    private void skip() {
        dialog.dismiss();
        //验证输入完成之后软键盘自动收起
        InputMethodManager imm = (InputMethodManager) VerificationActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        //跳转到主界面
        Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    private void initWindows() {
        textCaptcha = findViewById(R.id.textCaptcha);
        textCaptcha.setOnClickListener(this);
        editText = findViewById(R.id.edit_text_view);
        textError = findViewById(R.id.errorInformation);
        loadBuilder = new LoadingDailog.Builder(this);
        //为编辑框获取焦点
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        //打开软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textCaptcha:
                countTimer.start();
                Intent intents = getIntent();
                mobilePhone = intents.getStringExtra("mobilePhone");
                SendMessageRequest messageRequest = new SendMessageRequest(mobilePhone);
                messageRequest.sendRequest();
                messageRequest.setIsSendMessage(new SendMessageRequest.SendMessage() {
                    @Override
                    public void SendSucceed() {
                        Toast.makeText(VerificationActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void SendFalse(Throwable throwable) {
                        Toast.makeText(VerificationActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.iv_rt:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.translate_left_in, R.anim.translate_right_out);
                break;
            default:
                break;
        }
    }

    //为内部类实例化一个对象
    CountTimer countTimer = new CountTimer(60000, 1000);

    //新建一个CountTimer继承CountDownTimer,并使用他的onTick和onFinish方法
    public class CountTimer extends CountDownTimer {
        private CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int time = (int) (Math.round((double) millisUntilFinished / 1000) - 1);
            String s_time = Integer.toString(time);
            String resend = "s后重新发送";
            String inform = s_time + resend;
            textCaptcha.setText(inform);
            //当倒计时的时候，不可点击
            textCaptcha.setClickable(false);
            textCaptcha.setTextSize(16);
        }

        @Override
        public void onFinish() {
            textCaptcha.setTextSize(16);
            textCaptcha.setText("重新发送");
            textCaptcha.setClickable(true);
        }
    }
}
