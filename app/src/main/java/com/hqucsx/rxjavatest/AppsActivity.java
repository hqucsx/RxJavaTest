package com.hqucsx.rxjavatest;/**
 * Created by PCPC on 2016/7/11.
 */

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hqucsx.rxjavatest.adapter.AppAdapter;
import com.hqucsx.rxjavatest.model.App;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * 描述: 已安装应用列表
 * 名称: AppsActivity
 * User: csx
 * Date: 07-11
 */
public class AppsActivity extends AppCompatActivity {
    @BindView(R.id.iv_poster)
    ImageView mIvPoster;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ctl)
    CollapsingToolbarLayout mCtl;
    @BindView(R.id.appBar)
    AppBarLayout mAppBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    ArrayList<App> mApps = new ArrayList<>();
    ArrayList<App> resultApps = new ArrayList<>();
    List<PackageInfo> mPackageInfoList = new ArrayList<>();
    PackageManager mPackageManager;
    AppAdapter mAppAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCtl.setTitle("Apps");
        Glide.with(this)
                .load("http://img.popodd.com/20160627165300208")
                .into(mIvPoster);

        mPackageManager = getPackageManager();
        mAppAdapter = new AppAdapter(mApps);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        showApps();
    }

    /**
     * Observable.merge(Observable,Observable)
     * merge()方法将两个甚至更多的Observable合并发射
     * Observable.zip(Observable,Observable,Func2)
     * zip()方法合并两个甚至更多的Observables发射出的数据，根据指定函数Func*变换他们，并发射一个新值
     *
     */
    private void showApps() {
        /**
         * Test zip
         */
//        Observable<App> observableApp = Observable.from(getApps());
//        Observable<Long> tictoc = Observable.interval(1, TimeUnit.SECONDS);
//        Observable.zip(observableApp, tictoc, new Func2<App, Long, App>() {
//            @Override
//            public App call(App app, Long aLong) {
//                app.setAppName(app.getAppName() + "" + aLong);
//                return app;
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .filter(new Func1<App, Boolean>() {
//                    @Override
//                    public Boolean call(App app) {
//                        return true;
//                    }
//                })
//                .take(5)
//
//                .subscribe(new Subscriber<App>() {
//                    @Override
//                    public void onCompleted() {
//                        mAppAdapter.setNewData(resultApps);
//                        mRecyclerView.setAdapter(mAppAdapter);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(App app) {
//                        resultApps.add(app);
//                    }
//                });
        Observable<App> observableApp = Observable.from(getApps());
        Observable<Long> tictoc = Observable.interval(1, TimeUnit.SECONDS);
        Observable.zip(observableApp, tictoc, new Func2<App, Long, App>() {
            @Override
            public App call(App app, Long aLong) {
                app.setAppName(app.getAppName() + "" + aLong);
                return app;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<App, Boolean>() {
                    @Override
                    public Boolean call(App app) {
                        return true;
                    }
                })
                .take(5)

                .subscribe(new Subscriber<App>() {
                    @Override
                    public void onCompleted() {
                        mAppAdapter.setNewData(resultApps);
                        mRecyclerView.setAdapter(mAppAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(App app) {
                        resultApps.add(app);
                    }
                });

    }

    private ArrayList<App> getApps() {
        mPackageInfoList = mPackageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : mPackageInfoList) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {//非系统应用
                App app = new App();
                app.setAppName(packageInfo.applicationInfo.loadLabel(mPackageManager).toString());
                app.setAppPackageName(packageInfo.packageName);
                app.setVersionName(packageInfo.versionName);
                app.setVersionCode(packageInfo.versionCode);
                app.setAppIcon(packageInfo.applicationInfo.loadIcon(mPackageManager));
                mApps.add(app);
            }
        }
        return mApps;
    }


}
