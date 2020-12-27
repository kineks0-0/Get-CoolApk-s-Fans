import api.CoolApkFansApi;
import api.dataclass.Data;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.bg.PixelBoundaryBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FansWordCloud {

    //部分默认值
    //字体
    static Font font = new Font("黑体",Font.BOLD, 22);
    //词云颜色，越靠前的颜色权重越高
    static ColorPalette colorPalette = new ColorPalette(new Color(0x1C1CD3), new Color(0x6D0090), new Color(0xB1B1FE),
            new Color(0xF40009), new Color(0xBA0044), new Color(0xA0005D), new Color(0xFDFDFE));
    //词云背景颜色
    static Color backgroundColor = new Color(0, 0, 1);


    //配置词云数据来源输出
    final static class WordCloudFilePath {
        final int userID;
        int width;
        int height;
        final String name;
        final String file;
        final String path;
        ArrayList<Data> fansDataList;

        public WordCloudFilePath(int userID, int width, int height, String name) {
            this.userID = userID;
            this.width = width;
            this.height = height;
            this.name = name;

            fansDataList = getFansDataList();

            file = fansDataList.get(0).getFuid() + "-" + fansDataList.get(0).getFusername()
                    + "-" + width + "x" + height
                    + "-FansWordCloud" + name + ".png";
            path = "./pic/" + file;
        }

        public WordCloudFilePath(int userID, String name, String backgroundImage) {
            int[] WH = getImageWidthHeight(backgroundImage);// 获取图片的宽高
            this.userID = userID;
            this.width = WH[0];
            this.height = WH[1];
            this.name = name;

            fansDataList = getFansDataList();

            file = fansDataList.get(0).getFuid() + "-" + fansDataList.get(0).getFusername()
                    + "-" + width + "x" + height
                    + "-FansWordCloud" + name + ".png";
            path = "./pic/" + file;
        }

        protected ArrayList<Data> getFansDataList() {
            ArrayList<Data> fansDataList = runOnGetOldFansData(userID);
            if (fansDataList == null) {
                fansDataList = runOnGetNewFansData(userID);
            }
            return fansDataList;
        }

        public List<WordFrequency> getWordFrequencies() {
            if (fansDataList == null) {
                System.out.println("Error: in getWordFrequencies() fansDataList == null");
                fansDataList = getFansDataList();
            }
            return FansWordCloud.getWordFrequencies(fansDataList);
        }

    }


    //重载方法
    public static WordCloudFilePath getFansWordCloudFilePathWithName(int userID, int width, int height, String name) {
        return new WordCloudFilePath(userID,width,height,name);
    }
    public static WordCloudFilePath getFansWordCloudFilePath(int userID, int width, int height) {
        return new WordCloudFilePath(userID,width,height,"");
    }

    public static WordCloudFilePath getFansWordCloudFilePathWithImage(int userID, int width, int height) {
        return new WordCloudFilePath(userID,width,height,"WithImage");
    }
    public static WordCloudFilePath getFansWordCloudFilePathWithImage(int userID, String backgroundImage) {
        return new WordCloudFilePath(userID,"WithImage",backgroundImage);
    }




    /**
     * 获取所有粉丝的用户名,之后生成词云图片(矩形)
     *
     * @param userID          设置用户词云数据来源
     * @param width           设置生成词云图片的宽
     * @param height          设置生成词云图片的高
     *
     */
    public static void getFansWordCloud(int userID, int width, int height) {
        WordCloudFilePath fileName = getFansWordCloudFilePath(userID,width,height);
        getFansWordCloud(font, width, height, fileName.path, fileName.getWordFrequencies());
    }

    /**
     * 获取所有粉丝的用户名,之后生成词云图片(使用图片背景蒙版) 注意使用的图片需要自行把不需要渲染的部分擦除成透明像素,否则和矩形一样
     *
     * @param userID          设置用户词云数据来源
     * @param backgroundImage 设置词云使用的背景蒙版
     *
     */
    public static void getFansWordCloudWithImage(int userID, String backgroundImage) {
        WordCloudFilePath fileName = getFansWordCloudFilePathWithImage(userID,backgroundImage);
        getFansWordCloudWithImage("黑体", 20,
                fileName.path, backgroundImage,fileName.getWordFrequencies());
    }



    /**
     * 获取所有粉丝的用户名,之后生成词云图片(矩形)
     *
     * @param userID          设置用户词云数据来源
     * @param width           设置生成词云图片的宽
     * @param height          设置生成词云图片的高
     * @param outputFile      设置写出的路径
     *
     */
    public static void getFansWordCloud(int userID, int width, int height, String outputFile) {
        getFansWordCloud(font, width, height,
                outputFile,getWordFrequencies(userID));
    }

    /**
     * 获取所有粉丝的用户名,之后生成词云图片(使用图片背景蒙版) 注意使用的图片需要自行把不需要渲染的部分擦除成透明像素,否则和矩形一样
     *
     * @param userID          设置用户词云数据来源
     * @param outputFile      设置写出的路径
     * @param backgroundImage 设置词云使用的背景蒙版
     *
     */
    public static void getFansWordCloudWithImage(int userID, String outputFile, String backgroundImage) {
        getFansWordCloudWithImage("黑体", 20, outputFile,backgroundImage,getWordFrequencies(userID));
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
    public static void getFansWordCloud(String fontStr, int fontSize, int width, int height, String outputFile,
                                        List<WordFrequency> wordFrequencies) {

        java.awt.Font font = new java.awt.Font(fontStr, Font.BOLD, fontSize);
        // 设置字体,如果找不到设置的就会使用默认字体,字号请自行看情况调整
        getFansWordCloud(font,width,height,outputFile,wordFrequencies);
    }






    //工作代码

    /**
     * 获取所有粉丝的用户名,之后生成词云图片(矩形)
     *
     * @param width           设置生成词云图片的宽
     * @param height          设置生成词云图片的高
     * @param font            设置使用的字体
     * @param outputFile      设置写出的路径
     * @param wordFrequencies 设置词云数据
     *
     */
    public static void getFansWordCloud(Font font, int width, int height, String outputFile,
                                        List<WordFrequency> wordFrequencies) {

        // 词云的建议去看官方文档 https://github.com/kennycason/kumo
        // 也可以自己去谷歌,不过依赖的坑文档居然没说


        Dimension dimension = new Dimension(width, height);

        //WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);// 矩形
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);//完美像素 大概是尽量填充
        wordCloud.setBackground(new RectangleBackground(dimension));
        wordCloud.setPadding(3);

        wordCloud.setColorPalette(colorPalette);//词云颜色

        ////wordCloud.setColorPalette(new ColorPalette(Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE));//官方文档默认的
        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.setBackgroundColor(backgroundColor);
        //wordCloud.setBackgroundColor(new Color(255, 255, 255));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile(outputFile);
    }


    //完全自定义

    /**
     * 获取所有粉丝的用户名,之后生成词云图片(矩形)
     *
     * @param font              设置词云图片的字体
     * @param colorPalette      设置词云图片的配色
     * @param backgroundColor   设置词云图片背景色
     * @param collisionMode     设置词云如何填充字
     * @param padding           设置词云字词间隔值
     * @param background        设置词云画板的背景
     * @param dimension         设置词云绘制的画板
     * @param wordCloudFilePath 设置词云输出的配置
     * @param wordFrequencies   设置词云数据的来源
     *
     */
    public static void getFansWordCloud(Font font, ColorPalette colorPalette, Color backgroundColor,
                                        CollisionMode collisionMode, int padding,
                                        Background background, Dimension dimension,
                                        WordCloudFilePath wordCloudFilePath,
                                        List<WordFrequency> wordFrequencies) {

        // 词云的建议去看官方文档 https://github.com/kennycason/kumo
        // 也可以自己去谷歌,不过依赖的坑文档居然没说

        //WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);// 矩形
        WordCloud wordCloud = new WordCloud(dimension, collisionMode);//完美像素 大概是尽量填充
        wordCloud.setBackground(background);
        wordCloud.setPadding(padding);

        wordCloud.setColorPalette(colorPalette);//词云颜色

        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.setBackgroundColor(backgroundColor);
        //wordCloud.setBackgroundColor(new Color(255, 255, 255));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile(wordCloudFilePath.path);
    }


    /**
     * 获取所有粉丝的用户名,之后生成词云图片(使用图片背景蒙版) 注意使用的图片需要自行把不需要渲染的部分擦除成透明像素,否则和矩形一样
     *
     * @param fontStr         设置使用的字体,例如 '宋体'
     * @param fontSize        设置使用的字体大小,例如 16
     * @param outputFile      设置写出的路径
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
            wordCloud.setPadding(2);
            java.awt.Font font = new java.awt.Font(fontStr, Font.BOLD, fontSize);// 设置字体,如果找不到设置的就会使用默认字体,字号请自行看情况调整

            wordCloud.setColorPalette(new ColorPalette(new Color(0x1C1CD3), new Color(0x6D0090), new Color(0xB1B1FE),
                    new Color(0xF40009), new Color(0xBA0044), new Color(0xA0005D), new Color(0xFDFDFE)));//词云颜色

            wordCloud.setKumoFont(new KumoFont(font));
            wordCloud.setFontScalar(new LinearFontScalar(10, 40));
            wordCloud.setBackgroundColor(new Color(0, 0, 0));
            wordCloud.build(wordFrequencies);
            wordCloud.writeToFile(outputFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //以下为解耦分出的代码


    //将字串符包装为数据流
    public static InputStream getStringStream(String sInputString) {
        if (sInputString != null && !sInputString.trim().equals("")){
            return new ByteArrayInputStream(sInputString.getBytes());
        }
        return null;
    }

    //返回词云list,使用 KUAM 的中文分词功能
    protected static List<WordFrequency> getWordFrequenciesWithChineseWordTokenizer(ArrayList<Data> fansDataList, List<String> stopWords) {

        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(1000);
        frequencyAnalyzer.setMinWordLength(2);
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
        if (stopWords!=null) {
            frequencyAnalyzer.setStopWords(stopWords);
        }

        StringBuilder fansName = new StringBuilder();
        for ( String str: CoolApkFansApi.getFansName(fansDataList)) {
            fansName.append(str).append("\n");
        }

        try {
            return frequencyAnalyzer.load(getStringStream(fansName.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //重载方法
    protected static List<WordFrequency> getWordFrequenciesWithChineseWordTokenizer(int userID,List<String> stopWords) {
        ArrayList<Data> fansDataList = runOnGetOldFansData(userID);
        if (fansDataList == null) {
            fansDataList = runOnGetNewFansData(userID);
        }
        return getWordFrequenciesWithChineseWordTokenizer(fansDataList,stopWords);
    }
    //重载方法
    protected static List<WordFrequency> getWordFrequenciesWithChineseWordTokenizer(int userID) {
        return getWordFrequenciesWithChineseWordTokenizer(userID,null);
    }
    //重载方法
    protected static List<WordFrequency> getWordFrequenciesWithChineseWordTokenizer(ArrayList<Data> fansDataList) {
        return getWordFrequenciesWithChineseWordTokenizer(fansDataList,null);
    }



    //自定义词云list,如果检查到为互相关注则提高权重为3否则为1
    protected static List<WordFrequency> getWordFrequencies(ArrayList<Data> fansDataList) {

        List<WordFrequency> wordFrequencies = new ArrayList<>();

        Data userData;
        for (int index=0; index!=fansDataList.size(); index++) {
            userData = fansDataList.get(index);
            if (userData.getIsfriend() != 0) {
                //System.out.println("Is Friend : " + userData.getUid() + "  -  " + userData.getUsername());
                System.out.printf("INDO: Is Friend:  %-9d %1s %s\n",userData.getUid(),"-",userData.getUsername());
                wordFrequencies.add(new WordFrequency(userData.getUsername(), 3));
            } else {
                wordFrequencies.add(new WordFrequency(userData.getUsername(), 1));
            }

        }
        return wordFrequencies;
    }
    //重载方法
    protected static List<WordFrequency> getWordFrequencies(int userID) {

        ArrayList<Data> fansDataList = runOnGetOldFansData(userID);
        if (fansDataList == null) {
            fansDataList = runOnGetNewFansData(userID);
        }
        return getWordFrequencies(fansDataList);
    }



    //序列化的代码 保存
    private static ArrayList<Data> runOnGetNewFansData(int userID) {
        ArrayList<Data> fansData = CoolApkFansApi.getFansData(userID);
        try {
            FileOutputStream fileOut = new FileOutputStream("./ser/" + userID + "-FansDataList.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(fansData);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + "./ser/" + userID + "-FansDataList.ser");
        } catch(IOException i) {
            i.printStackTrace();
        }
        return fansData;
    }

    //序列化的代码 恢复
    private static ArrayList<Data> runOnGetOldFansData(int userID) {
        ArrayList<Data> fansData;
        try {
            FileInputStream fileIn = new FileInputStream("./ser/" + userID + "-FansDataList.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            fansData = (ArrayList<Data>) in.readObject();
            in.close();
            fileIn.close();
        } catch(IOException i) {
            System.out.println("Wrong: Cant Load File : " + "./ser/" + userID + "-FansDataList.ser");
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


    //获取图片宽高
    private static int[] getImageWidthHeight(String str) {
        int[] Width_Height = new int[2];
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(str).getAbsoluteFile());
            Width_Height[0] = bufferedImage.getWidth();
            Width_Height[1] = bufferedImage.getHeight();
            return Width_Height;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(Width_Height[0] + " " + Width_Height[1]);
            System.out.println(str);
            System.out.println(new File(str).getAbsoluteFile().toString());
        }
        return Width_Height;
    }

}
