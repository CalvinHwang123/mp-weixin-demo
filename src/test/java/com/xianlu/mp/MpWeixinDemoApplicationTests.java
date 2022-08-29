package com.xianlu.mp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xianlu.mp.model.MpArticle;
import com.xianlu.mp.util.URLDownload;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//@SpringBootTest
class MpWeixinDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testFileDownload() throws IOException {
        final String url = "http://mmbiz.qpic.cn/mmbiz_png/Pn4Sm0RsAugymrbtchsKxe9kqm5BRia4FnrbKibc6mRekjMBebmyT73B3qFMpnFPdBrbzNASm15FLWRTd1oVZdJw/0?wx_fmt=png";
        FileUtil.writeFromStream(new URL(url).openStream(), "D:/img.png");
    }

    @Test
    void testURLDownload() {
        //目标URL
        String url = "https://mp.weixin.qq.com/s?__biz=MjM5MjAwODM4MA==&mid=2650934213&idx=1&sn=14913053279174cead5e4af07e2d2ddc&chksm=bd5a7a168a2df3002ca95f8273a07a7810f3690933a122e04144f0376d58866c46fc030afd82#rd";
        //文件夹的名称
//        String fileName = url.split("//")[1];//取得是以域名作为二级文件夹的名字
        String fileName = "output";
        Date date = new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");//一级文件夹的名字
        //保存路径
        String savePath="E://"+ format.format(date)+"/";
        //根据URL下载HTML页面
        URLDownload.downloadFileFromUrl(fileName, url, savePath);

//        ReadHtml.readFileByLines(savePath + fileName+"index.html");//下载的HTML文件
    }

    @Test
    void testHTMLContent() throws IOException {
        String link = "https://mp.weixin.qq.com/s?__biz=MjM5MjAwODM4MA==&mid=2650934213&idx=1&sn=14913053279174cead5e4af07e2d2ddc&chksm=bd5a7a168a2df3002ca95f8273a07a7810f3690933a122e04144f0376d58866c46fc030afd82#rd";
        Connection connect = Jsoup.connect(link);
        Document document = connect.get();
        Element body = document.body();
        Elements richMediaWrp = body.getElementsByClass("rich_media_wrp");
        if (ObjectUtil.isEmpty(richMediaWrp)) {
            System.out.println("文章找不到 <div class='rich_media_wrp'> 标签，爬取 HTML 内容失败");
            return;
        }
        Element richMedia = richMediaWrp.get(0);
        String s = richMedia.toString().replaceAll("visibility: hidden;", "");
        System.out.println(s);
    }

    @Test
    void testSubList() {
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> list2 = list.subList(-1, -1);
        System.out.println(list2);
    }

}
