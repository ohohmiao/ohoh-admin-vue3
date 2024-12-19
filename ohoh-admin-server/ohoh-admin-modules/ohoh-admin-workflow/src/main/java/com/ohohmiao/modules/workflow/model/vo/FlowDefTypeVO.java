package com.ohohmiao.modules.workflow.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonTreeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程定义类别
 *
 * @author ohohmiao
 * @date 2024-12-02 09:46
 */
@ApiModel("流程定义类别")
@Getter
@Setter
public class FlowDefTypeVO extends CommonTreeVO {

    @ApiModelProperty(value = "流程类别id")
    private String deftypeId;

    @ApiModelProperty(value = "名称")
    private String deftypeName;

    @ApiModelProperty(value = "编码")
    private String deftypeCode;

    @ApiModelProperty(value = "备注")
    private String deftypeRemark;

}
