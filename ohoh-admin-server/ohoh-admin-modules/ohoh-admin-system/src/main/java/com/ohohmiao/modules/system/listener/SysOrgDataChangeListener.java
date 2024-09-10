package com.ohohmiao.modules.system.listener;

import com.ohohmiao.framework.security.enums.AuthConstEnum;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.framework.common.listener.CommonDataChangeListener;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.modules.system.enums.SysCacheKeyEnum;
import com.ohohmiao.modules.system.model.entity.SysOrg;
import com.ohohmiao.modules.system.service.SysUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织机构数据变更监听器
 *
 * @author ohohmiao
 * @date 2023-04-14 10:50
 */
@Component
public class SysOrgDataChangeListener implements CommonDataChangeListener<SysOrg> {

    @Resource
    private PlatRedisUtil platRedisUtil;

    @Resource
    private SysUserService sysUserService;

    @Override
    public void doAddWithDataList(List<SysOrg> dataList) {
        platRedisUtil.deleteCacheObject(SysCacheKeyEnum.ORG_ALL.getKey());
        // 将新增的组织机构id，组装进当前登录用户的数据授权范围
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            List<String> dataIds = dataList.stream().map(SysOrg::getOrgId).collect(Collectors.toList());
            // 新增数据范围写入当前会话
            dataIds.forEach(dataId -> {
                loginUser.getGrantedDataScopes().add(dataId);
            });
            StpPCUtil.getTokenSession().set(AuthConstEnum.LOGINUSER_SESSIONKEY.getValue(), loginUser);
            // 新增数据范围写入数据库
            sysUserService.addGrantDataScope4LoginUser(dataIds);
        }
    }

    @Override
    public void doEditWithDataList(List<SysOrg> dataList) {
        platRedisUtil.deleteCacheObject(SysCacheKeyEnum.ORG_ALL.getKey());
    }

    @Override
    public void doDeleteWithDataIdList(List<String> dataIdList) {
        platRedisUtil.deleteCacheObject(SysCacheKeyEnum.ORG_ALL.getKey());
    }
}
