package com.yeyu.zhihuinanchang.Fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yeyu.zhihuinanchang.MainActivity;
import com.yeyu.zhihuinanchang.R;
import com.yeyu.zhihuinanchang.View.NoScrollViewPager;
import com.yeyu.zhihuinanchang.base.Basepager;
import com.yeyu.zhihuinanchang.base.Tag_pager.GovAffairsPager;
import com.yeyu.zhihuinanchang.base.Tag_pager.HomePager;
import com.yeyu.zhihuinanchang.base.Tag_pager.NewsPager;
import com.yeyu.zhihuinanchang.base.Tag_pager.SettingPager;
import com.yeyu.zhihuinanchang.base.Tag_pager.SmartServicePager;

import java.util.ArrayList;

/**
 *
 * 实现主目录的方法，主页面
 * Created by gaoyehua on 2016/8/8.
 */
public class ContentFragment extends BaseFragment {

    private NoScrollViewPager mViewPager;
    private RadioGroup rg_group;
    private ArrayList<Basepager> mPager;

    @Override
    public View intiView() {
        View view =View.inflate(mActivity, R.layout.fragment_content,null);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void intiData() {
        mPager = new ArrayList<Basepager>();
        //将五个标签页添加到数组中
        mPager.add(new HomePager(mActivity));
        mPager.add(new NewsPager(mActivity));
        mPager.add(new SmartServicePager(mActivity));
        mPager.add(new GovAffairsPager(mActivity));
        mPager.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new contentAdapter());
        //在底边栏设置监听
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        // 首页
                        // mViewPager.setCurrentItem(0);
                        mViewPager.setCurrentItem(0, false);// 参2:表示是否具有滑动动画
                        break;
                    case R.id.rb_news:
                        // 新闻中心
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        // 智慧服务
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        // 政务
                        mViewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        // 设置
                        mViewPager.setCurrentItem(4, false);
                        break;

                    default:
                        break;
                }
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Basepager pager = mPager.get(position);
                pager.initData();

                if (position == 0 || position == mPager.size() - 1) {
                    // 首页和设置页要禁用侧边栏
                    setSlidingMenuEnable(false);
                } else {
                    // 其他页面开启侧边栏
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 手动加载第一页数据
        mPager.get(0).initData();
        // 首页禁用侧边栏
        setSlidingMenuEnable(false);
    }

    /**
     * 开启或禁用侧边栏
     *
     * @param enable
     */
    protected void setSlidingMenuEnable(boolean enable) {
        // 获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
    class contentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPager.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Basepager basepager =mPager.get(position);
            View view =basepager.mRootView;
            container.addView(view);// 获取当前页面对象的布局

            // pager.initData();// 初始化数据, viewpager会默认加载下一个页面,
            // 为了节省流量和性能,不要在此处调用初始化数据的方法
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
