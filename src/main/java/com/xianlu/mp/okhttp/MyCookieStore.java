package com.xianlu.mp.okhttp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xianlu.mp.util.JsonUtils;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * cookie管理
 * @author zsq
 * @version 1.0
 * @date 2021/3/31 16:17
 */
public class MyCookieStore implements CookieJar {

    /**打印日志用**/
    private static volatile boolean debug = false;

    /**登录后拿到的token**/
    private static String TOKEN = "";

    /**管理cookie**/
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>> cookieStore = new ConcurrentHashMap<>();

    /**token资源文件位置**/
    public static final String TOKEN_FILE_PATH = "/store/token.txt";

    /**cookie资源文件位置**/
    public static final String COOKIE_FILE_PATH = "/store/cookie.txt";


    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        log("-----保存cookie");

        //保存cookie
        log("httpUrl host: " + httpUrl.host());
        ConcurrentHashMap<String, Cookie> cookieMap = cookieStore.get(httpUrl.host());
        if (cookieMap == null) {
            cookieMap = new ConcurrentHashMap<>();
            cookieStore.put(httpUrl.host(), cookieMap);
        }
        for (Cookie cookie : list) {
            cookieMap.put(cookie.name(), cookie);
        }

        if (list != null && list.size() > 0) {
            //存到本地，需要自己写持久化的实现，可用redis，或本地
        }

        log("-----保存到cookie：" + cookieStore.toString());
        log("------------------------：");
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        log("---加载 cookie");
        List<Cookie> cookies = new ArrayList<>();
        Map<String, Cookie> cookieMap = cookieStore.get(httpUrl.host());
        if (cookieMap != null) {
            cookieMap.forEach((name, cookie)->{
                log("--cookie:" + name + "=" + cookie.value());
                cookies.add(cookie);
            });
        }
        log("---load cookie");
        return cookies;
    }

    public static void setToken(String token) {
        TOKEN = token;
    }

    public static String getToken() {
        return TOKEN;
    }

    /**
     * 保存cookie
     * @param resPath 资源文件目录
     */
    public static void saveCookie (String resPath, ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>> cookieMap) {

    }

    /**
     * 保存到文件
     * @param resPath 资源文件目录
     */
    public static void saveToFile (String resPath, String str) {
        URL resource = MyCookieStore.class.getResource(resPath);
        try {
            IOUtils.write(str.getBytes(), new FileOutputStream(resource.getFile()));
        } catch (IOException e) {
            System.out.println("---保存数据到本地失败");
            e.printStackTrace();
        }
    }

    /**
     * 加载cookie到内存
     */
    public static void loadCookie() {
        InputStream resourceAsStream = MyCookieStore.class.getResourceAsStream(COOKIE_FILE_PATH);
        String cookieTxt = null;
        try {
            cookieTxt = IOUtils.toString(resourceAsStream);
            if (StringUtils.isNotBlank(cookieTxt)) {
                //转成对象
                ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>> nativeCookie = JsonUtils.jsonToObj(cookieTxt,
                        new TypeReference<ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>>>() {});
                cookieStore = nativeCookie;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载token到内存
     */
    public static void loadToken() {
        InputStream resourceAsStream = MyCookieStore.class.getResourceAsStream(TOKEN_FILE_PATH);
        String s = null;
        try {
            s = IOUtils.toString(resourceAsStream);
            TOKEN = s;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String log) {
        if (!debug) {
            return;
        }
        System.out.println(log);
    }

}
