package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonVO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * 系统角色
 *
 * @author ohohmiao
 * @date 2023-04-06 11:32
 */
@Getter
@Setter
public class SysRoleVO extends CommonVO {

    private String roleId;

    private String orgId;

    private String orgName;

    private String roleName;

    private Integer publicroleFlag;

    private Integer datascopeType;

    private Set<String> datascopeOrgIds;

    private String roleRemark;

    private Integer roleSort;

}
