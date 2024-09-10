package com.ohohmiao.modules.system.listener;

import cn.dev33.satoken.stp.StpUtil;
import com.ohohmiao.framework.common.listener.CommonDataChangeListener;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.modules.system.enums.SysCacheKeyEnum;
import com.ohohmiao.modules.system.model.entity.SysUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户数据变更监听器
 *
 * @author ohohmiao
 * @date 2023-04-18 15:19
 */
@Component
public class SysUserDataChangeListener implements CommonDataChangeListener<SysUser> {

    @Resource
    private PlatRedisUtil platRedisUtil;

    @Override
    public void doAddWithDataList(List<SysUser> dataList) {
        platRedisUtil.deleteCacheObject(SysCacheKeyEnum.USER_ALL.getKey());
    }

    @Override
    public void doEditWithDataList(List<SysUser> dataList) {
        platRedisUtil.deleteCacheObject(SysCacheKeyEnum.USER_ALL.getKey());
    }

    @Override
    public void doDeleteWithDataIdList(List<String> dataIdList) {
        platRedisUtil.deleteCacheObject(SysCacheKeyEnum.USER_ALL.getKey());
        // 将删掉的在线用户踢下线
        dataIdList.forEach(StpUtil::kickout);
    }

}
