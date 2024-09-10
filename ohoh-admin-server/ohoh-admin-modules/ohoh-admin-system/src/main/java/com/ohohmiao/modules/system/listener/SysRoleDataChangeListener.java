package com.ohohmiao.modules.system.listener;

import com.ohohmiao.framework.common.listener.CommonDataChangeListener;
import com.ohohmiao.modules.system.model.entity.SysRole;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色数据变更监听器
 *
 * @author ohohmiao
 * @date 2023-04-18 15:19
 */
@Component
public class SysRoleDataChangeListener implements CommonDataChangeListener<SysRole> {

    @Override
    public void doAddWithDataList(List<SysRole> dataList) {

    }

    @Override
    public void doEditWithDataList(List<SysRole> dataList) {

    }

    @Override
    public void doDeleteWithDataIdList(List<String> dataIdList) {

    }

}
