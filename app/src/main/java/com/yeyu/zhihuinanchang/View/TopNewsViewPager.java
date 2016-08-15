package com.yeyu.zhihuinanchang.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 功能：
 * 头条新闻栏设置
 * Created by gaoyehua on 2016/8/10.
 */
public class TopNewsViewPager extends ViewPager {

    private int startX;
    private int startY;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
    页面头条的滑动事件，1.上下滑动需要拦截，2.向右滑动，且当前是第一个页面时，需要拦截，
    3.向左滑动，且是最后一个页面时，需要拦截。
     */

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX =(int) ev.getX();
                int endY =(int) ev.getY();

                int dx =endX -startX;
                int dy =endY -startY;

                if(Math.abs(dy)<Math.abs(dx)){
                    int currentItem =getCurrentItem();
                    //左右滑动
                    if(dx>0){
                        //向右滑动
                        if(currentItem==0){
                            //第一个页面，需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else {
                        //向左滑动
                        int count = getAdapter().getCount();//获取条目总数
                        if (currentItem == count - 1) {
                            //最后一个页面，拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else {
                        //上下滑动，拦截
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }

                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
