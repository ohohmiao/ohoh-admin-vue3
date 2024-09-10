package com.ohohmiao.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典
 *
 * @author ohohmiao
 * @date 2023-05-25 11:37
 */
@Getter
@Setter
@TableName(value = "sys_dic")
public class SysDic extends CommonEntity {

    @TableId
    private String dicId;

    @TableField
    private String dictypeId;

    @TableField
    private String dictypeCode;

    @TableField
    private String dicName;

    @TableField
    private String dicCode;

    @TableField
    private Integer dicSort;

    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String extendField;

}
