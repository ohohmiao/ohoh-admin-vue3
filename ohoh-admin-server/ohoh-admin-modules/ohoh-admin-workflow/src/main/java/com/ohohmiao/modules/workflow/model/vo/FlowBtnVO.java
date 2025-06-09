package com.ohohmiao.modules.workflow.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程按钮
 *
 * @author ohohmiao
 * @date 2025-06-07 15:20
 */
@ApiModel("流程按钮")
@Getter
@Setter
public class FlowBtnVO extends CommonVO {

    @ApiModelProperty(value = "流程按钮id")
    private String btnId;

    @ApiModelProperty(value = "按钮文本")
    private String btnText;

    @ApiModelProperty(value = "按钮颜色")
    private String btnColor;

    @ApiModelProperty(value = "执行方法")
    private String btnFun;

}
