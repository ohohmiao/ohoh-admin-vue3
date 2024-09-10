package com.ohohmiao.modules.system.model.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ohohmiao.modules.system.enums.SysUserPropEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotBlank;

/**
 * 系统用户属性新增或编辑
 *
 * @author ohohmiao
 * @date 2023/4/16 16:03
 */
@Getter
@Setter
@ScriptAssert(lang = "javascript", script = "_this.isPropSortRequired()", message = "属性排序不能为空")
@ScriptAssert(lang = "javascript", script = "_this.isDefaultFlagRequired()", message = "是否默认属性不能为空")
public class SysUserPropAddOrEditDTO {

    private String userId;

    @NotBlank(message = "关联类别不能为空")
    private String propTablename;

    @NotBlank(message = "关联记录id不能为空")
    private String propRecordid;

    private Integer propSort;

    private String propExtendid;

    private Integer defaultFlag;

    public boolean isPropSortRequired(){
        if(StrUtil.isNotEmpty(this.getPropTablename())){
            if(SysUserPropEnum.SYSORG.getValue().equals(this.getPropTablename())){
                return ObjectUtil.isNotNull(this.getPropSort());
            }else{
                return true;
            }
        }
        return true;
    }

    public boolean isDefaultFlagRequired(){
        if(SysUserPropEnum.SYSORG.getValue().equals(this.getPropTablename())){
            return ObjectUtil.isNotNull(this.getDefaultFlag());
        }else{
            return true;
        }
    }

}
