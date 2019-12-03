package com.example.familyeducationhelp.map;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.example.familyeducationhelp.R;
import com.example.familyeducationhelp.activity.BaseActivity;
import com.example.familyeducationhelp.activity.MainActivity;
import com.example.familyeducationhelp.mapCardView.ChildData;
import com.example.familyeducationhelp.mapCardView.FatherData;
import com.example.familyeducationhelp.mapCardView.InformationBarAdapter;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class MapActivity extends BaseActivity {
    private MapView mMapView = null;
    private BaiduMap mBaiDuMap = null;
    private LocationClient mLocationClient = null;
    private UiSettings mUiSettings = null;
    private MyOrientationListener myOrientationListener;
    private RoutePlanSearch mSearch;
    private CheckDistance mCheckDistance;
    private GeoCoder mGeoCoderA,mGeoCoderB;
    private InformationBarAdapter mInformationBarAdapter;
    private ExpandableListView mExpandableListView;
    private ArrayList<FatherData> datas;
    private MyString distance = new MyString("0");//初始化MyString
    private boolean isFirstAdd = true;
    private Intent intent;
    private ImageView ivLocation;
    private MaterialCardView mMaterialCardView;
    private boolean isOpen = false;
    private ImageView expand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setStatusBar(this,true,true);
//        Debug.startMethodTracing("map");
        initMap();
        setLocation();//初始化定位
        initCardView();
//        setCardViewData();
//        setAdapter();
//        Debug.stopMethodTracing();
    }
    private void initLocation() {
        mGeoCoderA.geocode(new GeoCodeOption()
                .city("成都")// 城市
                .address("龙泉驿万达广场")); // 地址
        mGeoCoderB.geocode(new GeoCodeOption()
                .city("成都")
                .address("四川师范大学成龙校区"));
        mCheckDistance.getOnDistanceValue(new CheckDistance.OnDistance() {
            @Override
            public void onDistanceValue(String x) {
                if (isFirstAdd){
                    distance.setStr(x);
                    setAdapter();//distance属于datas的数据源，当获取他的值时，需要再次向Adapter传送数据
                    isFirstAdd = false;
                }
            }
        });
    }

    private void initCardView() {
        mMaterialCardView = findViewById(R.id.cardView);
        ImageView btBack = findViewById(R.id.image_back);
//        mExpandableListView = findViewById(R.id.expandList);
//        mExpandableListView.setGroupIndicator(null);
//        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            @Override
//            public void onGroupExpand(int i) {
//                initLocation();//获取距离
//            }
//        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                /**
                 * overridePendingTransition(anim.1,anim.2);
                 * anim.1是后者进入动画
                 * anim.2是前者出去动画
                 */
                overridePendingTransition(R.anim.translate_left_in,R.anim.translate_right_out);
                finish();
            }
        });
        expand = findViewById(R.id.expand);
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand.setImageResource(R.drawable.ic_expand_more_black_32dp);
//                ConstraintLayout.LayoutParams linearParams =(ConstraintLayout.LayoutParams) mMaterialCardView.getLayoutParams();
//                linearParams.height= ViewGroup.LayoutParams.WRAP_CONTENT;
//                final float scale = getResources().getDisplayMetrics().density;
//                ConstraintLayout childLayout = findViewById(R.id.childLayout);
                int mixHeight = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics()));
                int maxHeight = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 235, getResources().getDisplayMetrics()));
//                Log.d("height",String.valueOf(maxHeight));
                if (!isOpen){
//                    mMaterialCardView.setLayoutParams(linearParams);
                    foldAnimator(mixHeight,maxHeight);
                    expand.setImageResource(R.drawable.ic_expand_more_black_32dp);
                    isOpen = true;
                }else {
                    //dp高度设置方法
                    foldAnimator(maxHeight,mixHeight);
                    expand.setImageResource(R.drawable.ic_expand_less_black_32dp);
                    isOpen = false;
                }
            }
        });
    }
    private void foldAnimator(int startHeight,int endAnimator){
        ValueAnimator va = ValueAnimator.ofInt(startHeight,endAnimator);
        va.setInterpolator(new FastOutLinearInInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mMaterialCardView.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
                mMaterialCardView.requestLayout();
            }
        });
        va.setDuration(300);
        va.start();
    }
    private void setCardViewData() {
       if (datas == null){
           datas = new ArrayList<>();
       }
       FatherData fatherData = new FatherData(R.drawable.luosheng,"LeBron",R.drawable.icon_female,"三年级","语文","80");
       ArrayList<ChildData> childDataArrayList = new ArrayList<>();
        ChildData childData = new ChildData("2019年8月9日", "一周3次，一次2小时", "人必须长得帅，有教师资格证明，具有两年家教经验",
                "武侯区中华名园二期", distance);
       childDataArrayList.add(childData);
       fatherData.setList(childDataArrayList);
       datas.add(fatherData);
    }

    private void setAdapter() {
//        if (mInformationBarAdapter == null){
//            mInformationBarAdapter = new InformationBarAdapter(this,datas);
//            mExpandableListView.setAdapter(mInformationBarAdapter);
//        }else {
//            mInformationBarAdapter.flashData(datas);
//        }

    }

    private void initMap() {
        mGeoCoderA = GeoCoder.newInstance();
        mGeoCoderB = GeoCoder.newInstance();
        mCheckDistance = new CheckDistance();
        ivLocation = findViewById(R.id.ivLocation);
//        mSearch = RoutePlanSearch.newInstance();
//        mSearch.setOnGetRoutePlanResultListener(new CheckTransitRoute(mBaiDuMap));

        mGeoCoderA.setOnGetGeoCodeResultListener(mCheckDistance);
        mGeoCoderB.setOnGetGeoCodeResultListener(mCheckDistance);
        mMapView = findViewById(R.id.mapView);
        mMapView.setVisibility(View.GONE);
        mBaiDuMap = mMapView.getMap();
        mUiSettings = mBaiDuMap.getUiSettings();
        TextView textTitle = findViewById(R.id.text_employment);
        textTitle.setText("个人信息");
        //隐藏百度logo
        View child = mMapView.getChildAt(1);
        if (child instanceof ImageView || child instanceof ZoomControls){
            child.setVisibility(View.GONE);
        }
        //设置地图缩放按钮位置
        mMapView.getChildAt(2).setPadding(0,0,8,300);
        /*
        MyLocationConfiguration
        NO.1：设置地图定位模式
        NO.2：是否开启定位方向
        NO.3：设置定位精度圈填充颜色
         */
        MyLocationConfiguration mConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,null);
        //        mConfiguration.accuracyCircleFillColor = 0xAAFFFF88;//设置精度全填充颜色
        mBaiDuMap.setMyLocationConfiguration(mConfiguration);
        mMapView.showScaleControl(false);//关闭比例尺
        mBaiDuMap.setMyLocationEnabled(true);//打开图层
        //方向传感器
        myOrientationListener = new MyOrientationListener(this);
    }
    private void setLocation() {
        //定位初始化
        mLocationClient = new LocationClient(getApplicationContext());
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//必须大于一秒（1000毫秒）
        //设置locationClientOption
        mLocationClient.setLocOption(option);
        MyLocationListener myLocationListener = new MyLocationListener(mBaiDuMap, mMapView, myOrientationListener, mSearch, ivLocation);
        //注册LocationListener监听器
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();
        mLocationClient.requestLocation();//图片点击事件，回到定位点
    }
    @Override
    protected void onStart() {
        myOrientationListener.star();
        super.onStart();
    }

    @Override
    protected void onStop() {
//        mBaiDuMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        // 关闭方向传感器
        myOrientationListener.stop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
           super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
//        mSearch.destroy();
        mGeoCoderB.destroy();
        mGeoCoderA.destroy();
        mBaiDuMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}
