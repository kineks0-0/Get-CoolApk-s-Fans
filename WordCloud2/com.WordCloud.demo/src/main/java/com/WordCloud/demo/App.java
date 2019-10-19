package com.WordCloud.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestException;
import java.util.List;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
//import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;

public class App {
    public static void main(String[] args) throws IOException, DigestException {
        try {
            
            //如果想改成自己的,只要把 UserID 改成自己的就行
            String UsersName = JSON.getUsersName(2086596, "https://api.coolapk.com/v6/user/fansList?uid=");
            //会自动爬取粉丝数据,然后返回所有的用户名,以'\n'隔开
            writeFile.writetoFile("E:\\WordCloud\\fansList.txt", UsersName);
            //写出数据,完成爬取
            System.out.println("GET FansName Done.");

            //这里建议去看官方的文档 https://github.com/kennycason/kumo
            //也可以去看看这个 https://blog.csdn.net/qq_14853889/article/details/79283251

            FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            frequencyAnalyzer.setWordFrequenciesToReturn(300);
            frequencyAnalyzer.setMinWordLength(1);
            //frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
            List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("E:\\WordCloud\\fansList.txt");//读取文件
            Dimension dimension = new Dimension(640, 566);//这里如果用图片作为生成蒙版,则需要和图片分辨率一样

            //WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);//矩形
            WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);//完美像素大概指的是完成填充

            wordCloud.setBackground(new PixelBoundryBackground(getInputStream("E:\\WordCloud\\1571410668531_61da37cff600702232e0335212033656_1_.png")));
            //设置背景蒙版使用图片,注意图片需要自行把不需要渲染的部分擦除成透明像素,否则会和矩形一样

            //wordCloud.setBackground(new RectangleBackground(dimension));
            wordCloud.setPadding(0);//设置边界
            java.awt.Font font = new java.awt.Font("华康圆体W7(P)", Font.PLAIN, 20);//设置字体,如果找不到设置的就会使用默认字体
            wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1),
                    new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));//设置颜色,
            wordCloud.setKumoFont(new KumoFont(font));
            wordCloud.setFontScalar(new LinearFontScalar(10, 40));
            wordCloud.setBackgroundColor(new Color(0,0,0));
            wordCloud.build(wordFrequencies);
            wordCloud.writeToFile("E:\\WordCloud\\FansWordColud.png");
            
        } catch (Exception e) {
            System.out.println("BUG");
        }
        // System.out.println("Hello World!");

    }

    private static InputStream getInputStream(String str) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(str);
        // 读取文件的数据到字节流inputStream。
        return inputStream;
    }
}
