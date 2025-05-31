package com.ohohmiao.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 系统用户实体
 *
 * @author ohohmiao
 * @date 2023-03-30 16:49
 */
@Getter
@Setter
@TableName(value = "sys_user")
public class SysUser extends CommonEntity {

    @TableId
    private String userId;

    @TableField
    private String userName;

    @TableField
    private String userAccount;

    @TableField
    private String userPassword;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String userMobile;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Integer userGender;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String userEmail;

    @TableField
    private Integer userStatus;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String lastLoginip;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Date lastLogintime;

}
