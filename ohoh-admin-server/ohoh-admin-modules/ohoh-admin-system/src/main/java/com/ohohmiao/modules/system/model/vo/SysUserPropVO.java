package com.ohohmiao.modules.system.model.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统用户属性
 *
 * @author ohohmiao
 * @date 2023-04-13 16:32
 */
@Getter
@Setter
public class SysUserPropVO {

    private String propId;

    private String userId;

    private String propTablename;

    private String propRecordid;

    private String propRecordname;

    private Integer propSort;

    private String propExtendid;

    private Integer defaultFlag;

}
