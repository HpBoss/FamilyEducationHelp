package com.example.familyeducationhelp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyeducationhelp.R;
import com.example.familyeducationhelp.adapter.MyAccountAdapter;
import com.example.familyeducationhelp.classList.MyAccountInformation;
import com.example.familyeducationhelp.classList.MyWheelPickerDialog;
import com.example.familyeducationhelp.classList.wheelpickerwidget.OnWheelSelectedListener;
import com.example.familyeducationhelp.classList.wheelpickerwidget.WheelView;

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
    private Uri imageUriSave;
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
    //WheelPicker
    private WheelView sexWheelPicker;
    private WheelView identityWheelPicker;
    private WheelView gradeWheelPicker;
    //List  :保存WheelPicker的数据
    private List<String> sexList;
    private List<String> identityList;
    private List<String> gradeList;
    //保存myAccountActivity的一些数据，便于在内部类中调用
    private String sexType = "";
    private String identityType = "";
    private String gradeType = "";
    //使用自定义Dialog
    private MyWheelPickerDialog identityDialog;
    private MyWheelPickerDialog sexDialog;
    private MyWheelPickerDialog iconDialog;
    //身份添加数据
    private String[][] userGrade = {{"一年级", "二年级", "三年级", "四年级", "五年级", "六年级",
            "初一", "初二", "初三", "高一", "高二", "高三"}, {""}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount_layout);
        initData();
        init_myAccount_information();

        //ListView的适配
        mRecyclerView = findViewById(R.id.myAccount_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//给每一个item添加分割线
        myAccountAdapter = new MyAccountAdapter(myAccountInformationList);
        myAccountAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(myAccountAdapter);
        myAccountAdapter.setOnItemClickListener(new MyAccountAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
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
        if (myAccountInformationList.size() == 0) {
            @SuppressLint("SdCardPath") Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Android/data/com.example.familyeducationhelp/cache/output_image.jpg");
            if (bitmap != null) {
                myAccountAdapterIcon = new MyAccountInformation("头像", bitmap, "", R.drawable.ic_navigate_next_black_24dp);
                myAccountInformationList.add(myAccountAdapterIcon);
                myAccountAdapterIcon.save();
            } else {
                myAccountAdapterIcon = new MyAccountInformation("头像", "", R.drawable.un_login_header, R.drawable.ic_navigate_next_black_24dp);
                myAccountInformationList.add(myAccountAdapterIcon);
                myAccountAdapterIcon.save();
            }
            myAccountAdapterUserName = new MyAccountInformation("用户名", null, "未设置", R.drawable.ic_navigate_next_black_24dp);
            myAccountInformationList.add(myAccountAdapterUserName);
            myAccountAdapterUserName.save();
            myAccountAdapterPassWord = new MyAccountInformation("账号密码", null, "未设置", R.drawable.ic_navigate_next_black_24dp);
            myAccountInformationList.add(myAccountAdapterPassWord);
            myAccountAdapterPassWord.save();
            myAccountAdapterSex = new MyAccountInformation("性别", null, "未设置", R.drawable.ic_navigate_next_black_24dp);
            myAccountInformationList.add(myAccountAdapterSex);
            myAccountAdapterSex.save();
            myAccountAdapterIdentity = new MyAccountInformation("身份", null, "未设置", R.drawable.ic_navigate_next_black_24dp);
            myAccountInformationList.add(myAccountAdapterIdentity);
            myAccountAdapterIdentity.save();
            myAccountAdapterPhone = new MyAccountInformation("手机号", null, "199****2664", R.drawable.ic_navigate_next_black_24dp);
            myAccountInformationList.add(myAccountAdapterPhone);
            myAccountAdapterPhone.save();
            myAccountAdapterAddress = new MyAccountInformation("地址", null, "xxx xxx xxx", R.drawable.ic_navigate_next_black_24dp);
            myAccountInformationList.add(myAccountAdapterAddress);
            myAccountAdapterAddress.save();
        } else {
            //注，一下id不能乱序
            initIconData();
            myAccountAdapterUserName = LitePal.find(MyAccountInformation.class, 2);
            myAccountAdapterPassWord = LitePal.find(MyAccountInformation.class, 3);
            myAccountAdapterSex = LitePal.find(MyAccountInformation.class, 4);
            myAccountAdapterIdentity = LitePal.find(MyAccountInformation.class, 5);
            myAccountAdapterPhone = LitePal.find(MyAccountInformation.class, 6);
            myAccountAdapterAddress = LitePal.find(MyAccountInformation.class, 7);
        }
        //直接把数据库里面的数据交给变量，避免再次进入该活动时sexType为null，导致默认选项为第一行
        sexType = myAccountAdapterSex.getAccountItemContent();
        if (myAccountAdapterIdentity.getAccountItemContent().split(" ").length == 1) {
            identityType = myAccountAdapterIdentity.getAccountItemContent().split(" ")[0];//获取字符串的前半段作为身份
        } else {
            identityType = myAccountAdapterIdentity.getAccountItemContent().split(" ")[0];//获取字符串的前半段作为身份
            gradeType = myAccountAdapterIdentity.getAccountItemContent().split(" ")[1];//获取字符串的后半段作为年级
        }
    }

    //将头像加载出来，
    //后期尽可能在此优化一下，因为每次myAccountAdapter.notifyDataSetChanged()这个办法之后，
    // 头像会消失，所以我每次改变数据后都要加载一下头像
    public void initIconData() {
        myAccountAdapterIcon = LitePal.find(MyAccountInformation.class, 1);
        @SuppressLint("SdCardPath") Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Android/data/com.example.familyeducationhelp/cache/output_image.jpg");
        if (bitmap != null) {
            myAccountAdapterIcon = new MyAccountInformation("头像", bitmap, "", R.drawable.ic_navigate_next_black_24dp);
            myAccountInformationList.set(0, myAccountAdapterIcon);
            myAccountAdapterIcon.update(1);
        } else {
            myAccountAdapterIcon = new MyAccountInformation("头像", "", R.drawable.un_login_header, R.drawable.ic_navigate_next_black_24dp);
            myAccountInformationList.set(0, myAccountAdapterIcon);
            myAccountAdapterIcon.update(1);
        }
    }

    //控件的初始化
    private void initData() {
        picture = findViewById(R.id.myAccount_image);
        myAccountInformationList = new ArrayList<>();
        myAccount_back = findViewById(R.id.image_back);
        //设置性别选择器的内容
        sexList = new ArrayList<>();
        sexList.add("男");
        sexList.add("女");
        //设置身份选择器的内容
        identityList = new ArrayList<>();
        identityList.add("学生");
        identityList.add("老师");
        //保存年级的信息，后面根据身份的信息在对其就行赋值
        gradeList = new ArrayList<>();

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
    public void showSexDialog() {
        sexDialog = new MyWheelPickerDialog(MyAccountActivity.this,
                R.layout.activity_myaccount_sex_dialog_layout, R.id.sexWheelPicker, R.id.sexConfirm);
        sexDialog.setFirstData(sexList);
        sexDialog.setFirstDefaultItemIndex(sexList.indexOf(sexType));//设置默认的item
        sexDialog.showSingleWPDialog();
        sexWheelPicker = sexDialog.getFirstWheelPicker();
        //当不触摸WheelPicker时，默认第0个item作为被选数据
        if ("".equals(sexType)) {
            sexType = sexList.get(0);
        }
        //确定按钮的监听
        TextView sexConfirm = sexDialog.getConfirmBtn();
        sexConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexType = (String) sexWheelPicker.getCurrentItem();
                myAccountAdapterSex.setAccountItemContent(sexType);
                myAccountAdapterSex.update(4);//更新数据库
                myAccountInformationList.set(3, myAccountAdapterSex);
                myAccountAdapter.notifyDataSetChanged();
                initIconData();
                sexDialog.dismiss();
            }
        });
    }

    //显示身份Dialog
    public void showIdentityDialog() {
        identityDialog = new MyWheelPickerDialog(MyAccountActivity.this,
                R.layout.activity_myaccount_identity_dialog_layout, R.id.identityWheelPicker, R.id.gradeWheelPicker, R.id.identityConfirm);
        //得到WheelPicker对象
        identityWheelPicker = identityDialog.getFirstWheelPicker();
        gradeWheelPicker = identityDialog.getSecondWheelPicker();
        //设置数据
        identityDialog.setFirstData(identityList);//给WheelPicker设置数据
        identityDialog.setSecondData(gradeList);
        //设置默认的item
        identityDialog.setFirstDefaultItemIndex(identityList.indexOf(identityType));
        identityDialog.setSecondDefaultItemIndex(gradeList.indexOf(gradeType));
        identityDialog.showDoubleWPDialog();
        //当不触摸WheelPicker时，默认第0个item作为被选数据
        if ("未设置".equals(identityType) || identityList.indexOf(identityType) == 0) {
            identityType = identityList.get(0);
            gradeWheelPicker.setData(Arrays.asList(userGrade[0]));
            gradeList.clear();
            gradeList.addAll(Arrays.asList(userGrade[0]));
            gradeType = gradeList.get(0);
            gradeWheelPicker.notifyDataSetChanged();
        }
        if (identityList.indexOf(identityType) == 1) {
            gradeWheelPicker.setData(Arrays.asList(userGrade[1]));
            gradeList.clear();
            gradeList.addAll(Arrays.asList(userGrade[1]));
        }
        //实现两级联动
        //监听事件
        identityWheelPicker.setWheelSelectedListener(new OnWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position, Object item) {
                if (position == 0) {
                    gradeWheelPicker.setData(Arrays.asList(userGrade[position]));
                    gradeWheelPicker.notifyDataSetChanged();
                } else if (position == 1) {
                    gradeWheelPicker.setData(Arrays.asList(userGrade[position]));
                    gradeWheelPicker.notifyDataSetChanged();
                }
            }
        });
        //确定按钮的监听
        TextView identityConfirm = identityDialog.getConfirmBtn();
        identityConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identityType = (String) identityWheelPicker.getCurrentItem();
                gradeType = (String) gradeWheelPicker.getCurrentItem();
                if ("".equals(gradeType)) {
                    myAccountAdapterIdentity.setAccountItemContent(identityType);
                } else {
                    myAccountAdapterIdentity.setAccountItemContent(identityType + " " + gradeType);
                }
                myAccountAdapterIdentity.update(5);//更新数据库
                myAccountInformationList.set(4, myAccountAdapterIdentity);
                myAccountAdapter.notifyDataSetChanged();
                initIconData();
                identityDialog.dismiss();
            }
        });
    }


    //显示头像Dialog
    public void showIconDialog() {
        iconDialog = new MyWheelPickerDialog(MyAccountActivity.this, R.layout.activity_myaccount_icon_dialog_layout,
                R.id.myAccount_viewIcon, R.id.myAccount_takePhoto, R.id.myAccount_takePic, R.id.myAccount_cancel);
        iconDialog.showViewDialog();
        TextView viewIconBtn = iconDialog.getBtnFirst();
        TextView takePhotoBtn = iconDialog.getBtnSecond();
        TextView takePicBtn = iconDialog.getBtnThird();
        TextView cancelBtn = iconDialog.getBtnFourth();
        //拍照
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {//若图片已存在，则删除(但用户点击拍照之后，没有成功，回来图片就成NULL了)
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //将File对象转换成Uri对象
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(MyAccountActivity.this,
                            "com.example.familyeducationhelp.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                imageUriSave = imageUri;
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//跳转到拍照页面
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//活动之间的值传递
                //拍完之后会有结果返回到onActivityResult()中
                startActivityForResult(intent, TAKE_PHOTO);
                iconDialog.dismiss();
            }
        });
        //选择照片
        takePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconDialog.dismiss();
                if (ContextCompat.checkSelfPermission(MyAccountActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MyAccountActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
        //查看图片
        viewIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //取消
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconDialog.dismiss();
            }
        });
    }

    //从相册中选择照片
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                //如果拍照成功，通过BitmapFactory将图片解析成Bitmap对象，再设置到ImageView中显示出来
                if (resultCode == RESULT_OK) {
                    try {
                        picture = findViewById(R.id.myAccount_image);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                        picture.setScaleType(ImageView.ScaleType.FIT_XY);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {//如果不加上这个判断的话，当用户点击拍照后，不拍照，直接返回，会使图片变为NULL
                    Bitmap bitmap = myAccountAdapterIcon.getAccountBitmap();
                    picture = findViewById(R.id.myAccount_image);
                    if (bitmap != null) {
                        picture.setImageBitmap(bitmap);
                    } else {
                        picture.setImageResource(R.drawable.un_login_header);
                    }
                    picture.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }


    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if (uri != null && "com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if (uri != null && "com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if (uri != null && "content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        } else if (uri != null && "file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }


    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    //得到图片路径
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
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

}
