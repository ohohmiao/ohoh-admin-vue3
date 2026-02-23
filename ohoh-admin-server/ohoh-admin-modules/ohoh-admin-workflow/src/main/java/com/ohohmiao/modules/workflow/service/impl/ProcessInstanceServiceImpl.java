package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.modules.system.service.SysRestDayService;
import com.ohohmiao.modules.workflow.enums.FlowCacheKeyEnum;
import com.ohohmiao.modules.workflow.enums.FlowProcessStateEnum;
import com.ohohmiao.modules.workflow.enums.FlowTaskStateEnum;
import com.ohohmiao.modules.workflow.enums.ProcessLimitTypeEnum;
import com.ohohmiao.modules.workflow.mapper.ProcessInstanceMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowMyApprovalPageDTO;
import com.ohohmiao.modules.workflow.model.entity.ProcessInstance;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import com.ohohmiao.modules.workflow.model.vo.ProcessInstanceVO;
import com.ohohmiao.modules.workflow.service.ProcessInstanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
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

    @Resource
    private ProcessInstanceMapper processInstanceMapper;

    @Override
    public List<FlowTaskHandler> getCreator(FlowInfoVO flowInfoVO){
        List<FlowTaskHandler> resultList = new ArrayList<>();
        if(StrUtil.isNotBlank(flowInfoVO.getProcessId())){
            // 从流程实例表查询
            ProcessInstance processInstance = this.getById(flowInfoVO.getProcessId());
            FlowTaskHandler handler = new FlowTaskHandler();
            handler.setHandlerId(processInstance.getCreatorId());
            handler.setHandlerName(processInstance.getCreatorName());
            handler.setHandlerOrgid(processInstance.getCreatorOrgid());
            handler.setHandlerOrgname(processInstance.getCreatorOrgname());
            resultList.add(handler);
        }else{
            // 返回当前登录用户信息
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            FlowTaskHandler handler = new FlowTaskHandler();
            handler.setHandlerId(loginUser.getUserId());
            handler.setHandlerName(loginUser.getUserName());
            handler.setHandlerOrgid(loginUser.getSwitchOrg().getOrgId());
            handler.setHandlerOrgname(loginUser.getSwitchOrg().getOrgName());
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
            processInstance.setCreatorId(flowInfoVO.getCreatorId());
            processInstance.setCreatorName(flowInfoVO.getCreatorName());
            processInstance.setCreatorOrgid(flowInfoVO.getCreatorOrgid());
            processInstance.setCreatorOrgname(flowInfoVO.getCreatorOrgname());
            this.save(processInstance);
            // 设置流程实例id
            flowInfoVO.setProcessId(processInstance.getProcessId());
        }else{
            ProcessInstance processInstance = this.getById(flowInfoVO.getProcessId());
            if(ObjectUtil.isNotNull(processInstance) &&
                    processInstance.getProcessState() == FlowProcessStateEnum.DRAFT.ordinal() &&
                    !isTempSave){
                // 计算截止日期
                LocalDateTime curDateTime = LocalDateTime.now();
                processInstance.setProcessStarttime(curDateTime);
                processInstance.setEndDeadline(getProcessDeadline(curDateTime, flowInfoVO));
                this.updateById(processInstance);
            }
        }
    }

    @Override
    public void updateCurRunningInfo(String processId, String curRunningNodeIds,
                                     String curRunningNodeNames, String curHandlerIds, String curHandlerNames){
        LambdaUpdateWrapper<ProcessInstance> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProcessInstance::getProcessId, processId);
        updateWrapper.set(ProcessInstance::getCurrunningNodeids, curRunningNodeIds);
        updateWrapper.set(ProcessInstance::getCurrunningNodenames, curRunningNodeNames);
        updateWrapper.set(ProcessInstance::getCurHandlerids, curHandlerIds);
        updateWrapper.set(ProcessInstance::getCurHandlernames, curHandlerNames);
        this.update(updateWrapper);
    }

    @Override
    public void updateHandleEndedInfo(String processId, Integer processState, String finalOpinion){
        LocalDateTime curDateTime = LocalDateTime.now();
        ProcessInstance processInstance = this.getById(processId);
        LocalDateTime startTime = processInstance.getProcessStarttime();
        LocalDateTime deadline = processInstance.getEndDeadline();

        LambdaUpdateWrapper<ProcessInstance> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProcessInstance::getProcessId, processId);
        updateWrapper.set(ProcessInstance::getCurrunningNodeids, null);
        updateWrapper.set(ProcessInstance::getCurrunningNodenames, null);
        updateWrapper.set(ProcessInstance::getCurHandlerids, null);
        updateWrapper.set(ProcessInstance::getCurHandlernames, null);
        updateWrapper.set(ProcessInstance::getFinalOpinion, finalOpinion);
        updateWrapper.set(ProcessInstance::getProcessEndtime, curDateTime);
        long consumeSeconds = Duration.between(startTime, curDateTime).getSeconds();
        updateWrapper.set(ProcessInstance::getConsumeSeconds, consumeSeconds);
        if(deadline != null){
            if(curDateTime.isAfter(deadline)){
                updateWrapper.set(ProcessInstance::getOvertimeFlag, CommonWhetherEnum.YES.getCode());
                long exceedSeconds = Duration.between(deadline, curDateTime).getSeconds();
                updateWrapper.set(ProcessInstance::getExceedSeconds, exceedSeconds);
            }else{
                updateWrapper.set(ProcessInstance::getOvertimeFlag, CommonWhetherEnum.NO.getCode());
            }
        }
        this.update(updateWrapper);
    }

    @Override
    public Page<ProcessInstanceVO> listMyApprovalPage(FlowMyApprovalPageDTO pageDTO){
        QueryWrapper<ProcessInstanceVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("p.PROCESS_ID");
        queryWrapper.eq("k.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        queryWrapper.eq("p.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        queryWrapper.in("k.TASK_STATE", FlowTaskStateEnum.SUSPENDED.ordinal(), FlowTaskStateEnum.RUNNING.ordinal());
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        queryWrapper.eq("k.HANDLER_ID", loginUser.getUserId());
        // 查询条件
        queryWrapper.eq(StrUtil.isNotEmpty(pageDTO.getProcessNum()), "p.PROCESS_NUM", pageDTO.getProcessNum());
        queryWrapper.like(StrUtil.isNotEmpty(pageDTO.getProcessSubject()), "p.PROCESS_SUBJECT", pageDTO.getProcessSubject());
        queryWrapper.orderByDesc("k.TASK_ID");
        return processInstanceMapper.listMyApprovalPage(CommonPageRequest.constructPage(
                pageDTO.getCurrent(), pageDTO.getSize()), queryWrapper);
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
        if(flowInfoVO.getProcessLimittype() == ProcessLimitTypeEnum.WORKDAY.ordinal()){
            // 按工作日
            return sysRestDayService.calcWorkday(datetime, flowInfoVO.getProcessLimitvalue());
        }else if(flowInfoVO.getProcessLimittype() == ProcessLimitTypeEnum.DAY.ordinal()){
            // 按自然日
            return datetime.plusDays(flowInfoVO.getProcessLimitvalue());
        }else if(flowInfoVO.getProcessLimittype() == ProcessLimitTypeEnum.HOUR.ordinal()){
            // 按小时
            return datetime.plusHours(flowInfoVO.getProcessLimitvalue());
        }else{
            return null;
        }
    }

}
