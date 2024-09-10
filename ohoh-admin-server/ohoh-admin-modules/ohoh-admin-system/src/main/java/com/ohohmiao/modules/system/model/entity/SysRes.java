package com.ohohmiao.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonTreeEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统资源实体
 *
 * @author ohohmiao
 * @date 2023-04-04 11:05
 */
@Getter
@Setter
@TableName(value = "sys_res")
public class SysRes extends CommonTreeEntity {

    @TableId
    private String resId;

    @TableField
    private String resName;

    @TableField
    private String resCode;

    @TableField
    private Integer resType;

    @TableField
    private String resIcon;

    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private Integer menuType;

    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String resPath;

    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private Integer hideFlag;

    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private Integer fullscreenFlag;

}
