package com.ohohmiao.modules.workflow.model.dto;

import cn.hutool.core.util.ObjectUtil;
import com.ohohmiao.modules.workflow.enums.FlowTaskAssignTypeEnum;
import com.ohohmiao.modules.workflow.enums.FlowTaskMultiAssignRuleEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 流程环节属性新增或编辑
 *
 * @author ohohmiao
 * @date 2025-05-30 17:13
 */
@ApiModel("流程环节属性新增或编辑")
@Getter
@Setter
@ScriptAssert(lang = "javascript", script = "_this.isMultiassignRuleRequired()", message = "多人决策方式不能为空")
@ScriptAssert(lang = "javascript", script = "_this.isMultiassignRatioRequired()", message = "多人决策比例值不能为空")
@ScriptAssert(lang = "javascript", script = "_this.isMultiassignWeightjsonRequired()", message = "多人决策权重json不能为空")
public class FlowNodeAddOrEditDTO {

    @ApiModelProperty(value = "流程编码", required = true)
    @NotBlank(message = "流程编码不能为空")
    private String defCode;

    @ApiModelProperty(value = "版本号", required = true)
    @NotNull(message = "版本号不能为空")
    private Integer defVersion;

    @ApiModelProperty(value = "环节id", required = true)
    @NotBlank(message = "环节id不能为空")
    private String nodeId;

    @ApiModelProperty(value = "环节名称", required = true)
    @NotBlank(message = "环节名称不能为空")
    private String nodeName;

    @ApiModelProperty(value = "任务指派类别", required = true)
    @NotNull(message = "任务指派类别不能为空")
    private Integer taskAssigntype;

    @ApiModelProperty(value = "多人决策方式")
    private Integer multiassignRule;

    @ApiModelProperty(value = "多人决策比例值")
    private Integer multiassignRatio;

    @ApiModelProperty(value = "多人决策权重json")
    private String multiassignWeightjson;

    @ApiModelProperty(value = "退回执行方式", required = true)
    @NotNull(message = "退回执行方式不能为空")
    private Integer taskReturntype;

    @ApiModelProperty(value = "允许指定办理期限", required = true)
    @NotNull(message = "允许指定办理期限不能为空")
    private Integer processlimitPermit;

    @ApiModelProperty(value = "添加审批结果控件", required = true)
    @NotNull(message = "添加审批结果控件不能为空")
    private Integer approvalPermit;

    public boolean isMultiassignRuleRequired(){
        if(ObjectUtil.isNotNull(this.taskAssigntype)){
            if(this.taskAssigntype.equals(FlowTaskAssignTypeEnum.MULTI.ordinal())){
                return ObjectUtil.isNotNull(this.multiassignRule);
            }else{
                this.setMultiassignRule(null);
                this.setMultiassignRatio(null);
                this.setMultiassignWeightjson(null);
            }
        }
        return true;
    }

    public boolean isMultiassignRatioRequired(){
        if(ObjectUtil.isNotNull(this.multiassignRule)){
            if(this.multiassignRule.equals(FlowTaskMultiAssignRuleEnum.RATIO.ordinal())){
                return ObjectUtil.isNotNull(this.multiassignRatio);
            }else{
                this.setMultiassignRatio(null);
            }
        }
        return true;
    }

    public boolean isMultiassignWeightjsonRequired(){
        if(ObjectUtil.isNotNull(this.multiassignRule)){
            if(this.multiassignRule.equals(FlowTaskMultiAssignRuleEnum.WEIGHT.ordinal())){
                return ObjectUtil.isNotNull(this.multiassignWeightjson);
            }else{
                this.setMultiassignWeightjson(null);
            }
        }
        return true;
    }

}
