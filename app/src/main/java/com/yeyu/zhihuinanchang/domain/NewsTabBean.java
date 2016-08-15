package com.yeyu.zhihuinanchang.domain;

import java.util.ArrayList;

/**
 * Created by gaoyehua on 2016/8/11.
 */
public class NewsTabBean {

    public NewsTab data;

    public class NewsTab{
        public String more;
        public ArrayList<NewsData> news;
        public ArrayList<TopNews> topnews;
    }

    // 新闻列表对象
    public class NewsData {
        public int id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
        @Override
        public String toString() {
            return "NewsData [title=" + title + "]";
        }
    }

    // 头条新闻
    public class TopNews {
        public int id;
        public String topimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
        @Override
        public String toString() {
            return "TopNews [title=" + title + "]";
        }
    }
}
