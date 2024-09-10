package com.ohohmiao.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.modules.system.model.entity.SysUserProp;
import com.ohohmiao.modules.system.mapper.SysUserPropMapper;
import com.ohohmiao.modules.system.service.SysUserPropService;
import com.ohohmiao.modules.system.model.vo.SysUserPropVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统用户属性Service实现
 *
 * @author ohohmiao
 * @date 2023-04-06 14:13
 */
@Service
public class SysUserPropServiceImpl extends ServiceImpl<SysUserPropMapper, SysUserProp> implements SysUserPropService {

    @Override
    public List<SysUserPropVO> listByUserIdAndTableName(String userId, String tableName){
        LambdaQueryWrapper<SysUserProp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUserProp::getUserId, userId);
        if(StrUtil.isNotEmpty(tableName)){
            lambdaQueryWrapper.eq(SysUserProp::getPropTablename, tableName);
        }
        lambdaQueryWrapper.orderByDesc(SysUserProp::getDefaultFlag);
        lambdaQueryWrapper.orderByAsc(SysUserProp::getPropSort);
        List<SysUserProp> userProps = this.list(lambdaQueryWrapper);
        return userProps.stream().map(p -> {
            SysUserPropVO result = new SysUserPropVO();
            BeanUtil.copyProperties(p, result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SysUserPropVO> listByUserIdsAndTableName(Collection<String> userIds, String tableName){
        LambdaQueryWrapper<SysUserProp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysUserProp::getUserId, userIds);
        if(StrUtil.isNotEmpty(tableName)){
            lambdaQueryWrapper.eq(SysUserProp::getPropTablename, tableName);
        }
        lambdaQueryWrapper.orderByDesc(SysUserProp::getDefaultFlag);
        List<SysUserProp> userProps = this.list(lambdaQueryWrapper);
        return userProps.stream().map(p -> {
            SysUserPropVO result = new SysUserPropVO();
            BeanUtil.copyProperties(p, result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public Set<String> listRecordIdsByUserIdAndTableName(String userId, String tableName){
        List<SysUserPropVO> props = this.listByUserIdAndTableName(userId, tableName);
        return props.stream().map(SysUserPropVO::getPropRecordid).collect(Collectors.toSet());
    }

    @Override
    public Set<String> listRecordIdsByUserIdsAndTableName(Collection<String> userIds, String tableName){
        List<SysUserPropVO> props = this.listByUserIdsAndTableName(userIds, tableName);
        return props.stream().map(SysUserPropVO::getPropRecordid).collect(Collectors.toSet());
    }

    @Override
    public List<SysUserPropVO> listByTableNameAndRecordId(String tableName, String recordId){
        LambdaQueryWrapper<SysUserProp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUserProp::getPropTablename, tableName);
        lambdaQueryWrapper.eq(SysUserProp::getPropRecordid, recordId);
        List<SysUserProp> userProps = this.list(lambdaQueryWrapper);
        return userProps.stream().map(p -> {
            SysUserPropVO result = new SysUserPropVO();
            BeanUtil.copyProperties(p, result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public Set<String> listUserIdsByTableNameAndRecordId(String tableName, String recordId){
        List<SysUserPropVO> props = this.listByTableNameAndRecordId(tableName, recordId);
        return props.stream().map(SysUserPropVO::getUserId).collect(Collectors.toSet());
    }

    @Override
    public Long countByUserIdAndTableName(String userId, String tableName){
        LambdaQueryWrapper<SysUserProp> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(SysUserProp::getUserId, userId);
        countWrapper.eq(SysUserProp::getPropTablename, tableName);
        return this.count(countWrapper);
    }

    @Override
    public Long countByTableNameAndRecordId(String tableName, String recordId){
        LambdaQueryWrapper<SysUserProp> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(SysUserProp::getPropTablename, tableName);
        countWrapper.eq(SysUserProp::getPropRecordid, recordId);
        return this.count(countWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTableNameAndRecordId(String tableName, String recordId){
        QueryWrapper<SysUserProp> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(SysUserProp::getPropTablename, tableName);
        deleteWrapper.lambda().eq(SysUserProp::getPropRecordid, recordId);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTableNameAndRecordIds(String tableName, List<String> recordIds){
        QueryWrapper<SysUserProp> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(SysUserProp::getPropTablename, tableName);
        deleteWrapper.lambda().in(SysUserProp::getPropRecordid, recordIds);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserId(String userId){
        QueryWrapper<SysUserProp> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().in(SysUserProp::getUserId, userId);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserIds(List<String> userIds){
        QueryWrapper<SysUserProp> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().in(SysUserProp::getUserId, userIds);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserIdAndTableName(String userId, String tableName){
        QueryWrapper<SysUserProp> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(SysUserProp::getUserId, userId);
        deleteWrapper.lambda().eq(SysUserProp::getPropTablename, tableName);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserIdsAndTableName(List<String> userIds, String tableName){
        QueryWrapper<SysUserProp> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().in(SysUserProp::getUserId, userIds);
        deleteWrapper.lambda().eq(SysUserProp::getPropTablename, tableName);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(String userId, String tableName, List<SysUserProp> props){
        this.deleteByUserIdAndTableName(userId, tableName);

        if(CollUtil.isNotEmpty(props)){
            Set<SysUserProp> propList = props.stream().map(prop -> {
                prop.setPropTablename(tableName);
                prop.setUserId(userId);
                return prop;
            }).collect(Collectors.toSet());
            this.saveBatch(propList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateMultiUserProps(String tableName, String recordId, List<String> userIds){
        this.deleteByTableNameAndRecordId(tableName, recordId);

        if(CollUtil.isNotEmpty(userIds)){
            Set<String> userIdSet = new HashSet<>(userIds);
            List<SysUserProp> propList = userIdSet.stream().map(userId -> {
                SysUserProp prop = new SysUserProp();
                prop.setPropTablename(tableName);
                prop.setPropRecordid(recordId);
                prop.setUserId(userId);
                return prop;
            }).collect(Collectors.toList());
            this.saveBatch(propList);
        }
    }

}
