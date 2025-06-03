package com.ohohmiao.modules.workflow.model.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ohohmiao.modules.system.model.pojo.SysReferRes;
import com.ohohmiao.modules.workflow.enums.FlowInitiatorScopeEnum;
import com.ohohmiao.modules.workflow.enums.ProcessLimitTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 流程定义历史部署修改
 *
 * @author ohohmiao
 * @date 2025-01-08 10:31
 */
@ApiModel("流程定义历史部署修改")
@Getter
@Setter
@ScriptAssert(lang = "javascript", script = "_this.isTargetInitiatorsRequired()", message = "指定人员不能为空")
@ScriptAssert(lang = "javascript", script = "_this.isProcessLimitvalueRequired()", message = "时限限制值不能为空")
public class FlowHisDeployDTO {

    @ApiModelProperty(value = "类别id", required = true)
    @NotBlank(message = "类别id不能为空")
    private String deftypeId;

    @ApiModelProperty(value = "流程名称", required = true)
    @NotBlank(message = "流程名称不能为空")
    private String defName;

    @ApiModelProperty(value = "流程编码", required = true)
    @NotBlank(message = "流程编码不能为空")
    private String defCode;

    @ApiModelProperty(value = "版本号", required = true)
    @NotNull(message = "版本号不能为空")
    private Integer defVersion;

    @ApiModelProperty(value = "排序", required = true)
    @NotNull(message = "排序不能为空")
    private Integer defSort;

    @ApiModelProperty(value = "发起范围", required = true)
    @NotNull(message = "发起范围不能为空")
    private Integer initiatorScope;

    @ApiModelProperty(value = "指定人员")
    private List<SysReferRes> targetInitiators;

    @ApiModelProperty(value = "时限限制类别", required = true)
    @NotNull(message = "时限限制类别不能为空")
    private Integer processLimittype;

    @ApiModelProperty(value = "时限限制值")
    private Integer processLimitvalue;

    public boolean isTargetInitiatorsRequired(){
        if(ObjectUtil.isNotNull(this.initiatorScope)){
            if(this.initiatorScope.equals(FlowInitiatorScopeEnum.TARGET.ordinal())){
                if(CollUtil.isEmpty(this.targetInitiators)){
                    return false;
                }
            }else{
                if(this.targetInitiators != null){
                    this.targetInitiators.clear();
                }
            }
        }
        return true;
    }

    public boolean isProcessLimitvalueRequired(){
        if(ObjectUtil.isNotNull(this.processLimittype)){
            if(!this.processLimittype.equals(ProcessLimitTypeEnum.NO.ordinal())){
                if(ObjectUtil.isNull(this.processLimitvalue)){
                    return false;
                }
            }else{
                this.setProcessLimitvalue(null);
            }
        }
        return true;
    }

}
