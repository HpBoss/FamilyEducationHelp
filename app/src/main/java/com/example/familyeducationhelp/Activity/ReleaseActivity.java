package com.example.familyeducationhelp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyeducationhelp.Adapter.ReleaseAdapter;
import com.example.familyeducationhelp.ClassList.ReleaseInformation;
import com.example.familyeducationhelp.Dialog.FrequencyDialog;
import com.example.familyeducationhelp.Dialog.PriceDialog;
import com.example.familyeducationhelp.Dialog.SubjectDialog;
import com.example.familyeducationhelp.Dialog.datepicker.CustomDatePicker;
import com.example.familyeducationhelp.Dialog.datepicker.DateFormatUtils;
import com.example.familyeducationhelp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ReleaseActivity extends BaseActivity implements TextWatcher {
    private List<ReleaseInformation> mReleaseInformationList;
    private ReleaseAdapter releaseAdapter;
    private RecyclerView recyclerView;
    private SubjectDialog subjectDialog;
    private TextInputEditText mRequestEdit;
    private CharSequence tempChar;
    private Intent intent;
    private TextView buttonRelease;
    private CustomDatePicker mDatePicker;
    private String[] littleTitle = {"科目","开始时间","上课频率","课时单价","其它要求"};
    private String[] subject = {"语文","数学","英语","物理","化学","生物","政治","历史","地理"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Debug.startMethodTracing("App");
        setContentView(R.layout.activity_release);
        addStatusViewWithColor(this, Color.parseColor("#39C2D0"));
        initView();
        loadData();
        initDatePicker();
        setAdapter();
//        Debug.stopMethodTracing();
    }

    private void initView() {
        TextView textTitle = findViewById(R.id.text_employment);
        textTitle.setText("发布");
        ImageView left_back =findViewById(R.id.image_back);
        left_back.setVisibility(View.GONE);
        TextView cancel = findViewById(R.id.cancel);
        cancel.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recy_information);
        buttonRelease = findViewById(R.id.bt_release);
        buttonRelease.setVisibility(View.VISIBLE);
        buttonRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //处理点击发布的事件
            }
        });
        mRequestEdit = findViewById(R.id.otherRequest);
        mRequestEdit.addTextChangedListener(this);
        intent = new Intent(this,MainActivity.class);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = false;
                for (int i = 1; i < 4; i++){
                    if (!mReleaseInformationList.get(i).getInformation().equals("") || !mReleaseInformationList.get(0).getSubject().equals("")){
                        alterDialogShow();
                        flag = true;
                        break;
                    }
                }
                if (!flag){//如果点击取消按钮时，未编写任何内容，可以直接返回MainActivity
                    jumpMethod();
                }
            }
        });
    }
    private void jumpMethod(){
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.alpha_in,R.anim.dialog_out_anim);
    }
    //显示界面中心对话框
    private void alterDialogShow(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("你要放弃本次编辑吗？");
        builder.setCancelable(false);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                jumpMethod();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void loadData() {
        ReleaseInformation releaseInformation;
        mReleaseInformationList = new ArrayList<>();
        for (int i = 0;i < 4; i++){
            if (i == 0){
                releaseInformation = new ReleaseInformation(littleTitle[i],"","");
            }else {
                releaseInformation = new ReleaseInformation(littleTitle[i],"","");
            }
            mReleaseInformationList.add(releaseInformation);
        }
    }

    private void setAdapter() {
        if (releaseAdapter == null){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        }
        releaseAdapter = new ReleaseAdapter(mReleaseInformationList);
        recyclerView.setAdapter(releaseAdapter);
        /** for循环和if条件判断语句
         * 判断前五项信息是否填写完整
         */
        boolean isCompleteEmpty = true;
        for (int i = 1;i < 4; i++){
            if (mReleaseInformationList.get(i).getInformation().equals("")){
                isCompleteEmpty = false;
                break;
            }
        }
        if (isCompleteEmpty){
            buttonRelease.setBackgroundResource(R.drawable.ellipse_deepyellow_background_shape);
        }

        releaseAdapter.setOnClickItems(new ReleaseAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(int position) {
                showDialogs(position);
            }
        });
    }
    //检测当前item的subject内容是什么，将此反应到dialog中
    private int searchNum(List<ReleaseInformation> releaseInformationList){
        int i = 0;
        for (; i < 9; i++){
            if (subject[i].equals(releaseInformationList.get(0).getSubject())){
                break;
            }
        }
        return i;
    }

    private void showDialogs(int position) {
        switch (position){
            case 0:
                int locationNum = searchNum(mReleaseInformationList);
                subjectDialog = new SubjectDialog(this,R.style.ReleaseDialog,locationNum);
                subjectDialog.show();
                subjectDialog.setUpdateSubjectListener(new SubjectDialog.updateSubjectListener() {
                    @Override
                    public void setUpdateSubject(int count) {
                        subjectDialog.dismiss();
                        mReleaseInformationList.get(0).setSubject(subject[count]);//更新list中相对应的subject数据,不需要重新加载所有数据
                        setAdapter();
                    }
                });
                break;
            case 1:
                mDatePicker.show(mReleaseInformationList.get(1).getInformation());
                break;
            case 2:
                final FrequencyDialog frequencyDialog = new FrequencyDialog(this,R.style.ReleaseDialog);
                frequencyDialog.show();
                frequencyDialog.setUpdateFrequencyListener(new FrequencyDialog.updateFrequencyListener() {
                    @Override
                    public void setUpdateFrequency(String count, String hour) {
                        mReleaseInformationList.get(2).setInformation("一周" + count + "次/" + "一次" + hour + "小时");
                        frequencyDialog.dismiss();
                        setAdapter();
                    }
                });
                break;
            case 3:
                final PriceDialog priceDialog = new PriceDialog(this,R.style.ReleaseDialog);
                priceDialog.show();
                priceDialog.setReadEditContentListener(new PriceDialog.readEditContentListener() {
                    @Override
                    public void readEditContent(String content) {
                        mReleaseInformationList.get(3).setInformation(content + "/小时");
                        setAdapter();
                    }
                });
                break;
        }
    }
    //初始化日期选择器
    private void initDatePicker() {
        long beginTimestamp = System.currentTimeMillis();
        long endTimestamp = DateFormatUtils.str2Long("3000-01-01", false);

        //mReleaseInformationList.get(1).setInformation(DateFormatUtils.long2Str(beginTimestamp, false));
        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mReleaseInformationList.get(1).setInformation(DateFormatUtils.long2Str(timestamp, false));
                setAdapter();
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(true);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }
    //要求输入框内容监听
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        tempChar = charSequence;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        int editEnd = mRequestEdit.getSelectionEnd();
        if (tempChar.length() > 30){
            editable.delete(editEnd-1,editEnd);
            mRequestEdit.setText(editable);
            mRequestEdit.setSelection(editEnd-1);
        }
    }
}
