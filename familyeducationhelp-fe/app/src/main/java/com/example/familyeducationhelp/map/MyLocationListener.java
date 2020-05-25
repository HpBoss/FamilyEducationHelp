package com.example.familyeducationhelp.map;

import android.view.View;
import android.widget.ImageView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;

public class MyLocationListener extends BDAbstractLocationListener {
    private BaiduMap mBaiDuMap;
    private MapView mMapView;
    private float mLastX;
    private LatLng ll;
    private boolean isFirstLoc = true;
    private MyOrientationListener mMyOrientationListener;
    private RoutePlanSearch mSearch;
    private ImageView ivLocation;

    MyLocationListener(BaiduMap map, MapView mapView, MyOrientationListener mMyOrientationListener, RoutePlanSearch search, ImageView imageView) {
        this.mBaiDuMap = map;
        this.mMapView = mapView;
        this.mMyOrientationListener = mMyOrientationListener;
        this.mSearch = search;
        ivLocation = imageView;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        //mapView 销毁后不再处理新接收的位置
        if (location == null || mMapView == null) {
            return;
        }
        mMyOrientationListener.setmOnOrientationListener(new MyOrientationListener.OnOrientationListener() {//重写接口，从而在当前类中实现方向传感器
            @Override
            public void onOrientationChanged(float x) {
                mLastX = x;//赋值获取到的方向度数。
            }
        });
        // 此处设置开发者获取到的方向信息，顺时针0-360度
        MyLocationData localData = new MyLocationData.Builder()
                .accuracy(location.getRadius()).speed(1000)
                // 此处设置开发者获取到的方向信息，顺时针0-360度
                .direction(mLastX).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiDuMap.setMyLocationData(localData);
        //回到移动之前的位置
        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //把定位点再次显现出来
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(ll);
                mBaiDuMap.animateMapStatus(mapStatusUpdate);
            }
        });
        if (isFirstLoc) {//判断是否是第一次加载地图
            isFirstLoc = false;
            ll = new LatLng(location.getLatitude(),
                    location.getLongitude());//经纬度设置
            float f = mBaiDuMap.getMaxZoomLevel();// 18.0
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,
                    f - 3);
            mBaiDuMap.animateMapStatus(u);
            mMapView.setVisibility(View.VISIBLE);//等待地图数据加载完成后，再显示MapView
            //规划路线
            PlanNode stNode = PlanNode.withCityNameAndPlaceName("成都", "四川师范大学成龙校区");
            PlanNode enNode = PlanNode.withCityNameAndPlaceName("成都", "龙泉驿万达广场");
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode)
                    .to(enNode)
                    .city("成都"));
        }
    }
}