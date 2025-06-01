package com.ohohmiao.modules.workflow.model.dto;

import com.ohohmiao.framework.common.model.dto.CommonPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 流程办理人分页查询条件
 *
 * @author ohohmiao
 * @date 2025-06-01 11:23
 */
@ApiModel("流程办理人分页查询条件")
@Getter
@Setter
public class FlowHandlerPageDTO extends CommonPageDTO {

    @ApiModelProperty(value = "流程编码")
    @NotBlank(message = "流程编码不能为空")
    private String defCode;

    @ApiModelProperty(value = "版本号")
    @NotNull(message = "版本号不能为空")
    private Integer defVersion;

    @ApiModelProperty(value = "环节id")
    @NotBlank(message = "环节id不能为空")
    private String nodeId;

}
