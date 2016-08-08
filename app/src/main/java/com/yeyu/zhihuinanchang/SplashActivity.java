package com.yeyu.zhihuinanchang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.yeyu.zhihuinanchang.Utils.PrefUtls;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rl_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
        //设置动画
        //旋转动画
        RotateAnimation rotateAnimation =new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1000);//动画时间
        rotateAnimation.setFillAfter(true);//保持动画的状态
        //缩放动画
        ScaleAnimation scaleAnimation =new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        //渐变动画
        AlphaAnimation alphaAnimation =new AlphaAnimation(0,1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        //设置动画集合
        AnimationSet animationSet =new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        //启动动画
        rl_splash.startAnimation(animationSet);

        //页面跳转监听
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //动画开始
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束
                //进行页面跳转
                boolean isFirstEnter =PrefUtls.getBoolean(SplashActivity.this,"is-first-Enter",true);

                Intent intent;
                if(isFirstEnter){
                    //跳转到引导界面
                    intent =new Intent(SplashActivity.this,GuideActivity.class);
                }else {
                    //跳转到主界面
                    intent =new Intent(SplashActivity.this,MainActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
