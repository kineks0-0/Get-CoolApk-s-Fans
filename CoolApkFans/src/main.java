


public class main {

    public static void main(String[] args) {

        //示例生成
        FansWordCloud.getFansWordCloud(2086596,1000,1000,"./pic/FansWordCloud.png");
        FansWordCloud.getFansWordCloudWithImage(2086596,"./pic/FansWordCloudWithImage.png","./pic/UserFace.png");


        FansWordCloud.getFansWordCloud(2086596,1000,1000);
        FansWordCloud.getFansWordCloudWithImage(2086596,"./pic/UserFace.png");



    }


}
