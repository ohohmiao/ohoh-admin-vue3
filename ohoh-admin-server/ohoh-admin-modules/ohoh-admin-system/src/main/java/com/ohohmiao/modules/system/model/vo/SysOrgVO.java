package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonTreeVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 组织机构
 *
 * @author ohohmiao
 * @date 2023-04-13 11:09
 */
@Getter
@Setter
public class SysOrgVO extends CommonTreeVO {

    private String orgId;

    private String orgName;

    private String orgShortname;

    private String orgCode;

    private Integer orgLevel;

}
