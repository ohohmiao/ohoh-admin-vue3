package com.ohohmiao.modules.workflow.model.dto;

import com.ohohmiao.framework.common.model.dto.CommonPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 待我审批流程查询条件
 *
 * @author ohohmiao
 * @date 2026-02-23 21:30
 */
@ApiModel("待我审批流程查询条件")
@Getter
@Setter
public class FlowMyApprovalPageDTO extends CommonPageDTO {

    @ApiModelProperty(value = "流水号")
    private String processNum;

    @ApiModelProperty(value = "流程标题")
    private String processSubject;

}
