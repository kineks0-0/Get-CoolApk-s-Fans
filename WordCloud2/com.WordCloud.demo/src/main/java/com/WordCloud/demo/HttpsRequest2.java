package com.WordCloud.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class HttpsRequest2 {

    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * 
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url)
            throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

        URL myURL = new URL(url);
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm = { new MyX509TrustManager() };
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
        HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();
        httpsConn.setSSLSocketFactory(ssf);
        // 之后和获取http的使用方法一样

        HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
        conn.setRequestMethod("GET");
        // 设置通用的请求属性
        //conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");// "application/json");
        //conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("User-Agent","Dalvik/2.1.0 (Linux; U; Android 5.1; m1 note Build/LMY47D) (#Build; Meizu; m1 note; Flyme 6.3.0.2A; 5.1) +CoolMarket/9.6-1909191");
        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        conn.setRequestProperty("X-Sdk-Int", "22");
        conn.setRequestProperty("X-Sdk-Locale", "zh-CN");
        conn.setRequestProperty("X-App-Id", "com.coolapk.market");
        conn.setRequestProperty("X-App-Token", getToken.getXToken());
        conn.setRequestProperty("X-App-Version", "9.6");
        conn.setRequestProperty("X-App-Code", "1909191");
        conn.setRequestProperty("X-Api-Version", "9");
        conn.setRequestProperty("X-App-Device",
                "lR3buBSMtByO1pXal1EI7UnepVWTgszMyoDO5oTOFpTQxozQCpDOzAyOsxWduByO5AzN3YzN4IDMyYTO2YDOgszMzMGZ0EWYwIGM3UmY1ATN");
        conn.setRequestProperty("X-Dark-Mode", "0");
        conn.setRequestProperty("Host", "api.coolapk.com");
        conn.setRequestProperty("Connection", "Keep-Alive");
        //conn.setRequestProperty("Accept-Encoding", "gzip");//如果使用了 gzip 压缩会导致返回的数据乱码

        // 必须设置false，否则会自动redirect到重定向后的地址
        conn.setInstanceFollowRedirects(true);
        conn.connect();
        String result = getReturn(conn);
        return result;
    }

    /* 请求url获取返回的内容 */
    public static String getReturn(HttpURLConnection connection) throws IOException {
        StringBuffer buffer = new StringBuffer();
        // 将返回的输入流转换成字符串
        try (InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, ConstantInfo.CHARSET);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            String result = buffer.toString();
            return result;
        }
    }
}
