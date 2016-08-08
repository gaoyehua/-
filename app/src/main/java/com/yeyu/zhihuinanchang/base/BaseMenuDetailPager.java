package com.yeyu.zhihuinanchang.base;

import android.app.Activity;
import android.view.View;

/**
 * 标签页中的四个详情页面
 * 基类
 * Created by gaoyehua on 2016/8/9.
 */
public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;//详情页的布局

    public BaseMenuDetailPager(Activity activity){
        mActivity =activity;
        mRootView =initView();
    }
    public abstract View initView();
    //初始化数据
    public void initData(){

    }
}
