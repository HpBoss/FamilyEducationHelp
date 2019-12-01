package com.example.familyeducationhelp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.familyeducationhelp.R;

public class FrequencyDialog extends Dialog implements View.OnClickListener{
    private TextView textCount,textHour;
    private updateFrequencyListener mUpdateFrequencyListener;

    public void setUpdateFrequencyListener(updateFrequencyListener updateFrequencyListener) {
        mUpdateFrequencyListener = updateFrequencyListener;
    }

    public FrequencyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_frequency);
        Window window = this.getWindow();
        assert window != null;
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFrequency();
    }

    private void setFrequency() {
        ImageView reduceCount = findViewById(R.id.reduce_count);
        ImageView reduceHour = findViewById(R.id.reduce_hour);
        ImageView addCount = findViewById(R.id.add_count);
        ImageView addHour = findViewById(R.id.add_hour);
        textCount = findViewById(R.id.text_count);
        textHour = findViewById(R.id.text_hour);
        addCount.setOnClickListener(this);
        addHour.setOnClickListener(this);
        reduceHour.setOnClickListener(this);
        reduceCount.setOnClickListener(this);
        TextView confirmFrequency = findViewById(R.id.confirm);
        confirmFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpdateFrequencyListener.setUpdateFrequency(String.valueOf(textCount.getText()),String.valueOf(textHour.getText()));
            }
        });
    }
    @Override
    public void onClick(View view) {
        int counts;
        switch (view.getId()) {
            case R.id.add_count:
                counts = Integer.parseInt(String.valueOf(textCount.getText()));
                textCount.setText(String.valueOf(++counts));
                break;
            case R.id.add_hour:
                counts = Integer.parseInt(String.valueOf(textHour.getText()));
                textHour.setText(String.valueOf(++counts));
                break;
            case R.id.reduce_count:
                counts = Integer.parseInt(String.valueOf(textCount.getText()));
                if (counts != 0) {
                    textCount.setText(String.valueOf(--counts));
                }
                break;
            case R.id.reduce_hour:
                counts = Integer.parseInt(String.valueOf(textHour.getText()));
                if (counts != 0) {
                    textHour.setText(String.valueOf(--counts));
                }
                break;
        }
    }
    public interface updateFrequencyListener{
        void setUpdateFrequency(String count,String hour);
    }
}
