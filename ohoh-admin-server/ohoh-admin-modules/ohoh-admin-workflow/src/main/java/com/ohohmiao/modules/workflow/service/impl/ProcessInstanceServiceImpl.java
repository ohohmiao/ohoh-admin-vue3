package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.modules.system.service.SysRestDayService;
import com.ohohmiao.modules.workflow.enums.FlowCacheKeyEnum;
import com.ohohmiao.modules.workflow.enums.FlowProcessStateEnum;
import com.ohohmiao.modules.workflow.enums.ProcessLimitTypeEnum;
import com.ohohmiao.modules.workflow.mapper.ProcessInstanceMapper;
import com.ohohmiao.modules.workflow.model.entity.ProcessInstance;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import com.ohohmiao.modules.workflow.service.ProcessInstanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程实例Service实现
 *
 * @author ohohmiao
 * @date 2025-06-11 10:22
 */
@Service("processInstanceService")
public class ProcessInstanceServiceImpl extends CommonServiceImpl<ProcessInstanceMapper, ProcessInstance> implements ProcessInstanceService {

    @Resource
    private PlatRedisUtil platRedisUtil;

    @Resource
    private SysRestDayService sysRestDayService;

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

    @Override
    public void saveOrUpdate(FlowInfoVO flowInfoVO, boolean isTempSave){
        if(StrUtil.isBlank(flowInfoVO.getProcessId())){
            ProcessInstance processInstance = new ProcessInstance();
            processInstance.setDefCode(flowInfoVO.getDefCode());
            processInstance.setDefVersion(flowInfoVO.getDefVersion());
            LocalDateTime curDateTime = LocalDateTime.now();
            processInstance.setProcessStarttime(curDateTime);
            processInstance.setProcessNum(generateWorkflowSerial(flowInfoVO.getDefCode()));
            if(isTempSave){
                processInstance.setProcessState(FlowProcessStateEnum.DRAFT.ordinal());
                processInstance.setCurrunningNodeids(flowInfoVO.getCurNodeInfo().getNodeId());
                processInstance.setCurrunningNodenames(flowInfoVO.getCurNodeInfo().getNodeName());
                processInstance.setCurHandlerids(flowInfoVO.getCreatorId());
                processInstance.setCurHandlernames(flowInfoVO.getCreatorName());
            }else{
                processInstance.setProcessState(FlowProcessStateEnum.RUNNING.ordinal());
                // 计算截止日期
                processInstance.setEndDeadline(getProcessDeadline(curDateTime, flowInfoVO));
            }
            processInstance.setBusTablename(flowInfoVO.getBusTableName());
            processInstance.setBusRecordid(flowInfoVO.getBusRecordId());
            processInstance.setProcessSubject(String.format("%s【发起人：%s】",
                    flowInfoVO.getDefName(), flowInfoVO.getCreatorName()));
            processInstance.setCreatorType(flowInfoVO.getCreatorType());
            processInstance.setCreatorId(flowInfoVO.getCreatorId());
            processInstance.setCreatorName(flowInfoVO.getCreatorName());
            this.save(processInstance);
            // 设置流程实例id
            flowInfoVO.setProcessId(processInstance.getProcessId());
        }else{
            ProcessInstance processInstance = this.getById(flowInfoVO.getProcessId());
            if(ObjectUtil.isNotNull(processInstance) && processInstance.getProcessState().equals(
                    FlowProcessStateEnum.DRAFT.ordinal()) && !isTempSave){
                // 计算截止日期
                LocalDateTime curDateTime = LocalDateTime.now();
                processInstance.setProcessStarttime(curDateTime);
                processInstance.setEndDeadline(getProcessDeadline(curDateTime, flowInfoVO));
                this.updateById(processInstance);
            }
        }
    }

    /**
     * 生成流程实例流水号
     * @param defCode
     * @return
     */
    private String generateWorkflowSerial(String defCode){
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String redisKey = String.format(FlowCacheKeyEnum.WORKFLOW_SERIAL.getKey(), defCode, date);
        // 原子递增序列号
        Long sequence = platRedisUtil.increment(redisKey);
        // 设置过期时间为1天，避免Redis空间增长
        if(sequence != null && sequence == 1){
            platRedisUtil.expire(redisKey, FlowCacheKeyEnum.WORKFLOW_SERIAL.getTtl());
        }
        return String.format("%s%s%04d", defCode, date, sequence);
    }

    private LocalDateTime getProcessDeadline(LocalDateTime datetime, FlowInfoVO flowInfoVO){
        if(flowInfoVO.getProcessLimittype().equals(ProcessLimitTypeEnum.WORKDAY.ordinal())){
            // 按工作日
            return sysRestDayService.calcWorkday(datetime, flowInfoVO.getProcessLimitvalue());
        }else if(flowInfoVO.getProcessLimittype().equals(ProcessLimitTypeEnum.DAY.ordinal())){
            // 按自然日
            return datetime.plusDays(flowInfoVO.getProcessLimitvalue());
        }else if(flowInfoVO.getProcessLimittype().equals(ProcessLimitTypeEnum.HOUR.ordinal())){
            // 按小时
            return datetime.plusHours(flowInfoVO.getProcessLimitvalue());
        }else{
            return null;
        }
    }

}
