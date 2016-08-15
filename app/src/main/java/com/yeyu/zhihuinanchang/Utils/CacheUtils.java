package com.yeyu.zhihuinanchang.Utils;

import android.content.Context;

/**
 * 功能：
 * 实现网络数据缓存工具类
 *
 * Created by gaoyehua on 2016/8/10.
 */
public class CacheUtils {
    /*
    保存缓存
    实现数据包缓存，以json为缓存对象，
    以URL为key，json为value值
     */
    public static void setCache(String url, String json, Context context){
        //也可以用文件缓存，以MD5（url）为文件名，json为文件内容
        PrefUtils.setString(context,url,json);

    }
    /*
    获取缓存，得到缓存数据
     */
    public static String getCache(String url,Context context){
        //
        return PrefUtils.getString(context,url,null);
    }


}
