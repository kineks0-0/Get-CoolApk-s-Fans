package com.WordCloud.demo;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSON {
    public static void main(String[] args) {
        
    }

    public static String getFansNumber(String Json) {
        // 先准备要用到的数组
        JSONObject json = JSONObject.parseObject(Json);
        System.out.println("Get Json object Done.");
        // 解析刚刚获取的数组,用的是阿里巴巴的fastjson
        JSONArray array = json.getJSONArray("data");
        System.out.println("Get List object Done.");
        // 解析 'data' 得到所有粉丝用户的数组
        // 每一次(1Page)得到的上限是20个
        // 准备解析第一个数组得到总粉丝数
        // System.out.println("Done.");
        JSONObject jo = array.getJSONObject(0);
        JSONObject jo2 = jo.getJSONObject("fUserInfo");

        return jo2.getString("fans");
    }

    public static String getUsersName(String Json) {
        // 先准备要用到的变量
        String UsersName = "";
        JSONObject json = JSONObject.parseObject(Json);
        // System.out.println("Get Json object Done.");

        // 解析刚刚获取的数组,用的是阿里巴巴的fastjson
        // 解析 'data' 得到所有粉丝用户的数组
        JSONArray array = json.getJSONArray("data");
        // System.out.println("Get List object Done.");

        // 每一次(1Page)得到的上限是20个
        // int max = 20;

        // 准备历遍所有数组得到用户名
        // System.out.println("Done.");
        for (int x = 0; x != array.size(); x++) {
            // JSONArray UsersArray = json.getJSONArray(x+"");
            JSONObject jo = array.getJSONObject(x);
            UsersName = UsersName + jo.getString("username") + "\n\r";
        }

        return UsersName;
    }

    public static String getUsersName(int UserID, String url)
            throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        url = url + UserID;
        // 不带参数的Get到的数据和加上 '&page=1' 的一样
        System.out.println("Try to Get : " + url);
        String str = HttpsRequest2.sendGet(url);
        System.out.println("Get Done.");
        String UsersName = "";

        Long fansNLong = Long.parseLong(getFansNumber(str));
        Long max = fansNLong / 20;
        if (fansNLong % 20 != 0) {
            max = max +1;
        }
        //计算出一共有多少页(page)

        // 准备历遍所有数组得到用户名
        System.out.println("Done.");
        for (int x = 0; x != max + 1; x++) {
            String url2 = url + "&page=" + (x + 1);
            UsersName = UsersName + getUsersName(HttpsRequest2.sendGet(url2));

        }
        
        return UsersName;
    }
}
/*
 * String url = "https://api.coolapk.com/v6/user/fansList?uid=2086596";
 * //Get到的数据和加上 '&page=1' 的一样 System.out.println("Try to Get : " + url); String
 * str = HttpsRequest2.sendGet(url); System.out.println(str);
 * writeFile.writetoFile("E:\\fansList.json", str);
 * System.out.println("Get Done."); //这里完成获取数据,准备解析数据来得到用户名和粉丝数
 * 
 */
/*
 * //先初始化要用到的变量 String UsersName = ""; JSONObject json =
 * JSONObject.parseObject(str); System.out.println("Get Json object Done.");
 * //解析刚刚获取的数组,用了阿里巴巴的fastjson JSONArray array = json.getJSONArray("data");// =
 * JSONObject.parseArray("data",Object.class);//JSONObject.getJSONArray("data");
 * //JSON.parseArray(object.getJSONArray("data").toJSONString(),Integer.class);
 * System.out.println("Get List object Done."); //解析 'data' 得到所有粉丝用户的数组 int max
 * = 20;
 */
// 每一次(1Page)得到的上限是20个
/*
 * JSONObject object2 = new JSONObject(); object.put("list",Object); String
 * ListUser1 = object2.toString(); System.out.println("Get UserList  Done.");
 */

// 准备历遍所有数组得到用户名和总粉丝数
/*
 * System.out.println("Done."); for(int x = 0 ;x != max;x++){ //JSONArray
 * UsersArray = json.getJSONArray(x+""); JSONObject jo = array.getJSONObject(x);
 * UsersName = UsersName + jo.getString("username") + "\n"; }
 */
// String str =
// HttpRequest.sendGet("https://api.coolapk.com/v6/user/fansList?uid=2086596&page=1",
// "");
