package com.ohohmiao.framework.security.model.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("登录用户")
@Getter
@Setter
public abstract class StpLoginUser {

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "账号")
    private String userAccount;

    @ApiModelProperty(value = "上次登录IP")
    private String lastLoginip;

    @ApiModelProperty(value = "上次登录时间")
    private Date lastLogintime;

    @ApiModelProperty(value = "手机号码")
    private String userMobile;

    @ApiModelProperty(value = "性别")
    private Integer userGender;

    @ApiModelProperty(value = "电子邮箱")
    private String userEmail;

    @ApiModelProperty(value = "状态")
    private Integer userStatus;

    /**
     * 主机构id
     */
    @ApiModelProperty(value = "主机构id")
    private String mainOrgId;

    /**
     * 主机构名称
     */
    @ApiModelProperty(value = "主机构名称")
    private String mainOrgName;

    /**
     * 主机构简称
     */
    @ApiModelProperty(value = "主机构简称")
    private String mainOrgShortname;

    /**
     * 主机构编码
     */
    @ApiModelProperty(value = "主机构编码")
    private String mainOrgCode;

    /**
     * 所在机构
     */
    @ApiModelProperty(value = "所在机构")
    private List<UserOrg> orgList;

    /**
     * 当前操作机构，可前端切换
     * TODO 待实现
     */
    @ApiModelProperty(value = "当前操作机构，可前端切换")
    private UserOrg switchOrg;

    /**
     * 授权的角色id
     */
    @ApiModelProperty(value = "授权的角色id")
    private Set<String> grantedRoleIds;

    /**
     * 授权的资源id
     */
    @ApiModelProperty(value = "授权的资源id")
    private Set<String> grantedResIds;

    /**
     * 授权的资源url
     */
    @ApiModelProperty(value = "授权的资源url")
    private Set<String> grantedResUrls;

    /**
     * 授权的数据范围，即机构id
     */
    @ApiModelProperty(value = "授权的数据范围，即机构id")
    private Set<String> grantedDataScopes;

    /**
     * 是否超管标识
     */
    @ApiModelProperty(value = "是否超管")
    private Boolean superAdmin;

    public abstract Boolean isSuperAdmin();

    @ApiModel("机构")
    @Getter
    @Setter
    public static class UserOrg {

        @ApiModelProperty(value = "机构id")
        private String orgId;

        @ApiModelProperty(value = "名称")
        private String orgName;

        @ApiModelProperty(value = "简称")
        private String orgShortname;

        @ApiModelProperty(value = "编码")
        private String orgCode;

        @ApiModelProperty(value = "是否主机构")
        private Integer defaultFlag;

        @ApiModelProperty(value = "用户排序")
        private Integer userSort;

        @ApiModelProperty(value = "扩展字段")
        private String propExtendid;

    }

}
