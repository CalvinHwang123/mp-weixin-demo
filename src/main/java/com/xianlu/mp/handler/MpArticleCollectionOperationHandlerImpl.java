package com.xianlu.mp.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xianlu.mp.api.WeiXinApi;
import com.xianlu.mp.model.MpArticle;
import com.xianlu.mp.model.MpArticleCollection;
import com.xianlu.mp.model.MpInfo;
import com.xianlu.mp.model.WxResultBody;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.erupt.annotation.fun.OperationHandler;
import xyz.erupt.core.context.MetaContext;
import xyz.erupt.core.util.EruptSpringUtil;
import xyz.erupt.jpa.dao.EruptDao;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MpArticleCollectionOperationHandlerImpl implements OperationHandler<MpArticle, MpArticleCollection> {

    @Value("${erupt.uploadPath}")
    private String uploadPath;

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String exec(List<MpArticle> data, MpArticleCollection mpArticleCollection, String[] param) {
        // 公众号
        MpInfo mpInfo = mpArticleCollection.getMpInfo();
        System.out.println("公众号【" + mpInfo.getNickname() + "】采集开始");
        long startTime = System.currentTimeMillis();
        // 采集类型
        Boolean collectionAll = mpArticleCollection.getCollectionAll();
        if (!collectionAll) {
            if (ObjectUtil.isEmpty(mpArticleCollection.getCollectStartDate())) {
                throw new RuntimeException("操作失败：文章发布开始日期不能为空");
            }
            if (ObjectUtil.isEmpty(mpArticleCollection.getCollectEndDate())) {
                throw new RuntimeException("操作失败：文章发布结束日期不能为空");
            }
            if (mpArticleCollection.getCollectStartDate().compareTo(mpArticleCollection.getCollectEndDate()) > 0) {
                throw new RuntimeException("操作失败：文章发布开始日期不能大于文章发布结束日期");
            }
        }
        EruptDao eruptDao = EruptSpringUtil.getBean(EruptDao.class);
        mpInfo = eruptDao.queryEntity(MpInfo.class, "id = " + mpInfo.getId());
        String fakeid = mpInfo.getFakeid();
        String startDate = mpArticleCollection.getCollectStartDate();
        String endDate = mpArticleCollection.getCollectEndDate();
        if (collectionAll) {
            if (ObjectUtil.isEmpty(mpArticleCollection.getDeleteAll())) {
                throw new RuntimeException("操作失败：清空旧数据不能为空");
            }
            if (mpArticleCollection.getDeleteAll()) {
                deleteAll(mpInfo, eruptDao);
            }
            recursionFetchMpArticle(fakeid, mpInfo, eruptDao, "1");
        } else {
            // 存储所有文章的集合
            List<Map> allList = new ArrayList<>();
            recursionFetchMpArticle2(fakeid, "1", allList, startDate);
            if (!CollectionUtils.isEmpty(allList)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                // 算出区间
                int fromIndex = 0;
                int toIndex = allList.size();
                // 文章发布日期（最大值）
                String maxArticlePublishDate = "1970-01-01";
                for (int i = 0; i < allList.size(); i++) {
                    Map map = allList.get(i);
                    String createTime = sdf.format(new Date(Long.parseLong(map.get("create_time").toString()) * 1000));
                    if (i == 0) {
                        maxArticlePublishDate = createTime;
                    }
                    if (createTime.compareTo(endDate) <= 0) {
                        fromIndex = i;
                        break;
                    }
                }

                for (int i = allList.size() - 1; i >= 0; i--) {
                    Map map = allList.get(i);
                    String createTime = sdf.format(new Date(Long.parseLong(map.get("create_time").toString()) * 1000));
                    if (createTime.compareTo(startDate) >= 0) {
                        toIndex = i;
                        break;
                    }
                }
                if (maxArticlePublishDate.compareTo(startDate) >= 0) {
                    // 右区间也得取值，所以加一（但是又不能越界，所以判断）
                    if (toIndex < allList.size()) {
                        toIndex = toIndex + 1;
                    }
                    // 最终得到的文章集合
                    List<Map> resultList = allList.subList(fromIndex, toIndex);
                    persistMpArticle(resultList, mpInfo, eruptDao);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("公众号【" + mpInfo.getNickname() + "】采集结束，耗时（ms）" + (endTime - startTime));
        return "mpArticleCollectionFunctionCallback()";
    }

    private void deleteAll(MpInfo mpInfo, EruptDao eruptDao) {
        Long mpInfoId = mpInfo.getId();
        eruptDao.getJdbcTemplate().execute(" delete from t_mp_article where 1=1 and mp_info_id = " + mpInfoId + " ");
    }

    private void recursionFetchMpArticle(String fakeid, MpInfo mpInfo, EruptDao eruptDao, String pageNo) {
        WxResultBody<List<Map>> resultBody = WeiXinApi.findExList(fakeid, pageNo);
        if (resultBody.getBase_resp().getRet() == 0) {
            List<Map> articleList = resultBody.getApp_msg_list();
            if (!CollectionUtils.isEmpty(articleList)) {
                persistMpArticle(articleList, mpInfo, eruptDao);
                sleep();
                // 递归获取文章
                recursionFetchMpArticle(fakeid, mpInfo, eruptDao, (Integer.parseInt(pageNo) + 1) + "");
            }
        } else {
            throw new RuntimeException("操作失败：接口异常 " + resultBody.getErr_msg());
        }
    }

    /**
     *
     * @param fakeid
     * @param pageNo
     * @param allList
     * @param startDate 用于提前结束递归查询，之所以用 startDate 是因为文章列表是按发表日期倒序的
     */
    private void recursionFetchMpArticle2(String fakeid, String pageNo, List<Map> allList, String startDate) {
        WxResultBody<List<Map>> resultBody = WeiXinApi.findExList(fakeid, pageNo);
        if (resultBody.getBase_resp().getRet() == 0) {
            List<Map> articleList = resultBody.getApp_msg_list();
            if (!CollectionUtils.isEmpty(articleList)) {
                allList.addAll(articleList);
                if (!needStopRecursionFetch(articleList, startDate)) {
                    sleep();
                    // 递归获取文章
                    recursionFetchMpArticle2(fakeid, (Integer.parseInt(pageNo) + 1) + "", allList, startDate);
                }
            }
        } else {
            throw new RuntimeException("操作失败：接口异常 " + resultBody.getErr_msg());
        }
    }

    private void sleep() {
        // 延迟
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 是否需要提前结束递归查询
     * @param articleList
     * @param startDate
     * @return
     */
    private boolean needStopRecursionFetch(List<Map> articleList, String startDate) {
        if (!CollectionUtils.isEmpty(articleList)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Map map: articleList) {
                String createDate = sdf.format(new Date(Long.parseLong(map.get("create_time").toString()) * 1000));
                // 开始区间比 文章创建时间 大，即可停止采集
                return startDate.compareTo(createDate) >= 0;
            }
        }
        return false;
    }

    private void persistMpArticle(List<Map> list, MpInfo mpInfo, EruptDao eruptDao) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (Map map: list) {
            MpArticle mpArticle = new MpArticle();
            mpArticle.setAid(map.get("aid").toString());
            mpArticle.setAppmsgid(map.get("appmsgid").toString());
            mpArticle.setTitle(map.get("title").toString());
            mpArticle.setArticleCreateTime(LocalDateTime.ofEpochSecond(Long.parseLong(map.get("create_time").toString()), 0, ZoneOffset.ofHours(8)));
            mpArticle.setArticleUpdateTime(LocalDateTime.ofEpochSecond(Long.parseLong(map.get("update_time").toString()), 0, ZoneOffset.ofHours(8)));
            mpArticle.setLink(map.get("link").toString());
            mpArticle.setCoverUrl(map.get("cover").toString());
            // 微信官方图片做了防盗链，不可直接引用展示，因此下载到本地
            String coverPath = "/mpArticleCover/" + mpInfo.getNickname() + "/" + mpArticle.getAid() + ".png";
            try {
                FileUtil.writeFromStream(new URL(map.get("cover").toString()).openStream(), uploadPath + coverPath);
//                saveHTMLContent(mpArticle);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mpArticle.setCover(coverPath);
            mpArticle.setDigest(map.get("digest").toString());
            mpArticle.setPaySubscribe(Integer.valueOf(map.get("is_pay_subscribe").toString()));
            beforeAdd(mpArticle);
            mpArticle.setMpInfo(mpInfo);
            eruptDao.persist(mpArticle);
        }
    }

    private void beforeAdd(MpArticle mpArticle) {
        mpArticle.setCreateTime(LocalDateTime.now());
        mpArticle.setCreateBy(MetaContext.getUser().getName());
        mpArticle.setUpdateTime(mpArticle.getCreateTime());
        mpArticle.setUpdateBy(mpArticle.getCreateBy());
    }

    private void saveHTMLContent(MpArticle mpArticle) throws IOException {
        String link = mpArticle.getLink();
        Connection connect = Jsoup.connect(link);
        Document document = connect.get();
        Element body = document.body();
        Elements richMediaWrp = body.getElementsByClass("rich_media_wrp");
        if (ObjectUtil.isEmpty(richMediaWrp)) {
            System.out.println("文章【" + mpArticle.getTitle() + "】找不到 <div class='rich_media_wrp'> 标签，爬取 HTML 内容失败");
            return;
        }
        Element richMedia = richMediaWrp.get(0);
        String result = richMedia.toString().replaceAll("visibility: hidden;", "");
//        mpArticle.setContent(result);
    }
}
