package com.ohohmiao.modules.workflow.model.dto;

import com.ohohmiao.framework.common.model.dto.CommonPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程表单查询条件
 *
 * @author ohohmiao
 * @date 2025-06-07 15:35
 */
@ApiModel("流程表单查询条件")
@Getter
@Setter
public class FlowFormPageDTO extends CommonPageDTO {

    @ApiModelProperty(value = "流程类别id")
    private String parentId;

    @ApiModelProperty(value = "流程名称")
    private String formName;

}
