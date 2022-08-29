package com.xianlu.mp.model;

import com.xianlu.mp.handler.MpTokenOperationHandlerImpl;
import lombok.Data;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.Power;
import xyz.erupt.annotation.sub_erupt.RowOperation;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.Readonly;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.ViewType;
import xyz.erupt.annotation.sub_field.sub_edit.AttachmentType;
import xyz.erupt.jpa.model.MetaModelVo;

import javax.persistence.Entity;
import javax.persistence.Table;

@Erupt(
        name = "登录TOKEN",
        power = @Power(
                add = false,
                edit = false,
                viewDetails = false
        ),
        rowOperation = @RowOperation(
                title = "生成二维码",
                code = "SINGLE",
                icon = "fa fa-qrcode",
                mode = RowOperation.Mode.BUTTON,
                operationHandler = MpTokenOperationHandlerImpl.class
        ),
        desc = "生成二维码后请尽快使用微信扫码，二维码默认路径：D:/erupt/pictures/qrCode",
        orderBy = "createTime desc"
)
@Table(name = "t_mp_token")
@Entity
@Data
public class MpToken extends MetaModelVo {

    @EruptField(
            views = @View(
                    title = "二维码",
                    type = ViewType.IMAGE
            ),
            edit = @Edit(
                    title = "二维码",
                    type = EditType.ATTACHMENT,
                    readonly = @Readonly,
                    attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE)
            )
    )
    private String qrCode;

    @EruptField(
            views = @View(title = "token"),
            edit = @Edit(
                    title = "token",
                    readonly = @Readonly
            )
    )
    private String token;

    @EruptField(
            views = @View(title = "状态"),
            edit = @Edit(
                    title = "状态",
                    readonly = @Readonly
            )
    )
    private String status;

}
