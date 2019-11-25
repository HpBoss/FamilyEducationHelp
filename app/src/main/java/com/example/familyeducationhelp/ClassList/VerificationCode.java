package com.example.familyeducationhelp.ClassList;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.familyeducationhelp.R;

public class VerificationCode extends RelativeLayout {
    private TextView[] textViews;   //保存输入
    private EditText editText;
    private static int MAX = 6;     //验证码位数
    private String inputContent="";
    public VerificationCode(Context context){
        this(context,null);
    }

    public VerificationCode(Context context, AttributeSet attrs){
        this(context,attrs,0);

    }

    public VerificationCode(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        View.inflate(context, R.layout.item_verifiaction_code,this);
        
        textViews = new TextView[MAX];
        textViews[0] = findViewById(R.id.tv_one);
        textViews[1] = findViewById(R.id.tv_two);
        textViews[2] = findViewById(R.id.tv_third);
        textViews[3] = findViewById(R.id.tv_four);
        textViews[4] = findViewById(R.id.tv_five);
        textViews[5] = findViewById(R.id.tv_six);
        editText = findViewById(R.id.edit_text_view);

        editText.setCursorVisible(false);//隐藏光标
        setEditTextListener();
    }

    private void setEditTextListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputContent = editText.getText().toString();//获取EditText中的内容
                if(!inputContent.isEmpty()){
                    if(editable.length() == MAX){
//                        editText.removeTextChangedListener(this);这段代码应该注释：当输入验证码长度等于MAX时，再无法对editText作出改动
                        inputCompleteListener.inputComplete();//满足验证码位数是的操作
                    }else{
                        inputCompleteListener.invalidContent();//不满足时的操作
                    }
                }
                //将输入的内容显示到TextView上，并满足增加、删除的操作
                for(int i=0;i<MAX;i++){
                    if(i < inputContent.length()){
                        textViews[i].setText(String.valueOf(inputContent.charAt(i)));
                    }else{
                        textViews[i].setText("");
                    }
                }
            }
        });
    }

    private InputCompleteListener inputCompleteListener;  //监视器

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener){
        this.inputCompleteListener = inputCompleteListener;
    }

    public interface InputCompleteListener{
        void inputComplete();
        void invalidContent();
    }

}
