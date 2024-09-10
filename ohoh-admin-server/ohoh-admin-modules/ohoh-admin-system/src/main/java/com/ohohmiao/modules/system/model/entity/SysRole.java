package com.ohohmiao.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统角色实体
 *
 * @author ohohmiao
 * @date 2023-04-06 11:18
 */
@Getter
@Setter
@TableName(value = "sys_role")
public class SysRole extends CommonEntity {

    @TableId
    private String roleId;

    @TableField
    private String orgId;

    @TableField
    private String roleName;

    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private Integer publicroleFlag;

    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private Integer datascopeType;

    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String roleRemark;

    @TableField
    private Integer roleSort;

}
