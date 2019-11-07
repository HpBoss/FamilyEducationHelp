package com.example.familyeducationhelp.ClassList;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.familyeducationhelp.R;

public class PasswordTextOnListener implements TextWatcher {
    private ImageView mImageView;
    private boolean isFirstIn = true;
    private Button bt_getVerify;
    private isExist mIsExist;

    public void setIsExist(isExist isExist) {
        mIsExist = isExist;
    }

    public interface isExist{
        void setAppear();
        void setDisappear();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            mIsExist.setDisappear();
            isFirstIn = true;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!"".equals(String.valueOf(s)) && isFirstIn) {
            mIsExist.setAppear();
            isFirstIn = false;
        }
    }
}
