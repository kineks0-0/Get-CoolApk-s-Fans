package com.WordCloud.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestException;
import java.util.List;

import javax.imageio.ImageIO;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

public class App {
    public static void main(String[] args) throws IOException, DigestException {
        try {

            // 请先按注释把参数修改,否则可能导致一些问题发生
            // 矩形
            GetFansWordColud(2086596, 1000, 1000, "华康圆体W7(P)", 16, "E:\\WordCloud\\FansWordColud2.png",
                    "E:\\WordCloud\\fansList.txt");

            // 使用图片为背景蒙版,使用前先把图片准备好
            GetFansWordColud(2086596, "华康圆体W7(P)", 16, "E:\\WordCloud\\FansWordColud.png",
                    "E:\\WordCloud\\fansList.txt",
                    "E:\\WordCloud\\1571410668531_61da37cff600702232e0335212033656_1_6.png");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有粉丝的用户名,之后生成词云图片(使用图片背景蒙版) 注意使用的图片需要自行把不需要渲染的部分擦除成透明像素,否则和矩形一样
     * 
     * @param ID              用户ID
     * @param FontStr         设置使用的字体,例如 '宋体'
     * @param FontSize        设置使用的字体大小,例如 16
     * @param OutputFile      设置写出的路径
     * @param OutputFanslist  设置粉丝数据写出的路径
     * @param BackgroundImage 设置词云使用的背景蒙版
     * 
     */
    public static void GetFansWordColud(int ID, String FontStr, int FontSize, String OutputFile, String OutputFanslist,
            String BackgroundImage) {
        try {

            String UsersName = JSON.getUsersName(ID, "https://api.coolapk.com/v6/user/fansList?uid=");// 自动爬取粉丝数据,返回所有的用户名都以'\n'隔开
            writeFile.writetoFile(OutputFanslist, UsersName);// 写出数据,完成爬取
            System.out.println("GET FansName Done.");

            // 词云的建议去看官方的文档 https://github.com/kennycason/kumo
            // 也可以去看看这个 https://blog.csdn.net/qq_14853889/article/details/79283251

            FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            frequencyAnalyzer.setWordFrequenciesToReturn(300);
            frequencyAnalyzer.setMinWordLength(1);// frequencyAnalyzer.setWordTokenizer(new //
                                                  // ChineseWordTokenizer());//中文的解词

            List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(OutputFanslist);

            int[] WH = getImageWidthHeight(BackgroundImage);// 获取图片的宽高
            Dimension dimension = new Dimension(WH[0], WH[1]);// 640, 566);//这里如果用图片作为生成蒙版,则需要和图片分辨率一样

            // WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);//矩形
            WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);// 完美像素大概指的是完成填充
            wordCloud.setBackground(new PixelBoundryBackground(getInputStream(BackgroundImage)));// 设置背景蒙版使用图片,注意图片需要自行把不需要渲染的部分擦除成透明像素,否则会和矩形一样
            wordCloud.setPadding(0);
            java.awt.Font font = new java.awt.Font(FontStr, Font.PLAIN, FontSize);// 设置字体,如果找不到设置的就会使用默认字体,字号请自行看情况调整

            wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1),
                    new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
            wordCloud.setKumoFont(new KumoFont(font));
            wordCloud.setFontScalar(new LinearFontScalar(10, 40));
            wordCloud.setBackgroundColor(new Color(0, 0, 0));
            wordCloud.build(wordFrequencies);
            wordCloud.writeToFile(OutputFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有粉丝的用户名,之后生成词云图片(矩形)
     * 
     * @param ID             用户ID
     * @param width          设置生成词云图片的宽
     * @param height         设置生成词云图片的高
     * @param FontStr        设置使用的字体,例如 '宋体'
     * @param FontSize       设置使用的字体大小,例如 16
     * @param OutputFile     设置写出的路径
     * @param OutputFanslist 设置粉丝数据写出的路径
     * 
     */
    public static void GetFansWordColud(int ID, int width, int height, String FontStr, int FontSize, String OutputFile,
            String OutputFanslist) {
        try {

            String UsersName = JSON.getUsersName(ID, "https://api.coolapk.com/v6/user/fansList?uid=");// 自动爬取粉丝数据,返回所有的用户名都以'\n'隔开
            writeFile.writetoFile(OutputFanslist, UsersName);// 写出数据,完成爬取
            System.out.println("GET FansName Done.");

            // 词云的建议去看官方的文档 https://github.com/kennycason/kumo
            // 也可以去看看这个 https://blog.csdn.net/qq_14853889/article/details/79283251

            FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            frequencyAnalyzer.setWordFrequenciesToReturn(300);
            frequencyAnalyzer.setMinWordLength(1);// frequencyAnalyzer.setWordTokenizer(new //
                                                  // ChineseWordTokenizer());//中文的解词

            List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(OutputFanslist);

            Dimension dimension = new Dimension(width, height);

            WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);// 矩形
            // WordCloud wordCloud = new WordCloud(dimension,
            // CollisionMode.PIXEL_PERFECT);//完美像素 大概是完成填充
            wordCloud.setBackground(new RectangleBackground(dimension));
            wordCloud.setPadding(0);
            java.awt.Font font = new java.awt.Font(FontStr, Font.PLAIN, FontSize);// 设置字体,如果找不到设置的就会使用默认字体,字号请自行看情况调整

            wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1),
                    new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
            wordCloud.setKumoFont(new KumoFont(font));
            wordCloud.setFontScalar(new LinearFontScalar(10, 40));
            wordCloud.setBackgroundColor(new Color(0, 0, 0));
            wordCloud.build(wordFrequencies);
            wordCloud.writeToFile(OutputFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static InputStream getInputStream(String str) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(str);
        // 读取文件的数据到字节流inputStream。
        return inputStream;
    }

    private static int[] getImageWidthHeight(String str) {
        int[] Width_Height = new int[2];
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(str));
            Width_Height[0] = bufferedImage.getWidth();
            Width_Height[1] = bufferedImage.getHeight();
            return Width_Height;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Width_Height;
    }
}
