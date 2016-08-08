package com.yeyu.zhihuinanchang.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 实
 * Created by gaoyehua on 2016/8/8.
 */
public abstract class BaseFragment extends Fragment {
    public Activity mActivity;//这个活动就是MainActivity

    //创建Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }
    //初始化Fragment布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view =intiView();
        return view;
    }
    //fragment所依赖的Activity在Oncreate放啊中执行完成
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //加载数据
        intiData();
    }
    public abstract View intiView();
    public abstract void intiData();
}