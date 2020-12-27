import api.CoolApkFansApi;
import api.dataclass.Data;
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

        //示例生成
        FansWordCloud.getFansWordCloud(2086596,1000,1000,"./pic/FansWordCloud.png");
        FansWordCloud.getFansWordCloudWithImage(2086596,"./pic/FansWordCloudWithImage.png","./pic/UserFace.png");


        FansWordCloud.getFansWordCloud(2086596,1000,1000);
        FansWordCloud.getFansWordCloudWithImage(2086596,"./pic/UserFace.png");


        FansWordCloud.getFansWordCloud(3596689,800,800);

        FansWordCloud.getFansWordCloud(1809499,1000,1000);
        FansWordCloud.getFansWordCloud(2749583,1000,1000);
        FansWordCloud.getFansWordCloud(1316828,1000,1000);
    }


}
