package com.ohohmiao.modules.workflow.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * 流程核心信息
 *
 * @author ohohmiao
 * @date 2025-06-08 11:10
 */
@ApiModel("流程核心信息")
@Getter
@Setter
public class FlowInfoVO<T> {

    @ApiModelProperty(value = "是否发起流程阶段")
    private Boolean startFlowFlag;

    @ApiModelProperty(value = "流程编码")
    private String defCode;

    @ApiModelProperty(value = "版本号")
    private Integer defVersion;

    @ApiModelProperty(value = "流程定义名称")
    private String defName;

    @ApiModelProperty(value = "定义XML")
    private String defXml;

    @ApiModelProperty(value = "定义JSON")
    private String defJson;

    @ApiModelProperty(value = "绑定的表单id")
    private String formId;

    @ApiModelProperty(value = "绑定的表单路径")
    private String formPath;

    private String flowSubject;

    @ApiModelProperty(value = "当前操作环节")
    private FlowNodeVO curNodeInfo;

    @ApiModelProperty(value = "当前流程正在运行环节id")
    private String curRunningNodeIds;

    @ApiModelProperty(value = "当前环节绑定的流程按钮")
    private List<FlowBtnVO> flowBtns;

    @ApiModelProperty(value = "流程实例id")
    private String processId;

    @ApiModelProperty(value = "下一环节节点id，如存储分支判断节点事件结果")
    private Set<String> nextTaskNodeIds;

    @ApiModelProperty(value = "业务实体类")
    private String flowEntityClassName;

    @ApiModelProperty(value = "业务实体VO")
    private T entityVO;

    //@ApiModelProperty(value = "业务表记录id")
    //private String entityId;

    private String curTaskId;

    @ApiModelProperty(value = "是否处于查阅状态")
    private Boolean doQueryFlag;

}
