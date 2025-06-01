package com.ohohmiao.modules.workflow.model.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ohohmiao.framework.common.model.pojo.CommonReferRes;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.modules.workflow.enums.FlowHandlerTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * 流程环节办理人配置新增或编辑
 *
 * @author ohohmiao
 * @date 2025-06-01 17:45
 */
@ApiModel("流程环节办理人配置新增或编辑")
@Getter
@Setter
@ScriptAssert(lang = "javascript", script = "_this.isTargetReferResListRequired()", message = "指定人员不能为空")
@ScriptAssert(lang = "javascript", script = "_this.isInterfaceCodeRequired()", message = "指定接口不能为空")
public class FlowHandlerAddOrEditDTO {

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String handlerId;

    @ApiModelProperty(value = "流程编码", required = true)
    @NotBlank(message = "流程编码不能为空")
    private String defCode;

    @ApiModelProperty(value = "版本号", required = true)
    @NotNull(message = "版本号不能为空")
    private Integer defVersion;

    @ApiModelProperty(value = "环节id", required = true)
    @NotBlank(message = "环节id不能为空")
    private String nodeId;

    @ApiModelProperty(value = "下一环节id", required = true)
    @NotBlank(message = "下一环节id不能为空")
    private String nextnodeId;

    @ApiModelProperty(value = "下一环节名称", required = true)
    @NotBlank(message = "下一环节名称不能为空")
    private String nextnodeName;

    @ApiModelProperty(value = "办理人类型", required = true)
    @NotNull(message = "办理人类型不能为空")
    private Integer handlerType;

    @ApiModelProperty(value = "指定人员")
    private List<CommonReferRes> targetReferResList;

    @ApiModelProperty(value = "指定接口")
    private String interfaceCode;

    @ApiModelProperty(value = "过滤规则")
    private String filterRule;

    @ApiModelProperty(value = "多人审核方式", required = true)
    @NotNull(message = "多人审核方式不能为空")
    private Integer multiHandletype;

    @ApiModelProperty(value = "是否允许重选办理人", required = true)
    @NotNull(message = "是否允许重选办理人不能为空")
    private Integer reselectPermit;

    public boolean isTargetReferResListRequired(){
        if(ObjectUtil.isNotNull(this.handlerType)){
            if(this.handlerType.equals(FlowHandlerTypeEnum.REFERRES.ordinal())){
                return CollectionUtil.isNotEmpty(this.targetReferResList);
            }else{
                this.setInterfaceCode(null);
            }
        }
        return true;
    }

    public boolean isInterfaceCodeRequired(){
        if(ObjectUtil.isNotNull(this.handlerType)){
            if(this.handlerType.equals(FlowHandlerTypeEnum.INTERFACE.ordinal())){
                if(this.targetReferResList != null){
                    this.targetReferResList.clear();
                }
                return StrUtil.isNotBlank(this.interfaceCode);
            }
        }
        return true;
    }

}
