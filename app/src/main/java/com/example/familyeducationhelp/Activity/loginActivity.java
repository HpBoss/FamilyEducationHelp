package com.example.familyeducationhelp.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyeducationhelp.R;

import cn.leancloud.sms.AVSMS;
import cn.leancloud.sms.AVSMSOption;
import cn.leancloud.types.AVNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class loginActivity extends BaseActivity implements View.OnClickListener, TextWatcher{
    private EditText input_phoneN;
    private TextView password_login;
    private Button bt_getVerify;
    private ImageView image_delete;
    //edit输入框变量
    private static final String TAG ="长度输出" ;
    private int sLastLength=0;
    private int oldLength = 0;
    private String content = "";
    private boolean isChange = true;
    private int curLength = 0;
    private int emptyNumB = 0;  //初始空格数
    private int emptyNumA = 0;  //遍历添加空格后的空格数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        addStatusViewWithColor(this, Color.parseColor("#39C2D0"));
        init();
    }
    private void init() {
        input_phoneN = findViewById(R.id.input_phone);
        input_phoneN.addTextChangedListener(this);
        password_login = findViewById(R.id.password_login);
        password_login.setOnClickListener(this);
        bt_getVerify = findViewById(R.id.get_verify);
        bt_getVerify.setOnClickListener(this);
        image_delete = findViewById(R.id.image_delete);
        image_delete.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.password_login:

                break;
            case R.id.get_verify:
                if (content.length()==11){
                    AVSMSOption option = new AVSMSOption();
                    option.setApplicationName("家教帮");
                    option.setSignatureName("loginFEH");
//                    option.setTemplateName("lastNewInform");
//                    option.setTtl(5);
                    // use default signature and template.
                    //String mobilePhone = "19980492664"; // please replace with your real phonenumber
                    AVSMS.requestSMSCodeInBackground(content, option).subscribe(new Observer<AVNull>() {
                        @Override
                        public void onSubscribe(Disposable disposable) {

                        }

                        @Override
                        public void onNext(AVNull avNull) {
                            print_succeed();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            print_failed(throwable);
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
                }
                break;
            case R.id.image_delete:
                input_phoneN.setText("");//清空手机号码输入框
                sLastLength=0;//因为是删除所有内容，此时sLastLength也必须为0
                bt_getVerify.setBackgroundResource(R.drawable.rectangle_button_dark);//点击右侧删除按钮后，获取验证码按钮背景变暗
                break;
        }
    }

    private void print_succeed(){
        Toast.makeText(this,"发送成功！",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, VerificationActivity.class);
        intent.putExtra("mobilePhone",content);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }
    private void print_failed(Throwable throwable){
        Toast.makeText(this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        oldLength = s.length();
        Log.i(TAG, "未改变长度: " + oldLength);
        emptyNumB = 0;
        for (int i = 0; i < s.toString().length(); i++) {
            if (s.charAt(i) == ' ') emptyNumB++;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        curLength = s.length();
        Log.i(TAG, "当前长度: " + curLength);
        //优化处理,如果长度未改变或则改变后长度小于3就不需要添加空格
        if (curLength == oldLength || curLength <= 3) {
            isChange = false;
        }
        else {
            isChange = true;
        }

    }
    @Override
    public void afterTextChanged(Editable s) {
        if (!s.equals("")) {
            image_delete.setVisibility(View.VISIBLE);
        }
        if (isChange) {
            boolean delete;
            if (curLength - sLastLength < 0) {  //判断是editext中的字符串是在减少 还是在增加
                delete = true;
            } else {
                delete = false;
            }
            sLastLength = curLength;
            int selectIndex = input_phoneN.getSelectionEnd();//获取光标位置
            content = s.toString().replaceAll(" ", "");
            if (content.length()==11){
                bt_getVerify.setBackgroundResource(R.drawable.rectangle_button_light);
            }else {
                bt_getVerify.setBackgroundResource(R.drawable.rectangle_button_dark);
            }
            Log.i(TAG, "content:" + content);
            StringBuffer sb = new StringBuffer(content);
            //遍历加空格
            int index = 1;
            emptyNumA = 0;
            for (int i = 0; i < content.length(); i++) {
                if (i == 2) {
                    sb.insert(i + index, " ");
                    index++;
                    emptyNumA++;
                } else if (i == 6) {
                    sb.insert(i + index, " ");
                    index++;
                    emptyNumA++;
                }
            }
            Log.i(TAG, "result content:" + sb.toString());
            String result = sb.toString();
            //遍历加空格后 如果发现最后一位是空格 就把这个空格去掉
            if (result.endsWith(" ")) {
                result = result.substring(0, result.length() - 1);
                emptyNumA--;
            }
            //用遍历添加空格后的字符串 来替换EditText变化后的字符串
            s.replace(0, s.length(), result);
            //处理光标位置
            if (emptyNumA > emptyNumB){
                selectIndex = selectIndex + (emptyNumA - emptyNumB);
            }
            if (selectIndex > result.length()) {
                selectIndex = result.length();
            } else if (selectIndex < 0) {
                selectIndex = 0;
            }
            // 例如"123 45"且光标在4后面 这时需要删除4后光标的处理
            if (selectIndex > 1 && s.charAt(selectIndex - 1) == ' ') {
                if (delete) {
                    selectIndex--;
                } else {
                    selectIndex++;
                }
            }
            input_phoneN.setSelection(selectIndex);
            isChange = false;
        }
    }
}
