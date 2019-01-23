package com.linyang.pay;

import android.app.Application;

/**
 * 描述:APP入口
 * Created by fzJiang on 2018-10-11
 */
public class MyApplication extends Application {

    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
