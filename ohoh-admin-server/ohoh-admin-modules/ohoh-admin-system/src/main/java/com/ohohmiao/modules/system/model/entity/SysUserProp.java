package com.ohohmiao.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户属性实体
 *
 * @author ohohmiao
 * @date 2023-04-06 11:59
 */
@Getter
@Setter
@TableName(value = "sys_userprop")
public class SysUserProp implements Serializable {

    @TableId
    private String propId;

    @TableField
    private String userId;

    @TableField
    private String propTablename;

    @TableField
    private String propRecordid;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Integer propSort;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String propExtendid;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Integer defaultFlag;

}
