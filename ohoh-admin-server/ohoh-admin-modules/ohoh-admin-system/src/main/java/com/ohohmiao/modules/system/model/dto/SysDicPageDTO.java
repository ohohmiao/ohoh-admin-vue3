package com.ohohmiao.modules.system.model.dto;

import com.ohohmiao.framework.common.model.dto.CommonPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典查询条件
 *
 * @author ohohmiao
 * @date 2023-05-25 15:50
 */
@ApiModel("系统字典查询条件")
@Getter
@Setter
public class SysDicPageDTO extends CommonPageDTO {

    @ApiModelProperty(value = "字典类别id")
    private String parentId;

    @ApiModelProperty(value = "字典名称")
    private String dicName;

}
