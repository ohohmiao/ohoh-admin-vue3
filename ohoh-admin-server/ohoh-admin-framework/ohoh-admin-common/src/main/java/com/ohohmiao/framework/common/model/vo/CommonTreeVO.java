package com.ohohmiao.framework.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用树形返回数据
 *
 * @author ohohmiao
 * @date 2023/4/15 15:10
 */
@ApiModel("通用树形返回数据")
@Getter
@Setter
public class CommonTreeVO extends CommonVO {

    /**
     * 父节点id
     */
    @ApiModelProperty("父节点id")
    private String parentId;

    /**
     * 树层级
     */
    @ApiModelProperty("树层级")
    private Integer treeLevel;

    /**
     * 树路径
     */
    @ApiModelProperty("树路径")
    private String treePath;

    /**
     * 树排序
     */
    @ApiModelProperty("树排序")
    private Integer treeSort;

}
