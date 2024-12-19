package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonTreeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单
 *
 * @author ohohmiao
 * @date 2023-08-23 10:42
 */
@ApiModel("菜单")
@Getter
@Setter
public class SysMenuVO extends CommonTreeVO {

    @ApiModelProperty(value = "菜单id")
    private String resId;

    @ApiModelProperty(value = "名称")
    private String resName;

    @ApiModelProperty(value = "编码")
    private String resCode;

    @ApiModelProperty(value = "类别")
    private Integer resType;

    @ApiModelProperty(value = "图标")
    private String resIcon;

    @ApiModelProperty(value = "菜单类别")
    private Integer menuType;

    @ApiModelProperty(value = "菜单路径")
    private String resPath;

    @ApiModelProperty(value = "是否隐藏")
    private Integer hideFlag;

    @ApiModelProperty(value = "是否全屏")
    private Integer fullscreenFlag;

}
