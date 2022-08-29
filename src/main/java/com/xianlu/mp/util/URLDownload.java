package com.xianlu.mp.util;

import java.io.File;
import java.net.URL;

public class URLDownload {

    public static boolean downloadFileFromUrl(String fileName, String downloadUrl, String savePath) {
        boolean result = false;
        try {

            //先判断文件是否存在
            File file1 = new File(savePath + fileName);
            if (file1.exists()) {
                System.out.println("删除已存在的文件");
                file1.delete();
            }
            long begin = System.currentTimeMillis();
            URL url = new URL(downloadUrl);
            File file = new File(savePath + fileName+"index.html");
            org.apache.commons.io.FileUtils.copyURLToFile(url, file);
            long end = System.currentTimeMillis();
            System.out.println("文件下载耗时：" + (end - begin) / 1000 + "s");
            //执行到此，说明文件下载完毕
            result = true;
            System.out.println(12);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
