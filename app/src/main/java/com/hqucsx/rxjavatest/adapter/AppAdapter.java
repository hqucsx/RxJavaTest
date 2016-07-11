package com.hqucsx.rxjavatest.adapter;/**
 * Created by PCPC on 2016/7/11.
 */

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqucsx.rxjavatest.R;
import com.hqucsx.rxjavatest.model.App;

import java.util.List;

/**
 * 描述: TODO
 * 名称: AppAdapter
 * User: csx
 * Date: 07-11
 */
public class AppAdapter extends BaseQuickAdapter<App> {
    public AppAdapter(List<App> data) {
        super(R.layout.item_app,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, App app) {
        baseViewHolder.setText(R.id.tv_appName,app.getAppName())
                .setText(R.id.tv_versionName,app.getVersionName())
                .setText(R.id.tv_packageName,app.getAppPackageName())
        .setImageDrawable(R.id.iv_icon,app.getAppIcon());
    }
}
