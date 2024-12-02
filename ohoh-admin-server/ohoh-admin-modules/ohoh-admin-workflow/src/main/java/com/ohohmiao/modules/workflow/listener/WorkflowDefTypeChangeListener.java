package com.ohohmiao.modules.workflow.listener;

import com.ohohmiao.framework.common.listener.CommonDataChangeListener;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.modules.workflow.enums.WorkflowCacheKeyEnum;
import com.ohohmiao.modules.workflow.model.entity.WorkflowDefType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 流程定义类别数据变更监听器
 *
 * @author ohohmiao
 * @date 2024-12-02 10:31
 */
@Component
public class WorkflowDefTypeChangeListener implements CommonDataChangeListener<WorkflowDefType> {

    @Resource
    private PlatRedisUtil platRedisUtil;

    @Override
    public void doAddWithDataList(List<WorkflowDefType> dataList) {
        platRedisUtil.deleteCacheObject(WorkflowCacheKeyEnum.DEFTYPE_ALL.getKey());
    }

    @Override
    public void doEditWithDataList(List<WorkflowDefType> dataList) {
        platRedisUtil.deleteCacheObject(WorkflowCacheKeyEnum.DEFTYPE_ALL.getKey());
    }

    @Override
    public void doDeleteWithDataIdList(List<String> dataIdList) {
        platRedisUtil.deleteCacheObject(WorkflowCacheKeyEnum.DEFTYPE_ALL.getKey());
    }

}
