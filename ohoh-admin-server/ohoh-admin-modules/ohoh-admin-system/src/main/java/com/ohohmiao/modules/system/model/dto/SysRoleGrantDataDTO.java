package com.ohohmiao.modules.system.model.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ohohmiao.modules.system.enums.SysDataScopeTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统角色数据范围授权
 *
 * @author ohohmiao
 * @date 2023/4/16 09:22
 */
@ApiModel("系统角色数据范围授权")
@Getter
@Setter
@ScriptAssert(lang = "javascript", script = "_this.isDatascopeOrgIdsRequired()", message = "自定义数据范围机构不能为空")
@ScriptAssert(lang = "javascript", script = "_this.isValidDatascopeType()", message = "数据范围类别配置非法")
public class SysRoleGrantDataDTO {

    @ApiModelProperty(value = "角色id", required = true)
    @NotBlank(message = "角色id不能为空")
    private String roleId;

    @ApiModelProperty(value = "机构id", required = true)
    @NotBlank(message = "所属机构不能为空")
    private String orgId;

    @ApiModelProperty(value = "数据范围类别", required = true)
    @NotNull(message = "数据范围类别不能为空")
    private Integer datascopeType;

    @ApiModelProperty(value = "自定义数据范围")
    private List<String> datascopeOrgIds;

    /**
     * 判断是否合法的数据范围字段
     * @return boolean
     */
    public boolean isValidDatascopeType(){
        if(StrUtil.isNotEmpty(this.orgId)){
            if(ObjectUtil.isNotNull(this.datascopeType)){
                // 仅挂在顶级机构的角色能配置全部机构的数据级权限类别
                if(!"0".equals(this.orgId) && this.datascopeType == SysDataScopeTypeEnum.ALL.getType()){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 自定义数据范围机构条件必填判断
     * @return boolean
     */
    public boolean isDatascopeOrgIdsRequired(){
        if(ObjectUtil.isNotNull(this.datascopeType)){
            if(this.datascopeType == SysDataScopeTypeEnum.CUSTOM.getType()){
                if(CollUtil.isNotEmpty(this.datascopeOrgIds)){
                    this.datascopeOrgIds.removeIf(orgId -> orgId.equals("0"));
                    return true;
                }else{
                    return false;
                }
            }else{
                this.setDatascopeOrgIds(null);
                return true;
            }
        }
        return true;
    }

}
