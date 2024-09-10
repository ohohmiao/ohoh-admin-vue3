package com.ohohmiao.framework.mybatis.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 公共实体类
 *
 * @author ohohmiao
 * @date 2023-03-30 14:38
 */
@Getter
@Setter
public class CommonEntity implements Serializable {

    /**
     * 是否删除0=否1=是
     */
    @TableLogic
    @TableField(fill= FieldFill.INSERT, select = false)
    private Integer deleteFlag;

    /**
     * 创建者id
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUserid;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者id
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateUserid;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}
