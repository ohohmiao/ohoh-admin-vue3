package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.enums.FlowEventImplTypeEnum;
import com.ohohmiao.modules.workflow.enums.FlowNodeBindTypeEnum;
import com.ohohmiao.modules.workflow.mapper.FlowEventMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowEventAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowEventPageDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeBindAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeBindQueryDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowEvent;
import com.ohohmiao.modules.workflow.model.vo.FlowEventVO;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import com.ohohmiao.modules.workflow.service.FlowEventService;
import com.ohohmiao.modules.workflow.service.FlowNodeBindService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;

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

    @Resource
    private FlowNodeBindService flowNodeBindService;

    @Override
    public Page<FlowEventVO> listByPage(FlowEventPageDTO flowEventPageDTO){
        FlowNodeBindQueryDTO queryDTO = new FlowNodeBindQueryDTO();
        queryDTO.setDefCode(flowEventPageDTO.getDefCode());
        queryDTO.setDefVersion(flowEventPageDTO.getDefVersion());
        queryDTO.setBindType(FlowNodeBindTypeEnum.EVENT.ordinal());
        return flowEventMapper.page(CommonPageRequest.constructPage(
                flowEventPageDTO.getCurrent(), flowEventPageDTO.getSize()), queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(FlowEventAddOrEditDTO flowEventAddOrEditDTO){
        FlowEvent flowEvent = BeanUtil.copyProperties(flowEventAddOrEditDTO, FlowEvent.class);
        flowEvent.setCreateTime(new Date());
        flowEventMapper.insert(flowEvent);
        // 绑定关系表
        FlowNodeBindAddOrEditDTO flowNodeBindAddOrEditDTO =
                BeanUtil.copyProperties(flowEventAddOrEditDTO, FlowNodeBindAddOrEditDTO.class);
        flowNodeBindAddOrEditDTO.setBindType(FlowNodeBindTypeEnum.EVENT.ordinal());
        flowNodeBindAddOrEditDTO.setBindObjid(flowEvent.getEventId());
        flowNodeBindService.save(flowNodeBindAddOrEditDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(FlowEventAddOrEditDTO flowEventAddOrEditDTO){
        FlowEvent flowEvent = this.getById(flowEventAddOrEditDTO.getEventId());
        if(ObjectUtil.isNull(flowEvent)){
            throw new CommonException("流程事件不存在！");
        }
        BeanUtil.copyProperties(flowEventAddOrEditDTO, flowEvent);
        flowEventMapper.updateById(flowEvent);
        // 绑定关系表
        flowNodeBindService.deleteByBindTypeAndBindObjid(
                FlowNodeBindTypeEnum.EVENT.ordinal(), flowEventAddOrEditDTO.getEventId());
        FlowNodeBindAddOrEditDTO flowNodeBindAddOrEditDTO =
                BeanUtil.copyProperties(flowEventAddOrEditDTO, FlowNodeBindAddOrEditDTO.class);
        flowNodeBindAddOrEditDTO.setBindType(FlowNodeBindTypeEnum.EVENT.ordinal());
        flowNodeBindAddOrEditDTO.setBindObjid(flowEventAddOrEditDTO.getEventId());
        flowNodeBindService.save(flowNodeBindAddOrEditDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void multiDelete(CommonIdListDTO idListDTO){
        // 删除流程事件
        this.removeBatchByIds(idListDTO.getId());
        // 删除绑定关系表
        flowNodeBindService.deleteByBindTypeAndBindObjid(
                FlowNodeBindTypeEnum.EVENT.ordinal(),
                idListDTO.getId().toArray(new String[0]));
    }

    @Override
    public FlowEventVO get(String defCode, Integer defVersion, String nodeId, Integer eventType){
        return flowEventMapper.get(defCode, defVersion, nodeId, eventType);
    }

    @Override
    public FlowInfoVO executeBindEvent(FlowInfoVO flowInfoVO, Integer eventType){
        FlowEventVO flowEventVO = get(flowInfoVO.getDefCode(), flowInfoVO.getDefVersion(),
                flowInfoVO.getCurNodeInfo().getNodeId(), eventType);
        if(ObjectUtil.isNull(flowEventVO)){
            return null;
        }
        if(flowEventVO.getImplType().equals(FlowEventImplTypeEnum.LOCAL_SERVICE.ordinal())){
            // 本地服务情形
            try {
                String[] localService = flowEventVO.getImplLocalservice().split("\\.");
                String beanId = localService[0];
                String method = localService[1];
                Object serviceBean = SpringUtil.getBean(beanId);
                Method invokeMethod = serviceBean.getClass().getDeclaredMethod(method, FlowInfoVO.class);
                flowInfoVO = (FlowInfoVO) invokeMethod.invoke(serviceBean, flowInfoVO);
            } catch (Exception e) {
                log.error(ExceptionUtil.stacktraceToString(e));
                throw new CommonException(String.format("执行流程事件%s出错！", flowEventVO.getImplLocalservice()));
            }
        }else if(flowEventVO.getImplType().equals(FlowEventImplTypeEnum.SCRIPT.ordinal())){
            // TODO 执行脚本情形

        }
        return flowInfoVO;
    }

}
