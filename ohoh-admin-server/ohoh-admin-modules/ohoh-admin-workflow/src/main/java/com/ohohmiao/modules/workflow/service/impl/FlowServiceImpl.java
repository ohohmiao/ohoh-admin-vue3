package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.modules.workflow.model.dto.FlowInfoQueryDTO;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;
import com.ohohmiao.modules.workflow.model.vo.FlowFormVO;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import com.ohohmiao.modules.workflow.service.FlowFormService;
import com.ohohmiao.modules.workflow.service.FlowHisDeployService;
import com.ohohmiao.modules.workflow.service.FlowService;
import com.ohohmiao.modules.workflow.util.WorkflowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 流程核心Service实现
 *
 * @author ohohmiao
 * @date 2025-06-08 11:24
 */
@Service
public class FlowServiceImpl implements FlowService {

    @Resource
    private FlowHisDeployService flowHisDeployService;

    @Resource
    private FlowFormService flowFormService;

    @Override
    public FlowInfoVO getFlowInfo(FlowInfoQueryDTO queryDTO){
        FlowInfoVO flowInfoVO = new FlowInfoVO();
        FlowDefVO flowDefVO = null;
        if(StrUtil.isNotBlank(queryDTO.getExeId())){

        }else{
            flowInfoVO.setStartFlowFlag(true);
            flowInfoVO.setDefCode(queryDTO.getDefCode());
            flowInfoVO.setDefVersion(queryDTO.getDefVersion());
            flowInfoVO.setDoQueryFlag(false);
            Integer defVersion = ObjectUtil.isNotNull(queryDTO.getDefVersion())? queryDTO.getDefVersion(): 1;
            // 查询指定版本流程定义
            flowDefVO = flowHisDeployService.get(queryDTO.getDefCode(), defVersion, false);
            flowInfoVO.setDefName(flowDefVO.getDefName());
            flowInfoVO.setDefJson(flowDefVO.getDefJson());
            flowInfoVO.setDefXml(flowDefVO.getDefXml());
            // 查询第一个任务节点
            Map firstTaskNode = WorkflowUtil.getFirstTaskNode(flowInfoVO.getDefJson());
            String curNodeId = (String)firstTaskNode.get("id");
            Map curNode = WorkflowUtil.getFlowNode(flowInfoVO.getDefJson(), curNodeId);
            String curNodeName = (String)curNode.get("name");
            flowInfoVO.setCurNodeId(curNodeId);
            flowInfoVO.setCurNodeName(curNodeName);
            flowInfoVO.setCurRunningNodeIds(curNodeId);

        }
        // 查询绑定的流程表单
        FlowFormVO flowFormVO = flowFormService.getBindForm(flowInfoVO.getDefCode(),
                flowInfoVO.getDefVersion(), flowInfoVO.getCurNodeId());
        if(ObjectUtil.isNull(flowFormVO)){
            throw new CommonException("操作出错，流程表单未绑定！");
        }
        flowInfoVO.setFormId(flowFormVO.getFormId());
        flowInfoVO.setFormPath(flowFormVO.getFormPath());

        return flowInfoVO;
    }

}
