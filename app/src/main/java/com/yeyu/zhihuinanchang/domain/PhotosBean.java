package com.yeyu.zhihuinanchang.domain;

import java.util.ArrayList;

/**
 * Created by gaoyehua on 2016/8/13.
 */
public class PhotosBean {
    public PhotosData data;

    public class PhotosData {
        public ArrayList<PhotoNews> news;
    }

    public class PhotoNews {
        public int id;
        public String listimage;
        public String title;
    }
}
