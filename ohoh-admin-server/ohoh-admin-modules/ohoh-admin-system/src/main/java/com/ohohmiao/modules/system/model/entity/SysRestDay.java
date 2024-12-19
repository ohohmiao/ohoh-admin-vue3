package com.ohohmiao.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 节假日实体
 *
 * @author ohohmiao
 * @date 2024-12-18 14:15
 */
@Getter
@Setter
@TableName(value = "sys_restday")
public class SysRestDay {

    @TableId
    private String restdayId;

    @TableField
    private String restdayDate;

}
