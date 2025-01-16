package com.ohohmiao.modules.workflow.service.impl;

import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.mapper.FlowNodeBindMapper;
import com.ohohmiao.modules.workflow.model.entity.FlowNodeBind;
import com.ohohmiao.modules.workflow.service.FlowNodeBindService;
import org.springframework.stereotype.Service;

/**
 * 流程环节绑定信息Service实现类
 *
 * @author ohohmiao
 * @date 2025-01-16 10:02
 */
@Service
public class FlowNodeBindServiceImpl extends CommonServiceImpl<FlowNodeBindMapper, FlowNodeBind> implements FlowNodeBindService {
}
