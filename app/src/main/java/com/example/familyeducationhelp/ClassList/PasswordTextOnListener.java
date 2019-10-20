package com.example.familyeducationhelp.ClassList;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

public class PasswordTextOnListener implements TextWatcher {
    private ImageView mImageView;
    private boolean isFirstIn = true;

    public PasswordTextOnListener(ImageView imageView){
        this.mImageView = imageView;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            mImageView.setVisibility(View.GONE);
            isFirstIn = true;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!"".equals(String.valueOf(s)) && isFirstIn) {
            mImageView.setVisibility(View.VISIBLE);
            isFirstIn = false;
        }
    }
}
