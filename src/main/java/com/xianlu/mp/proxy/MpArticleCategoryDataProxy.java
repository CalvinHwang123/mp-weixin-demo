package com.xianlu.mp.proxy;

import com.xianlu.mp.model.MpArticleCategory;
import com.xianlu.mp.util.ModelUtil;
import xyz.erupt.annotation.fun.DataProxy;

import java.util.Arrays;

public class MpArticleCategoryDataProxy implements DataProxy<MpArticleCategory> {

    @Override
    public void beforeAdd(MpArticleCategory mpArticleCategory) {
        ModelUtil.validateUnique(mpArticleCategory, Arrays.asList("code", "name"));
    }

    @Override
    public void beforeUpdate(MpArticleCategory mpArticleCategory) {
        ModelUtil.validateUnique(mpArticleCategory, Arrays.asList("code", "name"));
    }
}
