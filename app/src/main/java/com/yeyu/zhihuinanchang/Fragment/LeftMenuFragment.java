package com.yeyu.zhihuinanchang.Fragment;

import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yeyu.zhihuinanchang.MainActivity;
import com.yeyu.zhihuinanchang.R;
import com.yeyu.zhihuinanchang.base.Tag_pager.NewsPager;
import com.yeyu.zhihuinanchang.domain.NewsMenu;

import java.util.ArrayList;

/**
 * 实现父类的方法，左边菜单栏的fragment
 *
 * Created by gaoyehua on 2016/8/8.
 */
public class LeftMenuFragment extends BaseFragment {
    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuData;
    private int mCurrentPos;
    private LeftMenuAdapter mAdapter;

    @ViewInject(R.id.lv_list)
    private ListView lvList;
    @Override
    public View intiView() {
        View view =View.inflate(mActivity, R.layout.fragment_left_menu,null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void intiData() {

    }

    //给侧边栏设置数据
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data){

        mCurrentPos = 0;//页面位置归零
        mNewsMenuData = data;//更新页面
        mAdapter = new LeftMenuAdapter();
        lvList.setAdapter(mAdapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {
                mCurrentPos =position;
                mAdapter.notifyDataSetChanged();//刷新listview
                //收起侧边栏
                toggle();

                //侧边栏点击之后，要切换FragmentLayout的布局
                setCurrentDetailPager(position);
            }
        });



    }
    /**
     * 设置当前菜单详情页
     *
     * @param position
     */
    protected void setCurrentDetailPager(int position) {
        // 获取新闻中心的对象
        MainActivity mainUI = (MainActivity) mActivity;
        // 获取ContentFragment
        ContentFragment fragment = mainUI.getContentFragment();
        // 获取NewsCenterPager
        NewsPager newsCenterPager = fragment.getNewsCenterPager();
        // 修改新闻中心的FrameLayout的布局
        newsCenterPager.setCurrentDetailPager(position);
    }

    /**
     * 打开或者关闭侧边栏
     */
    protected void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之亦然
    }

    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return mNewsMenuData.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View view1 =View.inflate(mActivity,R.layout.list_item_menu,null);
            TextView tv_menu =(TextView) view1.findViewById(R.id.tv_menu);
            NewsMenu.NewsMenuData item =getItem(position);
            tv_menu.setText(item.title);
            if(position ==mCurrentPos){
                tv_menu.setEnabled(true);
            }else {
                tv_menu.setEnabled(false);
            }


            return view1;
        }
    }


}
