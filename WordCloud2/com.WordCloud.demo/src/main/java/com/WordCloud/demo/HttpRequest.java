package com.WordCloud.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
 
public class HttpRequest {
    /**
     * 向指定URL发送GET方法的请求
     * 由于这个只支持http的get和post
     * 因此换用了 HttpsRequest2
     * 所以可以放心删除这个类
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            //connection.setRequestProperty("accept", "*/*");
            //connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent","Dalvik/2.1.0 (Linux; U; Android 5.1; m1 note Build/LMY47D) (#Build; Meizu; m1 note; Flyme 6.3.0.2A; 5.1) +CoolMarket/9.6-1909191");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setRequestProperty("X-Sdk-Int", "22");
            connection.setRequestProperty("X-Sdk-Locale", "zh-CN");
            connection.setRequestProperty("X-App-Id", "com.coolapk.market");
            connection.setRequestProperty("X-App-Token", "57cdbc8d418f9403bb0bc72ce8a7cdd666968dc0-829e-33f2-985d-900cfa72e93a0x5da67745");
            connection.setRequestProperty("X-App-Version", "9.6");
            connection.setRequestProperty("X-App-Code", "1909191");
            connection.setRequestProperty("X-Api-Version", "9");
            connection.setRequestProperty("X-App-Device", "lR3buBSMtByO1pXal1EI7UnepVWTgszMyoDO5oTOFpTQxozQCpDOzAyOsxWduByO5AzN3YzN4IDMyYTO2YDOgszMzMGZ0EWYwIGM3UmY1ATN");
            connection.setRequestProperty("X-Dark-Mode", "0");
            connection.setRequestProperty("Host", "api.coolapk.com");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept-Encoding", "gzip");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
 
    /**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //1.获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //2.中文有乱码的需要将PrintWriter改为如下
            //out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8")
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        System.out.println("post推送结果："+result);
        return result;
    }
    /*
    public static void main(String[] args) {
        //发送 GET 请求
        String s=HttpRequest.sendGet("http://localhost:6144/Home/RequestString", "key=123&v=456");
        System.out.println(s);
        
        //发送 POST 请求
        String sr=HttpRequest.sendPost("http://localhost:6144/Home/RequestPostString", "key=123&v=456");
        System.out.println(sr);
    }*/
}