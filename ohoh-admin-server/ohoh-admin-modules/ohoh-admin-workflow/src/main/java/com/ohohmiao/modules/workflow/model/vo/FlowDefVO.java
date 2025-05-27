package com.ohohmiao.modules.workflow.model.vo;

import com.ohohmiao.framework.common.model.pojo.CommonReferRes;
import com.ohohmiao.framework.common.model.vo.CommonVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 流程定义
 *
 * @author ohohmiao
 * @date 2024-12-02 11:17
 */
@ApiModel("流程定义")
@Getter
@Setter
public class FlowDefVO extends CommonVO {

    @ApiModelProperty(value = "流程定义id")
    private String defId;

    @ApiModelProperty(value = "类别id")
    private String deftypeId;

    @ApiModelProperty(value = "类别名称")
    private String deftypeName;

    @ApiModelProperty(value = "名称")
    private String defName;

    @ApiModelProperty(value = "编码")
    private String defCode;

    @ApiModelProperty(value = "版本号")
    private Integer defVersion;

    @ApiModelProperty(value = "排序")
    private Integer defSort;

    @ApiModelProperty(value = "发起范围")
    private Integer initiatorScope;

    @ApiModelProperty(value = "指定人员")
    private List<CommonReferRes> targetInitiators;

    @ApiModelProperty(value = "时限限制类别")
    private Integer processLimittype;

    @ApiModelProperty(value = "时限限制值")
    private Integer processLimitvalue;

    @ApiModelProperty(value = "定义XML")
    private String defXml;

    @ApiModelProperty(value = "定义JSON")
    private String defJson;

    @ApiModelProperty(value = "定义SVG")
    private String defSvg;

}
