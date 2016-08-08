package com.yeyu.zhihuinanchang.Fragment;

import android.app.Fragment;
import android.view.View;

import com.yeyu.zhihuinanchang.R;

/**
 * 实现父类的方法，左边菜单栏的fragment
 *
 * Created by gaoyehua on 2016/8/8.
 */
public class LeftMenuFragment extends BaseFragment {


    @Override
    public View intiView() {
        View view =View.inflate(mActivity, R.layout.fragment_left_menu,null);
        return view;
    }

    @Override
    public void intiData() {

    }
}
