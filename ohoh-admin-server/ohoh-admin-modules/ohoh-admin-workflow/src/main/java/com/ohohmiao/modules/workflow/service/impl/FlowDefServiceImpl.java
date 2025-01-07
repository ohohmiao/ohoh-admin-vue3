package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.XML;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.listener.CommonDataChangeEventCenter;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.enums.FlowDataListenerEnum;
import com.ohohmiao.modules.workflow.mapper.FlowDefMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowDefAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowDefPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowDef;
import com.ohohmiao.modules.workflow.model.entity.FlowDefType;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;
import com.ohohmiao.modules.workflow.service.FlowDefService;
import com.ohohmiao.modules.workflow.service.FlowDefTypeService;
import com.ohohmiao.modules.workflow.util.WorkflowUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 流程定义Service实现类
 *
 * @author ohohmiao
 * @date 2024-12-08 19:57
 */
@Service
public class FlowDefServiceImpl extends CommonServiceImpl<FlowDefMapper, FlowDef> implements FlowDefService {

    @Resource
    private FlowDefMapper flowDefMapper;

    @Resource
    private FlowDefTypeService flowDefTypeService;

    @Override
    public Page<FlowDefVO> listByPage(FlowDefPageDTO flowDefPageDTO){
        QueryWrapper<FlowDefVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("p.DEFTYPE_ID");
        queryWrapper.eq("d.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        //查询条件
        queryWrapper.like(StrUtil.isNotEmpty(flowDefPageDTO.getParentId()),
                "p.TREE_PATH", flowDefPageDTO.getParentId()+".");
        queryWrapper.like(StrUtil.isNotEmpty(flowDefPageDTO.getDefName()),
                "d.DEF_NAME", flowDefPageDTO.getDefName());
        queryWrapper.like(StrUtil.isNotEmpty(flowDefPageDTO.getDefCode()),
                "d.DEF_CODE", flowDefPageDTO.getDefCode());
        //排序
        queryWrapper.orderByAsc(CollectionUtil.newArrayList("p.TREE_LEVEL", "p.TREE_SORT", "d.DEF_SORT"));
        return flowDefMapper.pageByWrapper(CommonPageRequest.constructPage(
                flowDefPageDTO.getCurrent(), flowDefPageDTO.getSize()), queryWrapper);
    }

    @Override
    public boolean isExistDefCode(FlowDefAddOrEditDTO flowDefAddOrEditDTO){
        LambdaQueryWrapper<FlowDef> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(FlowDef::getDefCode, flowDefAddOrEditDTO.getDefCode());
        countWrapper.ne(StrUtil.isNotEmpty(flowDefAddOrEditDTO.getDefId()),
                FlowDef::getDefId, flowDefAddOrEditDTO.getDefId());
        return this.count(countWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(FlowDefAddOrEditDTO flowDefAddOrEditDTO){
        FlowDefType flowDefType = flowDefTypeService.getById(flowDefAddOrEditDTO.getDeftypeId());
        if(ObjectUtil.isNull(flowDefType)){
            throw new CommonException("流程类别不存在！");
        }
        if(flowDefType.getTreeLevel() != 2){
            throw new CommonException("仅限挂在二级类别下！");
        }
        boolean isExistDefCode = this.isExistDefCode(flowDefAddOrEditDTO);
        if(isExistDefCode){
            throw new CommonException("流程编码重复，请重新输入！");
        }
        FlowDef flowDef = BeanUtil.copyProperties(flowDefAddOrEditDTO, FlowDef.class);
        // 重新解析xml->json
        String formatJson = transferDefXml2DefJson(flowDefAddOrEditDTO.getDefXml());
        flowDef.setDefJson(formatJson);
        flowDef.setDefVersion(1);
        Integer maxSort = flowDefMapper.getMaxSortByDeftypeId(flowDefAddOrEditDTO.getDeftypeId());
        flowDef.setDefSort(ObjectUtil.isNotNull(maxSort)? maxSort + 1: 1);
        this.save(flowDef);

        CommonDataChangeEventCenter.doAddWithData(FlowDataListenerEnum.DEF.getName(), flowDef);
    }

    @Override
    public FlowDefVO get(String defId){
        FlowDef flowDef = flowDefMapper.selectById(defId);
        if(ObjectUtil.isNotNull(flowDef)){
            FlowDefVO flowDefVO = new FlowDefVO();
            BeanUtil.copyProperties(flowDef, flowDefVO);
            return flowDefVO;
        }else{
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(FlowDefAddOrEditDTO flowDefAddOrEditDTO){
        FlowDef flowDef = this.getById(flowDefAddOrEditDTO.getDefId());
        if(ObjectUtil.isNull(flowDef)){
            throw new CommonException("流程定义不存在！");
        }
        FlowDefType flowDefType = flowDefTypeService.getById(flowDefAddOrEditDTO.getDeftypeId());
        if(ObjectUtil.isNull(flowDefType)){
            throw new CommonException("流程类别不存在！");
        }
        if(flowDefType.getTreeLevel() != 2){
            throw new CommonException("仅限挂在二级类别下！");
        }
        boolean isExistDefCode = this.isExistDefCode(flowDefAddOrEditDTO);
        if(isExistDefCode){
            throw new CommonException("流程编码重复，请重新输入！");
        }
        BeanUtil.copyProperties(flowDefAddOrEditDTO, flowDef);
        // 重新解析xml -> json
        String formatJson = transferDefXml2DefJson(flowDefAddOrEditDTO.getDefXml());
        flowDef.setDefJson(formatJson);
        //TODO 判断当前版本是否已发起流程，是=版本+1且存入历史部署表，否=不处理

        this.updateById(flowDef);

        CommonDataChangeEventCenter.doEditWithData(FlowDataListenerEnum.DEF.getName(), flowDefAddOrEditDTO);
    }

    /**
     * 将流程xml解析成流程json
     * @param defXml
     * @return
     */
    private String transferDefXml2DefJson(String defXml){
        String xml = defXml.replace("<bpmn:startEvent", "<bpmn:startEvent nodetype=\"start\"")
                .replace("<bpmn:task", "<bpmn:task nodetype=\"task\"")
                .replace("<bpmn:exclusiveGateway", "<bpmn:exclusiveGateway nodetype=\"exclusiveGateway\"")
                .replace("<bpmn:parallelGateway", "<bpmn:parallelGateway nodetype=\"parallelGateway\"")
                .replace("<bpmn:inclusiveGateway", "<bpmn:inclusiveGateway nodetype=\"inclusiveGateway\"")
                .replace("<bpmn:endEvent", "<bpmn:endEvent nodetype=\"end\"")
                .replace("<bpmn:", "<").replace("</bpmn:", "</");
        // xml -> json
        JSONObject xmlJSONObj = XML.toJSONObject(xml);
        String processJson = xmlJSONObj.toString();
        // 取出process节点
        Object processNode = WorkflowUtil.getBpmnProcessNode(processJson);
        if(ObjectUtil.isNull(processNode) || processNode instanceof List) {
            throw new CommonException("未检测到流程定义或存在多个流程定义！");
        }
        processJson = processNode.toString();
        // 取出startEvent节点
        Object startEventNode = WorkflowUtil.getStartEventNode(processJson);
        if(ObjectUtil.isNull(startEventNode) || startEventNode instanceof List) {
            throw new CommonException("未检测到发起节点或存在多个发起节点！");
        }
        return processJson;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdDTO idDTO){
        //TODO 待实现
        //TODO 判断是否有未办结的流程，存在则拒绝删除

        CommonDataChangeEventCenter.doDeleteWithId(FlowDataListenerEnum.DEF.getName(), idDTO.getId());
    }

}
