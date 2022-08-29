package com.xianlu.mp.handler;

import cn.hutool.core.io.FileUtil;
import com.xianlu.mp.api.WeiXinApi;
import com.xianlu.mp.enums.QrCodeStatus;
import com.xianlu.mp.model.MpToken;
import com.xianlu.mp.model.WxResultBody;
import com.xianlu.mp.okhttp.MyCookieStore;
import com.xianlu.mp.util.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.erupt.annotation.fun.OperationHandler;
import xyz.erupt.core.context.MetaContext;
import xyz.erupt.core.util.EruptSpringUtil;
import xyz.erupt.jpa.dao.EruptDao;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

@Service
public class MpTokenOperationHandlerImpl implements OperationHandler<MpToken, Void> {

    @Value("${erupt.uploadPath}")
    private String uploadPath;

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    //返回值为事件触发执行函数
    @Override
    public String exec(List<MpToken> data, Void vo, String[] param) {
        // 生成登录二维码
        generateQrCode();
        return null;
    }

    private void generateQrCode() {
        // 1. POST请求开始登录接口，初始化cookie
        String sessionid = "" + System.currentTimeMillis() + (int)(Math.random()*100);
        WxResultBody wxResultBody = WeiXinApi.startLogin(sessionid);
        System.out.println("---请求开始登录接口 返回结果:" + wxResultBody.toString());

        EruptDao eruptDao = EruptSpringUtil.getBean(EruptDao.class);
        // 2. 请求获取二维码图片接口，得到流
        MpToken mpToken = saveQrCode(eruptDao, sessionid);

        // 3.轮询二维码状态接口
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                while (true) {
                    WxResultBody askQRCode = WeiXinApi.askQRCode();
                    Integer status = askQRCode.getStatus();
                    if (status == QrCodeStatus.EXPIRED.getStatus()) {
                        System.out.println("二维码已过期");
                        return QrCodeStatus.EXPIRED.getStatus();
                    } else if (status == QrCodeStatus.SCANNED.getStatus()) {
                        System.out.println("已扫码，等待确认");
                    } else if (status == QrCodeStatus.CONFIRMED.getStatus()) {
                        System.out.println("已确认登录，请稍后...");
                        return QrCodeStatus.CONFIRMED.getStatus();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        while (true) {
            Thread queryThread = new Thread(futureTask);
            queryThread.start();
            if (!futureTask.isDone()) {
                System.out.println("--正在查询扫码状态，请尽快扫码！");
            }

            try {
                Integer integer = futureTask.get();
                String status = QrCodeStatus.SCANNED.getName();;
                if (integer == QrCodeStatus.CONFIRMED.getStatus()) {
                    //说明扫码确认成功，否则重新获取图片
                    System.out.println("扫码确认成功");
                    status = QrCodeStatus.CONFIRMED.getName();
                    mpToken.setStatus(status);
                    beforeUpdate(mpToken);
                    eruptDao.merge(mpToken);
                    break;
                } else {
                    if (integer == QrCodeStatus.EXPIRED.getStatus()) {
                        status = QrCodeStatus.EXPIRED.getName();
                    }
                    mpToken.setStatus(status);
                    beforeUpdate(mpToken);
                    eruptDao.mergeAndFlush(mpToken);
                    // 重新获取二维码图片
                    mpToken = saveQrCode(eruptDao, "" + System.currentTimeMillis() + (int)(Math.random()*100));
                }
            } catch (Exception e) {
                System.out.println("查询接口出错了");
                break;
            }
        }

        // 4.确认登录后，请求登录接口，拿到登录状态的cookie
        WxResultBody bizlogin = WeiXinApi.bizlogin();
        //重定向地址
        String redirect_url = bizlogin.getRedirect_url();
        //解析成键值对
        Map<String, String> loginRes = HttpUtils.parseQueryParams(redirect_url);
        System.out.println("loginRes");
        System.out.println(loginRes);
        //得到token
        String token = loginRes.get("token");
        //设置全局token值
        MyCookieStore.setToken(token);

        // token 保存数据库
        mpToken.setStatus(QrCodeStatus.SUCCESS.getName());
        mpToken.setToken(token);
        beforeUpdate(mpToken);
        eruptDao.merge(mpToken);

        System.out.println("---恭喜你，登录成功！");
    }

    private MpToken saveQrCode(EruptDao eruptDao,String sessionid) {
        InputStream qrCodeIs = WeiXinApi.getQRCode();
        // 删除原有二维码
        FileUtil.clean(uploadPath + "/qrCode");
        // 清空旧二维码数据
        eruptDao.getJdbcTemplate().execute(" update t_mp_token set qr_code = null, status = '" + QrCodeStatus.EXPIRED.getName() + "' order by id desc limit 1 ");
        // 保存图片到本地
        String imagePath = "/qrCode/" + sessionid + ".jpg";
        FileUtil.writeFromStream(qrCodeIs, uploadPath + imagePath);

        MpToken mpToken = new MpToken();
        mpToken.setStatus(QrCodeStatus.UNSCAN.getName());
        mpToken.setQrCode(imagePath);
        beforeAdd(mpToken);
        eruptDao.persist(mpToken);
        return mpToken;
    }

    private void beforeAdd(MpToken mpToken) {
        mpToken.setCreateTime(LocalDateTime.now());
        mpToken.setCreateBy(MetaContext.getUser().getName());
        mpToken.setUpdateTime(mpToken.getCreateTime());
        mpToken.setUpdateBy(mpToken.getCreateBy());
    }

    private void beforeUpdate(MpToken mpToken) {
        mpToken.setUpdateTime(LocalDateTime.now());
        mpToken.setUpdateBy(MetaContext.getUser().getName());
    }

}
