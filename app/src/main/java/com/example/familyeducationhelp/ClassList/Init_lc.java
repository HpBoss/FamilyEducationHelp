package com.example.familyeducationhelp.ClassList;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;

public class Init_lc extends LitePalApplication {
    private static final String LC_APP_ID = "N0BgS5JiJ9uvbGAaP6CLg94G-gzGzoHsz";
    private static final String LC_APP_KEY = "oKfV8fm67mH5kbPcAtP6hGsM";
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
        AVOSCloud.initialize(this, LC_APP_ID, LC_APP_KEY);
        //初始化数据库
        LitePal.initialize(this);
    }
}
