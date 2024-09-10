package com.ohohmiao.framework.security.model.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * sa-token登录用户基础对象
 *
 * @author ohohmiao
 * @date 2023/4/5 17:08
 */
@Getter
@Setter
public abstract class StpLoginUser {

    private String userId;

    private String userName;

    private String userAccount;

    private String lastLoginip;

    private Date lastLogintime;

    private String userMobile;

    private Integer userGender;

    private String userEmail;

    private Integer userStatus;

    /**
     * 主机构id
     */
    private String mainOrgId;

    /**
     * 主机构名称
     */
    private String mainOrgName;

    /**
     * 主机构简称
     */
    private String mainOrgShortname;

    /**
     * 主机构编码
     */
    private String mainOrgCode;

    /**
     * 所在机构
     */
    private List<UserOrg> orgList;

    /**
     * 授权的角色id
     */
    private Set<String> grantedRoleIds;

    /**
     * 授权的资源id
     */
    private Set<String> grantedResIds;

    /**
     * 授权的资源url
     */
    private Set<String> grantedResUrls;

    /**
     * 授权的数据范围，即机构id
     */
    private Set<String> grantedDataScopes;

    /**
     * 是否超管标识
     */
    private Boolean superAdmin;

    public abstract Boolean isSuperAdmin();

    @Getter
    @Setter
    public static class UserOrg {

        private String orgId;

        private String orgName;

        private String orgShortname;

        private String orgCode;

        private Integer defaultFlag;

        private Integer userSort;

        private String propExtendid;

    }

}
