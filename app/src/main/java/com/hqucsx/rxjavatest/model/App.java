package com.hqucsx.rxjavatest.model;/**
 * Created by PCPC on 2016/7/11.
 */

import android.graphics.drawable.Drawable;

/**
 * 描述: TODO
 * 名称: App
 * User: csx
 * Date: 07-11
 */
public class App {
    private String appName;
    private String versionName;
    private int versionCode;
    private Drawable appIcon;
    private String appPackageName;

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
