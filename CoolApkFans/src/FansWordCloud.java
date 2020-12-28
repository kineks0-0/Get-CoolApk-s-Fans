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
import java.util.Objects;

public class FansWordCloud {


    /**
     *
     *   配置词云数据输入输出
     *
     * */
    final static class WordCloudFilePath {
        int    userID;
        int    width ;
        int    height;
        int    padding;
        Font   font  ;
        String name;
        String file;
        String path;

        private ArrayList<Data>     fansDataList   ;
        private List<WordFrequency> wordFrequencies;
        ColorPalette                colorPalette   ;
        Color                       backgroundColor;

        static int WORD_FREQUENCIES_TYPE_FANS_LIST = 0;
        static int WORD_FREQUENCIES_TYPE_CHINESE_WORD_TOKENIZER = 1;
        static int wordFrequenciesType = WORD_FREQUENCIES_TYPE_FANS_LIST;


        //----------------------------------------------//
        public WordCloudFilePath() {
            System.out.println("Wrong: on WordCloudFilePath(), have to init for your self now.");
        }
        //----------------------------------------------//
        public WordCloudFilePath(int userID, int width, int height, String name) { init(userID,width,height,name,null,null,null); }
        public WordCloudFilePath(int userID, int width, int height, String name, Font font) { init(userID,width,height,name,font,null,null); }
        public WordCloudFilePath(int userID, int width, int height, String name, Font font, List<WordFrequency> wordFrequencies, ArrayList<Data> fansDataList) { init(userID,width,height,name,font,wordFrequencies,fansDataList); }
        public WordCloudFilePath(int userID, String name, String backgroundImage) { int[] WH = getImageWidthHeight(backgroundImage);init(userID,WH[0],WH[1],name,null,null,null); }
        public WordCloudFilePath(int userID, String name, String backgroundImage, Font font, List<WordFrequency> wordFrequencies, ArrayList<Data> fansDataList) { int[] WH = getImageWidthHeight(backgroundImage);init(userID,WH[0],WH[1],name,font,wordFrequencies,fansDataList); }

        void init(int userID, int width, int height, String name, Font font, List<WordFrequency> wordFrequencies, ArrayList<Data> fansDataList) {
            this.userID = userID;
            this.width = width;
            this.height = height;
            this.name = name;
            this.padding = 3;

            if (fansDataList == null) { this.fansDataList = getFansDataList(); } else { this.fansDataList = fansDataList; }
            //this.fansDataList = Objects.requireNonNullElseGet(fansDataList, this::getFansDataList);
            this.wordFrequencies = Objects.requireNonNullElseGet(wordFrequencies, this::getNewWordFrequencies);
            this.font = Objects.requireNonNullElseGet(font, () -> new Font("黑体", Font.BOLD, 22));
            this.colorPalette
                    = new ColorPalette(
                    new Color(0x1C1CD3), new Color(0x6D0090), new Color(0xB1B1FE),
                    new Color(0xF40009), new Color(0xBA0044), new Color(0xA0005D),
                    new Color(0xFDFDFE));
            backgroundColor = new Color(0, 0, 1);

            if (this.fansDataList == null) {
                this.file = "Null-Null-" + width + "x" + height + "-FansWordCloud" + name + ".png";
            } else {
                this.file =
                        this.fansDataList.get(0).getFuid()
                            + "-"
                        + this.fansDataList.get(0).getFusername()
                            + "-" + width + "x" + height
                            + "-FansWordCloud" + name + ".png";
            }
            this.path = "./pic/" + file;
        }

        //----------------------------------------------//

        protected ArrayList<Data> getFansDataList() {
            if (userID == -1 || userID == 0) {
                System.out.println("Error: on getFansDataList(), UserID == " + userID);
                return null;
            }

            ArrayList<Data> fansDataList = runOnGetOldFansData(userID);
            if (fansDataList == null) {
                fansDataList = runOnGetNewFansData(userID);
            }
            return fansDataList;
        }

        //----------------------------------------------//

        public List<WordFrequency> getNewWordFrequencies() {
            if (fansDataList == null) {
                fansDataList = getFansDataList();
            }
            return FansWordCloud.getWordFrequencies(fansDataList);
        }

        public List<WordFrequency> getNewWordFrequenciesWithChineseWordTokenizer() {
            if (fansDataList == null) {
                fansDataList = getFansDataList();
            }
            return FansWordCloud.getWordFrequenciesWithChineseWordTokenizer(fansDataList);
        }

        //----------------------------------------------//

        public void setWordFrequencies(List<WordFrequency> wordFrequencies) {
            this.wordFrequencies = wordFrequencies;
        }

        public List<WordFrequency> getWordFrequencies(int type) {
            if (type == WORD_FREQUENCIES_TYPE_FANS_LIST) {
                return getWordFrequencies();
            } else {
                return getWordFrequenciesWithChineseWordTokenizer();
            }
        }

        public List<WordFrequency> getWordFrequencies() {
            if (wordFrequencies == null || wordFrequenciesType!=WORD_FREQUENCIES_TYPE_FANS_LIST) {
                wordFrequencies = getNewWordFrequencies();
            }
            return wordFrequencies;
        }

        public List<WordFrequency> getWordFrequenciesWithChineseWordTokenizer() {
            if (wordFrequencies == null || wordFrequenciesType!=WORD_FREQUENCIES_TYPE_CHINESE_WORD_TOKENIZER) {
                wordFrequencies = getNewWordFrequenciesWithChineseWordTokenizer();
            }
            return wordFrequencies;
        }

    }


    //---------------------------------------------------------------//



    /**
     *  重载方法
     *  用于快速生成 WordCloudFilePath 对象
     *  目前为了兼容保留该方法
     *
     */
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







    //---------------------------------------------------------------//

    //   用于快速生成的重载方法  //

    /**
     * 获取所有粉丝的用户名,之后生成词云图片(矩形)
     *
     * @param userID          设置用户词云数据来源
     * @param width           设置生成词云图片的宽
     * @param height          设置生成词云图片的高
     *
     */
    public static void getFansWordCloud(int userID, int width, int height) {
        getFansWordCloud(new WordCloudFilePath(userID,width,height,""));
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
        WordCloudFilePath wordCloudFilePath = new WordCloudFilePath(userID,width,height,"");
        wordCloudFilePath.path = outputFile;
        getFansWordCloud(wordCloudFilePath);
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
    public static void getFansWordCloud(String fontStr, int fontSize, int width, int height, String outputFile, List<WordFrequency> wordFrequencies) {
        java.awt.Font font = new java.awt.Font(fontStr, Font.BOLD, fontSize);
        WordCloudFilePath wordCloudFilePath = new WordCloudFilePath(-1,width,height,"",font,wordFrequencies,null);
        wordCloudFilePath.path = outputFile;
        System.out.println(wordCloudFilePath.path);
        getFansWordCloud(wordCloudFilePath);
    }

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
    public static void getFansWordCloud(Font font, int width, int height, String outputFile, List<WordFrequency> wordFrequencies) {
        WordCloudFilePath wordCloudFilePath = new WordCloudFilePath();
        wordCloudFilePath.init(-1,width,height,"",font,wordFrequencies,null);
        wordCloudFilePath.path = outputFile;
        System.out.println(wordCloudFilePath.path);
        getFansWordCloud(wordCloudFilePath);
    }

    /**
     * 获取所有粉丝的用户名,之后生成词云图片(矩形)
     *
     * @param wordCloudFilePath 设置词云输出的配置
     *
     */
    public static void getFansWordCloud(WordCloudFilePath wordCloudFilePath) {
        getFansWordCloud(wordCloudFilePath,WordCloudFilePath.WORD_FREQUENCIES_TYPE_FANS_LIST);
    }

    //   工作代码   //

    /**
     * 获取所有粉丝的用户名,之后生成词云图片(矩形)
     *
     * @param wordCloudFilePath     设置词云输出的配置
     * @param wordFrequenciesType   设置词云来源的类型
     *
     */
    public static void getFansWordCloud(WordCloudFilePath wordCloudFilePath,int wordFrequenciesType) {

        // 词云的建议去看官方文档 https://github.com/kennycason/kumo
        // 也可以自己去谷歌,不过依赖的坑文档居然没说


        Dimension dimension = new Dimension(wordCloudFilePath.width, wordCloudFilePath.height);

        //WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);  // 矩形
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);// 完美像素 大概是尽量填充
        wordCloud.setBackground(new RectangleBackground(dimension));                // 画板背景
        wordCloud.setPadding(wordCloudFilePath.padding);                            // 词云间距
        wordCloud.setColorPalette(wordCloudFilePath.colorPalette);                  // 词云颜色
        wordCloud.setKumoFont(new KumoFont(wordCloudFilePath.font));                // 词云字体
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.setBackgroundColor(wordCloudFilePath.backgroundColor);            // 词云背景颜色
        wordCloud.build(wordCloudFilePath.getWordFrequencies(wordFrequenciesType)); // 构建并传入词云数据
        wordCloud.writeToFile(wordCloudFilePath.path);                              // 设置写出目标的路径
    }

    /**
     *
     * 完全自定义
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









    //---------------------------------------------------------------//












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
     *
     * 完全自定义
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
















    //---------------------------------------------------------------//

    //以下为为了解耦分离出的代码


    //将字串符包装为数据流
    protected static InputStream getStringStream(String sInputString) {
        if (sInputString != null && !sInputString.trim().equals("")){
            return new ByteArrayInputStream(sInputString.getBytes());
        }
        return null;
    }

    //返回词云list,使用 kuam 的中文分词功能
    private static List<WordFrequency> getWordFrequenciesWithChineseWordTokenizer(ArrayList<Data> fansDataList, List<String> stopWords) {

        if (fansDataList == null) {
            System.out.println("Error: on getWordFrequenciesWithChineseWordTokenizer(ArrayList<Data> fansDataList, List<String> stopWords) fansDataList == null");
            return null;
        }

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
    private static List<WordFrequency> getWordFrequenciesWithChineseWordTokenizer(int userID,List<String> stopWords) {
        ArrayList<Data> fansDataList = runOnGetOldFansData(userID);
        if (fansDataList == null) {
            fansDataList = runOnGetNewFansData(userID);
        }
        return getWordFrequenciesWithChineseWordTokenizer(fansDataList,stopWords);
    }
    //重载方法
    private static List<WordFrequency> getWordFrequenciesWithChineseWordTokenizer(int userID) {
        return getWordFrequenciesWithChineseWordTokenizer(userID,null);
    }
    //重载方法
    private static List<WordFrequency> getWordFrequenciesWithChineseWordTokenizer(ArrayList<Data> fansDataList) {
        return getWordFrequenciesWithChineseWordTokenizer(fansDataList,null);
    }



    //自定义词云list,如果检查到为互相关注则提高权重为3否则为1
    private static List<WordFrequency> getWordFrequencies(ArrayList<Data> fansDataList) {

        if (fansDataList == null) {
            System.out.println("Error: on getWordFrequencies(ArrayList<Data> fansDataList) fansDataList == null");
            return null;
        }

        List<WordFrequency> wordFrequencies = new ArrayList<>();

        Data userData;
        for (int index=0; index!=fansDataList.size(); index++) {
            userData = fansDataList.get(index);
            if (userData.getIsfriend() != 0) {
                //System.out.println("Is Friend : " + userData.getUid() + "  -  " + userData.getUsername());
                System.out.printf("INFO: Is Friend:  %-9d %1s %s\n",userData.getUid(),"-",userData.getUsername());
                wordFrequencies.add(new WordFrequency(userData.getUsername(), 3));
            } else {
                wordFrequencies.add(new WordFrequency(userData.getUsername(), 1));
            }

        }
        return wordFrequencies;
    }
    //重载方法
    private static List<WordFrequency> getWordFrequencies(int userID) {

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
