package com.ohohmiao.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonTreeEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统组织机构实体
 *
 * @author ohohmiao
 * @date 2023-04-06 11:11
 */
@Getter
@Setter
@TableName(value = "sys_org")
public class SysOrg extends CommonTreeEntity {

    @TableId
    private String orgId;

    @TableField
    private String orgName;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String orgShortname;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String orgCode;

    @TableField
    private Integer orgLevel;

}
