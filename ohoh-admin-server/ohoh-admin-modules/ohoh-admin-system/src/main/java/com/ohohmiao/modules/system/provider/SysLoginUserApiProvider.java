package com.ohohmiao.modules.system.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.security.api.StpLoginUserApi;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.modules.system.enums.SysRolePropEnum;
import com.ohohmiao.modules.system.enums.SysUserPropEnum;
import com.ohohmiao.modules.system.model.entity.SysOrg;
import com.ohohmiao.modules.system.model.entity.SysUser;
import com.ohohmiao.modules.system.model.vo.SysLoginUser;
import com.ohohmiao.modules.system.model.vo.SysRoleVO;
import com.ohohmiao.modules.system.model.vo.SysUserPropVO;
import com.ohohmiao.modules.system.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登录用户api实现类
 *
 * @author ohohmiao
 * @date 2023-05-31 11:11
 */
@Service("loginUserApi")
public class SysLoginUserApiProvider implements StpLoginUserApi {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysUserPropService sysUserPropService;

    @Resource
    private SysRolePropService sysRolePropService;

    @Resource
    private SysResUrlService sysResUrlService;

    @Resource
    private SysOrgService sysOrgService;

    private List<SysLoginUser.UserOrg> listLoginUserOrgByUserId(String userId){
        List<SysUserPropVO> sysUserProps = sysUserPropService.listByUserIdAndTableName(
                userId, SysUserPropEnum.SYSORG.getValue());
        if(CollUtil.isEmpty(sysUserProps)){
            return CollectionUtil.newArrayList();
        }
        List<String> orgIds = sysUserProps.stream().map(SysUserPropVO::getPropRecordid).collect(Collectors.toList());
        List<SysOrg> orgList = sysOrgService.listByIds(orgIds);
        return orgList.stream().map(org -> {
            SysLoginUser.UserOrg userOrg = new SysLoginUser.UserOrg();
            BeanUtil.copyProperties(org, userOrg);
            Optional<SysUserPropVO> thizUserPropOptional = sysUserProps.stream().filter(
                    p -> p.getPropRecordid().equals(org.getOrgId())).findFirst();
            thizUserPropOptional.ifPresent(thizUserProp -> {
                userOrg.setDefaultFlag(thizUserProp.getDefaultFlag());
                userOrg.setUserSort(thizUserProp.getPropSort());
                userOrg.setPropExtendid(thizUserProp.getPropExtendid());
            });
            return userOrg;
        }).collect(Collectors.toList());
    }

    private void assembleGrantedInfo(Object sysLoginUser){
        SysLoginUser loginUser = (SysLoginUser) sysLoginUser;

        // 查找用户拥有的角色id
        List<SysRoleVO> grantedRoleList = sysUserService.listSysRoleByUserId(loginUser.getUserId());
        Set<String> grantedRoleIds = grantedRoleList.stream()
                .map(SysRoleVO::getRoleId).collect(Collectors.toSet());
        loginUser.setGrantedRoleIds(grantedRoleIds);

        // 查找用户拥有的资源id
        Set<String> grantedResIds = sysRolePropService.listRecordIdsByRoleIdsAndTableName(
                grantedRoleIds, SysRolePropEnum.SYSRES.getValue());
        loginUser.setGrantedResIds(grantedResIds);

        // 查找用户拥有的权限url
        Set<String> grantedResUrls = sysResUrlService.listByResIds(grantedResIds);
        loginUser.setGrantedResUrls(grantedResUrls);

        // 查找用户被授予的数据范围
        // =>角色授权的数据范围
        Set<String> grantedDataScopes =
                grantedRoleList.stream().flatMap(role -> role.getDatascopeOrgIds().stream()).collect(Collectors.toSet());
        // =>用户授权的数据范围
        Set<String> grantUserDataScopes = sysUserPropService.listRecordIdsByUserIdAndTableName(
                loginUser.getUserId(), SysUserPropEnum.DATASCOPE.getValue());
        grantedDataScopes.addAll(grantUserDataScopes);
        loginUser.setGrantedDataScopes(grantedDataScopes);
    }

    @Override
    public void assembleOrgAndGrantInfo(StpLoginUser loginUser, boolean includeGrantInfo){
        if(!loginUser.isSuperAdmin()) {
            // 查找用户所在的组织机构列表
            List<StpLoginUser.UserOrg> userOrgList = listLoginUserOrgByUserId(loginUser.getUserId());
            loginUser.setOrgList(userOrgList);
            if (CollUtil.isNotEmpty(userOrgList)) {
                Optional<StpLoginUser.UserOrg> mainUserOrgOptional = userOrgList.stream().filter(
                        o -> o.getDefaultFlag() == CommonWhetherEnum.YES.getCode()).findFirst();
                mainUserOrgOptional.ifPresent(mainUserOrg -> {
                    loginUser.setMainOrgId(mainUserOrg.getOrgId());
                    loginUser.setMainOrgName(mainUserOrg.getOrgName());
                    loginUser.setMainOrgShortname(mainUserOrg.getOrgShortname());
                    loginUser.setMainOrgCode(mainUserOrg.getOrgCode());
                    // 刚登录情形，切换机构默认设置为主机构
                    loginUser.setSwitchOrg(mainUserOrg);
                });
            }

            if(includeGrantInfo){
                assembleGrantedInfo(loginUser);
            }
        }
    }

    @Override
    public StpLoginUser getById(String userId){
        SysUser sysUser = sysUserService.getById(userId);
        if(ObjectUtil.isNull(sysUser)){
            return null;
        }
        StpLoginUser loginUser = BeanUtil.copyProperties(sysUser, SysLoginUser.class);
        assembleOrgAndGrantInfo(loginUser, false);
        return loginUser;
    }

}
