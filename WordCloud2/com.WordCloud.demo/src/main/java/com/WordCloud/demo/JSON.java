package com.WordCloud.demo;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSON {

    /**
     * 获取用户总粉丝数
     * 
     * @param Json
     *            待解析的Json文本
     * 
     * @return 返回一个文本的十进制整数
     */
    public static String getFansNumber(String Json) {
        // 先解析要用到的数组
        JSONObject json = JSONObject.parseObject(Json);
        JSONArray array = json.getJSONArray("data");

        // 解析 'data' 得到所有粉丝用户的数组
        // 准备解析第一个数组得到总粉丝数

        JSONObject jo = array.getJSONObject(0);
        JSONObject jo2 = jo.getJSONObject("fUserInfo");

        return jo2.getString("fans");
    }

    /**
     * 获取一页(page)里所有粉丝的用户名,以 '\n' 分割返回
     * 
     * @param Json
     *            待解析的Json文本
     * 
     * @return 返回一页(page)里所有粉丝用户名的文本,以 '\n' 分割返回
     */
    public static String getUsersName(String Json) {
        String UsersName = "";
        JSONObject json = JSONObject.parseObject(Json);
        JSONArray array = json.getJSONArray("data");// 解析数组 'data' 得到所有粉丝用户的数组

        // 每一次(1Page)得到的上限是20个,准备遍历所有数组得到用户名

        for (int x = 0; x != array.size(); x++) {
            JSONObject jo = array.getJSONObject(x);
            UsersName = UsersName + jo.getString("username") + "\n\r";
        }

        return UsersName;
    }

    /**
     * 获取所有粉丝的用户名,以 '\n' 分割返回
     * 
     * @param UserID
     *            用户ID
     * @param url
     *            Api地址
     * 
     * @return 返回一个所有粉丝用户名的文本,以 '\n' 分割返回
     */
    public static String getUsersName(int UserID, String url)
            throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        url = url + UserID;// 不带参数的Get到的数据和加上 '&page=1' 的一样
        System.out.println("Try to Get : " + url);
        String str = HttpsRequest2.sendGet(url);
        System.out.println("Get Done.");
        String UsersName = "";

        Long fansNLong = Long.parseLong(getFansNumber(str));
        Long max = fansNLong / 20;
        if (fansNLong % 20 != 0) {
            max = max +1;
        }
        //计算出一共有多少页(page),为了防止溢出而使用Long类型

        // 准备历遍所有数组得到用户名
        System.out.println("Done.");
        for (long x = 0; x != max + 1; x++) {
            String url2 = url + "&page=" + (x + 1);
            UsersName = UsersName + getUsersName(HttpsRequest2.sendGet(url2));

        }
        
        return UsersName;
    }
}
