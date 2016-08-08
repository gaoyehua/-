package com.yeyu.zhihuinanchang;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sdsmdg.tastytoast.TastyToast;
import com.yeyu.zhihuinanchang.Fragment.ContentFragment;
import com.yeyu.zhihuinanchang.Fragment.LeftMenuFragment;

/**
 * 主活动
 * Created by gaoyehua on 2016/8/7.
 */
public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private static final String TAG_CONTENT = "TAG_CONTENT";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //侧滑栏利用第三方库实现
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu =getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸
        slidingMenu.setBehindOffset(400);//预留300像素
        TastyToast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT,TastyToast.DEFAULT);

        //初始化
        initFragment();
    }
    /*
    初始化Fragment
     */
    private void initFragment() {
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction transaction =fm.beginTransaction();//开始事物
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),TAG_LEFT_MENU);
        transaction.replace(R.id.fl_main,new ContentFragment(),TAG_CONTENT);
        transaction.commit();
    }
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm =getSupportFragmentManager();
        LeftMenuFragment fragment =(LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
        return fragment;
    }
    // 获取主页fragment对象
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm
                .findFragmentByTag(TAG_CONTENT);// 根据标记找到对应的fragment
        return fragment;
    }

}
