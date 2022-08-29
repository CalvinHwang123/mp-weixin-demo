package com.xianlu.mp.proxy;

import com.xianlu.mp.model.MpInfo;
import com.xianlu.mp.util.ModelUtil;
import xyz.erupt.annotation.fun.DataProxy;

import java.util.Arrays;

public class MpInfoDataProxy implements DataProxy<MpInfo> {

    @Override
    public void beforeAdd(MpInfo mpInfo) {
        ModelUtil.validateUnique(mpInfo, Arrays.asList("code", "name"));
        mpInfo.setCheckStatus("N");
    }

    @Override
    public void beforeUpdate(MpInfo mpInfo) {
        ModelUtil.validateUnique(mpInfo, Arrays.asList("code", "nickname"));
    }
}
