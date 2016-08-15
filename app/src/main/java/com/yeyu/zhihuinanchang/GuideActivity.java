package com.yeyu.zhihuinanchang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yeyu.zhihuinanchang.Utils.PrefUtils;

import java.util.ArrayList;

/**
 * Created by gaoyehua on 2016/8/7.
 */
public class GuideActivity extends Activity{

    private ViewPager mViewPager;
    private LinearLayout ll_container;
    private Button btn_start;
    private ImageView iv_red_point;
    private ArrayList<ImageView> mImageViewList;

    private int[] mImageIds =new int[] {R.drawable.guide_1,
            R.drawable.guide_2,R.drawable.guide_3 };
    private int mpointDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        //初始化控件
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);
        btn_start = (Button) findViewById(R.id.btn_start);

        inintData();//初始化数据
        mViewPager.setAdapter(new GuideAdater());//设置数据

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //滑动时调用
                Log.i("data","当前位置:" + position + ";移动偏移百分比:"
                        + positionOffset);
                //更新小红点的距离
                int LeftMargin =(int)(mpointDis*positionOffset)+position*mpointDis;
                RelativeLayout.LayoutParams params =(RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
                params.leftMargin =LeftMargin;//修改左边距
                iv_red_point.setLayoutParams(params);


            }

            @Override
            public void onPageSelected(int position) {
                //页面选中时
                if(position == mImageViewList.size() -1){
                    btn_start.setVisibility(View.VISIBLE);
                }else {
                    btn_start.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //状态改变是调用
            }
        });

        //计算两个圆点的距离
        // 移动距离=第二个圆点left值 - 第一个圆点left值
        // measure->layout(确定位置)->draw(activity的onCreate方法执行结束之后才会走此流程)
        // mPointDis = llContainer.getChildAt(1).getLeft()
        // - llContainer.getChildAt(0).getLeft();
        // System.out.println("圆点距离:" + mPointDis);

        // 监听layout方法结束的事件,位置确定好之后再获取圆点间距
        // 视图树
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
            public void onGlobalLayout() {
                //移除监听，避免重复监听
                iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);//为了兼容低版本
                //小红点移动的距离
                        mpointDis = ll_container.getChildAt(1).getLeft()
                                -ll_container.getChildAt(0).getLeft();
                        Log.i("data","圆点的距离："+mpointDis);
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新sp，已经不是第一次进入
                PrefUtils.setBoolean(getApplicationContext(),"is-first-Enter",false);

                //跳转到主界面
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

    }
    /*
    初始化数据
     */
    private void inintData() {
        mImageViewList = new ArrayList<ImageView>();
        for(int i=0;i <mImageIds.length;i++){
            ImageView view =new ImageView(this);
            view.setImageResource(mImageIds[i]);
            mImageViewList.add(view);

            //初始化小圆点
            ImageView point =new ImageView(this);
            point.setImageResource(R.drawable.shape_gray_point);
            //初始化布局文件，宽高包裹内容，父控件是谁，就是谁声明的布局参数
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(i>0){
                //从第二个圆点开始设置间距
                params.leftMargin =10;
            }
            point.setLayoutParams(params);//为圆点设置参数
            ll_container.addView(point);//为容器添加圆点
        }
    }
    class GuideAdater extends PagerAdapter{


        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view =mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
