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
import com.ohohmiao.modules.workflow.mapper.FlowProcessMapper;
import com.ohohmiao.modules.workflow.model.entity.FlowProcess;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import com.ohohmiao.modules.workflow.service.FlowProcessService;
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
@Service("flowProcessService")
public class FlowProcessServiceImpl extends CommonServiceImpl<FlowProcessMapper, FlowProcess> implements FlowProcessService {

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
            FlowProcess flowProcess = new FlowProcess();
            flowProcess.setDefCode(flowInfoVO.getDefCode());
            flowProcess.setDefVersion(flowInfoVO.getDefVersion());
            LocalDateTime curDateTime = LocalDateTime.now();
            flowProcess.setProcessStarttime(curDateTime);
            flowProcess.setProcessNum(generateWorkflowSerial(flowInfoVO.getDefCode()));
            if(isTempSave){
                flowProcess.setProcessState(FlowProcessStateEnum.DRAFT.ordinal());
                flowProcess.setCurrunningNodeids(flowInfoVO.getCurNodeInfo().getNodeId());
                flowProcess.setCurrunningNodenames(flowInfoVO.getCurNodeInfo().getNodeName());
                flowProcess.setCurHandlerids(flowInfoVO.getCreatorId());
                flowProcess.setCurHandlernames(flowInfoVO.getCreatorName());
            }else{
                flowProcess.setProcessState(FlowProcessStateEnum.RUNNING.ordinal());
                // 计算截止日期
                flowProcess.setEndDeadline(getProcessDeadline(curDateTime, flowInfoVO));
            }
            flowProcess.setBusTablename(flowInfoVO.getBusTableName());
            flowProcess.setBusRecordid(flowInfoVO.getBusRecordId());
            flowProcess.setProcessSubject(String.format("%s【发起人：%s】",
                    flowInfoVO.getDefName(), flowInfoVO.getCreatorName()));
            flowProcess.setCreatorType(flowInfoVO.getCreatorType());
            flowProcess.setCreatorId(flowInfoVO.getCreatorId());
            flowProcess.setCreatorName(flowInfoVO.getCreatorName());
            this.save(flowProcess);
            // 设置流程实例id
            flowInfoVO.setProcessId(flowProcess.getProcessId());
        }else{
            FlowProcess flowProcess = this.getById(flowInfoVO.getProcessId());
            if(ObjectUtil.isNotNull(flowProcess) && flowProcess.getProcessState().equals(
                    FlowProcessStateEnum.DRAFT.ordinal()) && !isTempSave){
                // 计算截止日期
                LocalDateTime curDateTime = LocalDateTime.now();
                flowProcess.setProcessStarttime(curDateTime);
                flowProcess.setEndDeadline(getProcessDeadline(curDateTime, flowInfoVO));
                this.updateById(flowProcess);
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
