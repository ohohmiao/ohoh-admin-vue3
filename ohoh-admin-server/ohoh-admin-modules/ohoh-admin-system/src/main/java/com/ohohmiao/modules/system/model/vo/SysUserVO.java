package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonVO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 系统用户
 *
 * @author ohohmiao
 * @date 2023/4/16 14:47
 */
@Getter
@Setter
public class SysUserVO extends CommonVO {

    private String userId;

    private String userName;

    private String userAccount;

    private String userMobile;

    private Integer userGender;

    private String userEmail;

    private Integer userStatus;

    private String lastLoginip;

    private Date lastLogintime;

    private Integer userSort;

    private String orgPropid;

    private String orgId;

    private String orgName;

    private String orgShortname;

    private String orgCode;

    private String orgPath;

    private Integer defaultFlag;

    private List<SysUserPropVO> orgList;

}
