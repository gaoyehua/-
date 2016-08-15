package com.yeyu.zhihuinanchang.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yeyu.zhihuinanchang.R;

import java.util.Date;

/**
 *
 * 功能：
 * 下拉刷新
 * Created by gaoyehua on 2016/8/10.
 */
public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {
    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;

    private int mCurrentState = STATE_PULL_TO_REFRESH;// 当前刷新状态

    private View mHeaderView;
    private ImageView iv_arrow;
    private ProgressBar pb_loading;
    private TextView tv_title;
    private TextView tv_time;
    private int mHeaderViewHeight;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private int startY;
    private View mFooterView;
    private ProgressBar pb_loding;
    private TextView tv_title1;
    private int mFooterViewHeight;

    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }
    /*
    初始化头布局
     */
    private void initHeaderView(){
        //加载布局
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header,null);
        this.addHeaderView(mHeaderView);

        //初始化控件
        iv_arrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        pb_loading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
        tv_title = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tv_time = (TextView) mHeaderView.findViewById(R.id.tv_time);
        //隐藏布局
        mHeaderView.measure(0,0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);

        initAnim(); //初始化箭头动画
        setCurrentTime(); //设置刷新时间
    }

    /*
    初始化加載尾布局
     */
    private void initFooterView(){
        mFooterView = View.inflate(getContext(),
                R.layout.pull_to_refresh_footer,null);
        this.addFooterView(mFooterView);

        //初始化控件
        pb_loding = (ProgressBar) mFooterView.findViewById(R.id.pb_loading);
        tv_title1 = (TextView) mFooterView.findViewById(R.id.tv_title);

        //隱藏
        mFooterView.measure(0,0);
        mFooterViewHeight = mHeaderView.getMeasuredHeight();
        mFooterView.setPadding(0,-mFooterViewHeight,0,0);
        this.setOnScrollListener(this); //设置滑动监听





    }


    /*
    初始化动画
     */
    private void initAnim(){
        animUp = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);

    }
    /*
    设置刷新时间
     */
    @TargetApi(Build.VERSION_CODES.N)
    private void setCurrentTime(){
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time =format.format(new Date());
        tv_time.setText(time);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if(startY == -1){
                    //当用户按住头条新闻的ViewPager时，ACTION_DOWN会被ViewPager消费掉
                    //因此在此处需要重新获取
                    startY = (int) ev.getY();
                }
                if(mCurrentState ==STATE_REFRESHING){
                    //正在刷新，跳出循环
                    break;
                }

                int endY =(int) ev.getY();
                int dy =endY -startY;

                int firstVisionPosition =getFirstVisiblePosition();//当前显示的第一个item的位置

                //必须下拉，并且当前显示的是第一个item的位置
                if(dy >0 && firstVisionPosition ==0){
                    int padding =dy-mHeaderViewHeight;//计算当前下拉控件的padding值
                    mHeaderView.setPadding(0,padding,0,0);

                    if(padding >0 && mCurrentState !=STATE_RELEASE_TO_REFRESH){
                        //改为松开刷新
                        mCurrentState =STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    }else if(padding<0 && mCurrentState !=STATE_PULL_TO_REFRESH){
                        //改为下拉刷新
                        mCurrentState =STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
                startY =-1;

                if(mCurrentState ==STATE_RELEASE_TO_REFRESH){
                    mCurrentState =STATE_REFRESHING;
                    refreshState();
                    //完整展示刷新布局界面
                    mHeaderView.setPadding(0,0,0,0);

                    //4.进行回调
                    if(mListener!=null){
                        mListener.onRefresh();
                    }

                }else if(mCurrentState ==STATE_PULL_TO_REFRESH){
                    //隐藏头布局
                    mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
                }
                break;

            default:
                break;
        }

        return super.onTouchEvent(ev);
    }
    /*
    释放刷新，刷新数据
     */
    private void refreshState(){
        switch (mCurrentState){
            case STATE_PULL_TO_REFRESH:
                tv_title.setText("下拉刷新");
                pb_loading.setVisibility(INVISIBLE);
                iv_arrow.setVisibility(VISIBLE);
                iv_arrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tv_title.setText("松开刷新");
                pb_loading.setVisibility(INVISIBLE);
                iv_arrow.setVisibility(VISIBLE);
                iv_arrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tv_title.setText("正在刷新");

                iv_arrow.clearAnimation();//清除动画，否则无法隐藏

                pb_loading.setVisibility(VISIBLE);
                iv_arrow.setVisibility(INVISIBLE);
                break;
        }

    }

    /*
    回调监听刷新成功与否
     */
    //刷新结束后，收起控件
    public void onRefreshComplete(boolean success){
        if(! isLoadMore){
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

            mCurrentState = STATE_PULL_TO_REFRESH;
            tv_title.setText("下拉刷新");
            pb_loading.setVisibility(View.INVISIBLE);
            iv_arrow.setVisibility(View.VISIBLE);

            if (success) {// 只有刷新成功之后才更新时间
                setCurrentTime();
            }
        }else {
            //加载更多
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);
            isLoadMore =false;
        }

    }

    //3.定义成员变量，接收监听对象
    private OnRefreshListener mListener;
    //2.暴露接口，设置监听
    public void setOnRefreshListener(OnRefreshListener Listener){
        mListener =Listener;
    }

    //1.下拉刷新回调接口
    public interface OnRefreshListener{
        public void onRefresh();

        public void onLoadMore();
    }

    private boolean isLoadMore;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //滑动状态改变时
        if(scrollState ==SCROLL_STATE_IDLE){
            //空闲状态
            int lastVisiblePosition =getLastVisiblePosition();

            if(lastVisiblePosition ==getCount() -1 && ! isLoadMore){
                //最后一个，并且不在加载更多
                Log.i("data","加载更多...");

                isLoadMore =true;
                mFooterView.setPadding(0,0,0,0);

                setSelection(getCount() -1);
                //加载更多数据
                if(mListener!=null){
                    mListener.onLoadMore();
                }

            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
}
