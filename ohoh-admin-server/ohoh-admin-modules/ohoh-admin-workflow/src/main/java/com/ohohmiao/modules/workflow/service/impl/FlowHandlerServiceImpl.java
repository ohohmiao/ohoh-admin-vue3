package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.modules.system.api.SysUserApi;
import com.ohohmiao.modules.system.model.pojo.SysReferRes;
import com.ohohmiao.modules.system.model.vo.SysUserVO;
import com.ohohmiao.modules.workflow.enums.FlowDefBindTypeEnum;
import com.ohohmiao.modules.workflow.enums.FlowHandlerTypeEnum;
import com.ohohmiao.modules.workflow.mapper.FlowHandlerMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowHandlerAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowHandlerPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowHandler;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import com.ohohmiao.modules.workflow.model.vo.FlowHandlerVO;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import com.ohohmiao.modules.workflow.service.FlowDefBindService;
import com.ohohmiao.modules.workflow.service.FlowHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程环节办理人配置Service实现
 *
 * @author ohohmiao
 * @date 2025-06-01 11:01
 */
@Service("flowHandlerService")
public class FlowHandlerServiceImpl extends ServiceImpl<FlowHandlerMapper, FlowHandler> implements FlowHandlerService {

    @Resource
    private FlowHandlerMapper flowHandlerMapper;

    @Resource
    private FlowDefBindService flowDefBindService;

    @Resource(name = "sysUserApi")
    private SysUserApi sysUserApi;

    public FlowHandlerServiceImpl(FlowHandlerMapper flowHandlerMapper) {
        this.flowHandlerMapper = flowHandlerMapper;
    }

    @Override
    public Page<FlowHandler> listByPage(FlowHandlerPageDTO pageDTO){
        LambdaQueryWrapper<FlowHandler> pageWrapper = new LambdaQueryWrapper<>();
        pageWrapper.eq(FlowHandler::getDefCode, pageDTO.getDefCode());
        pageWrapper.eq(FlowHandler::getDefVersion, pageDTO.getDefVersion());
        pageWrapper.eq(FlowHandler::getNodeId, pageDTO.getNodeId());
        pageWrapper.orderByDesc(FlowHandler::getCreateTime);
        return this.page(CommonPageRequest.constructPage(pageDTO.getCurrent(), pageDTO.getSize()), pageWrapper);
    }

    @Override
    public FlowHandlerVO get(String handlerId){
        FlowHandler flowHandler = flowHandlerMapper.selectById(handlerId);
        if(ObjectUtil.isNotNull(flowHandler)){
            FlowHandlerVO flowHandlerVO = new FlowHandlerVO();
            BeanUtil.copyProperties(flowHandler, flowHandlerVO);
            if(flowHandlerVO.getHandlerType().equals(FlowHandlerTypeEnum.REFERRES.ordinal())){
                // 组装指定人员数据
                List<SysReferRes> targetReferResList = flowDefBindService.list(FlowDefBindTypeEnum.HANDLER.ordinal(),
                        flowHandlerVO.getDefCode(), flowHandlerVO.getDefVersion(), handlerId);
                flowHandlerVO.setTargetReferResList(targetReferResList);
            }
            return flowHandlerVO;
        }else{
            return null;
        }
    }

    @Override
    public FlowHandlerVO getNextNodeFlowHandler(String defCode, Integer defVersion, String nextNodeId){
        LambdaQueryWrapper<FlowHandler> getWrapper = new LambdaQueryWrapper<>();
        getWrapper.eq(FlowHandler::getDefCode, defCode);
        getWrapper.eq(FlowHandler::getDefVersion, defVersion);
        getWrapper.eq(FlowHandler::getNextnodeId, nextNodeId);
        FlowHandler flowHandler = flowHandlerMapper.selectOne(getWrapper);
        if(ObjectUtil.isNotNull(flowHandler)){
            FlowHandlerVO flowHandlerVO = new FlowHandlerVO();
            BeanUtil.copyProperties(flowHandler, flowHandlerVO);
            if(flowHandlerVO.getHandlerType().equals(FlowHandlerTypeEnum.REFERRES.ordinal())){
                // 组装指定人员数据
                List<SysReferRes> targetReferResList = flowDefBindService.list(FlowDefBindTypeEnum.HANDLER.ordinal(),
                        flowHandlerVO.getDefCode(), flowHandlerVO.getDefVersion(), flowHandler.getHandlerId());
                flowHandlerVO.setTargetReferResList(targetReferResList);
            }
            return flowHandlerVO;
        }else{
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(FlowHandlerAddOrEditDTO flowHandlerAddOrEditDTO){
        FlowHandler flowHandler = BeanUtil.copyProperties(flowHandlerAddOrEditDTO, FlowHandler.class);
        flowHandler.setCreateTime(new Date());
        this.save(flowHandler);
        if(flowHandler.getHandlerType().equals(FlowHandlerTypeEnum.REFERRES.ordinal())){
            // 更新指定人员
            flowDefBindService.save(FlowDefBindTypeEnum.HANDLER.ordinal(),
                    flowHandler.getDefCode(), flowHandler.getDefVersion(),
                    flowHandlerAddOrEditDTO.getTargetReferResList(), flowHandler.getHandlerId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(FlowHandlerAddOrEditDTO flowHandlerAddOrEditDTO){
        FlowHandler flowHandler = this.getById(flowHandlerAddOrEditDTO.getHandlerId());
        if(ObjectUtil.isNull(flowHandler)){
            throw new CommonException("环节办理人配置不存在！");
        }
        BeanUtil.copyProperties(flowHandlerAddOrEditDTO, flowHandler);
        this.updateById(flowHandler);
        // 删除指定人员
        flowDefBindService.deleteByBindTypeAndBindObjid(
                FlowDefBindTypeEnum.HANDLER.ordinal(), flowHandler.getHandlerId());
        if(flowHandler.getHandlerType().equals(FlowHandlerTypeEnum.REFERRES.ordinal())){
            // 更新指定人员
            flowDefBindService.save(FlowDefBindTypeEnum.HANDLER.ordinal(),
                    flowHandler.getDefCode(), flowHandler.getDefVersion(),
                    flowHandlerAddOrEditDTO.getTargetReferResList(), flowHandler.getHandlerId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void multiDelete(CommonIdListDTO idListDTO){
        this.removeBatchByIds(idListDTO.getId());
        flowDefBindService.deleteByBindTypeAndBindObjid(
                FlowDefBindTypeEnum.HANDLER.ordinal(), idListDTO.getId().toArray(new String[0]));
    }

    @Override
    public List<FlowTaskHandler> listFlowNodeHandler4ReferRes(String defCode, Integer defVersion, String nodeId){
        List<FlowTaskHandler> resultList = CollectionUtil.newArrayList();
        FlowHandlerVO flowHandlerVO = this.getNextNodeFlowHandler(defCode, defVersion, nodeId);
        if(ObjectUtil.isNull(flowHandlerVO)){
            return resultList;
        }
        Integer handlerType = flowHandlerVO.getHandlerType();
        if(handlerType.equals(FlowHandlerTypeEnum.REFERRES.ordinal())){
            // 指定人员情形，根据关联资源查找用户列表
            List<SysUserVO> userVOList = sysUserApi.listByReferRes(
                    flowHandlerVO.getTargetReferResList());
            resultList = userVOList.stream().map(user -> {
                FlowTaskHandler handler = new FlowTaskHandler();
                handler.setHandlerId(user.getUserId());
                handler.setHandlerName(user.getUserName());
                return handler;
            }).collect(Collectors.toList());
        }
        return resultList;
    }

    @Override
    public List<FlowTaskHandler> doHandlerFilterBySameOrg(FlowInfoVO flowInfoVO, List<FlowTaskHandler> sourceList){
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(ObjectUtil.isNotNull(loginUser.getSwitchOrg())){
            List<String> sourceUserIdList = sourceList.stream().map(FlowTaskHandler::getHandlerId).collect(Collectors.toList());
            List<SysUserVO> sysUserVOList = sysUserApi.doFilterBySameOrg(sourceUserIdList, loginUser.getSwitchOrg().getOrgId());
            return sysUserVOList.stream().map(user -> {
                FlowTaskHandler handler = new FlowTaskHandler();
                handler.setHandlerId(user.getUserId());
                handler.setHandlerName(user.getUserName());
                return handler;
            }).collect(Collectors.toList());
        }else{
            return sourceList;
        }
    }

    @Override
    public List<FlowTaskHandler> doHandlerFilterByOneLevelUpOrg(FlowInfoVO flowInfoVO, List<FlowTaskHandler> sourceList){
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(ObjectUtil.isNotNull(loginUser.getSwitchOrg())){
            List<String> sourceUserIdList = sourceList.stream().map(FlowTaskHandler::getHandlerId).collect(Collectors.toList());
            List<SysUserVO> sysUserVOList = sysUserApi.doFilterByOneLevelUpOrg(sourceUserIdList, loginUser.getSwitchOrg().getOrgId());
            return sysUserVOList.stream().map(user -> {
                FlowTaskHandler handler = new FlowTaskHandler();
                handler.setHandlerId(user.getUserId());
                handler.setHandlerName(user.getUserName());
                return handler;
            }).collect(Collectors.toList());
        }else{
            return sourceList;
        }
    }

    @Override
    public List<FlowTaskHandler> doHandlerFilterBySameAndChildOrg(FlowInfoVO flowInfoVO, List<FlowTaskHandler> sourceList){
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(ObjectUtil.isNotNull(loginUser.getSwitchOrg())){
            List<String> sourceUserIdList = sourceList.stream().map(FlowTaskHandler::getHandlerId).collect(Collectors.toList());
            List<SysUserVO> sysUserVOList = sysUserApi.doFilterBySameAndChildOrg(sourceUserIdList, loginUser.getSwitchOrg().getOrgId());
            return sysUserVOList.stream().map(user -> {
                FlowTaskHandler handler = new FlowTaskHandler();
                handler.setHandlerId(user.getUserId());
                handler.setHandlerName(user.getUserName());
                return handler;
            }).collect(Collectors.toList());
        }else{
            return sourceList;
        }
    }

}
