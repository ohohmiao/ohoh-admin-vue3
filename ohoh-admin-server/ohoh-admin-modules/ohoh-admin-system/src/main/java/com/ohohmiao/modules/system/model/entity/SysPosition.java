package com.ohohmiao.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonTreeEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 岗位
 *
 * @author ohohmiao
 * @date 2023-06-14 10:00
 */
@Getter
@Setter
@TableName(value = "sys_position")
public class SysPosition extends CommonTreeEntity {

    @TableId
    private String positionId;

    @TableField
    private String positionName;

    @TableField
    private String positionCode;

    @TableField
    private Integer positionLevel;

    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String positionRemark;

}
