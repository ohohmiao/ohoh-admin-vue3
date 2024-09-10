package com.ohohmiao.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统资源url实体
 *
 * @author ohohmiao
 * @date 2023-04-06 11:53
 */
@Getter
@Setter
@TableName(value = "sys_resurl")
public class SysResUrl {

    @TableId
    private String resurlId;

    @TableField
    private String resId;

    @TableField
    private String resUrl;

}
