package com.ohohmiao.modules.demo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.modules.demo.service.LeaveService;
import com.ohohmiao.modules.system.model.entity.SysPosition;
import com.ohohmiao.modules.system.service.SysPositionService;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author ohohmiao
 * @date 2025-06-11 14:34
 */
@Service("leaveService")
public class LeaveServiceImpl implements LeaveService {

    @Resource
    private SysPositionService sysPositionService;

    @Override
    public FlowInfoVO getFlowDecideResult(FlowInfoVO flowInfoVO){
        Set<String> result = CollectionUtil.newHashSet();
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        StpLoginUser.UserOrg switchOrg = loginUser.getSwitchOrg();
        if(ObjectUtil.isNotNull(switchOrg) && StrUtil.isNotBlank(switchOrg.getPropExtendid())){
            SysPosition sysPosition = sysPositionService.getById(switchOrg.getPropExtendid());
            if(ObjectUtil.isNotNull(sysPosition) && sysPosition.getPositionLevel() <= 5){
                // 主管及以上，分管副总审批
                result.add("Activity_1ru8eyo");
            }else{
                // 非主管及以下，业务部门审批
                result.add("Activity_1p9mujm");
            }
        }else{
            // 非主管及以下，业务部门审批
            result.add("Activity_1p9mujm");
        }
        flowInfoVO.setNextTaskNodeIds(result);
        return flowInfoVO;
    }

}
