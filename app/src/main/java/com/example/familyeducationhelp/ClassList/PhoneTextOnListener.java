package com.example.familyeducationhelp.ClassList;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneTextOnListener implements TextWatcher {
    //edit输入框变量
    private int sLastLength = 0;
    private int oldLength = 0;
    private boolean isChange = true;
    private int curLength = 0;
    private int emptyNumB = 0;  //初始空格数
    private EditText input_phoneN;
    private String content;
    private isNotEmpty mIsNotEmpty;
    private ImageView mImageView;
    private boolean isFirstIn = true;

    public void setsLastLength(int sLastLength) {
        this.sLastLength = sLastLength;
    }

    public PhoneTextOnListener(EditText input_phoneN, ImageView imageView){
        this.input_phoneN = input_phoneN;
        this.mImageView = imageView;
    }

    public interface isNotEmpty{
        void setBackgroundLight(String str);
        void setBackgroundDark();
    }

    public void setIsNotEmpty(isNotEmpty isNotEmpty) {
        mIsNotEmpty = isNotEmpty;
    }

    public static boolean isPhoneNumber(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        oldLength = s.length();
        emptyNumB = 0;
        for (int i = 0; i < s.toString().length(); i++) {
            if (s.charAt(i) == ' ') emptyNumB++;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        curLength = s.length();
        if (curLength == 0){
            mImageView.setVisibility(View.GONE);
            isFirstIn = true;
        }
        //优化处理,如果长度未改变或则改变后长度小于3就不需要添加空格
        isChange = curLength != oldLength && curLength > 3;
    }
    @Override
    public void afterTextChanged(Editable s) {
        if (!"".equals(String.valueOf(s)) && isFirstIn) {
            mImageView.setVisibility(View.VISIBLE);
            isFirstIn = false;
        }
        if (isChange) {
            boolean delete;
            //判断是ediText中的字符串是在减少 还是在增加
            delete = curLength - sLastLength < 0;
            sLastLength = curLength;
            int selectIndex = input_phoneN.getSelectionEnd();//获取光标位置
            content = s.toString().replaceAll(" ", "");
            if (isPhoneNumber(content)){//判断输入的内容是否是一个手机号码
                mIsNotEmpty.setBackgroundLight(content);
            }else {
                mIsNotEmpty.setBackgroundDark();
            }
            StringBuilder sb = new StringBuilder(content);
            //遍历加空格
            int index = 1;
            //遍历添加空格后的空格数
            int emptyNumA = 0;
            for (int i = 0; i < content.length(); i++) {
                if (i == 2 || i == 6) {
                    sb.insert(i + index, " ");
                    index++;
                    emptyNumA++;
                }
            }
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
            if (selectIndex > 0 && s.charAt(selectIndex - 1) == ' ') {
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
