package com.yeyu.zhihuinanchang;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yeyu.zhihuinanchang.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by gaoyehua on 2016/8/12.
 */
public class NewsDetailActivity extends Activity  {

    private String mUrl;
    private LinearLayout llControl;
    private ImageButton ib_back;
    private ImageButton ib_share;
    private ImageButton ib_textsize;
    private ImageButton ib_menu;
    private WebView wv_news_detail;
    private ProgressBar pb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetailpager);

        llControl = (LinearLayout) findViewById(R.id.ll_control);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_textsize = (ImageButton) findViewById(R.id.ib_textsize);
        ib_share = (ImageButton) findViewById(R.id.ib_share);
        ib_menu = (ImageButton) findViewById(R.id.ib_menu);
        wv_news_detail = (WebView) findViewById(R.id.wv_news_detail);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);


        llControl.setVisibility(View.VISIBLE);
        ib_back.setVisibility(View.VISIBLE);
        ib_menu.setVisibility(View.INVISIBLE);

        mUrl =getIntent().getStringExtra("url");

        //加载网址
        wv_news_detail.loadUrl(mUrl);
        //mWebView.loadUrl("http://www.baidu.com");

        WebSettings settings = wv_news_detail.getSettings();
        settings.setBuiltInZoomControls(true);// 显示缩放按钮(wap网页不支持)
        settings.setUseWideViewPort(true);// 支持双击缩放(wap网页不支持)
        settings.setJavaScriptEnabled(true);// 支持js功能

        wv_news_detail.setWebViewClient(new WebViewClient() {
            // 开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("开始加载网页了");
                pb_loading.setVisibility(View.VISIBLE);
            }

            // 网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("网页加载结束");

                pb_loading.setVisibility(View.INVISIBLE);
            }

            // 所有链接跳转会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("跳转链接:" + url);
                view.loadUrl(url);// 在跳转链接时强制在当前webview中加载
                return true;
            }
        });

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ib_textsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //修改网页字体的大小
                showChooseDialog();
            }
        });
        ib_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //一键分享
                showShare();
            }
        });
    }

    //shareSDK分享
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("智慧南昌");//(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }


   // @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_textsize:
                //修改网页字体的大小
                showChooseDialog();
                break;
            case R.id.ib_share:
                //一键分享
                break;

            default:
                break;
        }
    }
    private int mTempWhich; //记录临时选中的字体
    private int mCurrentWhich =2; //点击之后，记录当前选中的字体，默认的字体是正常

    /*
    展示字体选择的对话框
     */
    private void showChooseDialog(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("字体设置");

        String[] items =new String[] {"超大号字体","大号字体",
                "正常字体","小号字体","超小号字体" };
        builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTempWhich =which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //根据选择的字体来设置网络的字体
                WebSettings settings =wv_news_detail.getSettings();

                switch (mTempWhich){
                    case 0:
                        //超大号字体
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        // 大字体
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        // 正常字体
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        // 小字体
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        // 超小字体
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;

                    default:
                        break;
                }

                mCurrentWhich =mTempWhich;
            }
        });

        builder.setNegativeButton("取消",null);
        builder.show();
    }


}
