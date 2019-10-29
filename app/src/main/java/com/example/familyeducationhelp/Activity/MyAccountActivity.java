package com.example.familyeducationhelp.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.familyeducationhelp.Adapter.GradeAdapter;
import com.example.familyeducationhelp.Adapter.IdentityAdapter;
import com.example.familyeducationhelp.Adapter.MyAccountAdapter;
import com.example.familyeducationhelp.Adapter.SexAdapter;
import com.example.familyeducationhelp.ClassList.MyAccountInformation;
import com.example.familyeducationhelp.R;
import com.example.familyeducationhelp.widget.WheelView;


import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyAccountActivity extends BaseActivity {

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView picture;
    private Uri imageUri;
    //MyAccountInformation属性
    private MyAccountInformation myAccountAdapterIcon;
    private MyAccountInformation myAccountAdapterSex;
    private MyAccountInformation myAccountAdapterIdentity;
    private MyAccountInformation myAccountAdapterUserName;
    private MyAccountInformation myAccountAdapterPassWord;
    private MyAccountInformation myAccountAdapterPhone;
    private MyAccountInformation myAccountAdapterAddress;
    private MyAccountAdapter myAccountAdapter;
    private ImageView myAccount_back;
    private List<MyAccountInformation> myAccountInformationList;
    private RecyclerView mRecyclerView;
    private WheelView sexWheelPicker;
    private WheelView identityWheelPicker;
    private WheelView gradeWheelPicker;
    private List<String> sexList;
    //保存myAccountActivity的一些数据，便于在内部类中调用
    private String sexType = "";
    private String identityType = "";
    private String gradeType = "";
    //适配器
    private IdentityAdapter mIdentityAdapter;
    private GradeAdapter mGradeAdapter;
    //身份添加数据
    private String[][] userGrade = {{"一年级","二年级","三年级","四年级","五年级","六年级",
            "初一","初二","初三","高一","高二","高三"},{""}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount_layout);
        addStatusViewWithColor(this, getResources().getColor(R.color.colorMainBlue));
        initData();
        init_myAccount_information();

        //ListView的适配
        mRecyclerView = findViewById(R.id.myAccount_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));//给每一个item添加分割线
        myAccountAdapter = new MyAccountAdapter(myAccountInformationList);
        myAccountAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(myAccountAdapter);
        myAccountAdapter.setOnItemClickListener(new MyAccountAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        showIconDialog();
                        break;
                    case 3:
                        showSexDialog();
                        break;
                    case 4:
                        showIdentityDialog();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void init_myAccount_information() {
        //得到数据库
        myAccountInformationList = LitePal.findAll(MyAccountInformation.class);//把数据库中的数据读出来

        //判断是否已经把初始数据保存在了数据库
        if (myAccountInformationList.size() == 0){
            Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Android/data/com.example.familyeducationhelp/cache/output_image.jpg");
            if (bitmap != null) {
                myAccountAdapterIcon = new MyAccountInformation("头像",bitmap,"", R.drawable.next);
                myAccountInformationList.add(myAccountAdapterIcon);
                myAccountAdapterIcon.save();
            }else {
                myAccountAdapterIcon = new MyAccountInformation("头像","", R.drawable.unlogin_header, R.drawable.next);
                myAccountInformationList.add(myAccountAdapterIcon);
                myAccountAdapterIcon.save();
            }
            myAccountAdapterUserName = new MyAccountInformation("用户名",null,"未设置", R.drawable.next);
            myAccountInformationList.add(myAccountAdapterUserName);
            myAccountAdapterUserName.save();
            myAccountAdapterPassWord = new MyAccountInformation("账号密码",null,"未设置", R.drawable.next);
            myAccountInformationList.add(myAccountAdapterPassWord);
            myAccountAdapterPassWord.save();
            myAccountAdapterSex = new MyAccountInformation("性别",null,"未设置", R.drawable.next);
            myAccountInformationList.add(myAccountAdapterSex);
            myAccountAdapterSex.save();
            myAccountAdapterIdentity = new MyAccountInformation("身份",null,"未设置", R.drawable.next);
            myAccountInformationList.add(myAccountAdapterIdentity);
            myAccountAdapterIdentity.save();
            myAccountAdapterPhone = new MyAccountInformation("手机号",null,"199****2664", R.drawable.next);
            myAccountInformationList.add(myAccountAdapterPhone);
            myAccountAdapterPhone.save();
            myAccountAdapterAddress = new MyAccountInformation("地址",null,"xxx xxx xxx", R.drawable.next);
            myAccountInformationList.add(myAccountAdapterAddress);
            myAccountAdapterAddress.save();
        }else{
            //注，一下id不能乱序
            myAccountAdapterIcon = LitePal.find(MyAccountInformation.class,1);
            Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Android/data/com.example.familyeducationhelp/cache/output_image.jpg");
            if (bitmap != null) {
                MyAccountInformation myAccountAdapterIcon_temp = new MyAccountInformation("头像",bitmap,"", R.drawable.next);
                myAccountAdapterIcon = myAccountAdapterIcon_temp;
                myAccountInformationList.set(0,myAccountAdapterIcon);
                myAccountAdapterIcon.update(1);
            }else {
                MyAccountInformation myAccountAdapterIcon_temp = new MyAccountInformation("头像","", R.drawable.unlogin_header, R.drawable.next);
                myAccountAdapterIcon = myAccountAdapterIcon_temp;
                myAccountInformationList.set(0,myAccountAdapterIcon);
                myAccountAdapterIcon.update(1);
            }
            myAccountAdapterUserName = LitePal.find(MyAccountInformation.class,2);
            myAccountAdapterPassWord = LitePal.find(MyAccountInformation.class,3);
            myAccountAdapterSex = LitePal.find(MyAccountInformation.class,4);
            myAccountAdapterIdentity = LitePal.find(MyAccountInformation.class,5);
            myAccountAdapterPhone = LitePal.find(MyAccountInformation.class,6);
            myAccountAdapterAddress = LitePal.find(MyAccountInformation.class,7);
        }
        //直接把数据库里面的数据交给变量，避免再次进入该活动时sexType为null，导致默认选项为第一行
        sexType = myAccountAdapterSex.getAccountItemContent();
        if (myAccountAdapterIdentity.getAccountItemContent().split(" ").length == 1){
            identityType = myAccountAdapterIdentity.getAccountItemContent().split(" ")[0];//获取字符串的前半段作为身份
        }else{
            identityType = myAccountAdapterIdentity.getAccountItemContent().split(" ")[0];//获取字符串的前半段作为身份
            gradeType = myAccountAdapterIdentity.getAccountItemContent().split(" ")[1];//获取字符串的后半段作为年级
        }
    }

    //控件的初始化
    private void initData() {
        picture = findViewById(R.id.myAccount_image);
        myAccountInformationList = new ArrayList<>();
        myAccount_back = findViewById(R.id.myAccount_back);

        //返回MainActivity，将MainActivity设置成立singleTask，使返回的时候MainActivity依然是跳转前的状态
        myAccount_back.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.translate_left_in, R.anim.translate_right_out);
                finish();
            }
        });

    }

    //显示性别Dialog
    public void showSexDialog(){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.myaccount_sex_dialog_layout,null);
        sexWheelPicker = view.findViewById(R.id.sexWheelPicker);

        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置适配器
        final SexAdapter mSexAdapter = new SexAdapter();
        sexWheelPicker.setAdapter(mSexAdapter);
        dialog.show();
        //设置到当前选项
        int curIndexSex = mSexAdapter.sexList.indexOf(sexType);
        sexWheelPicker.setCurrentItem(curIndexSex);
        //用户点开Dialog后不点击的默认事件
        if ("未设置".equals(sexType)){
            sexType = mSexAdapter.sexList.get(0);
        }
        //sexWheelPicker监听事件
        sexWheelPicker.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                sexType = mSexAdapter.getItem(index);
            }
        });
        //确定按钮的监听
        dialog.findViewById(R.id.sexConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAccountAdapterSex.setAccountItemContent(sexType);
                /*if (!sexType.equals(myAccountAdapterSex.getAccountItemContent())){
                    myAccountAdapterSex.update(4);//更新数据库
                    myAccountAdapter.notifyDataSetChanged();
                }*/
                myAccountAdapterSex.update(4);//更新数据库
                myAccountInformationList.set(3,myAccountAdapterSex);
                myAccountAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }

    //显示身份Dialog
    public void showIdentityDialog(){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.myaccount_identity_dialog_layout,null);
        identityWheelPicker = view.findViewById(R.id.identityWheelPicker);
        gradeWheelPicker = view.findViewById(R.id.gradeWheelPicker);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mIdentityAdapter = new IdentityAdapter();
        mGradeAdapter = new GradeAdapter(0);
        //给WheelPicker设置Adapter，不然无法加载数据
        identityWheelPicker.setAdapter(mIdentityAdapter);
        gradeWheelPicker.setAdapter(mGradeAdapter);
        dialog.show();
        //当用户选择之后，下次打开时默认跳到那一项
        if(!"未设置".equals(identityType)){
            int curIndexIdentity = mIdentityAdapter.identityList.indexOf(identityType);
            int curIndexGrade = mGradeAdapter.gradeList.indexOf(gradeType);
            List<String> grade = Arrays.asList(userGrade[curIndexIdentity]);//当前身份
            mGradeAdapter.gradeList = new ArrayList<>();
            mGradeAdapter.gradeList.addAll(grade);
            mGradeAdapter.notifyDataSetChanged();
            identityWheelPicker.setCurrentItem(curIndexIdentity);
            gradeWheelPicker.setCurrentItem(curIndexGrade);
        }
        //当用户点出Dialog后，没有在Dialog上发生点击事件时，默认选择为第0项
        if ("未设置".equals(identityType)){
            identityType = mIdentityAdapter.identityList.get(0);
        }
        if ("".equals(gradeType)){
            gradeType = mGradeAdapter.gradeList.get(0);
        }
        //监听事件
        identityWheelPicker.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                List<String> grade = Arrays.asList(userGrade[index]);
                mGradeAdapter.gradeList = new ArrayList<>();
                mGradeAdapter.gradeList.addAll(grade);
                mGradeAdapter.notifyDataSetChanged();
                gradeWheelPicker.setCurrentItem(0);
                gradeType = mGradeAdapter.gradeList.get(0);//身份改变后，默认将年级改成第0个数据
                identityType = mIdentityAdapter.identityList.get(index);
            }
        });

        gradeWheelPicker.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                gradeType = mGradeAdapter.gradeList.get(index);
            }
        });

        dialog.findViewById(R.id.identityConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(gradeType)){
                    myAccountAdapterIdentity.setAccountItemContent(identityType);
                }else{
                    myAccountAdapterIdentity.setAccountItemContent(identityType+" "+gradeType);
                }
                /*if (!gradeType.equals(myAccountAdapterIdentity.getAccountItemContent())||
                        "未设置".equals(myAccountAdapterIdentity.getAccountItemContent())){
                    myAccountAdapterIdentity.update(5);//更新数据库
                    myAccountAdapter.notifyDataSetChanged();
                }*/
                myAccountAdapterIdentity.update(5);//更新数据库
                myAccountInformationList.set(4,myAccountAdapterIdentity);
                myAccountAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }


    //显示头像Dialog
    public void showIconDialog(){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.myaccount_icon_dialog_layout,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.myAccount_takePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.exists()){//若图片已存在，则删除
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //将File对象转换成Uri对象
                if (Build.VERSION.SDK_INT >= 24){
                    imageUri = FileProvider.getUriForFile(MyAccountActivity.this,
                            "com.example.familyeducationhelp.fileprovider",outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//跳转到拍照页面
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);//活动之间的值传递
                //拍完之后会有结果返回到onActivityResult()中
                startActivityForResult(intent,TAKE_PHOTO);
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.myAccount_takePic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(ContextCompat.checkSelfPermission(MyAccountActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MyAccountActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });

        dialog.findViewById(R.id.myAccount_viewIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dialog.findViewById(R.id.myAccount_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    //从相册中选择照片
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case TAKE_PHOTO:
                //如果拍照成功，通过BitmapFactory将图片解析成Bitmap对象，再设置到ImageView中显示出来
                if(resultCode == RESULT_OK){
                    try{
                        picture = findViewById(R.id.myAccount_image);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                        picture.setScaleType(ImageView.ScaleType.FIT_XY);
                        /*myAccountAdapterIcon.setAccountBitmap(bitmap);
                        myAccountAdapterIcon.update(1);//更新数据库
                        myAccountInformationList.set(0,myAccountAdapterIcon);
                        myAccountAdapter.notifyDataSetChanged();*/
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19 ){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }


    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }



    private  void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    //得到图片路径
    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)) ;
            }
            cursor.close();
        }
        return path;
    }

    //这是选择图片功能中的“展示图片代码”——显示图片
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
            picture.setScaleType(ImageView.ScaleType.FIT_XY);

        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_LONG).show();
        }
    }

    public ImageView getPicture() {
        return picture;
    }

    public void setPicture(ImageView picture) {
        this.picture = picture;
    }

}
