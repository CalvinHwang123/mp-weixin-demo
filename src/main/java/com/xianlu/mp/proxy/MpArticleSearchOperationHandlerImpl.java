package com.xianlu.mp.proxy;

import cn.hutool.core.util.ObjectUtil;
import com.xianlu.mp.model.MpArticle;
import com.xianlu.mp.model.MpArticleSearch;
import xyz.erupt.annotation.fun.OperationHandler;

import java.util.List;

public class MpArticleSearchOperationHandlerImpl implements OperationHandler<MpArticle, MpArticleSearch> {

    @Override
    public String exec(List<MpArticle> data, MpArticleSearch mpArticleSearch, String[] param) {
        String url;
        if (mpArticleSearch.getMethod()) {
            url = "/#/tpl/articleResult.html?keyword=" + mpArticleSearch.getKeyword();
        } else {
            url = "/#/tpl/mpResult.html?keyword=" + mpArticleSearch.getKeyword();
        }
        return "window.open('" + url + "')";
    }

}
