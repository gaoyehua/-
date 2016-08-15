package com.yeyu.zhihuinanchang.base.Tag_pager.menu;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnChildClick;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yeyu.zhihuinanchang.MainActivity;
import com.yeyu.zhihuinanchang.R;
import com.yeyu.zhihuinanchang.base.BaseMenuDetailPager;
import com.yeyu.zhihuinanchang.domain.NewsMenu;

import java.util.ArrayList;

import shanyao.tabpagerindictor.TabPageIndicator;


/**
 * 菜单详情页-新闻
 * 
 * @author Kevin
 * @date 2015-10-18
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements
		ViewPager.OnPageChangeListener{

	@ViewInject(R.id.vp_news_menu_detail)
	private ViewPager mViewPager;

	@ViewInject(R.id.indicator)
	private TabPageIndicator indicator;

	private ArrayList<NewsMenu.NewsTabData> mTabData;//网络页签的数据
	private ArrayList<TabDetailPager> mPagers;//页签的详情页面的集合

	public NewsMenuDetailPager(Activity activity,
							   ArrayList<NewsMenu.NewsTabData> children) {
		super(activity);
		mTabData =children;
	}

	/*
	加载布局
	 */
	@Override
	public View initView() {
		View view =View.inflate(mActivity, R.layout.pager_news_menu_detail,null);
		ViewUtils.inject(this,view);
		return view;
	}

	@Override
	public void initData() {

		//初始化页签
		mPagers =new ArrayList<TabDetailPager>();
		for(int i=0;i <mTabData.size();i++){
			TabDetailPager pager =new TabDetailPager(mActivity,mTabData.get(i));
			mPagers.add(pager);
		}

		mViewPager.setAdapter(new NewsMenuDetailAdapter());// 设置adapter
		indicator.setViewPager(mViewPager);// 绑定indicator

		// 设置页面滑动监听
		// mViewPager.setOnPageChangeListener(this);
		indicator.setOnPageChangeListener(this);// 此处必须给指示器设置页面监听,不能设置给viewpager

		setTabPagerIndicator();
	}
	/**
	 * 通过一些set方法，设置控件的属性
	 */
	private void setTabPagerIndicator() {
		indicator.setIndicatorMode(TabPageIndicator.IndicatorMode.MODE_NOWEIGHT_EXPAND_NOSAME);// 设置模式，一定要先设置模式
		//indicator.setDividerColor(Color.parseColor("#00bbcf"));// 设置分割线的颜色
		indicator.setDividerPadding(10);//设置
		indicator.setIndicatorColor(Color.parseColor("#A41614"));// 设置底部导航线的颜色
		indicator.setTextColorSelected(Color.parseColor("#A41614"));// 设置tab标题选中的颜色
		indicator.setTextColor(Color.parseColor("#797979"));// 设置tab标题未被选中的颜色
		indicator.setTextSize(50);// 设置字体大小
	}

	/*
	OnPageChangeListener接口的继承方法
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		//页签的页面选择，确定只有在位置为0的时候拉出侧边栏
		if(position==0){
			//开启侧边栏
			setSlidingMenuEnable(true);
		}else {
			//关闭侧边栏
			setSlidingMenuEnable(false);
		}

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	/*
	开启和关闭侧边栏
	 */
	protected void setSlidingMenuEnable(boolean enable){
		//获取侧边栏对象
		MainActivity mainui =(MainActivity) mActivity;
		SlidingMenu slidingMenu =mainui.getSlidingMenu();
		if(enable){
			slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_FULLSCREEN);
		}else {
			slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_NONE);
		}
	}

	/*
	页签页的ViewPager的适配器
	 */
	class NewsMenuDetailAdapter extends PagerAdapter{

		@Override
		public CharSequence getPageTitle(int position) {
			NewsMenu.NewsTabData data =mTabData.get(position);
			return data.title;
		}

		@Override
		public int getCount() {
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view ==object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager =mPagers.get(position);
			View view =pager.mRootView;
			container.addView(view);
			//container.addView(view);

			pager.initData();
			return view;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
	}
	/*
	页签的下一页按钮点击事件
	 */
	@OnClick(R.id.ib_tab_next)
	public void nextPage(View view){
		//跳转到下一个页面
		int currentItem =mViewPager.getCurrentItem();
		currentItem++;
		mViewPager.setCurrentItem(currentItem);
	}

}

