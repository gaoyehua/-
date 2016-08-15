package com.yeyu.zhihuinanchang.base.Tag_pager;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.sdsmdg.tastytoast.TastyToast;
import com.yeyu.zhihuinanchang.Fragment.LeftMenuFragment;
import com.yeyu.zhihuinanchang.Global.GlobalContants;
import com.yeyu.zhihuinanchang.MainActivity;
import com.yeyu.zhihuinanchang.Utils.CacheUtils;
import com.yeyu.zhihuinanchang.base.BaseMenuDetailPager;
import com.yeyu.zhihuinanchang.base.Basepager;
import com.yeyu.zhihuinanchang.base.Tag_pager.menu.InteractMenuDetailPager;
import com.yeyu.zhihuinanchang.base.Tag_pager.menu.NewsMenuDetailPager;
import com.yeyu.zhihuinanchang.base.Tag_pager.menu.PhotosMenuDetailPager;
import com.yeyu.zhihuinanchang.base.Tag_pager.menu.TopicMenuDetailPager;
import com.yeyu.zhihuinanchang.domain.NewsMenu;

import java.util.ArrayList;

/**
 * 主页面的五个标签之一
 * 智慧南昌
 * Created by gaoyehua on 2016/8/9.
 */
public class NewsPager extends Basepager {

    private NewsMenu mNewsData;
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;

    public NewsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.i("data","新闻页初始化。。");

        //给帧布局添加布局对象
        TextView view =new TextView(mActivity);
        view.setText("新闻中心");
        view.setTextColor(Color.RED);
        view.setTextSize(25);
        view.setGravity(Gravity.CENTER);


        fl_comtent.addView(view);
        //修改页面的标题
        tv_title.setText("新闻");
        //菜单按钮
        ib_menu.setVisibility(View.VISIBLE);

        //判断是否有缓存
        String cache =CacheUtils.getCache(GlobalContants.CATEGORY_URL,mActivity);
        if(!TextUtils.isEmpty(cache)){
            Log.i("data","有缓存。。。");
            processData(cache);
        }

        //请求服务器，获取数据
        getDataFromServer();
    }

    /*
    请求服务器获取数据，新闻中心
     */
    private void getDataFromServer() {
        HttpUtils utils =new HttpUtils();
        utils.send(HttpMethod.GET, GlobalContants.CATEGORY_URL,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        //请求成功
                        String result =responseInfo.result;//获取服务器返回结果
                        Log.i("data","服务器返回结果："+result);
                        System.out.println("服务器返回结果:" + result);

                        //写缓存
                        CacheUtils.setCache(GlobalContants.CATEGORY_URL,
                                result,mActivity);

                        // JsonObject, Gson
                        processData(result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        //请求失败
                        e.printStackTrace();
                        TastyToast.makeText(mActivity,s,TastyToast.LENGTH_SHORT,
                                TastyToast.ERROR);
                    }
                });

    }
    /*
    解析Json数据
     */
    protected void processData(String josn){
        Gson gson =new Gson();
        mNewsData = gson.fromJson(josn,NewsMenu.class);
        System.out.println("解析结果:" + mNewsData);
        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment fragment = mainUI.getLeftMenuFragment();

        // 给侧边栏设置数据
        fragment.setMenuData(mNewsData.data);

        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,mNewsData.data.get(0).children));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity,btnPhoto));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        // 将新闻菜单详情页设置为默认页面
        setCurrentDetailPager(0);
    }
    //设置菜单详情页
    public void setCurrentDetailPager(int position){
        //重新给fragment添加内容
        BaseMenuDetailPager pager =mMenuDetailPagers.get(position);//获取当前该显示的页面
        //添加布局
        View view =pager.mRootView;
        fl_comtent.removeAllViews();//清除之前的布局
        fl_comtent.addView(view);//添加新的布局
        pager.initData();
        tv_title.setText(mNewsData.data.get(position).title);//设置标题

        // 如果是组图页面, 需要显示切换按钮
        if (pager instanceof PhotosMenuDetailPager) {
            btnPhoto.setVisibility(View.VISIBLE);
        } else {
            // 隐藏切换按钮
            btnPhoto.setVisibility(View.GONE);
        }
    }
}
