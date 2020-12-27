# Get-CoolApk-s-Fans

---
- 自动**爬取**酷安 (CoolApk) 粉丝列表**生成词云**
<br>
- Clone 下后只需修改**几个参数**即可**快速生成**
---
<br>
<br>
<br>
<br>

# 程序特点

---
- 只需要提供待生成的 **UserID** 即可快速生成
<br>
- ~~但想要美化你自己去改字体和配色~~
- 现在支持完全自定义,字体颜色背景画板都可以自定义了. 
  
### 目前可以使用以下功能
   - [x] 获取酷安粉丝Api
      - -   [x] 数据类 [采用反序列化,代码根据API生成]
   - [x] 生成词云
      - -   [x] 自定义配置
      - -   [x] 颜色,字体,背景,前景
   - [ ] 移植到Android [KUMO依赖Java.awt.*,无法RunOnAndroid]    
  
<br><br>

### 示例代码

   - #### //  **./src/main.java**
```java


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


        fileNameWithChineseWordTokenizer =
                FansWordCloud.
                        getFansWordCloudFilePathWithName
                                (854032,1000,1000, "WithChineseWordTokenizer");
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



```

  <br>
  <br>
  <br>

---

# FAQ
   - ### UserID在哪里？
      - 分享**用户链接**就能找到了

   - ### Api失效
      - 如果 **Token** 失效还请自己解决 （~~算法没变就抓包找 **X-App-Device** 和 **deviceID**~~）

<br>

- **Api** 由 **[api.CoolApkFansApi.java](https://github.com/kineks0-0/Get-CoolApk-s-Fans/blob/master/CoolApkFans/src/api/CoolApkFansApi.java)** 负责,具体问题还请自行排除 （~~或者反馈给我~~）

   - ### 数据不会更新
      - 想要更新数据需要自己**删除** **‘./FansDataList.ser’** 文件,
      - 默认缓存了数据就**不再向酷安爬取** 来避免被**ban**.

<br>

<br>
<br>
<br>
<br>

---

# 注意事项
- ~~请注意这是一个Maven项目，在Clone之前请先配置好Maven环境.~~
<br>
- 现在采用**本地依赖jar包文件**，非 **IDEA** 可能需要自行导入jar包到项目
<br>
- 在实际运行前请先看完在 **'Main.java'** 中的 **注释** 后再使用
<br>
- **部分情况**可能要自己抓包找 **X-App-Device** 和 **deviceID** 替换
<br><br><br><br>

---

# 引用文档
本项目 参考~~复制~~ 了以下文档
<br>
<br>
   - [KUMO词云](https://github.com/kennycason/kumo)
   - [Token算法](https://www.jianshu.com/p/0e4f2dc0e919)
<br>
<br>
<br>

---

# 运行输出
![2086596-kineks0_0-1000x1000-FansWordCloud.png](https://github.com/kineks0-0/Get-CoolApk-s-Fans/blob/master/CoolApkFans/pic/2086596-kineks0_0-1000x1000-FansWordCloud.png?raw=true)
![2086596-kineks0_0-1200x1062-FansWordCloudWithImage.png](https://github.com/kineks0-0/Get-CoolApk-s-Fans/blob/master/CoolApkFans/pic/2086596-kineks0_0-1200x1062-FansWordCloudWithImage.png?raw=true)
![2086596-kineks0_0-1000x1000-FansWordCloudWithChineseWordTokenizer.png](https://github.com/kineks0-0/Get-CoolApk-s-Fans/blob/master/CoolApkFans/pic/2086596-kineks0_0-1000x1000-FansWordCloudWithChineseWordTokenizer.png?raw=true)
