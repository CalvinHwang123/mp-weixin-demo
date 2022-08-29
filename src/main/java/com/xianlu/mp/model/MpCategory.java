package com.xianlu.mp.model;/*
 * Copyright © 2020-2035 erupt.xyz All rights reserved.
 * Author: YuePeng (erupts@126.com)
 */

import javax.persistence.*;

import com.xianlu.mp.proxy.MpCategoryDataProxy;
import xyz.erupt.annotation.*;
import xyz.erupt.annotation.sub_erupt.*;
import xyz.erupt.annotation.sub_field.*;
import xyz.erupt.annotation.sub_field.sub_edit.*;
import xyz.erupt.upms.model.base.HyperModel;
import xyz.erupt.jpa.model.BaseModel;
import java.util.Set;
import java.util.Date;

@Erupt(
        name = "公众号分类",
        dataProxy = { MpCategoryDataProxy.class }
)
@Table(name = "t_mp_category")
@Entity
public class MpCategory extends BaseModel {

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