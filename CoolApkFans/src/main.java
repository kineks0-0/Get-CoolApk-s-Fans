import api.CoolApkFansApi;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.bg.PixelBoundaryBackground;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class main {

    public static void main(String[] args) {
        //System.out.println(CoolApkFansApi.getRawCoolApkFansJson(2086596));
        //CoolApkFansApi.getFansData(2086596);
        //System.out.println(CoolApkFansApi.getFansName(2086596));
        /*ArrayList<String> fansDataList = CoolApkFansApi.getFansName(2086596);
        StringBuilder FansName = new StringBuilder();
        for (String str:fansDataList) {
            FansName.append(str).append("\n");
        }
        FileWrite.write2File("./FansNameList.txt",FansName.toString());
        System.out.println(FansName);*/

        ArrayList<api.dataclass.Data> fansData = runOnGetOldFansData();
        if (fansData == null) {
            fansData = runOnGetNewFansData(2086596);
        }


        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(2);

        //引入中文解析器
        //frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());

        //List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("./FansNameList.txt");
        List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(CoolApkFansApi.getFansName(fansData));
        for (int x=0; x!=wordFrequencies.size(); x++) {
            if (fansData.get(x).getIsfriend() != 0) {
                WordFrequency w = wordFrequencies.get(x);
                wordFrequencies.set(x, new WordFrequency(w.getWord(), w.getFrequency()+4));
            }
        }
        getFansWordColud("黑体", 18, 2000, 2000,
                "./FansWordCloud.png",wordFrequencies);
        getFansWordCloudWithImage("黑体", 18,
                "./FansWordCloud.png","H:\\WordCloud\\UserFace.png",wordFrequencies);
    }



    /**
     * 获取所有粉丝的用户名,之后生成词云图片(使用图片背景蒙版) 注意使用的图片需要自行把不需要渲染的部分擦除成透明像素,否则和矩形一样
     *
     * @param fontStr         设置使用的字体,例如 '宋体'
     * @param fontSize        设置使用的字体大小,例如 16
     * @param outputFile      设置写出的路径
     * @param backgroundImage 设置词云使用的背景蒙版
     * @param backgroundImage 设置词云使用的背景蒙版
     * @param wordFrequencies 设置词云数据
     *
     */
    public static void getFansWordCloudWithImage(String fontStr, int fontSize, String outputFile, String backgroundImage,
                                        List<WordFrequency> wordFrequencies) {
        try {

            int[] WH = getImageWidthHeight(backgroundImage);  // 获取图片的宽高
            Dimension dimension = new Dimension(WH[0], WH[1]);//这里如果用图片作为生成蒙版,则需要和图片分辨率一样

            // WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);//矩形
            WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);// 完美像素大概指的是完成填充
            wordCloud.setBackground(new PixelBoundaryBackground(backgroundImage));// 设置背景蒙版使用图片,注意图片需要自行把不需要渲染的部分擦除成透明像素,否则会和矩形一样
            wordCloud.setPadding(0);
            java.awt.Font font = new java.awt.Font(fontStr, Font.PLAIN, fontSize);// 设置字体,如果找不到设置的就会使用默认字体,字号请自行看情况调整

            wordCloud.setColorPalette(new ColorPalette(new Color(0x5A3DFF), new Color(0x334EE0), new Color(0x29C1E7),
                    new Color(0x39D4BC), new Color(0x53C988), new Color(0xB1C15F)));
            wordCloud.setKumoFont(new KumoFont(font));
            wordCloud.setFontScalar(new LinearFontScalar(10, 40));
            wordCloud.setBackgroundColor(new Color(0, 0, 0));
            wordCloud.build(wordFrequencies);
            wordCloud.writeToFile(outputFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有粉丝的用户名,之后生成词云图片(矩形)
     *
     * @param width           设置生成词云图片的宽
     * @param height          设置生成词云图片的高
     * @param fontStr         设置使用的字体,例如 '宋体'
     * @param fontSize        设置使用的字体大小,例如 16
     * @param outputFile      设置写出的路径
     * @param wordFrequencies 设置词云数据
     *
     */
    public static void getFansWordColud(String fontStr, int fontSize, int width, int height, String outputFile,
                                        List<WordFrequency> wordFrequencies) {
        try {

            // 词云的建议去看官方的文档 https://github.com/kennycason/kumo
            // 也可以去看看这个 https://blog.csdn.net/qq_14853889/article/details/79283251


            /*final Dimension dimension = new Dimension(600, 600);
            final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
            wordCloud.setPadding(0);
            wordCloud.setBackground(new RectangleBackground(dimension));
            wordCloud.setColorPalette(new ColorPalette(Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE));
            wordCloud.setFontScalar(new LinearFontScalar(10, 40));
            wordCloud.build(wordFrequencies);
            wordCloud.writeToFile("./wordcloud_rectangle.png");*/


            Dimension dimension = new Dimension(width, height);

            //WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);// 矩形
            WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);//完美像素 大概是完成填充
            wordCloud.setBackground(new RectangleBackground(dimension));
            wordCloud.setPadding(0);
            java.awt.Font font = new java.awt.Font(fontStr, Font.PLAIN, fontSize);// 设置字体,如果找不到设置的就会使用默认字体,字号请自行看情况调整

            wordCloud.setColorPalette(new ColorPalette(new Color(0x5A3DFF), new Color(0x334EE0), new Color(0x29C1E7),
                    new Color(0x39D4BC), new Color(0x53C988), new Color(0xB1C15F)));
            //wordCloud.setColorPalette(new ColorPalette(Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE));
            wordCloud.setKumoFont(new KumoFont(font));
            wordCloud.setFontScalar(new LinearFontScalar(10, 40));
            wordCloud.setBackgroundColor(new Color(0, 0, 0));
            wordCloud.build(wordFrequencies);
            wordCloud.writeToFile(outputFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static ArrayList<api.dataclass.Data> runOnGetNewFansData(int userID) {
        ArrayList<api.dataclass.Data> fansData = CoolApkFansApi.getFansData(userID);
        try {
            FileOutputStream fileOut = new FileOutputStream("FansDataList.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(fansData);
            out.close();
            fileOut.close();
            System.out.print("Serialized data is saved in " + fileOut.toString());
        } catch(IOException i) {
            i.printStackTrace();
        }
        return fansData;
    }

    static ArrayList<api.dataclass.Data> runOnGetOldFansData() {
        ArrayList<api.dataclass.Data> fansData = null;
        try {
            FileInputStream fileIn = new FileInputStream("FansDataList.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            fansData = (ArrayList<api.dataclass.Data>) in.readObject();
            in.close();
            fileIn.close();
        } catch(IOException i) {
            i.printStackTrace();
            return null;
        } catch(ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
            return null;
        }
        System.out.println("Deserialized Employee...");
        return fansData;
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
