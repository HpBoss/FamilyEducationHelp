package com.example.familyeducationhelp.map;

import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CheckTransitRoute extends TransitRoutePlanOption implements OnGetRoutePlanResultListener {
    private BaiduMap mBaiDuMap;
    private FloatingActionButton mFab;
    private TransitRouteOverlay mOverlay;

    public CheckTransitRoute(BaiduMap map, FloatingActionButton fab) {
        mBaiDuMap = map;
        mFab = fab;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        //创建TransitRouteOverlay实例
        mOverlay = new TransitRouteOverlay(mBaiDuMap);
        //获取路径规划数据,(以返回的第一条数据为例)
        //为TransitRouteOverlay实例设置路径数据
        if (transitRouteResult.getRouteLines().size() > 0) {
            mOverlay.setData(transitRouteResult.getRouteLines().get(0));
            //在地图上绘制TransitRouteOverlay
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOverlay.addToMap();
                    mOverlay.zoomToSpan();
                }
            });
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
