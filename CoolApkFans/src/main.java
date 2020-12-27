import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.palette.ColorPalette;

import java.awt.*;

public final class main {

    public static void main(String[] args) {


        //示例生成
        FansWordCloud.
                getFansWordCloud(2086596,1000,1000,"./pic/FansWordCloud.png");
        FansWordCloud.
                getFansWordCloudWithImage
                        (2086596,"./pic/FansWordCloudWithImage.png","./pic/UserFace.png");



        //快速生成
        FansWordCloud.getFansWordCloud(2086596,1000,1000);
        FansWordCloud.getFansWordCloudWithImage(2086596,"./pic/UserFace.png");



        //自定义
        FansWordCloud.WordCloudFilePath fileName =
                FansWordCloud.getFansWordCloudFilePath(2086596,1000,1000);

        FansWordCloud.getFansWordCloud("黑体", 22,
                fileName.width, fileName.height, fileName.path,
                FansWordCloud.getWordFrequencies(fileName.fansDataList));

        FansWordCloud.getFansWordCloud(new Font("黑体",Font.BOLD,22),
                fileName.width, fileName.height, fileName.path,
                FansWordCloud.getWordFrequencies(fileName.fansDataList));



        //自定义并加入中文分词
        FansWordCloud.WordCloudFilePath fileNameWithChineseWordTokenizer =
                FansWordCloud.
                        getFansWordCloudFilePathWithName
                                (2086596,1000,1000, "WithChineseWordTokenizer");
        FansWordCloud.
                getFansWordCloud
                        ("黑体", 22,
                                fileNameWithChineseWordTokenizer.width, fileNameWithChineseWordTokenizer.height,
                                fileNameWithChineseWordTokenizer.path,
                                FansWordCloud.
                                        getWordFrequenciesWithChineseWordTokenizer
                                                (fileNameWithChineseWordTokenizer.fansDataList,null));




        //完全自定义

        //输出词云文件配置
        FansWordCloud.WordCloudFilePath wordCloudFilePath =
                FansWordCloud.getFansWordCloudFilePath(2086596,1000,1000);

        //字体
        Font font = new Font("黑体",Font.BOLD, 22);
        //词云颜色，越靠前的颜色权重越高
        ColorPalette colorPalette = new ColorPalette(new Color(0x1C1CD3), new Color(0x6D0090), new Color(0xB1B1FE),
                new Color(0xF40009), new Color(0xBA0044), new Color(0xA0005D), new Color(0xFDFDFE));
        //词云背景颜色
        Color backgroundColor = new Color(0, 0, 1);
        //词云画板设置
        Dimension dimension = new Dimension(wordCloudFilePath.width, wordCloudFilePath.height);
        //画板背景使用矩形
        Background background = new RectangleBackground(dimension);
        //调用 CollisionMode 为填充类型
        // CollisionMode.PIXEL_PERFECT
        // CollisionMode.RECTANGLE
        FansWordCloud.
                getFansWordCloud
                        (font,colorPalette,backgroundColor, CollisionMode.PIXEL_PERFECT, 3,
                                background,dimension,wordCloudFilePath,wordCloudFilePath.getWordFrequencies());


        //以下数据量有些大
        fileNameWithChineseWordTokenizer =
                FansWordCloud.
                        getFansWordCloudFilePathWithName
                                (854032,800,800, "WithChineseWordTokenizer");
        FansWordCloud.
                getFansWordCloud
                        ("黑体", 22,
                                fileNameWithChineseWordTokenizer.width, fileNameWithChineseWordTokenizer.height,
                                fileNameWithChineseWordTokenizer.path,
                                FansWordCloud.
                                        getWordFrequenciesWithChineseWordTokenizer
                                                (fileNameWithChineseWordTokenizer.fansDataList,null));



    }


}
