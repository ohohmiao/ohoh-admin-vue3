package com.ohohmiao.modules.workflow.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.enums.FlowNodeBindTypeEnum;
import com.ohohmiao.modules.workflow.mapper.FlowEventMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowEventPageDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeBindQueryDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowEvent;
import com.ohohmiao.modules.workflow.model.vo.FlowEventVO;
import com.ohohmiao.modules.workflow.service.FlowEventService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 流程事件Service实现类
 *
 * @author ohohmiao
 * @date 2025-01-16 10:00
 */
@Service
public class FlowEventServiceImpl extends CommonServiceImpl<FlowEventMapper, FlowEvent> implements FlowEventService {

    @Resource
    private FlowEventMapper flowEventMapper;

    @Override
    public Page<FlowEventVO> listByPage(FlowEventPageDTO flowEventPageDTO){
        FlowNodeBindQueryDTO queryDTO = new FlowNodeBindQueryDTO();
        queryDTO.setDefCode(flowEventPageDTO.getDefCode());
        queryDTO.setDefVersion(flowEventPageDTO.getDefVersion());
        queryDTO.setBindType(FlowNodeBindTypeEnum.EVENT.ordinal());
        return flowEventMapper.page(CommonPageRequest.constructPage(
                flowEventPageDTO.getCurrent(), flowEventPageDTO.getSize()), queryDTO);
    }

}
