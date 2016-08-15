package com.yeyu.zhihuinanchang.base.Tag_pager.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdsmdg.tastytoast.TastyToast;
import com.yeyu.zhihuinanchang.Global.GlobalContants;
import com.yeyu.zhihuinanchang.NewsDetailActivity;
import com.yeyu.zhihuinanchang.R;
import com.yeyu.zhihuinanchang.Utils.CacheUtils;
import com.yeyu.zhihuinanchang.Utils.PrefUtils;
import com.yeyu.zhihuinanchang.View.PullToRefreshListView;
import com.yeyu.zhihuinanchang.View.TopNewsViewPager;
import com.yeyu.zhihuinanchang.base.BaseMenuDetailPager;
import com.yeyu.zhihuinanchang.domain.NewsMenu;
import com.yeyu.zhihuinanchang.domain.NewsTabBean;

import java.util.ArrayList;
import java.util.logging.Handler;

import me.relex.circleindicator.CircleIndicator;

/**
 * 功能：
 * 页签下面的详情新闻页面
 *
 * Created by gaoyehua on 2016/8/10.
 */
public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenu.NewsTabData mTabData; //单个页签的网络数据

    @ViewInject(R.id.vp_top_news)
    private TopNewsViewPager mViewPager;

    @ViewInject(R.id.indicator)
    private CircleIndicator mIndicator;

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.lv_list)
    private PullToRefreshListView lvList;
    private String mUrl;
    private ArrayList<NewsTabBean.TopNews> mTopNews;
    private ArrayList<NewsTabBean.NewsData> mNewsList;
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;
    private android.os.Handler mHandler;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);

        mTabData = newsTabData;
        mUrl = GlobalContants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
        //添加布局
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this, view);
        //给ListView添加布局
        View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);
        ViewUtils.inject(this, mHeaderView);//此处必须将头布局注入
        lvList.addHeaderView(mHeaderView);

        lvList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                //判断加载下一页数据
                if(mMoreUrl!=null){
                    //有下一页
                    getMoreDataFromServer();
                }else {
                    //没有下一页
                    TastyToast.makeText(mActivity,"没有更多数据了",TastyToast.LENGTH_SHORT,TastyToast.DEFAULT);
                    //收起控件
                    lvList.onRefreshComplete(true);
                }

            }
        });
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                int headerViewCount =lvList.getHeaderViewsCount();//获得头布局的数量
                position =position -headerViewCount;
                Log.i("data","第"+position+"个被点击");

                NewsTabBean.NewsData news =mNewsList.get(position);
                //保存数据的标识
                String readIds = PrefUtils.getString(mActivity,"read_ids","");
                if(!readIds.contains(news.id+"")){
                    readIds =readIds +news.id+",";
                    PrefUtils.setString(mActivity,"read_ids",readIds);
                }
                //将被点击的条目字体设置为灰色
                TextView tvTitle =(TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);

                //跳转到新闻内容的页面
                Intent intent =new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url",news.url);
                mActivity.startActivity(intent);

            }
        });


        return view;
    }

    @Override
    public void initData() {

        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache,false);
        }

        getDataFromServer();
    }

    /*
    从服务器获取页签详情页的数据
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();

        if(mUrl!=null){

            utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String result = responseInfo.result;
                    Log.i("data", "详情页数据为：" + result);
                    //解析数据
                    processData(result,false);

                    CacheUtils.setCache(mUrl, result, mActivity);

                    // 收起下拉刷新控件
                    lvList.onRefreshComplete(true);

                }

                @Override
                public void onFailure(HttpException e, String s) {

                    e.printStackTrace();
                    TastyToast.makeText(mActivity, s, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    // 收起下拉刷新控件
                    lvList.onRefreshComplete(false);
                }
            });

        }

    }

    /*
    将从网络获取的数据解析
     */
    protected void processData(String result,boolean isMore) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);
        Log.i("data","解析结果："+newsTabBean);

        String moreUrl =newsTabBean.data.more;
        if(!TextUtils.isEmpty(moreUrl)){
            mMoreUrl = GlobalContants.SERVER_URL+moreUrl;
        }else {
            mMoreUrl =null;
        }

        if(!isMore){
            //头条新闻填充数据
            mTopNews = newsTabBean.data.topnews;
            if (mTopNews != null) {
                mViewPager.setAdapter(new TopNewsAdapter());
                mIndicator.setViewPager(mViewPager);

                //事件要设置给mIdicator
                mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        //更新头条新闻标题
                        NewsTabBean.TopNews topNews = mTopNews.get(position);
                        tvTitle.setText(topNews.title);
                        Log.i("data", "头条地址"+topNews.url);

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                //更新第一个标题
                tvTitle.setText(mTopNews.get(0).title);
                mIndicator.setSelected(true);
            }
            //新闻列表
            mNewsList = newsTabBean.data.news;
            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);
            }

            if(mHandler== null){
                mHandler = new android.os.Handler(){
                    public void handleMessage(Message message){

                        int currentItem =mViewPager.getCurrentItem();
                        currentItem++;

                        if(currentItem >mTopNews.size() -1){
                            currentItem =0;
                        }

                        mViewPager.setCurrentItem(currentItem);
                        mHandler.sendEmptyMessageDelayed(0,3000);
                    }
                };

                //保证启动轮播一次头条新闻
                mHandler.sendEmptyMessageDelayed(0,3000);
                mViewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                Log.i("data","停止轮播");
                                //停止轮播，消除所有消息
                                mHandler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                // 取消事件,
                                // 当按下viewpager后,直接滑动listview,导致抬起事件无法响应,但会走此事件
                                Log.i("data","取消事件");
                                // 启动广告
                                mHandler.sendEmptyMessageDelayed(0, 3000);
                                break;
                            case MotionEvent.ACTION_UP:
                                // 启动广告
                                Log.i("data","启动");
                                mHandler.sendEmptyMessageDelayed(0, 3000);
                                break;
                            default:
                                break;

                        }
                        return false;
                    }
                });
            }
        }else {
            //加载更多数据
            ArrayList<NewsTabBean.NewsData> moreNews =newsTabBean.data.news;
            mNewsList.addAll(moreNews);//将数据追加
            //刷新
            mNewsAdapter.notifyDataSetChanged();

        }


    }
    /*

    从网络加载更多的数据
     */
    protected void getMoreDataFromServer(){
        HttpUtils utils = new HttpUtils();
            utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String result = responseInfo.result;
                    Log.i("data", "加载更多数据为：" + result);
                    //解析数据
                    processData(result,true);

                    CacheUtils.setCache(mMoreUrl, result, mActivity);

                    // 收起下拉刷新控件
                    lvList.onRefreshComplete(true);

                }

                @Override
                public void onFailure(HttpException e, String s) {

                    e.printStackTrace();
                    TastyToast.makeText(mActivity, s, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    // 收起下拉刷新控件
                    lvList.onRefreshComplete(false);
                }
            });

    }


    //新闻头条适配器

    class TopNewsAdapter extends PagerAdapter{

        private BitmapUtils bitmapUtils;
        public TopNewsAdapter(){
            bitmapUtils =new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
            //默认加载的图片
        }
        @Override
        public int getCount() {
            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view =new ImageView(mActivity);
            //view.setImageResource(R.drawable.topnews_item_default);
            view.setScaleType(ImageView.ScaleType.FIT_XY);//图片加载方式

            String imageUrl =mTopNews.get(position).topimage;//图片下载链接

            bitmapUtils.display(view,imageUrl);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //新闻列表适配器
    class NewsAdapter extends BaseAdapter{
        private BitmapUtils bitmapUtils;
        public NewsAdapter(){
            bitmapUtils =new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
            //默认加载的图片
        }
        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if(view==null){
                view =View.inflate(mActivity,R.layout.list_item_news,null);
                viewHolder=new ViewHolder();
                viewHolder.iv_icon=(ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.tv_title=(TextView) view.findViewById(R.id.tv_title);
                viewHolder.tv_date=(TextView) view.findViewById(R.id.tv_date);

                view.setTag(viewHolder);

            }else {
                viewHolder=(ViewHolder) view.getTag();
            }
           NewsTabBean.NewsData news = (NewsTabBean.NewsData)  getItem(position);
            viewHolder.tv_title.setText(news.title);
            viewHolder.tv_date.setText(news.pubdate);
            //viewHolder.iv_icon.setImageResource(R.drawable.news_pic_default);

            bitmapUtils.display(viewHolder.iv_icon,news.listimage);

            //根据本地的数据来判断加载是否已读
            String readIds =PrefUtils.getString(mActivity,"read_ids","");
            if(readIds.contains(news.id+"")){
                viewHolder.tv_title.setTextColor(Color.GRAY);
            }else {
                viewHolder.tv_title.setTextColor(Color.BLACK);
            }
            return view;
        }
    }
    static class ViewHolder{

        public ImageView iv_icon;
        public TextView tv_title;
        public TextView tv_date;
    }
}
