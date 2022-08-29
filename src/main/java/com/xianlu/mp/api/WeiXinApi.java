package com.xianlu.mp.api;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.xianlu.mp.enums.WxResultStatus;
import com.xianlu.mp.exceptions.WxApiException;
import com.xianlu.mp.model.WxResultBody;
import com.xianlu.mp.util.HttpUtils;
import com.xianlu.mp.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import xyz.erupt.core.util.EruptSpringUtil;
import xyz.erupt.jpa.dao.EruptDao;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xianlu.mp.enums.MpBaseResp.FREQ_CONTROL;
import static com.xianlu.mp.enums.MpBaseResp.INVALID_SESSION;

/**
 * 微信api封装
 * @author zsq
 * @version 1.0
 * @date 2021/3/31 11:13
 */
public class WeiXinApi {

    /**接口地址**/
    public static final Map<String, String> URL_MAP = new HashMap<>();

    static {
        //初始化登录cookie
        URL_MAP.put("startLogin", "https://mp.weixin.qq.com/cgi-bin/bizlogin?action=startlogin");

        //二维码图片接口
        URL_MAP.put("getqrcode", "https://mp.weixin.qq.com/cgi-bin/scanloginqrcode");

        //二维码扫描状态接口
        URL_MAP.put("qrcodeAsk", "https://mp.weixin.qq.com/cgi-bin/scanloginqrcode");

        //扫码确认后，登录接口
        URL_MAP.put("bizlogin", "https://mp.weixin.qq.com/cgi-bin/bizlogin?action=login");

        // 公众号标题搜索接口
        URL_MAP.put("searchbiz", "https://mp.weixin.qq.com/cgi-bin/searchbiz");

        // 公众号内文章搜索接口
        URL_MAP.put("findListEx", "https://mp.weixin.qq.com/cgi-bin/appmsg");
    }

    /**
     * POST请求开始登录接口，初始化cookie
     * @param sessionid 时间戳，加两位随机数
     * @return
     */
    public static WxResultBody startLogin(String sessionid) {
        if (StringUtils.isBlank(sessionid)) {
            sessionid = "" + System.currentTimeMillis() + (int)(Math.random()*100);
        }

        Map<String, String> params = new HashMap<>(8);
        params.put("sessionid", sessionid);
        params.put("userlang", "zh_CN");
        params.put("redirect_url", "");
        params.put("login_type", "3");
        params.put("token", "");
        params.put("lang", "zh_CN");
        params.put("f", "json");
        params.put("ajax", "1");

        WxResultBody wxResultBody = parseWxResultBody(HttpUtils.doPost(URL_MAP.get("startLogin"), params));

        return wxResultBody;
    }

    /**
     * 获取二维码图片 字节输出流
     * @return
     */
    public static InputStream getQRCode() {
        Map<String, String> params = new HashMap<>(2);
        params.put("action", "getqrcode");
        params.put("random", System.currentTimeMillis() + "");
        InputStream inputStream = HttpUtils.doGetStream(URL_MAP.get("getqrcode"), params);

        return inputStream;
    }


    /**
     * GET 轮询二维码状态接口
     * @return
     */
    public static WxResultBody askQRCode() {
        Map<String, String> params = new HashMap<>(5);
        params.put("action", "ask");
        params.put("token", "");
        params.put("lang", "zh_CN");
        params.put("f", "json");
        params.put("ajax", "1");

        WxResultBody wxResultBody = parseWxResultBody(HttpUtils.doGet(URL_MAP.get("qrcodeAsk"), params));

        return wxResultBody;
    }

    /**
     * 扫码确认后，登录接口
     * @return
     */
    public static WxResultBody bizlogin() {
        Map<String, String> params = new HashMap<>(10);
        params.put("userlang", "zh_CN");
        params.put("redirect_url", "");
        params.put("cookie_forbidden", "0");
        params.put("cookie_cleaned", "0");
        params.put("plugin_used", "0");
        params.put("login_type", "3");
        params.put("token", "");
        params.put("lang", "zh_CN");
        params.put("f", "json");
        params.put("ajax", "1");

        WxResultBody wxResultBody = parseWxResultBody(HttpUtils.doPost(URL_MAP.get("bizlogin"), params));

        return wxResultBody;
    }


    /**
     * 搜索公众号
     *
     * 请求参数
         * action 动作
         * begin 列表的起始
         * count 列表的数目
         * query 查询的字符串
         * f 参数格式 这里为json
         * ajax 应该代码ajax请求
         * lang 语言 这里是中文
         * token 这应该是授权信息
     *
     * 响应参数
         * fakeid 为该公众号的唯一的id，为一串bs64编码
         * nikename 为公众号的名称
         * alias 为别名
         * round_head_img 为圆形logo的url
         * service_type 服务类型 不太清楚
     *
     * @return
     */
    public static WxResultBody<List<Map>> searchBiz(String keyword, String count){
        Map<String, String> params = new HashMap<>(10);
        params.put("action", "search_biz");
        params.put("begin", "0");
        params.put("count", count);
        params.put("query", keyword);
        params.put("token", getToken());
        params.put("lang", "zh_CN");
        params.put("f", "json");
        params.put("ajax", "1");

        WxResultBody<List<Map>> wxResultBody = parseWxResultBody(HttpUtils.doGet(URL_MAP.get("searchbiz"), params),
                new TypeReference<WxResultBody<List<Map>>>() {}
        );
        System.out.println(wxResultBody);

        return wxResultBody;
    }

    /**
     * 搜索公众号的文章
     *
     * 请求参数
         * action 行为
         * begin 列表开始索引
         * count 列表返回的公众号的时间区间长度，如5表示返回5天的数据
         * （经过验证不是这样的，count表示最近发布的次数，比如一天可以发布多次，每次又可以发布多篇）
         * fakeid 这个公众号的ID
         * type 不知道
         * query 检索的关键字，这里为空
         * token 用户的token
         * lang 语言
         * f 数据格式,这里为json
         * ajax
     *
     * 响应参数
         * app_msg_cnt 表示这个公众号已经发布了919次文章，不代表919篇文章
         * aid 文章唯一的id，应该是
         * appmsgid 代表一次群发，如三篇文章是一次性群发的，其appmsgid相同
         * cover 文章封面图片的url
         * create_time 创建时间戳
         * digest 文章的摘要信息
         * is_pay_subscribe
         * item_show_type
         * itemidx 在这次群发中的序号
         * link 文章的url
         * tagid 为一个列表
         * title 文章的标题
         * update_time 文章更新的时间戳
     *
     * @return
     */
    public static WxResultBody<List<Map>> findExList(String fakeid, String pageNo){
        Map<String, String> params = new HashMap<>(10);
        params.put("action", "list_ex");
        params.put("begin", ((Integer.parseInt(pageNo) - 1) * 5) + "");
        params.put("count", "5"); // 条数固定，5条，调大也没用
        params.put("fakeid", fakeid);
        params.put("token", getToken());
        params.put("type", "9");
        params.put("query", "");
        params.put("lang", "zh_CN");
        params.put("f", "json");
        params.put("ajax", "1");

        WxResultBody<List<Map>> wxResultBody = parseWxResultBody(HttpUtils.doGet(URL_MAP.get("findListEx"), params),
                new TypeReference<WxResultBody<List<Map>>>() {}
        );
        System.out.println(wxResultBody);

        return wxResultBody;
    }


    /**
     * 转成java bean
     * @param jsonRes json结果字符串
     * @return
     */
    public static WxResultBody parseWxResultBody(String jsonRes) {
        WxResultBody wxResultBody = JsonUtils.jsonToObj(jsonRes, WxResultBody.class);

        checkIsSuccess(wxResultBody);

        return wxResultBody;
    }

    /**
     * 转成java bean
     * @param jsonRes json结果字符串
     * @return
     */
    public static <T> T parseWxResultBody(String jsonRes, TypeReference<T> typeReference) {
        T wxResultBody = JsonUtils.jsonToObj(jsonRes, typeReference);
        checkIsSuccess((WxResultBody) wxResultBody);
        return wxResultBody;
    }

    public static void checkIsSuccess(WxResultBody wxResultBody) {
        // 判断是否请求成功
        if (wxResultBody == null) {
            throw new WxApiException(WxResultStatus.FAIL_NULL_RES);
        }

        if (wxResultBody.getBase_resp().getRet() == null
                || wxResultBody.getBase_resp().getRet() != 0) {
            // 需重新扫码登录
            if (wxResultBody.getBase_resp().getRet() == INVALID_SESSION.getRet()) {
                throw new RuntimeException("操作失败：" + INVALID_SESSION.getErrMsg());
            }
            // 频率控制
            if (wxResultBody.getBase_resp().getRet() == FREQ_CONTROL.getRet()) {
                throw new RuntimeException("操作失败：" + FREQ_CONTROL.getErrMsg());
            }
            throw new WxApiException(WxResultStatus.FAIL_STATUS);
        }
    }

    private static String getToken() {
        EruptDao eruptDao = EruptSpringUtil.getBean(EruptDao.class);
        List<Map<String, Object>> tokenList = eruptDao.getJdbcTemplate().queryForList(" select token from t_mp_token where 1=1 and status = '已成功' order by id desc limit 1 ");
        if (CollectionUtils.isEmpty(tokenList) || ObjectUtil.isEmpty(tokenList.get(0).get("token"))) {
            throw new RuntimeException("操作失败：" + INVALID_SESSION.getErrMsg());
        }
        return tokenList.get(0).get("token").toString();
    }

}
