import api.CoolApkFansApi;
import api.dataclass.Data;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundaryBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;
import io.FileWrite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FansWordCloud {


    public static String getFansWordCloudFilePathWithName(int userID, int width, int height, String name) {
        ArrayList<Data> fansDataList = runOnGetOldFansData(userID);
        if (fansDataList == null) {
            fansDataList = runOnGetNewFansData(userID);
        }
        return fansDataList.get(0).getFuid() + "-" + fansDataList.get(0).getFusername()
                + "-" + width + "x" + height
                + "-FansWordCloud" + name + ".png";
    }

    public static String getFansWordCloudFilePath(int userID, int width, int height) {
        return getFansWordCloudFilePathWithName(userID,width,height,"");
    }


    public static String getFansWordCloudFilePathWithImage(int userID, int width, int height) {
        return getFansWordCloudFilePathWithName(userID,width,height,"WithImage");
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
        String fileName = getFansWordCloudFilePath(userID,width,height);
        getFansWordCloud("黑体", 22, width, height,
                "./pic/" + fileName,getWordFrequencies(userID));
    }

    /**
     * 获取所有粉丝的用户名,之后生成词云图片(使用图片背景蒙版) 注意使用的图片需要自行把不需要渲染的部分擦除成透明像素,否则和矩形一样
     *
     * @param userID          设置用户词云数据来源
     * @param backgroundImage 设置词云使用的背景蒙版
     *
     */
    public static void getFansWordCloudWithImage(int userID, String backgroundImage) {
        int[] WH = getImageWidthHeight(backgroundImage);// 获取图片的宽高
        String fileName = getFansWordCloudFilePathWithImage(userID,WH[0],WH[1]);
        getFansWordCloudWithImage("黑体", 20,
                "./pic/" + fileName,backgroundImage,getWordFrequencies(userID));
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
        getFansWordCloud("黑体", 22, width, height,
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
        getFansWordCloudWithImage("黑体", 20,
                outputFile,"./pic/UserFace.png",getWordFrequencies(userID));
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

        // 词云的建议去看官方文档 https://github.com/kennycason/kumo
        // 也可以自己去谷歌,不过依赖的坑文档居然没说



        Dimension dimension = new Dimension(width, height);

        //WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);// 矩形
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);//完美像素 大概是尽量填充
        wordCloud.setBackground(new RectangleBackground(dimension));
        wordCloud.setPadding(3);
        java.awt.Font font = new java.awt.Font(fontStr, Font.BOLD, fontSize);// 设置字体,如果找不到设置的就会使用默认字体,字号请自行看情况调整

        wordCloud.setColorPalette(new ColorPalette(new Color(0x1C1CD3), new Color(0x6D0090), new Color(0xB1B1FE),
                new Color(0xF40009), new Color(0xBA0044), new Color(0xA0005D), new Color(0xFDFDFE)));//词云颜色

        ////wordCloud.setColorPalette(new ColorPalette(Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE));//官方文档默认的
        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.setBackgroundColor(new Color(0, 0, 1));
        //wordCloud.setBackgroundColor(new Color(255, 255, 255));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile(outputFile);
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


    public static InputStream getStringStream(String sInputString){
        if (sInputString != null && !sInputString.trim().equals("")){
            try{
                return new ByteArrayInputStream(sInputString.getBytes());
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return null;
    }

    //返回词云list,使用 KUAM 的分词功能
    protected static List<WordFrequency> getWordFrequenciesWithChineseWordTokenizer(int userID) {

        ArrayList<Data> fansDataList = runOnGetOldFansData(userID);
        if (fansDataList == null) {
            fansDataList = runOnGetNewFansData(userID);
        }

        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(2);
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());

        StringBuilder fansName = new StringBuilder();
        for ( String str: CoolApkFansApi.getFansName(fansDataList)) {
            fansName.append(str).append("\n");
        }
        //FileWrite.write2File("./out/" + fansDataList.get(0).getFuid() + "-FansNameList.txt",fansName.toStrinh());

        try {
            //return frequencyAnalyzer.load("./out/" + fansDataList.get(0).getFuid() + "-FansNameList.txt");
            return frequencyAnalyzer.load(getStringStream(fansName.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //自定义词云list,如果检查到为互相关注则提高权重为3否则为1
    protected static List<WordFrequency> getWordFrequencies(int userID) {

        ArrayList<Data> fansDataList = runOnGetOldFansData(userID);
        if (fansDataList == null) {
            fansDataList = runOnGetNewFansData(userID);
        }

        List<WordFrequency> wordFrequencies = new ArrayList<>();

        Data userData;
        for (int index=0; index!=fansDataList.size(); index++) {
            userData = fansDataList.get(index);
            if (userData.getIsfriend() != 0) {
                System.out.println("Is Friend : " + userData.getUid() + "  -  " + userData.getUsername());
                wordFrequencies.add(new WordFrequency(userData.getUsername(), 3));
            } else {
                wordFrequencies.add(new WordFrequency(userData.getUsername(), 1));
            }

        }
        return wordFrequencies;
    }

    //序列化的代码 一个保存 一个恢复
    private static ArrayList<api.dataclass.Data> runOnGetNewFansData(int userID) {
        ArrayList<api.dataclass.Data> fansData = CoolApkFansApi.getFansData(userID);
        try {
            FileOutputStream fileOut = new FileOutputStream("./ser/" + userID + "-FansDataList.ser");
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

    private static ArrayList<api.dataclass.Data> runOnGetOldFansData(int userID) {
        ArrayList<api.dataclass.Data> fansData = null;
        try {
            FileInputStream fileIn = new FileInputStream("./ser/" + userID + "-FansDataList.ser");
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


    //获取图片宽高
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
