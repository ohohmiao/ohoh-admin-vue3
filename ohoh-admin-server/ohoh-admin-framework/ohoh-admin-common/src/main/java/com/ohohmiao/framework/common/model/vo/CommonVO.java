package com.ohohmiao.framework.common.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 通用返回数据结构
 *
 * @author ohohmiao
 * @date 2023/4/15 15:07
 */
@Getter
@Setter
public class CommonVO {

    /**
     * 创建者id
     */
    private String createUserid;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者id
     */
    private String updateUserid;

    /**
     * 更新时间
     */
    private Date updateTime;
    
}
