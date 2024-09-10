package com.ohohmiao.modules.system.model.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ohohmiao.framework.common.annotation.IsValidEnum;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 系统角色新增或编辑
 *
 * @author ohohmiao
 * @date 2023-04-14 16:59
 */
@ApiModel("系统角色新增或编辑")
@Getter
@Setter
@ScriptAssert(lang = "javascript", script = "_this.isPublicRoleFlagRequired()", message = "是否公共角色字段不能为空")
public class SysRoleAddOrEditDTO {

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String roleId;

    @ApiModelProperty(value = "机构id", required = true)
    @NotBlank(message = "所属机构不能为空")
    private String orgId;

    @ApiModelProperty(value = "角色名称", required = true)
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @ApiModelProperty(value = "是否公共角色")
    @IsValidEnum(value = CommonWhetherEnum.class, message = "是否公共角色非法")
    private Integer publicroleFlag;

    @ApiModelProperty(value = "备注")
    private String roleRemark;

    @ApiModelProperty(value = "排序，编辑时必传")
    @NotNull(message = "排序不能为空", groups = {CommonEditGroup.class})
    private Integer roleSort;

    /**
     * 是否公共角色条件必填判断
     * @param
     * @return boolean
     */
    public boolean isPublicRoleFlagRequired(){
        if(StrUtil.isNotEmpty(this.orgId)){
            if("0".equals(this.orgId)){
                // 全局角色是否配置了是否公共角色字段
                return ObjectUtil.isNotNull(this.publicroleFlag);
            }else{
                // 仅全局角色能配置是否公共角色字段
                if(ObjectUtil.isNotNull(this.publicroleFlag)){
                    this.setPublicroleFlag(null);
                    return true;
                }
            }
        }
        return true;
    }

}
