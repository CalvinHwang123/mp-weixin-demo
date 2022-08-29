package com.xianlu.mp.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xianlu.mp.api.WeiXinApi;
import com.xianlu.mp.model.MpInfo;
import com.xianlu.mp.model.WxResultBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.erupt.annotation.fun.OperationHandler;
import xyz.erupt.core.util.EruptSpringUtil;
import xyz.erupt.jpa.dao.EruptDao;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Service
public class MpInfoOperationHandlerImpl implements OperationHandler<MpInfo, Void> {

    @Value("${erupt.uploadPath}")
    private String uploadPath;

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String exec(List<MpInfo> data, Void unused, String[] param) {
        String tempMsg = "";
        if (!CollectionUtils.isEmpty(data)) {
            // 暂时只支持单个数据
            MpInfo item = data.get(0);
            WxResultBody<List<Map>> resultBody = WeiXinApi.searchBiz(item.getNickname(), "1");
            if (resultBody.getBase_resp().getRet() == 0) {
                List<Map> list = resultBody.getList();
                if (!CollectionUtils.isEmpty(list)) {
                    // 只有一条数据
                    Map map = list.get(0);
                    // 公众号名称一致，表示校验成功
                    if (item.getNickname().equalsIgnoreCase(map.get("nickname").toString())) {
                        item.setFakeid(map.get("fakeid").toString());
                        item.setAlias(map.get("alias").toString());
                        item.setRoundHeadImgUrl(map.get("round_head_img").toString());
                        // 微信官方图片做了防盗链，不可直接引用展示，因此下载到本地
                        String avatarPath = "/mpAvatar/" + item.getAlias() + ".png";
                        try {
                            FileUtil.writeFromStream(new URL(map.get("round_head_img").toString()).openStream(), uploadPath + avatarPath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        item.setRoundHeadImg(avatarPath);
                        item.setServiceType(map.get("service_type").toString());
                        item.setSignature(ObjectUtil.isNotEmpty(map.get("signature")) ? map.get("signature").toString() : "");
                        item.setCheckStatus("Y");
                        EruptDao eruptDao = EruptSpringUtil.getBean(EruptDao.class);
                        eruptDao.merge(item);
                        tempMsg = "校验成功，相关字段已自动更新";
                    }
                } else {
                    tempMsg = "校验失败，该公众号不存在，请检查名称是否有误";
                }
            } else {
                throw new RuntimeException("操作失败：接口异常 " + resultBody.getErr_msg());
            }
        }
        return  "this.msg.success('" + tempMsg + "')";
    }

}
