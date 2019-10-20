package com.example.familyeducationhelp.Map;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MyLocationListener extends BDAbstractLocationListener {
    private MyLocationData locData;
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private float mLastX;
    private boolean isFirstLoc = true;
    private MyOrientationListener mMyOrientationListener;
    public MyLocationListener(BaiduMap map, MapView mapView, MyOrientationListener mMyOrientationListener){
        this.mBaiduMap = map;
        this.mMapView = mapView;
        this.mMyOrientationListener = mMyOrientationListener;
    }

        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不再处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            mMyOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {//重写接口，从而在当前类中实现方向传感器
                @Override
                public void onOrientationChanged(float x) {
                    mLastX = x;//赋值获取到的方向度数。
                }
            });
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius()).speed(1000)
                    // 此处设置开发者获取到的方向信息，顺时针0-360度
                    .direction(mLastX).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {//判断是否是第一次加载地图
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());//经纬度设置
                float f = mBaiduMap.getMaxZoomLevel();// 18.0
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,
                        f - 3);
                mBaiduMap.animateMapStatus(u);
            }
        }
    }