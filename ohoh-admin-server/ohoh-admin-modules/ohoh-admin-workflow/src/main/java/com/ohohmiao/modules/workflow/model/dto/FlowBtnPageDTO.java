package com.ohohmiao.modules.workflow.model.dto;

import com.ohohmiao.framework.common.model.dto.CommonPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程按钮查询条件
 *
 * @author ohohmiao
 * @date 2025-06-07 15:35
 */
@ApiModel("流程按钮查询条件")
@Getter
@Setter
public class FlowBtnPageDTO extends CommonPageDTO {

    @ApiModelProperty(value = "按钮文本")
    private String btnText;

    @ApiModelProperty(value = "执行方法")
    private String btnFun;

}
