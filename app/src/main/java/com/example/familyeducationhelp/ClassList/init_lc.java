package com.example.familyeducationhelp.ClassList;

import android.app.Application;

import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;

public class init_lc extends Application {
    private static final String LC_APP_ID = "N0BgS5JiJ9uvbGAaP6CLg94G-gzGzoHsz";
    private static final String LC_APP_KEY = "oKfV8fm67mH5kbPcAtP6hGsM";
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
        AVOSCloud.initialize(this, LC_APP_ID, LC_APP_KEY);
    }
}
