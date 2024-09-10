package com.ohohmiao.modules.system.listener;

import com.ohohmiao.framework.common.listener.CommonDataChangeListener;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.modules.system.enums.SysCacheKeyEnum;
import com.ohohmiao.modules.system.model.entity.SysPosition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 岗位数据变更监听器
 *
 * @author ohohmiao
 * @date 2023/7/11 22:13
 */
@Component
public class SysPositionChangeListener implements CommonDataChangeListener<SysPosition> {

    @Resource
    private PlatRedisUtil platRedisUtil;

    @Override
    public void doAddWithDataList(List<SysPosition> dataList){
        platRedisUtil.deleteCacheObject(SysCacheKeyEnum.POSITION_ALL.getKey());
    }

    @Override
    public void doEditWithDataList(List<SysPosition> dataList){
        platRedisUtil.deleteCacheObject(SysCacheKeyEnum.POSITION_ALL.getKey());
    }

    @Override
    public void doDeleteWithDataIdList(List<String> dataIdList) {
        platRedisUtil.deleteCacheObject(SysCacheKeyEnum.POSITION_ALL.getKey());
    }

}
