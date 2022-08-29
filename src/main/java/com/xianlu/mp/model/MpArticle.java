package com.xianlu.mp.model;/*
 * Copyright © 2020-2035 erupt.xyz All rights reserved.
 * Author: YuePeng (erupts@126.com)
 */

import com.xianlu.mp.handler.MpArticleCollectionOperationHandlerImpl;
import com.xianlu.mp.proxy.MpArticleDataProxy;
import com.xianlu.mp.proxy.MpArticleSearchOperationHandlerImpl;
import lombok.Data;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.RowOperation;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.Readonly;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.ViewType;
import xyz.erupt.annotation.sub_field.sub_edit.AttachmentType;
import xyz.erupt.annotation.sub_field.sub_edit.DateType;
import xyz.erupt.annotation.sub_field.sub_edit.InputType;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTableType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.jpa.model.MetaModelVo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Erupt(
        name = "公众号文章管理",
        dataProxy = { MpArticleDataProxy.class },
        rowOperation = {
                @RowOperation(
                        title = "文章采集",
                        code = "MPARTICLECOLLECTION",
                        icon = "fa fa-envira",
                        tip = "文章采集每日有次数限制，请稍后重试或者第二天重试，或者换个公众号扫码登录",
                        mode = RowOperation.Mode.BUTTON,
                        type = RowOperation.Type.ERUPT,
                        eruptClass = MpArticleCollection.class,
                        operationHandler = MpArticleCollectionOperationHandlerImpl.class
                ),
                @RowOperation(
                        title = "关键词搜索",
                        code = "MPARTICLESEARCH",
                        icon = "fa fa-search",
                        mode = RowOperation.Mode.BUTTON,
                        type = RowOperation.Type.ERUPT,
                        eruptClass = MpArticleSearch.class,
                        operationHandler = MpArticleSearchOperationHandlerImpl.class
                )
        },
        orderBy = "articleCreateTime desc"
)
@Table(name = "t_mp_article")
@Entity
@Data
public class MpArticle extends MetaModelVo {

        @EruptField(
                views = @View(
                        title = "文章标题"
                ),
                edit = @Edit(
                        title = "文章标题",
                        type = EditType.INPUT, search = @Search(vague = true), notNull = true,
                        inputType = @InputType
                )
        )
        private String title;

        @EruptField(
                views = @View(
                        title = "封面",
                        type = ViewType.IMAGE
                ),
                edit = @Edit(
                        title = "封面",
                        type = EditType.ATTACHMENT,
                        attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE)
                )
        )
        private String cover;

        @EruptField(
                views = @View(
                        title = "所属公众号",
                        column = "nickname"
                ),
                edit = @Edit(
                        title = "所属公众号",
                        type = EditType.REFERENCE_TABLE, search = @Search,
                        referenceTableType = @ReferenceTableType(id = "id", label = "nickname")
                )
        )
        @ManyToOne
        private MpInfo mpInfo;

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
        private MpArticleCategory mpArticleCategory;

//        @EruptField(
//                views = @View(
//                        title = "内容",
//                        type = ViewType.HTML
//                ),
//                edit = @Edit(
//                        title = "内容",
//                        type = EditType.HTML_EDITOR
//                )
//        )
//        private @Lob String content;

        @EruptField(
                views = @View(
                        title = "原始链接",
                        type = ViewType.LINK
                ),
                edit = @Edit(
                        title = "原始链接",
                        type = EditType.INPUT
                )
        )
        private String link;

        @EruptField(
                views = @View(title = "文章发布时间", sortable = true),
                edit = @Edit(title = "文章发布时间", readonly = @Readonly, dateType = @DateType(type = DateType.Type.DATE_TIME))
        )
        private LocalDateTime articleCreateTime;

        @EruptField(
                views = @View(title = "文章更新时间", sortable = true),
                edit = @Edit(title = "文章更新时间", readonly = @Readonly, dateType = @DateType(type = DateType.Type.DATE_TIME))
        )
        private LocalDateTime articleUpdateTime;

        @EruptField(
                views = @View(title = "唯一ID"),
                edit = @Edit(
                        title = "唯一ID",
                        readonly = @Readonly
                )
        )
        private String aid;

        @EruptField(
                views = @View(title = "群发ID"),
                edit = @Edit(
                        title = "群发ID",
                        readonly = @Readonly
                )
        )
        private String appmsgid;

        @EruptField(
                views = @View(
                        title = "封面原始链接",
                        show = false
                ),
                edit = @Edit(
                        title = "封面原始链接",
                        show = false
                )
        )
        private String coverUrl;

        @EruptField(
                views = @View(title = "摘要信息"),
                edit = @Edit(
                        title = "摘要信息",
                        readonly = @Readonly
                )
        )
        private String digest;

        @EruptField(
                views = @View(title = "是否付费文章"),
                edit = @Edit(
                        title = "是否付费文章",
                        readonly = @Readonly
                )
        )
        private Integer paySubscribe;

}