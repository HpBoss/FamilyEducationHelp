package com.example.familyeducationhelp.application;

import android.app.Activity;
import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import org.litepal.LitePal;

import java.util.Locale;

import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.AutoSizeLog;

public class BaseApplication extends Application {
    private static final String LC_APP_ID = "N0BgS5JiJ9uvbGAaP6CLg94G-gzGzoHsz";
    private static final String LC_APP_KEY = "oKfV8fm67mH5kbPcAtP6hGsM";
    private static final String LC_APP_serverURL = "https://n0bgs5ji.lc-cn-n1-shared.com";

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
        AVOSCloud.initialize(this, LC_APP_ID, LC_APP_KEY, LC_APP_serverURL);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        LitePal.initialize(this);

        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance().setCustomFragment(true)
                .setOnAdaptListener(new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {
                        //使用以下代码, 可以解决横竖屏切换时的屏幕适配问题
                        //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                        //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
//                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
//                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                        AutoSizeLog.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target.getClass().getName()));
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {
                        AutoSizeLog.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target.getClass().getName()));
                    }
                });
    }
}
