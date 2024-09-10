package com.ohohmiao.modules.system.model.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ohohmiao.framework.common.annotation.IsValidEnum;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.modules.system.enums.SysMenuTypeEnum;
import com.ohohmiao.modules.system.enums.SysResTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * 系统资源新增或编辑
 *
 * @author ohohmiao
 * @date 2023-04-10 11:53
 */
@ApiModel("系统资源新增或编辑")
@Getter
@Setter
@ScriptAssert(lang = "javascript", script = "_this.isMenuTypeRequired()", message = "菜单类型不能为空")
@ScriptAssert(lang = "javascript", script = "_this.isResPathRequired()", message = "资源路径不能为空")
@ScriptAssert(lang = "javascript", script = "_this.isResIconRequired()", message = "资源图标不能为空")
public class SysResAddOrEditDTO {

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "资源id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "资源id不能为空", groups = {CommonEditGroup.class})
    private String resId;

    @ApiModelProperty(value = "资源名称", required = true)
    @NotBlank(message = "资源名称不能为空")
    private String resName;

    @ApiModelProperty(value = "资源编码", required = true)
    @NotBlank(message = "资源编码不能为空")
    private String resCode;

    @ApiModelProperty(value = "资源类别", required = true)
    @NotNull(message = "资源类别不能为空")
    @IsValidEnum(value = SysResTypeEnum.class, message = "不存在的资源类别", method = "getType")
    private Integer resType;

    @ApiModelProperty(value = "资源图标")
    private String resIcon;

    @ApiModelProperty(value = "菜单类别", required = true)
    @IsValidEnum(value = SysMenuTypeEnum.class, message = "不存在的菜单类别", method = "getType")
    private Integer menuType;

    @ApiModelProperty(value = "资源路径")
    private String resPath;

    @ApiModelProperty(value = "父节点id", required = true)
    @NotBlank(message = "父节点id不能为空")
    private String parentId;

    @ApiModelProperty(value = "排序，编辑时必传")
    @NotNull(message = "排序不能为空", groups = {CommonEditGroup.class})
    private Integer treeSort;

    @ApiModelProperty(value = "是否隐藏", required = true)
    @NotNull(message = "是否隐藏不能为空")
    @IsValidEnum(value = CommonWhetherEnum.class, message = "是否隐藏非法")
    private Integer hideFlag;

    @ApiModelProperty(value = "是否全屏显示", required = true)
    @NotNull(message = "是否全屏显示不能为空")
    @IsValidEnum(value = CommonWhetherEnum.class, message = "是否全屏显示非法")
    private Integer fullscreenFlag;

    /**
     * 菜单类别条件必填判断
     * @return boolean
     */
    public boolean isMenuTypeRequired(){
        if(ObjectUtil.isNotNull(this.getResType())){
            if(this.getResType() == SysResTypeEnum.MENU.getType()){
                return ObjectUtil.isNotNull(this.getMenuType());
            }else if(this.getResType() == SysResTypeEnum.BUTTON.getType()){
                this.setMenuType(null);
                return true;
            }
        }
        return true;
    }

    /**
     * 资源路径条件必填判断
     * @return boolean
     */
    public boolean isResPathRequired(){
        if(ObjectUtil.isNotNull(this.getResType())){
            if(this.getResType() == SysResTypeEnum.MENU.getType()){
                if(ObjectUtil.isNotNull(this.getMenuType()) &&
                        this.getMenuType() != SysMenuTypeEnum.CATALOG.getType()){
                    return StrUtil.isNotEmpty(this.getResPath());
                }else{
                    this.setResPath(null);
                    return true;
                }
            }else if(this.getResType() == SysResTypeEnum.BUTTON.getType()){
                this.setResPath(null);
                return true;
            }
        }
        return true;
    }

    /**
     * 资源图标条件必填判断
     * @return boolean
     */
    public boolean isResIconRequired(){
        if(ObjectUtil.isNotNull(this.getResType())){
            if(this.getResType() == SysResTypeEnum.MENU.getType()){
                return StrUtil.isNotEmpty(this.getResIcon());
            }else if(this.getResType() == SysResTypeEnum.BUTTON.getType()){
                this.setResIcon(null);
                return true;
            }
        }
        return true;
    }

    /**
     * url集合
     */
    @ApiModelProperty(value = "url集合")
    private List<String> urlList;

}
