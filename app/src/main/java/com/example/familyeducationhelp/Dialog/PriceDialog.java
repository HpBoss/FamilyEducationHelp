package com.example.familyeducationhelp.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;

import com.example.familyeducationhelp.R;
import com.google.android.material.textfield.TextInputEditText;

public class PriceDialog extends Dialog implements View.OnClickListener, View.OnKeyListener{
    private Group group_button;
    private Group group_input;
    private TextInputEditText mTextInputEditText;
    private TextView flagTextView;
    private String textContent = "";
    private readEditContentListener mReadEditContentListener;

    public PriceDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setReadEditContentListener(readEditContentListener readEditContentListener) {
        mReadEditContentListener = readEditContentListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_price);
        Window window = this.getWindow();
        assert window != null;
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setPrice();
    }

    private void setPrice() {
        group_button = findViewById(R.id.group_button);
        group_input = findViewById(R.id.group_input);
        Group group_fold = findViewById(R.id.group_fold);
        group_fold.setOnClickListener(this);
        group_button.setOnClickListener(this);
        TextView confirmPrice = findViewById(R.id.confirm);
        confirmPrice.setOnClickListener(this);

        TextView price120 = findViewById(R.id.price120);
        TextView price60 = findViewById(R.id.price60);
        TextView price70 = findViewById(R.id.price70);
        TextView price80 = findViewById(R.id.price80);
        TextView price100 = findViewById(R.id.price100);
        TextView price140 = findViewById(R.id.price140);
        price60.setOnClickListener(this);
        price70.setOnClickListener(this);
        price80.setOnClickListener(this);
        price100.setOnClickListener(this);
        price120.setOnClickListener(this);
        price140.setOnClickListener(this);

        mTextInputEditText = findViewById(R.id.price_edit);
        mTextInputEditText.setOnKeyListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.group_button:
                group_button.setVisibility(View.GONE);
                group_input.setVisibility(View.VISIBLE);
                showKeyboard(mTextInputEditText,true);//弹出键盘
                if (flagTextView != null){
                    setLowLight(flagTextView);
                }
                break;
            case R.id.group_fold:
                group_input.setVisibility(View.GONE);
                group_button.setVisibility(View.VISIBLE);
                showKeyboard(mTextInputEditText,false);//隐藏键盘
                break;
            case R.id.confirm:
                if (!textContent.equals("")){
                    dismiss();
                    mReadEditContentListener.readEditContent(textContent);
                }
                break;
            default://价格表选择逻辑代码
                TextView textView = findViewById(id);
                setHighLight(textView);
                setLowLight(flagTextView);
                flagTextView = textView;
                if (mTextInputEditText.getText() != null){
                    group_input.setVisibility(View.GONE);
                    mTextInputEditText.setText("");
                }
                break;
        }
    }
    private void setHighLight(TextView textView){
        textView.setBackgroundResource(R.drawable.interger_border_accent);
        textView.setTextColor(Color.parseColor("#D81B60"));
        textContent = String.valueOf(textView.getText());
    }
    private void setLowLight(TextView textView){
        if (textView != null){
            textView.setBackgroundResource(R.drawable.interger_border_mainblue);
            textView.setTextColor(Color.parseColor("#39C2D0"));
        }
    }
//  mTextInputEditText.setFocusable(false);//setFocusable和setFocusableInTouchMode使用是有先后顺序的
//  mTextInputEditText.setFocusableInTouchMode(true);
//  mTextInputEditText.requestFocus();//获取焦点输入框，必须实现设置focusable何focusableInTouchMode,xml和代码均可设置
    /**
     * 监听点击回车键收起键盘
     * @param view 点击发生所在的控价或示图
     * @param i 点击的按键
     * @param keyEvent 点击事件
     * @return 返回布尔值，false表示处理完这些不再输入字符
     */
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN ){
            mTextInputEditText.clearFocus();
            showKeyboard(mTextInputEditText,false);
            textContent = String.valueOf(mTextInputEditText.getText());
        }
        return false;
    }

    public interface readEditContentListener{
        void readEditContent(String content);
    }
    //显示键盘
    private static void showKeyboard(View view,boolean flag) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (flag){
                view.requestFocus();
                imm.showSoftInput(view, 0);
            }else {
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
        }
    }

    /**
     *键盘焦点监听，焦点消失键盘收起
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                v.clearFocus();
                group_input.setVisibility(View.GONE);
                group_button.setVisibility(View.VISIBLE);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
