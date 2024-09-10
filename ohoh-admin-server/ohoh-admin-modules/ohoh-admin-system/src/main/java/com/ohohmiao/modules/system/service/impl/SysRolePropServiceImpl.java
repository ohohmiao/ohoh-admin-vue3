package com.ohohmiao.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.modules.system.model.entity.SysRoleProp;
import com.ohohmiao.modules.system.mapper.SysRolePropMapper;
import com.ohohmiao.modules.system.service.SysRolePropService;
import com.ohohmiao.modules.system.model.vo.SysRolePropVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色属性Service实现类
 *
 * @author ohohmiao
 * @date 2023-04-06 14:25
 */
@Service
public class SysRolePropServiceImpl extends ServiceImpl<SysRolePropMapper, SysRoleProp> implements SysRolePropService {

    @Override
    public List<SysRolePropVO> listByRoleIdAndTableName(String roleId, String tableName){
        LambdaQueryWrapper<SysRoleProp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRoleProp::getRoleId, roleId);
        if(StrUtil.isNotEmpty(tableName)){
            lambdaQueryWrapper.eq(SysRoleProp::getPropTablename, tableName);
        }
        List<SysRoleProp> roleProps = this.list(lambdaQueryWrapper);
        return roleProps.stream().map(p -> {
            SysRolePropVO result = new SysRolePropVO();
            BeanUtil.copyProperties(p, result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SysRolePropVO> listByRoleIdsAndTableName(Collection<String> roleIds, String tableName){
        if(CollUtil.isEmpty(roleIds)){
            return CollectionUtil.newArrayList();
        }
        LambdaQueryWrapper<SysRoleProp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysRoleProp::getRoleId, roleIds);
        if(StrUtil.isNotEmpty(tableName)){
            lambdaQueryWrapper.eq(SysRoleProp::getPropTablename, tableName);
        }
        List<SysRoleProp> roleProps = this.list(lambdaQueryWrapper);
        return roleProps.stream().map(p -> {
            SysRolePropVO result = new SysRolePropVO();
            BeanUtil.copyProperties(p, result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public Set<String> listRecordIdsByRoleIdAndTableName(String roleId, String tableName){
        List<SysRolePropVO> props = this.listByRoleIdAndTableName(roleId, tableName);
        return props.stream().map(SysRolePropVO::getPropRecordid).collect(Collectors.toSet());
    }

    @Override
    public Set<String> listRecordIdsByRoleIdsAndTableName(Collection<String> roleIds, String tableName){
        List<SysRolePropVO> props = this.listByRoleIdsAndTableName(roleIds, tableName);
        return props.stream().map(SysRolePropVO::getPropRecordid).collect(Collectors.toSet());
    }

    @Override
    public List<SysRolePropVO> listByTableNameAndRecordId(String tableName, String recordId){
        LambdaQueryWrapper<SysRoleProp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRoleProp::getPropTablename, tableName);
        lambdaQueryWrapper.eq(SysRoleProp::getPropRecordid, recordId);
        List<SysRoleProp> roleProps = this.list(lambdaQueryWrapper);
        return roleProps.stream().map(p -> {
            SysRolePropVO result = new SysRolePropVO();
            BeanUtil.copyProperties(p, result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public Set<String> listRoleIdsByTableNameAndRecordId(String tableName, String recordId){
        List<SysRolePropVO> props = this.listByTableNameAndRecordId(tableName, recordId);
        return props.stream().map(SysRolePropVO::getRoleId).collect(Collectors.toSet());
    }

    @Override
    public Long countByRoleIdAndTableName(String roleId, String tableName){
        LambdaQueryWrapper<SysRoleProp> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(SysRoleProp::getRoleId, roleId);
        countWrapper.eq(SysRoleProp::getPropTablename, tableName);
        return this.count(countWrapper);
    }

    @Override
    public Long countByTableNameAndRecordId(String tableName, String recordId){
        LambdaQueryWrapper<SysRoleProp> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(SysRoleProp::getPropTablename, tableName);
        countWrapper.eq(SysRoleProp::getPropRecordid, recordId);
        return this.count(countWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleId(String roleId){
        QueryWrapper<SysRoleProp> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(SysRoleProp::getRoleId, roleId);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleIds(List<String> roleIds){
        QueryWrapper<SysRoleProp> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().in(SysRoleProp::getRoleId, roleIds);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleIdAndTableName(String roleId, String tableName){
        QueryWrapper<SysRoleProp> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(SysRoleProp::getRoleId, roleId);
        deleteWrapper.lambda().eq(SysRoleProp::getPropTablename, tableName);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTableNameAndRecordId(String tableName, String recordId){
        QueryWrapper<SysRoleProp> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(SysRoleProp::getPropTablename, tableName);
        deleteWrapper.lambda().eq(SysRoleProp::getPropRecordid, recordId);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTableNameAndRecordIds(String tableName, List<String> recordIds){
        QueryWrapper<SysRoleProp> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(SysRoleProp::getPropTablename, tableName);
        deleteWrapper.lambda().in(SysRoleProp::getPropRecordid, recordIds);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(String roleId, String tableName, List<String> recordIds){
        this.deleteByRoleIdAndTableName(roleId, tableName);

        if(CollUtil.isNotEmpty(recordIds)){
            Set<String> recordIdSet = new HashSet<>(recordIds);
            List<SysRoleProp> sysRoleProps = recordIdSet.stream().map(recordId -> {
                SysRoleProp sysRoleProp = new SysRoleProp();
                sysRoleProp.setRoleId(roleId);
                sysRoleProp.setPropTablename(tableName);
                sysRoleProp.setPropRecordid(recordId);
                return sysRoleProp;
            }).collect(Collectors.toList());
            this.saveBatch(sysRoleProps);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateMultiRoleProps(String tableName, String recordId, List<String> roleIds){
        this.deleteByTableNameAndRecordId(tableName, recordId);

        if(CollUtil.isNotEmpty(roleIds)){
            Set<String> roleIdSet = new HashSet<>(roleIds);
            List<SysRoleProp> sysRoleProps = roleIdSet.stream().map(roleId -> {
                SysRoleProp sysRoleProp = new SysRoleProp();
                sysRoleProp.setRoleId(roleId);
                sysRoleProp.setPropTablename(tableName);
                sysRoleProp.setPropRecordid(recordId);
                return sysRoleProp;
            }).collect(Collectors.toList());
            this.saveBatch(sysRoleProps);
        }
    }

}
