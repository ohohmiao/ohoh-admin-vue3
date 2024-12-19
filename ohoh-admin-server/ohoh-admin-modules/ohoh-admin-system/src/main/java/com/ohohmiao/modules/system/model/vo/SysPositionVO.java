package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonTreeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 岗位
 *
 * @author ohohmiao
 * @date 2023/6/22 16:56
 */
@ApiModel("岗位")
@Getter
@Setter
public class SysPositionVO extends CommonTreeVO {

    @ApiModelProperty(value = "岗位id")
    private String positionId;

    @ApiModelProperty(value = "名称")
    private String positionName;

    @ApiModelProperty(value = "编码")
    private String positionCode;

    @ApiModelProperty(value = "层级")
    private Integer positionLevel;

    @ApiModelProperty(value = "备注")
    private String positionRemark;

}
