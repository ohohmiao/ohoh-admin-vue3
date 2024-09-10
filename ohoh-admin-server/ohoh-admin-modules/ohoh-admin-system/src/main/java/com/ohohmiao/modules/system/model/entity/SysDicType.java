package com.ohohmiao.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonTreeEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典类别
 *
 * @author ohohmiao
 * @date 2023-05-25 11:36
 */
@Getter
@Setter
@TableName(value = "sys_dictype")
public class SysDicType extends CommonTreeEntity {

    @TableId
    private String dictypeId;

    @TableField
    private String dictypeName;

    @TableField
    private String dictypeCode;

    @TableField
    private String dictypeRemark;

}
