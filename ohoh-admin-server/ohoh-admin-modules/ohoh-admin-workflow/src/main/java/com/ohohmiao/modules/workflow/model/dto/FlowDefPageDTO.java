package com.ohohmiao.modules.workflow.model.dto;

import com.ohohmiao.framework.common.model.dto.CommonPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程定义查询条件
 *
 * @author ohohmiao
 * @date 2024-12-02 11:18
 */
@ApiModel("流程定义查询条件")
@Getter
@Setter
public class FlowDefPageDTO extends CommonPageDTO {

    @ApiModelProperty(value = "流程类别id")
    private String parentId;

    @ApiModelProperty(value = "流程名称")
    private String defName;

    @ApiModelProperty(value = "流程编码")
    private String defCode;

}
