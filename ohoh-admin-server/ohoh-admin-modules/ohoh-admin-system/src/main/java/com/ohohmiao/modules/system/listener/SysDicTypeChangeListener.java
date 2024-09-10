package com.ohohmiao.modules.system.listener;

import com.ohohmiao.framework.common.listener.CommonDataChangeListener;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.modules.system.enums.SysCacheKeyEnum;
import com.ohohmiao.modules.system.model.entity.SysDicType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统字典类别数据变更监听器
 *
 * @author ohohmiao
 * @date 2023-05-25 16:39
 */
@Component
public class SysDicTypeChangeListener implements CommonDataChangeListener<SysDicType> {

    @Resource
    private PlatRedisUtil platRedisUtil;

    @Override
    public void doAddWithDataList(List<SysDicType> dataList) {
        platRedisUtil.deleteCacheObject(SysCacheKeyEnum.DICTYPE_ALL.getKey());
    }

    @Override
    public void doEditWithDataList(List<SysDicType> dataList) {
        platRedisUtil.deleteCacheObject(SysCacheKeyEnum.DICTYPE_ALL.getKey());
    }

    @Override
    public void doDeleteWithDataIdList(List<String> dataIdList) {
        platRedisUtil.deleteCacheObject(SysCacheKeyEnum.DICTYPE_ALL.getKey());
    }

}
