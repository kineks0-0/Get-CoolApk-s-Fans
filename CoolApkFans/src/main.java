


public class main {

    public static void main(String[] args) {

        //示例生成
        FansWordCloud.getFansWordCloud(2086596,1000,1000,"./pic/FansWordCloud.png");
        FansWordCloud.getFansWordCloudWithImage(2086596,"./pic/FansWordCloudWithImage.png","./pic/UserFace.png");

        //快速生成
        FansWordCloud.getFansWordCloud(2086596,1000,1000);
        FansWordCloud.getFansWordCloudWithImage(2086596,"./pic/UserFace.png");

        //自定义
        String fileName = FansWordCloud.getFansWordCloudFilePath(2086596,1000,1000);
        FansWordCloud.getFansWordCloud("黑体", 22, 1000,1000,
                "./pic/" + fileName,FansWordCloud.getWordFrequencies(2086596));

        //自定义并加入中文分词
        String fileNameWithChineseWordTokenizer =
                FansWordCloud.getFansWordCloudFilePathWithName(2086596,1000,1000,
                        "WithChineseWordTokenizer");
        FansWordCloud.getFansWordCloud("黑体", 22, 1000,1000,
                "./pic/" + fileNameWithChineseWordTokenizer,FansWordCloud.getWordFrequenciesWithChineseWordTokenizer(2086596));


        /*FansWordCloud.getFansWordCloud(3596689,800,800);

        FansWordCloud.getFansWordCloud(1809499,1000,1000);
        FansWordCloud.getFansWordCloud(2749583,1000,1000);
        FansWordCloud.getFansWordCloud(1316828,1000,1000);

        FansWordCloud.getFansWordCloud(854032,4400,4400);
        FansWordCloud.getFansWordCloud(854032,10000,10000);

        FansWordCloud.getFansWordCloud(436885,3300,3300);*/

    }


}
