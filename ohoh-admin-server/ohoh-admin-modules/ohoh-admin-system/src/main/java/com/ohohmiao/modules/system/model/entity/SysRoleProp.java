package com.ohohmiao.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 角色属性实体
 *
 * @author ohohmiao
 * @date 2023-04-06 11:55
 */
@Getter
@Setter
@TableName(value = "sys_roleprop")
public class SysRoleProp implements Serializable {

    @TableId
    private String propId;

    @TableField
    private String roleId;

    @TableField
    private String propTablename;

    @TableField
    private String propRecordid;

}
