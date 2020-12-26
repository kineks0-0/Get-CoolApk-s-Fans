package api;


import api.dataclass.Data;
import api.dataclass.FanInfo;
import api.dataclass.UserInfoRootBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import net.HttpsRequest;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class CoolApkFansApi {

    private final static String UA = "Dalvik/2.1.0 (Linux; U; Android 5.1; m1 note Build/LMY47D) (#Build; Meizu; m1 note; Flyme 6.3.0.2A; 5.1) +CoolMarket/9.6-1909191";

    static class Token {

        // 参考了 https://www.jianshu.com/p/0e4f2dc0e919 的文档
        protected static String getXToken(String deviceID) {
            try {
                return getXTokenWithException(deviceID);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: On " + Token.class.getCanonicalName() + " Callback Null on Token to Base64 ");
                return "NullCallBack";
            }
        }

        // 参考了 https://www.jianshu.com/p/0e4f2dc0e919 的文档
        protected static String getXTokenWithException(String deviceID) throws Exception {
            String tokenP1;
            //String deviceID = "66968dc0-829e-33f2-985d-900cfa72e93a";// "8513efac-09ea-3709-b214-95b366f1a185";
            Long time2long = getTime();
            String time = String.valueOf(time2long);
            String timeMin = stringToMD5(time);
            String timeHexString = String.format("%08X", time2long).toLowerCase();

            String head = "token://com.coolapk.market/c67ef5943784d09750dcfbb31020f0ab?";
            tokenP1 = head + timeMin + "$" + deviceID + "&com.coolapk.market";
            String BaseHead;

            try {
                BaseHead = Base64(tokenP1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new Exception("GetTokenError : base64 token return null");
            }

            return stringToMD5(BaseHead) + deviceID + "0x" + timeHexString;
        }


        protected static String getMD5String(String str) {
            try {
                // 生成一个MD5加密计算摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 计算md5函数
                md.update(str.getBytes());
                // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
                // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
                // 一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
                return new BigInteger(1, md.digest()).toString(16);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected static String stringToMD5(String plainText) {
            byte[] secretBytes = null;
            try {
                secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("没有这个md5算法！");
            }
            StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
            for (int i = 0; i < 32 - md5code.length(); i++) {
                md5code.insert(0, "0");
            }
            return md5code.toString();
        }

        protected static String Base64(String text) throws UnsupportedEncodingException {
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] textByte = text.getBytes(StandardCharsets.UTF_8);
            return encoder.encodeToString(textByte);
        }

        protected static long getTime() {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String day = sdf.format(new Date());
            long unixtimstamp1 = -1;
            try {
                unixtimstamp1 = (sdf.parse(day).getTime() / 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return unixtimstamp1;
        }
    }

    public static ArrayList<api.dataclass.Data> getFansData(int userID) {

        ArrayList<Data> userInfos ;
        String rawJSON = getRawCoolApkFansJson(userID,1);
        //parse POJO List对象
        Type type = new TypeReference<UserInfoRootBean>() {}.getType();
        UserInfoRootBean fansData = JSON.parseObject(rawJSON,type);

        userInfos = new ArrayList<Data>(fansData.getData());

        int fans = fansData.getData().get(0).getFUserInfo().getFans();
        int pageSize = fans / 20;
        if (fans % 20 != 0) {
            pageSize++;
        }
        // 计算出一共有多少页(PageSize)

        for(int i=1; i!=pageSize+1; i++){
            fansData = JSON.parseObject(getRawCoolApkFansJson(userID,i+1),type);
            userInfos.addAll(fansData.getData());
        }

        return userInfos;
    }

    public static ArrayList<String> getFansName(int userID) {
        return getFansName(getFansData(userID));
    }

    public static ArrayList<String> getFansName(ArrayList<Data> userInfos) {
        ArrayList<String> fansName = new ArrayList<>();

        final int size = userInfos.size();
        int l = 0;
        while (l != size) {
            fansName.add(userInfos.get(l).getUsername());
            l++;
        }
        return fansName;
    }

    public static String getRawCoolApkFansJson(int userID,int page) {
        try {
            return HttpsRequest.sendGet("https://api.coolapk.com/v6/user/fansList?uid=" + userID + "&page=" + page,
                    new HttpsRequest.OnSetRequestMethodListener() {
                @Override
                public HttpURLConnection onSetRequestMethod(HttpURLConnection conn) {
                    // 设置通用的请求属性
                    // conn.setRequestProperty("Content-type",
                    // "application/x-www-form-urlencoded;charset=UTF-8");// "application/json");
                    // conn.setRequestProperty("accept", "*/*");
                    conn.setRequestProperty("User-Agent", UA);
                    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                    conn.setRequestProperty("X-Sdk-Int", "22");
                    conn.setRequestProperty("X-Sdk-Locale", "zh-CN");
                    conn.setRequestProperty("X-App-Id", "com.coolapk.market");
                    conn.setRequestProperty("X-App-Token", Token.getXToken("66968dc0-829e-33f2-985d-900cfa72e93a"));
                    conn.setRequestProperty("X-App-Version", "9.6");
                    conn.setRequestProperty("X-App-Code", "1909191");
                    conn.setRequestProperty("X-Api-Version", "9");
                    conn.setRequestProperty("X-App-Device",
                            "lR3buBSMtByO1pXal1EI7UnepVWTgszMyoDO5oTOFpTQxozQCpDOzAyOsxWduByO5AzN3YzN4IDMyYTO2YDOgszMzMGZ0EWYwIGM3UmY1ATN");
                    conn.setRequestProperty("X-Dark-Mode", "0");
                    conn.setRequestProperty("Host", "api.coolapk.com");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    // conn.setRequestProperty("Accept-Encoding", "gzip");//如果使用了 gzip 压缩需要自己处理

                    // 设置 false 表示 不redirect 到重定向后的地址
                    conn.setInstanceFollowRedirects(false);
                    return conn;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
