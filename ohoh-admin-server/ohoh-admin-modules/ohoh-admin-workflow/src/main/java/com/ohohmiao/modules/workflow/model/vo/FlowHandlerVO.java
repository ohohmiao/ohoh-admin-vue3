package com.ohohmiao.modules.workflow.model.vo;

import com.ohohmiao.framework.common.model.pojo.CommonReferRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 流程环节办理人配置
 *
 * @author ohohmiao
 * @date 2025-06-01 11:05
 */
@ApiModel("流程环节办理人配置")
@Getter
@Setter
public class FlowHandlerVO {

    @ApiModelProperty(value = "主键id")
    private String handlerId;

    @ApiModelProperty(value = "流程编码")
    private String defCode;

    @ApiModelProperty(value = "版本号")
    private Integer defVersion;

    @ApiModelProperty(value = "环节id")
    private String nodeId;

    @ApiModelProperty(value = "下一环节id")
    private String nextnodeId;

    @ApiModelProperty(value = "下一环节名称")
    private String nextnodeName;

    @ApiModelProperty(value = "办理人类型")
    private Integer handlerType;

    @ApiModelProperty(value = "指定人员集合")
    private List<CommonReferRes> targetReferResList;

    @ApiModelProperty(value = "指定接口编码")
    private String interfaceCode;

    @ApiModelProperty(value = "过滤规则")
    private String filterRule;

    @ApiModelProperty(value = "多人审核方式")
    private Integer multiHandletype;

    @ApiModelProperty(value = "允许重选办理人")
    private Integer reselectPermit;

}
