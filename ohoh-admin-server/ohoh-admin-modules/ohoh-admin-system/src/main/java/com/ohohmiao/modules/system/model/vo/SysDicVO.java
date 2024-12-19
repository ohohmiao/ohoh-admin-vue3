package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典
 * 
 * @author ohohmiao
 * @date 2023-05-25 14:14
 */
@ApiModel("系统字典")
@Getter
@Setter
public class SysDicVO extends CommonVO {

    @ApiModelProperty(value = "字典id")
    private String dicId;

    @ApiModelProperty(value = "字典类别id")
    private String dictypeId;

    @ApiModelProperty(value = "字典名称")
    private String dicName;

    @ApiModelProperty(value = "字典编码")
    private String dicCode;

    @ApiModelProperty(value = "排序")
    private Integer dicSort;

    @ApiModelProperty(value = "扩展字段")
    private String extendField;

    @ApiModelProperty(value = "字典类别编码")
    private String dictypeCode;

    @ApiModelProperty(value = "字典类别名称")
    private String dictypeName;
    
}
