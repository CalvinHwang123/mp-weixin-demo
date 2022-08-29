package com.xianlu.mp.model;/*
 * Copyright © 2020-2035 erupt.xyz All rights reserved.
 * Author: YuePeng (erupts@126.com)
 */

import com.xianlu.mp.handler.MpInfoOperationHandlerImpl;
import com.xianlu.mp.proxy.MpInfoDataProxy;
import lombok.Data;
import org.springframework.stereotype.Service;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.RowOperation;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.Readonly;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.ViewType;
import xyz.erupt.annotation.sub_field.sub_edit.AttachmentType;
import xyz.erupt.annotation.sub_field.sub_edit.ChoiceType;
import xyz.erupt.annotation.sub_field.sub_edit.InputType;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTableType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.annotation.sub_field.sub_edit.VL;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Erupt(
        name = "公众号管理",
        dataProxy = { MpInfoDataProxy.class },
        rowOperation = @RowOperation(
                title = "校验",
                code = "SINGLE",
                icon = "fa fa-check-square-o",
                tip = "用于校验该公众号是否可用",
                mode = RowOperation.Mode.SINGLE,
                operationHandler = MpInfoOperationHandlerImpl.class
        )
)
@Table(name = "t_mp_info")
@Entity
@Data
public class MpInfo extends BaseModel {

        @EruptField(
                views = @View(
                        title = "公众号编号"
                ),
                edit = @Edit(
                        title = "公众号编号",
                        type = EditType.INPUT, search = @Search(vague = true), notNull = true,
                        inputType = @InputType
                )
        )
        private String code;

        @EruptField(
                views = @View(
                        title = "公众号名称"
                ),
                edit = @Edit(
                        title = "公众号名称",
                        type = EditType.INPUT, search = @Search(vague = true), notNull = true,
                        inputType = @InputType
                )
        )
        private String nickname;

        @EruptField(
                views = @View(
                        title = "头像",
                        type = ViewType.IMAGE
                ),
                edit = @Edit(
                        title = "头像",
                        readonly = @Readonly,
                        type = EditType.ATTACHMENT,
                        attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE)
                )
        )
        private String roundHeadImg;

        @EruptField(
                views = @View(
                        title = "头像原始链接",
                        show = false
                ),
                edit = @Edit(
                        title = "头像原始链接",
                        show = false
                )
        )
        private String roundHeadImgUrl;

        @EruptField(
                views = @View(
                        title = "所属分类",
                        column = "name"
                ),
                edit = @Edit(
                        title = "所属分类",
                        type = EditType.REFERENCE_TABLE, search = @Search,
                        referenceTableType = @ReferenceTableType(id = "id", label = "name")
                )
        )
        @ManyToOne
        private MpCategory mpCategory;

        @EruptField(
                views = @View(title = "校验状态"),
                edit = @Edit(
                        title = "校验状态",
                        type = EditType.CHOICE,
                        search = @Search,
                        choiceType = @ChoiceType(
                                vl = {
                                        @VL(
                                                label = "已校验",
                                                value = "Y"
                                        ),
                                        @VL(
                                                label = "未校验",
                                                value = "N"
                                        )
                                }

                        )
                )
        )
        private String checkStatus;

        @EruptField(
                views = @View(title = "唯一ID"),
                edit = @Edit(
                        title = "唯一ID",
                        readonly = @Readonly
                )
        )
        private String fakeid;

        @EruptField(
                views = @View(title = "原始昵称"),
                edit = @Edit(
                        title = "原始昵称",
                        readonly = @Readonly
                )
        )
        private String alias;

        // 服务类型，初步确认 0 订阅号（折叠面板显示，如 CSDN） 1 未知（如 CSDN企业招聘） 2 服务号（聊天会话显示，如 CSDN技术社区）
        @EruptField(
                views = @View(title = "服务类型"),
                edit = @Edit(
                        title = "服务类型",
                        readonly = @Readonly
                )
        )
        private String serviceType;

        @EruptField(
                views = @View(title = "公众号简介"),
                edit = @Edit(
                        title = "公众号简介",
                        type = EditType.TEXTAREA
                )
        )
        private String signature;

}