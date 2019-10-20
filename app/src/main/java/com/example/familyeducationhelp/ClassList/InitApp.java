package com.example.familyeducationhelp.ClassList;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;

public class InitApp extends Application {
    private static final String LC_APP_ID = "N0BgS5JiJ9uvbGAaP6CLg94G-gzGzoHsz";
    private static final String LC_APP_KEY = "oKfV8fm67mH5kbPcAtP6hGsM";
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
        AVOSCloud.initialize(this, LC_APP_ID, LC_APP_KEY);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
