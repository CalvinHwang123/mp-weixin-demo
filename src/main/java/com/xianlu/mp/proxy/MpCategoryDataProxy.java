package com.xianlu.mp.proxy;

import com.xianlu.mp.model.MpCategory;
import com.xianlu.mp.util.ModelUtil;
import xyz.erupt.annotation.fun.DataProxy;

import java.util.Arrays;

public class MpCategoryDataProxy implements DataProxy<MpCategory> {

    @Override
    public void beforeAdd(MpCategory mpCategory) {
        ModelUtil.validateUnique(mpCategory, Arrays.asList("code", "name"));
    }

    @Override
    public void beforeUpdate(MpCategory mpCategory) {
        ModelUtil.validateUnique(mpCategory, Arrays.asList("code", "name"));
    }
}
