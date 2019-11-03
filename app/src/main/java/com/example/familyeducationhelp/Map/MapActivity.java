package com.example.familyeducationhelp.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.example.familyeducationhelp.R;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;
    private UiSettings mUiSettings = null;
    private MyOrientationListener myOrientationListener;
    private RoutePlanSearch mSearch;
    private Button bt_check_route,bt_check_distance;
    private GeoCoder mGeoCoderA,mGeoCoderB;
    private CheckDistance mMcheckDistance = new CheckDistance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains);
        judgePermission();//6.0以后需要动态申请权限
        initMap();
        setLocation();//初始化定位
    }

    private void initMap() {
        mGeoCoderA = GeoCoder.newInstance();
        mGeoCoderB = GeoCoder.newInstance();
        mSearch = RoutePlanSearch.newInstance();
        mGeoCoderA.setOnGetGeoCodeResultListener(mMcheckDistance);
        mGeoCoderB.setOnGetGeoCodeResultListener(mMcheckDistance);

        bt_check_route = findViewById(R.id.display);
        bt_check_distance = findViewById(R.id.distance);
        bt_check_distance.setOnClickListener(this);
        bt_check_route.setOnClickListener(this);
        mMapView = findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        //隐藏百度logo
        View child = mMapView.getChildAt(1);
        if (child instanceof ImageView || child instanceof ZoomControls){
            child.setVisibility(View.GONE);
        }
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
        mSearch.setOnGetRoutePlanResultListener(new checkTransitRoute(mBaiduMap));
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
        MyLocationListener myLocationListener = new MyLocationListener(mBaiduMap,mMapView,myOrientationListener);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.display:
                PlanNode stNode = PlanNode.withCityNameAndPlaceName("成都", "四川师范大学成龙校区");
                PlanNode enNode = PlanNode.withCityNameAndPlaceName("成都", "春熙路");
                mSearch.transitSearch((new TransitRoutePlanOption())
                        .from(stNode)
                        .to(enNode)
                        .city("成都"));

                break;
            case R.id.distance:
                mGeoCoderA.geocode(new GeoCodeOption()
                        .city("成都")// 城市
                        .address("春熙路")); // 地址
                mGeoCoderB.geocode(new GeoCodeOption()
                        .city("成都")
                        .address("四川师范大学成龙校区"));
                //输出A与B之间的直线距离
                mMcheckDistance.getOnDistanceValue(new CheckDistance.OnDistance() {
                    @Override
                    public void onDistanceValue(String x) {
                        Toast.makeText(MapActivity.this,x, Toast.LENGTH_LONG).show();
                    }
                });
                break;
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
