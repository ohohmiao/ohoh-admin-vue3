package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.modules.system.model.pojo.SysReferRes;
import com.ohohmiao.modules.workflow.mapper.FlowDefBindMapper;
import com.ohohmiao.modules.workflow.model.entity.FlowDefBind;
import com.ohohmiao.modules.workflow.service.FlowDefBindService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 流程定义绑定信息Service实现
 *
 * @author ohohmiao
 * @date 2025-05-27 17:15
 */
@Service
public class FlowDefBindServiceImpl extends ServiceImpl<FlowDefBindMapper, FlowDefBind> implements FlowDefBindService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer bindType, String defCode, Integer defVersion){
        QueryWrapper<FlowDefBind> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(FlowDefBind::getBindType, bindType);
        deleteWrapper.lambda().eq(FlowDefBind::getDefCode, defCode);
        deleteWrapper.lambda().eq(FlowDefBind::getDefVersion, defVersion);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Integer bindType, String defCode, Integer defVersion,
                             List<SysReferRes> referResList, String bindObjid){
        if(StrUtil.isNotBlank(bindObjid)){
            this.deleteByBindTypeAndBindObjid(bindType, bindObjid);
        }else{
            this.delete(bindType, defCode, defVersion);
        }

        if(CollUtil.isNotEmpty(referResList)){
            Set<FlowDefBind> bindList = CollectionUtil.newHashSet();
            for(int i = 0; i < referResList.size(); i++){
                FlowDefBind bind = BeanUtil.copyProperties(referResList.get(i), FlowDefBind.class);
                bind.setBindType(bindType);
                bind.setDefCode(defCode);
                bind.setDefVersion(defVersion);
                bind.setBindObjid(bindObjid);
                bind.setBindSort(i + 1);
                bindList.add(bind);
            }
            this.saveBatch(bindList);
        }
    }

    @Override
    public List<SysReferRes> list(Integer bindType, String defCode, Integer defVersion, String bindObjid){
        LambdaQueryWrapper<FlowDefBind> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowDefBind::getBindType, bindType);
        queryWrapper.eq(FlowDefBind::getDefCode, defCode);
        queryWrapper.eq(FlowDefBind::getDefVersion, defVersion);
        queryWrapper.eq(StrUtil.isNotBlank(bindObjid), FlowDefBind::getBindObjid, bindObjid);
        queryWrapper.orderByAsc(FlowDefBind::getBindSort);
        return this.list(queryWrapper).stream().map(item ->
                BeanUtil.copyProperties(item, SysReferRes.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBindTypeAndBindObjid(Integer bindType, String... bindObjid){
        if(bindObjid.length == 0){
            return;
        }
        LambdaQueryWrapper<FlowDefBind> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(FlowDefBind::getBindType, bindType);
        deleteWrapper.in(FlowDefBind::getBindObjid, bindObjid);
        this.remove(deleteWrapper);
    }

}
