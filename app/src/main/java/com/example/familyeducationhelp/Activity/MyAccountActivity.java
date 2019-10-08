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
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyeducationhelp.Adapter.MyAccountAdapter;
import com.example.familyeducationhelp.ClassList.MyAccountInformation;
import com.example.familyeducationhelp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyAccountActivity extends BaseActivity{

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView picture;
    private Uri imageUri;
    private ImageView myAccount_back;
    private List<MyAccountInformation> myAccountInformationList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TextView text_title;
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
        MyAccountAdapter myAccountAdapter = new MyAccountAdapter(myAccountInformationList);
        mRecyclerView.setAdapter(myAccountAdapter);
        myAccountAdapter.setOnItemClickListener(new MyAccountAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position==0){
                    showBottomDialog();
                }
            }
        });
    }

    private void init_myAccount_information() {
        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Android/data/com.example.familyeducationhelp/cache/output_image.jpg");
        if (bitmap != null) {
            MyAccountInformation myAccountAdapter0 = new MyAccountInformation("头像",bitmap,"",R.drawable.next);
            myAccountInformationList.add(myAccountAdapter0);
        }else {
            MyAccountInformation myAccountAdapter1 = new MyAccountInformation("头像","",R.drawable.un_login_header,R.drawable.next);
            myAccountInformationList.add(myAccountAdapter1);
        }
        MyAccountInformation myAccountAdapter2 = new MyAccountInformation("用户名",null,"欧巴兽兽",R.drawable.next);
        myAccountInformationList.add(myAccountAdapter2);
        MyAccountInformation myAccountAdapter3 = new MyAccountInformation("账号密码",null,"未设置",R.drawable.next);
        myAccountInformationList.add(myAccountAdapter3);
        MyAccountInformation myAccountAdapter4 = new MyAccountInformation("手机号",null,"199****2664",R.drawable.next);
        myAccountInformationList.add(myAccountAdapter4);
        MyAccountInformation myAccountAdapter5 = new MyAccountInformation("地址",null,"xxx xxx xxx",R.drawable.next);
        myAccountInformationList.add(myAccountAdapter5);
    }

    //控件的初始化
    private void initData() {
        text_title = findViewById(R.id.text_employment);
        text_title.setText("我的账号");
        picture = findViewById(R.id.myAccount_image);
        myAccountInformationList = new ArrayList<>();
        myAccount_back = findViewById(R.id.image_back);
        //返回MainActivity，将MainActivity设置成立singleTask，使返回的时候MainActivity依然是跳转前的状态
        myAccount_back.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.translate_left_in, R.anim.translate_right_out);
                finish();
            }
        });
    }


    //显示Dialog
    public void showBottomDialog(){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this,R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this,R.layout.myaccount_dialog_layout,null);
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
                    if(outputImage.exists()){
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
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
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
//                        Log.d(TAG, "onActivityResult: "+getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                        picture.setScaleType(ImageView.ScaleType.FIT_XY);
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

    //显示图片
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
