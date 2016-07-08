package com.hqucsx.rxjavatest;/**
 * Created by PCPC on 2016/6/27.
 */

import android.app.Application;

import com.github.mmin18.layoutcast.LayoutCast;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * 描述: TODO
 * 名称: RxApplication
 * User: csx
 * Date: 06-27
 */
public class RxApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger
                .init("哈哈哈哈")                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(LogLevel.FULL);        // default LogLevel.FULL
        super.onCreate();

        if (BuildConfig.DEBUG) {
            LayoutCast.init(this);
        }
    }
}
