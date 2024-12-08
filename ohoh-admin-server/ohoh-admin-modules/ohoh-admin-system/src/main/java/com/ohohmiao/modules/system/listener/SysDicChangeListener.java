package com.ohohmiao.modules.system.listener;

import com.ohohmiao.framework.common.listener.CommonDataChangeListener;
import com.ohohmiao.modules.system.model.entity.SysDic;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统字典数据变更监听器
 *
 * @author ohohmiao
 * @date 2023-05-29 15:14
 */
@Component
public class SysDicChangeListener implements CommonDataChangeListener<SysDic> {

    @Override
    public void doAddWithDataList(List<SysDic> dataList) {

    }

    @Override
    public void doEditWithDataList(List<SysDic> dataList) {

    }

    @Override
    public void doDeleteWithDataIdList(List<String> dataIdList) {

    }

}
