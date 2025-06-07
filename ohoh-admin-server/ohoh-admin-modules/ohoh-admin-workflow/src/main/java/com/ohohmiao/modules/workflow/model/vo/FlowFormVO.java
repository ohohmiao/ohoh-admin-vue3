package com.ohohmiao.modules.workflow.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程表单
 *
 * @author ohohmiao
 * @date 2025-06-07 15:20
 */
@ApiModel("流程表单")
@Getter
@Setter
public class FlowFormVO extends CommonVO {

    @ApiModelProperty(value = "流程表单id")
    private String formId;

    @ApiModelProperty(value = "类别id")
    private String deftypeId;

    @ApiModelProperty(value = "类别名称")
    private String deftypeName;

    @ApiModelProperty(value = "表单名称")
    private String formName;

    @ApiModelProperty(value = "表单路径")
    private String formPath;

}
