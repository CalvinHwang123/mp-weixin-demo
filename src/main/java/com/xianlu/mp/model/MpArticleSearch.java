package com.xianlu.mp.model;

import com.xianlu.mp.proxy.MpArticleSearchDataProxy;
import lombok.Data;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.BoolType;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.Table;

@Erupt(
        name = "搜索表单",
        dataProxy = { MpArticleSearchDataProxy.class }
)
@Table(name = "t_mp_article_search")
@Entity
@Data
public class MpArticleSearch extends BaseModel {

    @EruptField(
            views = @View(title = "方式"),
            edit = @Edit(
                    title = "方式",
                    notNull = true,
                    type = EditType.BOOLEAN,
                    boolType = @BoolType(
                            trueText = "搜文章",
                            falseText = "搜公众号"
                    )
            )
    )
    private Boolean method;

    @EruptField(
            views = @View(title = "关键词"),
            edit = @Edit(
                    title = "关键词",
                    notNull = true
            )
    )
    private String keyword;

}
