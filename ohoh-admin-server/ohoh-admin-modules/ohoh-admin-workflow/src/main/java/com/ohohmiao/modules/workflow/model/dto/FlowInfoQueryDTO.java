package com.ohohmiao.modules.workflow.model.dto;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

/**
 * 流程信息查询
 *
 * @author ohohmiao
 * @date 2025-06-08 10:53
 */
@ApiModel("流程信息查询")
@Getter
@Setter
@ScriptAssert(lang = "javascript", script = "_this.isLegalParams()", message = "非法的流程传参")
public class FlowInfoQueryDTO {

    @ApiModelProperty(value = "流程编码")
    private String defCode;

    @ApiModelProperty(value = "版本号")
    private Integer defVersion;

    @ApiModelProperty(value = "发起人类别")
    private Integer creatorType;

    @ApiModelProperty(value = "发起人id")
    private String creatorId;

    @ApiModelProperty(value = "发起人")
    private String creatorName;

    @ApiModelProperty(value = "流程实例id")
    private String processId;

    @ApiModelProperty(value = "当前任务id")
    private String curTaskId;

    public boolean isLegalParams() {
        if(StrUtil.isBlank(defCode) && StrUtil.isBlank(processId)){
            return false;
        }
        return true;
    }

}
