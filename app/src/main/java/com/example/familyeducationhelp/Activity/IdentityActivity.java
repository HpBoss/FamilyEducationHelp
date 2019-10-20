package com.example.familyeducationhelp.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.familyeducationhelp.R;

public class IdentityActivity extends BaseActivity implements View.OnClickListener {
    private ImageView image_student;
    private ImageView image_teacher;
    private Button bt_nextStep;
    private boolean flagT = false;
    private boolean flagS = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_choice);
        addStatusViewWithColor(this, Color.parseColor("#39C2D0"));
        init();
    }
    private void init() {//变量初始化并设置监听
        image_student = findViewById(R.id.student_check);
        image_student.setOnClickListener(this);
        image_teacher = findViewById(R.id.teacher_check);
        image_teacher.setOnClickListener(this);
        bt_nextStep = findViewById(R.id.bt_nextStep);
        bt_nextStep.setOnClickListener(this);
    }
    //ImageView的监听事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){//监听事件选择两张身份卡片时，切换图片资源
            case R.id.student_check:
                image_student.setImageResource(R.drawable.student_check_light);
                image_teacher.setImageResource(R.drawable.teacher_check);
                bt_nextStep.setBackgroundResource(R.drawable.rectangle_button_light);
                flagS = true;
                flagT = false;
                break;
            case R.id.teacher_check:
                image_teacher.setImageResource(R.drawable.teacher_check_light);
                image_student.setImageResource(R.drawable.student_check);
                bt_nextStep.setBackgroundResource(R.drawable.rectangle_button_light);
                flagT = true;
                flagS = false;
                break;
            case R.id.bt_nextStep://当点击下一步按钮时，判断用户之前是否选择了自己的身份
                if (flagT||flagS){
                    skip();
                    break;
                }else {//用户未作出选择，则屏幕出现提示信息
                    Toast.makeText(this,"请选择身份信息",Toast.LENGTH_LONG).show();
                }

        }
    }
    //跳转到活动MainActivity
    private void skip() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
