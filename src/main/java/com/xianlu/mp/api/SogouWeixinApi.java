package com.xianlu.mp.api;

import cn.hutool.core.util.ObjectUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.erupt.core.annotation.EruptRouter;
import xyz.erupt.core.constant.EruptRestPath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xianlu.mp.constant.Constant.*;

@RequestMapping(EruptRestPath.ERUPT_API +"/sogou") //必须为 EruptRestPath.ERUPT_API 权限校验才会生效
@RestController
public class SogouWeixinApi {

    @GetMapping("searchBizSogou")
    @EruptRouter(verifyType = EruptRouter.VerifyType.LOGIN, authIndex = 0) //配置接口登录后可用
    public Map<String, Object> searchBizSogou(@RequestParam("keyword") String keyword, @RequestParam("pageNo") String pageNo){
        String url = SEARCH_BIZ_SOGOU.replace("${kw}", keyword).replace("${pageNo}", pageNo);
        Map<String, Object> resultMap = new HashMap<>(2);
        try {
            Map<String, String> cookieMap = new HashMap<>();
            /**
             * ppinf=5|1661416252|1662625852|dHJ1c3Q6MToxfGNsaWVudGlkOjQ6MjAxN3x1bmlxbmFtZTo0Om1vbW98Y3J0OjEwOjE2NjE0MTYyNTJ8cmVmbmljazo0Om1vbW98dXNlcmlkOjQ0Om85dDJsdU45cmJvNVlIQUU4TFNtcV9pQURucXdAd2VpeGluLnNvaHUuY29tfA;
             * pprdig=sj25rOl83Cc-sdhAg4Ye1dL3agNr2DZ2OfjOPGO1bPK2xJfDJaamgNawqP3Lsip1Pt0SxsJIOMWTSN7wKo-f93v0y1Vm0eRXISVIKmKr5crx1f630Zzl-gw4iKjUU3uCggpq6PuoBO6VJzgvjUdheUTPCF1pqIlL9vh4nLPh6lk;
             * ppinfo=cb411094c3;
             * passport=5|1661416252|1662625852|dHJ1c3Q6MToxfGNsaWVudGlkOjQ6MjAxN3x1bmlxbmFtZTo0Om1vbW98Y3J0OjEwOjE2NjE0MTYyNTJ8cmVmbmljazo0Om1vbW98dXNlcmlkOjQ0Om85dDJsdU45cmJvNVlIQUU4TFNtcV9pQURucXdAd2VpeGluLnNvaHUuY29tfA|7dbe8e6388|sj25rOl83Cc-sdhAg4Ye1dL3agNr2DZ2OfjOPGO1bPK2xJfDJaamgNawqP3Lsip1Pt0SxsJIOMWTSN7wKo-f93v0y1Vm0eRXISVIKmKr5crx1f630Zzl-gw4iKjUU3uCggpq6PuoBO6VJzgvjUdheUTPCF1pqIlL9vh4nLPh6lk;
             * sgid=14-55758143-AWMHMzzcrINXrtAVD8J0wmE;
             * ppmdig=1661416252000000b7f030179f4b6bc396ed2f956986c206;
             * ariaDefaultTheme=undefined
             */
            cookieMap.put("ppinf", "5|1661416252|1662625852|dHJ1c3Q6MToxfGNsaWVudGlkOjQ6MjAxN3x1bmlxbmFtZTo0Om1vbW98Y3J0OjEwOjE2NjE0MTYyNTJ8cmVmbmljazo0Om1vbW98dXNlcmlkOjQ0Om85dDJsdU45cmJvNVlIQUU4TFNtcV9pQURucXdAd2VpeGluLnNvaHUuY29tfA");
            cookieMap.put("pprdig", "sj25rOl83Cc-sdhAg4Ye1dL3agNr2DZ2OfjOPGO1bPK2xJfDJaamgNawqP3Lsip1Pt0SxsJIOMWTSN7wKo-f93v0y1Vm0eRXISVIKmKr5crx1f630Zzl-gw4iKjUU3uCggpq6PuoBO6VJzgvjUdheUTPCF1pqIlL9vh4nLPh6lk");
            cookieMap.put("ppinfo", "cb411094c3");
            cookieMap.put("passport", "5|1661416252|1662625852|dHJ1c3Q6MToxfGNsaWVudGlkOjQ6MjAxN3x1bmlxbmFtZTo0Om1vbW98Y3J0OjEwOjE2NjE0MTYyNTJ8cmVmbmljazo0Om1vbW98dXNlcmlkOjQ0Om85dDJsdU45cmJvNVlIQUU4TFNtcV9pQURucXdAd2VpeGluLnNvaHUuY29tfA|7dbe8e6388|sj25rOl83Cc-sdhAg4Ye1dL3agNr2DZ2OfjOPGO1bPK2xJfDJaamgNawqP3Lsip1Pt0SxsJIOMWTSN7wKo-f93v0y1Vm0eRXISVIKmKr5crx1f630Zzl-gw4iKjUU3uCggpq6PuoBO6VJzgvjUdheUTPCF1pqIlL9vh4nLPh6lk");
            cookieMap.put("sgid", "14-55758143-AWMHMzzcrINXrtAVD8J0wmE");
            cookieMap.put("ppmdig", "1661416252000000b7f030179f4b6bc396ed2f956986c206");
            cookieMap.put("ariaDefaultTheme", "undefined");

            Document document = Jsoup
                    .connect(url)
                    .header(USER_AGENT, USER_AGENT_VALUE)
//                    .cookies(cookieMap)
                    .get();
            Element body = document.body();
            Elements newsBoxs = body.getElementsByClass("news-box");
            if (CollectionUtils.isEmpty(newsBoxs)) {
                System.out.println("搜狗微信采集失败：没有找到 <div class='news-box'>");
                resultMap.put("total", 0);
                resultMap.put("list", new ArrayList<>());
            }
            List<Map<String, Object>> list = new ArrayList<>();
            Element newsBox = newsBoxs.get(0);
            Element newsList2 = newsBox.getElementsByClass("news-list2").get(0);
            Elements lis = newsList2.getElementsByTag("li");
            for (Element li: lis) {
                Element gzhBox2 = li.getElementsByClass("gzh-box2").get(0);
                Element imgBox = gzhBox2.getElementsByClass("img-box").get(0);
                Element img = imgBox.getElementsByTag("img").get(0);
                String src = img.attr("src");
                Element txtBox = gzhBox2.getElementsByClass("txt-box").get(0);
                Element a = txtBox.getElementsByTag("a").get(0);
                Element info = txtBox.getElementsByClass("info").get(0);
                Elements dls = li.getElementsByTag("dl");
                Element pop = gzhBox2.getElementsByClass("ew-pop").get(0).getElementsByClass("pop").get(0);
                pop.getElementsByTag("a");
                Map<String, Object> map = new HashMap<>();
                map.put("src", src);
                map.put("name", a.text().replaceAll("\"", ""));
                map.put("href", a.attr("href"));
                map.put("alias", info.text());
                map.put("qrcode", pop.getElementsByTag("img").get(0).attr("src"));
                for (int i = 0; i < dls.size(); i++) {
                    map.put("desc" + i, dls.get(i).text());
                }
                list.add(map);
            }
            Element pagebarContainer = newsBox.getElementById("pagebar_container");
            if (ObjectUtil.isNotEmpty(pagebarContainer)) {
                String total = pagebarContainer.getElementsByClass("mun").get(0).text()
                        .replaceAll("[^0-9]", ""); // 提取数字
                resultMap.put("total", total);
            } else {
                resultMap.put("total", lis.size());
            }
            resultMap.put("list", list);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("接口异常：" + e.getMessage());
        }
        return resultMap;
    }

}
