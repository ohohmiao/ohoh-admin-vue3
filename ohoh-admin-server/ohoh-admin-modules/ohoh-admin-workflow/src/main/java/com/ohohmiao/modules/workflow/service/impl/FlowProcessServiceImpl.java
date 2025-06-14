package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.modules.workflow.mapper.FlowProcessMapper;
import com.ohohmiao.modules.workflow.model.entity.FlowProcess;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import com.ohohmiao.modules.workflow.service.FlowProcessService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程实例Service实现
 *
 * @author ohohmiao
 * @date 2025-06-11 10:22
 */
@Service("flowProcessService")
public class FlowProcessServiceImpl extends CommonServiceImpl<FlowProcessMapper, FlowProcess> implements FlowProcessService {

    @Override
    public List<FlowTaskHandler> getCreator(FlowInfoVO flowInfoVO){
        List<FlowTaskHandler> resultList = new ArrayList<>();
        if(StrUtil.isNotBlank(flowInfoVO.getProcessId())){
            // TODO 从流程实例表查询
        }else{
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            FlowTaskHandler handler = new FlowTaskHandler();
            handler.setHandlerId(loginUser.getUserId());
            handler.setHandlerName(loginUser.getUserName());
            resultList.add(handler);
        }
        return resultList;
    }

}
