import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.palette.ColorPalette;

import java.awt.*;

public final class main {

    public static void main(String[] args) {


        /*   示例生成   */

        // 默认生成矩形词云
        FansWordCloud.
                getFansWordCloud
                        (2086596,1000,1000,"./pic/FansWordCloud.png");
        // 以图片为蒙版生成, 注意图片需要把不需要渲染的部分擦除成透明像素 否则和矩形一样
        FansWordCloud.
                getFansWordCloudWithImage
                        (2086596,"./pic/FansWordCloudWithImage.png","./pic/UserFace.png");




        /*   快速生成   */

        FansWordCloud.
                getFansWordCloud
                        (2086596,1000,1000);
        FansWordCloud.
                getFansWordCloudWithImage
                        (2086596,"./pic/UserFace.png");




        /*   自定义   */

        FansWordCloud.getFansWordCloud(
                new FansWordCloud.WordCloudFilePath(2086596,1000,1000,"",
                        new Font("黑体", Font.BOLD, 22),null,null));




        /*   自定义和分析数据词频   */

        FansWordCloud.WordCloudFilePath wordCloudFilePathWithChineseWordTokenizer =
                FansWordCloud.
                        getFansWordCloudFilePathWithName
                                (2086596,1000,1000, "WithChineseWordTokenizer");
        FansWordCloud.
                getFansWordCloud
                        (wordCloudFilePathWithChineseWordTokenizer,
                                FansWordCloud.WordCloudFilePath.WORD_FREQUENCIES_TYPE_CHINESE_WORD_TOKENIZER);





        /*         完全自定义          */


        //       输出词云文件配置       //

        FansWordCloud.WordCloudFilePath fansWordCloudFilePath =
                FansWordCloud.getFansWordCloudFilePath(2086596,1000,1000);


        //           字体            //

        Font font = new Font("黑体",Font.BOLD, 22);


        // 词云颜色,越靠前的颜色权重越高 //

        ColorPalette colorPalette = new ColorPalette(new Color(0x1C1CD3), new Color(0x6D0090), new Color(0xB1B1FE),
                new Color(0xF40009), new Color(0xBA0044), new Color(0xA0005D), new Color(0xFDFDFE));


        //        词云背景颜色        //

        Color backgroundColor = new Color(0, 0, 1);


        //        词云画板设置       //

        Dimension dimension = new Dimension(fansWordCloudFilePath.width, fansWordCloudFilePath.height);


        //       画板背景使用矩形    //

        Background background = new RectangleBackground(dimension);


        /*
         调用 CollisionMode 填充类型
            CollisionMode.PIXEL_PERFECT
            CollisionMode.RECTANGLE
        */

        //      padding 间距      //


        FansWordCloud.
                getFansWordCloud
                        (font,colorPalette,backgroundColor, CollisionMode.PIXEL_PERFECT, 3,
                                background,dimension,fansWordCloudFilePath,fansWordCloudFilePath.getWordFrequencies());





        /*     以下数据量有些大    */
        /*
        wordCloudFilePathWithChineseWordTokenizer =
                FansWordCloud.
                        getFansWordCloudFilePathWithName
                                (854032,800,800, "WithChineseWordTokenizer");
        FansWordCloud.
                getFansWordCloud
                        (wordCloudFilePathWithChineseWordTokenizer,
                                FansWordCloud.WordCloudFilePath.WORD_FREQUENCIES_TYPE_CHINESE_WORD_TOKENIZER);*/



    }


}
