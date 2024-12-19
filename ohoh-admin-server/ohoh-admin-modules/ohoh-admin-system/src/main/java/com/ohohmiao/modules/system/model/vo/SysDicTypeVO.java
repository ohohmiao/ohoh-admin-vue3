package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonTreeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典类别
 *
 * @author ohohmiao
 * @date 2023-05-25 14:12
 */
@ApiModel("系统字典类别")
@Getter
@Setter
public class SysDicTypeVO extends CommonTreeVO {

    @ApiModelProperty(value = "字典类别id")
    private String dictypeId;

    @ApiModelProperty(value = "字典类别名称")
    private String dictypeName;

    @ApiModelProperty(value = "字典类别编码")
    private String dictypeCode;

    @ApiModelProperty(value = "备注")
    private String dictypeRemark;

}
