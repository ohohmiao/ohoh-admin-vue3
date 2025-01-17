package com.ohohmiao.modules.workflow.model.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.modules.workflow.enums.FlowEventImplTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * 流程事件新增或编辑
 *
 * @author ohohmiao
 * @date 2025-01-16 18:59
 */
@ApiModel("流程事件新增或编辑")
@Getter
@Setter
@ScriptAssert(lang = "javascript", script = "_this.isValidImpl()", message = "实现方式传参非法")
public class FlowEventAddOrEditDTO {

    @ApiModelProperty(value = "流程编码", required = true)
    @NotBlank(message = "流程编码不能为空")
    private String defCode;

    @ApiModelProperty(value = "版本号", required = true)
    @NotNull(message = "版本号不能为空")
    private Integer defVersion;

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String eventId;

    @ApiModelProperty(value = "事件名称", required = true)
    @NotBlank(message = "事件名称不能为空")
    private String eventName;

    @ApiModelProperty(value = "事件类别", required = true)
    @NotNull(message = "事件类别不能为空")
    private String eventType;

    @ApiModelProperty(value = "绑定环节id", required = true)
    @NotEmpty(message = "绑定环节id不能为空")
    private List<String> bindNodeIds;

    @ApiModelProperty(value = "绑定环节名称", required = true)
    @NotEmpty(message = "绑定环节名称不能为空")
    private List<String> bindNodeNames;

    @ApiModelProperty(value = "实现方式", required = true)
    @NotNull(message = "实现方式不能为空")
    private Integer implType;

    @ApiModelProperty(value = "本地服务")
    private String implLocalservice;

    @ApiModelProperty(value = "实现脚本")
    private String implScript;

    public boolean isValidImpl(){
        if(ObjectUtil.isNotNull(this.implType)){
            if(this.implType.equals(FlowEventImplTypeEnum.LOCAL_SERVICE.ordinal())){
                if(StrUtil.isBlank(this.implLocalservice)){
                    return false;
                }
                this.implScript = null;
            }
            if(this.implType.equals(FlowEventImplTypeEnum.SCRIPT.ordinal())){
                if(StrUtil.isBlank(this.implScript)){
                    return false;
                }
                this.implLocalservice = null;
            }
        }
        return true;
    }

}
