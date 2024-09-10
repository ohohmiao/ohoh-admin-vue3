package com.ohohmiao.modules.system.model.dto;

import cn.hutool.core.collection.CollUtil;
import com.ohohmiao.framework.common.annotation.IsValidEnum;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.modules.system.enums.SysUserPropEnum;
import com.ohohmiao.modules.system.enums.SysUserStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * 系统用户新增或编辑
 * 
 * @author ohohmiao
 * @date 2023/4/16 13:33
 */
@ApiModel("系统角色新增或编辑")
@Getter
@Setter
@ScriptAssert(lang = "javascript", script = "_this.isValidOrgList()", message = "用户机构传参非法")
public class SysUserAddOrEditDTO {

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String userId;

    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank(message = "姓名不能为空")
    private String userName;

    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "账号不能为空")
    private String userAccount;

    @ApiModelProperty(value = "手机号码")
    private String userMobile;

    @ApiModelProperty(value = "性别", required = true)
    @NotNull(message = "性别不能为空")
    private Integer userGender;

    @ApiModelProperty(value = "电子邮箱")
    private String userEmail;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "状态不能为空")
    @IsValidEnum(value = SysUserStatusEnum.class, method = "getStatus", message = "不支持的状态")
    private Integer userStatus;

    @ApiModelProperty(value = "用户机构", required = true)
    @NotEmpty(message = "用户机构不能为空")
    private List<SysUserPropAddOrEditDTO> orgList;

    /**
     * 用户组织是否配置正确
     * @param 
     * @return boolean
     */
    public boolean isValidOrgList(){
        if(CollUtil.isNotEmpty(this.orgList)){
            long filterOrgListCount = this.orgList.stream().filter(
                    org -> org.getPropTablename().equals(SysUserPropEnum.SYSORG.getValue())).count();
            if(filterOrgListCount != this.orgList.size()){
                return false;
            }

            long mainOrgCount = this.orgList.stream().filter(
                    org -> org.getDefaultFlag() == CommonWhetherEnum.YES.getCode()).count();
            return mainOrgCount == 1;
        }
        return true;
    }

}
