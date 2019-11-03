package com.example.familyeducationhelp.Map;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;

import java.text.DecimalFormat;

public class CheckDistance implements OnGetGeoCoderResultListener {
    private int i = 0;
    private LatLng startNode = null,endNode = null;
    private OnDistance mOnDistance = null;
    public interface OnDistance{
        void onDistanceValue(String x);
//        void onAddress(String location);
    }
    public void getOnDistanceValue(OnDistance mOnDistance){
        this.mOnDistance = mOnDistance;
    }
    //地理编码
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        i++;
        if (i/2 != 0){
            startNode = geoCodeResult.getLocation();
        }else {
            endNode = geoCodeResult.getLocation();
        }
        if (startNode != null && endNode != null){
            //计算“直线”距离
            Double metre,kilometre;
            metre = DistanceUtil.getDistance(startNode, endNode);
            kilometre = metre/1000;
            DecimalFormat df = new DecimalFormat(".00");
            String distance = String.valueOf(df.format(kilometre));
            String finalDistance = "全程" + distance + "公里";
            mOnDistance.onDistanceValue(finalDistance);
        }
    }
    //反地理编码
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
//        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
//            return;
//        }
//        mOnDistance.onAddress(reverseGeoCodeResult.getAddress());
    }
}
