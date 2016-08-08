package com.yeyu.zhihuinanchang;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by gaoyehua on 2016/8/7.
 */
public class MainActivity extends SlidingFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu =getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸
        slidingMenu.setBehindOffset(400);//预留300像素
        TastyToast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT,TastyToast.DEFAULT);
    }
}
