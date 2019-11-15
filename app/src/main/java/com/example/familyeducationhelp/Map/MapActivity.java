package com.example.familyeducationhelp.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.familyeducationhelp.cardView.ChildData;
import com.example.familyeducationhelp.cardView.FatherData;
import com.example.familyeducationhelp.cardView.InformationBarAdapter;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
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
    private TextView textTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
//        judgePermission();//6.0以后需要动态申请权限
        initMap();
        setLocation();//初始化定位
        initCardView();
        setCardViewData();
        setAdapter();
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
        mExpandableListView = findViewById(R.id.expandList);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                initLocation();//获取距离
            }
        });
    }

    private void setCardViewData() {
       if (datas == null){
           datas = new ArrayList<>();
       }
       FatherData fatherData = new FatherData(R.drawable.luosheng,"Lebron",R.drawable.icon_female,"三年级","语文","80");
       ArrayList<ChildData> childDataArrayList = new ArrayList<>();
        ChildData childData = new ChildData("2019年8月9日", "一周3次，一次2小时", "人必须长得帅，有教师资格证明，具有两年家教经验",
                "武侯区中华名园二期", distance);
       childDataArrayList.add(childData);
       fatherData.setList(childDataArrayList);
       datas.add(fatherData);
    }

    private void setAdapter() {
        if (mInformationBarAdapter == null){
            mInformationBarAdapter = new InformationBarAdapter(this,datas);
            mExpandableListView.setAdapter(mInformationBarAdapter);
        }else {
            mInformationBarAdapter.flashData(datas);
        }
    }

    private void initMap() {
        mGeoCoderA = GeoCoder.newInstance();
        mGeoCoderB = GeoCoder.newInstance();
        mCheckDistance = new CheckDistance();
        mSearch = RoutePlanSearch.newInstance();
        mGeoCoderA.setOnGetGeoCodeResultListener(mCheckDistance);
        mGeoCoderB.setOnGetGeoCodeResultListener(mCheckDistance);
        mMapView = findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        textTitle = findViewById(R.id.text_employment);
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
        mBaiduMap.setMyLocationConfiguration(mConfiguration);
        mMapView.showScaleControl(false);//关闭比例尺
        mBaiduMap.setMyLocationEnabled(true);//打开图层
        mSearch.setOnGetRoutePlanResultListener(new CheckTransitRoute(mBaiduMap));
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
        MyLocationListener myLocationListener = new MyLocationListener(mBaiduMap,mMapView,myOrientationListener,mSearch);
        //注册LocationListener监听器
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();
    }
    //6.0之后要动态获取权限，重要！！！
    protected void judgePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            // 检查该权限是否已经获取
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            List<String> mPermissionList = new ArrayList<>();
            mPermissionList.clear();
            for (String power : permissions
                 ) {
                if (ContextCompat.checkSelfPermission(this,power) != PackageManager.PERMISSION_GRANTED){
                    mPermissionList.add(power);
                }
            }
            if (mPermissionList.size() >0){
                ActivityCompat.requestPermissions(this, permissions, 100);
            }
        }else{
            //做任何需要满足所有权限才能做的事情
        }
    }
    @Override
    protected void onStart() {
        myOrientationListener.star();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mBaiduMap.setMyLocationEnabled(false);
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
        mSearch.destroy();
        mGeoCoderB.destroy();
        mGeoCoderA.destroy();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}
