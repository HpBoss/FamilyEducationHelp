package com.example.familyeducationhelp.classList;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.familyeducationhelp.classList.wheelpickerwidget.WheelView;
import com.example.familyeducationhelp.R;

import java.util.ArrayList;
import java.util.List;

public class MyWheelPickerDialog {
    private Context mContext;
    private int layoutResource;
    private Dialog dialog;
    private int confirmBtnId;
    private TextView confirmBtn;
    private View view;

    private int wheelPickerFirstId = 0;
    private int wheelPickerSecondId = 0;
    private int indexFirst = 0;
    private int indexSecond = 0;
    private List<String> mListFirst;
    private List<String> mListSecond;
    private WheelView wheelPickerFirst;
    private WheelView wheelPickerSecond;

    private int btnFirstId;
    private int btnSecondId;
    private int btnThirdId;
    private int btnFourthId;
    private TextView btnFirst;
    private TextView btnSecond;
    private TextView btnThird;
    private TextView btnFourth;

    public MyWheelPickerDialog(Context mContext, int layoutResource, int wheelPickerId, int confirmBtnId){
        this.mContext = mContext;
        this.layoutResource = layoutResource;
        this.wheelPickerFirstId = wheelPickerId;
        this.confirmBtnId = confirmBtnId;
        initData();
    }

    public MyWheelPickerDialog(Context mContext, int layoutResource, int wheelPickerFirstId, int wheelPickerSecondId, int confirmBtnId){
        this.mContext = mContext;
        this.layoutResource = layoutResource;
        this.wheelPickerFirstId = wheelPickerFirstId;
        this.wheelPickerSecondId = wheelPickerSecondId;
        this.confirmBtnId = confirmBtnId;
        initData();
    }

    public MyWheelPickerDialog(Context context, int layoutResource, int btnFirstId) {
        mContext = context;
        this.layoutResource = layoutResource;
        this.btnFirstId = btnFirstId;
        initData();
    }

    public MyWheelPickerDialog(Context mContext, int layoutResource, int btnFirstId, int btnSecondId, int btnThirdId, int btnFourthId){
        this.mContext = mContext;
        this.layoutResource = layoutResource;
        this.btnFirstId = btnFirstId;
        this.btnSecondId = btnSecondId;
        this.btnThirdId = btnThirdId;
        this.btnFourthId = btnFourthId;
        initData();
    }

    //注意在构造方法后面添加initData()这个方法，不然在调用处的空指针异常让你想 “狗带”
    public void initData(){
        mListFirst = new ArrayList<>();
        mListSecond = new ArrayList<>();
        dialog = new Dialog(mContext, R.style.DialogTheme);
        view = View.inflate(mContext,layoutResource,null);
        dialog.setContentView(view);
        confirmBtn = dialog.findViewById(confirmBtnId);

        if(wheelPickerFirstId != 0){
            wheelPickerFirst = view.findViewById(wheelPickerFirstId);
            wheelPickerFirst.setCurved(true);//是否呈现3D效果
            wheelPickerFirst.setTextSize(60);
        }
        if(wheelPickerSecondId!=0){
            wheelPickerSecond = view.findViewById(wheelPickerSecondId);
            wheelPickerSecond.setCurved(true);//是否呈现3D效果
            wheelPickerSecond.setTextSize(60);
        }
        if(btnFirstId != 0){
            btnFirst = view.findViewById(btnFirstId);
        }
        if(btnSecondId != 0){
            btnSecond = view.findViewById(btnSecondId);
        }
        if(btnThirdId != 0){
            btnThird = view.findViewById(btnThirdId);
        }
        if(btnFirstId != 0){
            btnFourth = view.findViewById(btnFourthId);
        }
    }

    public void showSingleWPDialog(){
        wheelPickerFirst.setData(mListFirst);//给wheelPicker设置数据
        wheelPickerFirst.setDefaultItemPosition(indexFirst);//该方法的无效果
        if(indexFirst>=0&&indexFirst<mListFirst.size()){//设置默认的item
            wheelPickerFirst.setDefaultItem(mListFirst.get(indexFirst));
        }
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void showDoubleWPDialog(){
        wheelPickerFirst.setData(mListFirst);//给wheelPicker设置数据
        wheelPickerSecond.setData(mListSecond);//给wheelPicker设置数据
        wheelPickerFirst.setDefaultItemPosition(indexFirst);//该方法的无效果
        wheelPickerSecond.setDefaultItemPosition(indexSecond);
        if(indexFirst>=0&&indexFirst<mListFirst.size()){//设置默认的item
            wheelPickerFirst.setDefaultItem(mListFirst.get(indexFirst));
        }
        if(indexSecond>=0&&indexSecond<mListSecond.size()){//设置默认的item
            wheelPickerSecond.setDefaultItem(mListSecond.get(indexSecond));
        }
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
    //放全是TextView的Dialog
    public void showViewDialog(){
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void setFirstDefaultItemIndex(int index){
        this.indexFirst = index;
    }

    public void setSecondDefaultItemIndex(int index){
        this.indexSecond = index;
    }

    public WheelView getFirstWheelPicker() {
        return wheelPickerFirst;
    }

    public WheelView getSecondWheelPicker() {
        return wheelPickerSecond;
    }

    public TextView getConfirmBtn(){
        return confirmBtn;
    }

    public void setFirstData(List<String> mList){
        this.mListFirst = mList;
    }

    public void setSecondData(List<String> mList){
        this.mListSecond = mList;
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public TextView getBtnFirst() {
        return btnFirst;
    }

    public TextView getBtnSecond() {
        return btnSecond;
    }

    public TextView getBtnThird() {
        return btnThird;
    }

    public TextView getBtnFourth() {
        return btnFourth;
    }
}
