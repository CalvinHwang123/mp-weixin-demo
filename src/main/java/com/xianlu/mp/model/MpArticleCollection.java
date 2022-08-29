package com.xianlu.mp.model;

import com.xianlu.mp.proxy.MpArticleCollectionProxy;
import lombok.Data;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.BoolType;
import xyz.erupt.annotation.sub_field.sub_edit.DateType;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTableType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.annotation.sub_field.sub_edit.ShowBy;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Erupt(
        name = "采集表单",
        dataProxy = { MpArticleCollectionProxy.class }
)
@Table(name = "t_mp_article_collection")
@Entity
@Data
public class MpArticleCollection extends BaseModel {

    @EruptField(
            views = @View(
                    title = "公众号",
                    column = "nickname"
            ),
            edit = @Edit(
                    title = "公众号",
                    notNull = true,
                    type = EditType.REFERENCE_TABLE, search = @Search,
                    referenceTableType = @ReferenceTableType(id = "id", label = "nickname")
            )
    )
    @ManyToOne
    private MpInfo mpInfo;

    @EruptField(
            views = @View(title = "采集方式"),
            edit = @Edit(
                    title = "采集方式",
                    desc = "采集【所有】很容易被官方限制，建议每次【区间】采集一个月",
                    notNull = true,
                    type = EditType.BOOLEAN,
                    boolType = @BoolType(
                            trueText = "所有",
                            falseText = "区间"
                    )
            )
    )
    private Boolean collectionAll;

    @EruptField(
            views = @View(title = "清空旧数据"),
            edit = @Edit(
                    title = "清空旧数据",
                    desc = "选择是，会清空该公众号所有本地文章",
                    showBy = @ShowBy(
                            dependField = "collectionAll",
                            expr = "value == true"
                    ),
                    type = EditType.BOOLEAN,
                    boolType = @BoolType(
                            trueText = "是",
                            falseText = "否"
                    )
            )
    )
    private Boolean deleteAll;

    @EruptField(
            views = @View(title = "文章发布开始日期"),
            edit = @Edit(
                    title = "文章发布开始日期",
                    desc = "为避免文章重复采集，可先查看上次采集该公众号的【文章发布时间】，错开它即可",
                    showBy = @ShowBy(
                            dependField = "collectionAll",
                            expr = "value == false"
                    ),
                    type = EditType.DATE,
                    dateType = @DateType(type = DateType.Type.DATE)
            )
    )
    private String collectStartDate;

    @EruptField(
            views = @View(title = "文章发布结束日期"),
            edit = @Edit(
                    title = "文章发布结束日期",
                    showBy = @ShowBy(
                            dependField = "collectionAll",
                            expr = "value == false"
                    ),
                    type = EditType.DATE,
                    dateType = @DateType(type = DateType.Type.DATE)
            )
    )
    private String collectEndDate;

}
