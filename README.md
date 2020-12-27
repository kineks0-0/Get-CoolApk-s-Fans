# Get-CoolApk-s-Fans
自动爬取酷安 (CoolApk) 粉丝列表生成词云
<br>
Clone 下后只需修改几个参数即可快速生成


# 特性
只需要提供待生成的**UserID**即可快速生成
<br>
<br>

# FAQ
## 什么UserID？
自己分享用户链接就看到了

## Api失效
如果Token失效还请自己解决

## 数据不会更新
想要更新数据需要自己删除 **‘./FansDataList.ser’** 文件,
<br>
默认缓存了数据就**不再向酷安爬取** 来避免被ban.




# 注意
~~请注意这是一个Maven项目，在Clone之前请先配置好Maven环境.~~
<br>
现在已经用**本地jar包文件**，非 **IDEA** 需要**自行导入**jar包到项目
<br>
在**实际运行前**请先看完在 **'Main.java'** 中的 **注释** 后再使用


# 运行输出
![KJNvuR.png](https://github.com/kineks0-0/Get-CoolApk-s-Fans/blob/master/CoolApkFans/pic/FansWordCloud.png?raw=true)
![KJUNV0.png](https://github.com/kineks0-0/Get-CoolApk-s-Fans/blob/master/CoolApkFans/pic/FansWordCloudWithImage.png?raw=true)
