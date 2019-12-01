package com.example.familyeducationhelp.map;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

public class CheckTransitRoute extends TransitRoutePlanOption implements OnGetRoutePlanResultListener {
    private BaiduMap mBaiduMap;

    public CheckTransitRoute(BaiduMap map){
        mBaiduMap = map;
    }
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        //创建TransitRouteOverlay实例
        TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
        //获取路径规划数据,(以返回的第一条数据为例)
        //为TransitRouteOverlay实例设置路径数据
        if (transitRouteResult.getRouteLines().size() > 0) {
            overlay.setData(transitRouteResult.getRouteLines().get(0));
            //在地图上绘制TransitRouteOverlay
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }
}
