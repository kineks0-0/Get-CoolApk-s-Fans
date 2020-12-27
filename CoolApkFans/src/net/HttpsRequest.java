package net;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpsRequest {


    public interface OnSetRequestMethodListener {
        HttpURLConnection onSetRequestMethod(HttpURLConnection conn);
    }

    static class MyX509TrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
        public X509Certificate[] getAcceptedIssuers() { return null; }
    }



    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url 发送请求的URL
     *
     * @param onSetRequestMethodListener 对请求头设置的回调
     *
     * @return String 所代表远程资源的响应结果
     */
    public static String sendGet(String url,OnSetRequestMethodListener onSetRequestMethodListener)
            throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException{
        String result = null;
        URL MyUrl = new URL(url);
        if (MyUrl.getProtocol().equalsIgnoreCase("https")) {
            result = getReturn(httpsSendGet(MyUrl,onSetRequestMethodListener));
        } else {
            result = getReturn(httpSendGet(MyUrl,onSetRequestMethodListener));
        }
        return result;
    }



    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url 发送请求的URL
     *
     * @param onSetRequestMethodListener 对请求头设置的回调
     *
     * @return Byte[] 所代表远程资源的响应结果
     */
    public static byte[] getByteArray(String url,OnSetRequestMethodListener onSetRequestMethodListener)
            throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException{

        byte[] result = null;
        URL MyUrl = new URL(url);
        if (MyUrl.getProtocol().equalsIgnoreCase("https")) {
            result = toByteArray(httpsSendGet(MyUrl,onSetRequestMethodListener));
        } else {
            result = toByteArray(httpSendGet(MyUrl,onSetRequestMethodListener));
        }
        return result;
    }



    /**
     * 向指定URL发送GET方法的请求(Http)
     *
     * @param url 发送请求的URL
     *
     * @return 返回一个输入流(InputStream)
     */
    public static InputStream httpSendGet(URL url,OnSetRequestMethodListener onSetRequestMethodListener)
            throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

        if(onSetRequestMethodListener != null) {
            onSetRequestMethodListener.onSetRequestMethod(conn);
        }

        conn.connect();

        return conn.getInputStream();
    }



    /**
     * 向指定URL发送GET方法的请求(Https)
     *
     * @param url 发送请求的URL
     *
     * @return 返回一个输入流(InputStream)
     */
    public static InputStream httpsSendGet(URL myURL,OnSetRequestMethodListener onSetRequestMethodListener)
            throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm = {new MyX509TrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
        HttpsURLConnection conn = (HttpsURLConnection) myURL.openConnection();
        conn.setSSLSocketFactory(ssf);
        // 之后和获取http的使用方法一样

        conn.setRequestMethod("GET");

        if(onSetRequestMethodListener != null) {
            onSetRequestMethodListener.onSetRequestMethod(conn);
        }

        conn.connect();
        return conn.getInputStream();
    }


    /**
     * 将返回的输入流(InputStream)转换字符串(String)
     *
     * @param InputStream 待转换的输入流(InputStream)
     *
     * @return 返回一个字符串(String)
     */
    public static String getReturn(InputStream inputStream) throws IOException {
        StringBuilder buffer = new StringBuilder();
        // 将返回的输入流转换成字符串
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.defaultCharset());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String strCache = null;
        while ((strCache = bufferedReader.readLine()) != null) {
            buffer.append(strCache);
        }
        return buffer.toString();
    }


    /**
     * InputStream 转换 ByteArray
     *
     * @param input InputStream
     *
     * @return 返回一个字节数组(ByteArray)
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int byteCache = 0;
        while (-1 != (byteCache = input.read(buffer))) {
            output.write(buffer, 0, byteCache);
        }
        return output.toByteArray();
    }

}
