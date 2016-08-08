package com.yeyu.zhihuinanchang.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yeyu.zhihuinanchang.MainActivity;
import com.yeyu.zhihuinanchang.R;

/**
 * 主页面的五个标签页的基类
 *
 * Created by gaoyehua on 2016/8/9.
 */
public class Basepager {
    public Activity mActivity;

    public View mRootView; //当前页面的布局对象
    public TextView tv_title;
    public ImageButton ib_menu;
    public FrameLayout fl_comtent;

    public Basepager(Activity activity){
        mActivity =activity;
        mRootView =initView();
    }
    /*
    初始化布局
     */
    public View initView(){
        View view =View.inflate(mActivity, R.layout.base_pager,null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        fl_comtent = (FrameLayout) view.findViewById(R.id.fl_content);

        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        return view;
    }
    /*
    打开和关闭侧边栏
     */
    protected void toggle() {
        MainActivity activity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = activity.getSlidingMenu();
        slidingMenu.toggle();//如果当前状态是关，调用之后就关闭，反之亦然
    }
    /*
    初始化数据
     */
    public void initData(){

    }

}
