package com.xianlu.mp.model;/*
 * Copyright © 2020-2035 erupt.xyz All rights reserved.
 * Author: YuePeng (erupts@126.com)
 */

import com.xianlu.mp.proxy.MpArticleCategoryDataProxy;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.InputType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Erupt(
        name = "公众号文章分类",
        dataProxy = { MpArticleCategoryDataProxy.class }
)
@Table(name = "t_mp_article_category")
@Entity
public class MpArticleCategory extends BaseModel {

        @EruptField(
                views = @View(
                        title = "分类编号"
                ),
                edit = @Edit(
                        title = "分类编号",
                        type = EditType.INPUT, search = @Search(vague = true), notNull = true,
                        inputType = @InputType
                )
        )
        private String code;

        @EruptField(
                views = @View(
                        title = "分类名称"
                ),
                edit = @Edit(
                        title = "分类名称",
                        type = EditType.INPUT, search = @Search(vague = true), notNull = true,
                        inputType = @InputType
                )
        )
        private String name;

        @EruptField(
                views = @View(
                        title = "备注"
                ),
                edit = @Edit(
                        title = "备注",
                        type = EditType.TEXTAREA
                )
        )
        private @Lob String remark;

}