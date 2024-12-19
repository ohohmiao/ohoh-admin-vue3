package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonTreeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 组织机构
 *
 * @author ohohmiao
 * @date 2023-04-13 11:09
 */
@ApiModel("组织机构")
@Getter
@Setter
public class SysOrgVO extends CommonTreeVO {

    @ApiModelProperty(value = "组织机构id")
    private String orgId;

    @ApiModelProperty(value = "名称")
    private String orgName;

    @ApiModelProperty(value = "简称")
    private String orgShortname;

    @ApiModelProperty(value = "编码")
    private String orgCode;

    @ApiModelProperty(value = "层级")
    private Integer orgLevel;

}
