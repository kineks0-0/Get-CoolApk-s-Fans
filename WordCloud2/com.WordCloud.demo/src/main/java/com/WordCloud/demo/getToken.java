package com.WordCloud.demo;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class getToken {

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(getXToken());
    }

    /**
     * 获取发送头中的X-Token
     * 
     * @return 自动返回
     */
    public static String getXToken() throws UnsupportedEncodingException {
        //不要问为什么令名不规范(
        //参考了 https://www.jianshu.com/p/0e4f2dc0e919 的文档
        String totken_P1 = null;
        String deviceID = "66968dc0-829e-33f2-985d-900cfa72e93a";// "8513efac-09ea-3709-b214-95b366f1a185";
        Long t2L = getTime();
        String t = String.valueOf(t2L);
        String t_M = stringToMD5(t);
        String hexString = String.format("%08X", t2L).toLowerCase();

        String a = "token://com.coolapk.market/c67ef5943784d09750dcfbb31020f0ab?";

        totken_P1 = a + t_M + "$" + deviceID + "&com.coolapk.market";

        String Base_a = Base64(totken_P1);

        String totken_P2 = stringToMD5(Base_a) + deviceID + "0x" + hexString;
        return totken_P2;
    }

    public static String getMD5String(String str) {
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

    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static String Base64(String text) throws UnsupportedEncodingException {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] textByte = text.getBytes("UTF-8");
        String encodedText = encoder.encodeToString(textByte);
        return encodedText;
    }

    public static long getTime() {

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