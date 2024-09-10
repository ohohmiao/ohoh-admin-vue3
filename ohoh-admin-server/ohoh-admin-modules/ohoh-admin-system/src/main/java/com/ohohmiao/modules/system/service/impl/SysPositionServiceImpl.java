package com.ohohmiao.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.listener.CommonDataChangeEventCenter;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.mybatis.service.impl.CommonTreeServiceImpl;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.modules.system.enums.SysCacheKeyEnum;
import com.ohohmiao.modules.system.enums.SysDataListenerEnum;
import com.ohohmiao.modules.system.mapper.SysPositionMapper;
import com.ohohmiao.modules.system.model.dto.SysPositionAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysPositionGrantUserDTO;
import com.ohohmiao.modules.system.model.dto.SysPositionUserPageDTO;
import com.ohohmiao.modules.system.model.entity.SysPosition;
import com.ohohmiao.modules.system.model.vo.SysPositionUserVO;
import com.ohohmiao.modules.system.model.vo.SysPositionVO;
import com.ohohmiao.modules.system.service.SysPositionService;
import com.ohohmiao.modules.system.enums.SysUserPropEnum;
import com.ohohmiao.modules.system.model.entity.SysUserProp;
import com.ohohmiao.modules.system.service.SysUserPropService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 岗位Service实现类
 *
 * @author ohohmiao
 * @date 2023-06-20 11:39
 */
@Service
public class SysPositionServiceImpl extends CommonTreeServiceImpl<SysPositionMapper, SysPosition> implements SysPositionService {

    @Resource
    private SysPositionMapper sysPositionMapper;

    @Resource
    private PlatRedisUtil platRedisUtil;

    @Resource
    private SysUserPropService sysUserPropService;

    @Override
    public List<SysPositionVO> listCachedAllPositions(){
        //尝试从缓存获取
        List<Map<String, Object>> cachedList = platRedisUtil.getCacheList(SysCacheKeyEnum.POSITION_ALL.getKey());
        if(CollUtil.isNotEmpty(cachedList)){
            return cachedList.stream().map(m ->
                    BeanUtil.toBean(m, SysPositionVO.class)
            ).collect(Collectors.toList());
        }

        //从库中查找，并写入缓存
        List<SysPosition> positionList = this.list(new LambdaQueryWrapper<SysPosition>()
                .orderByAsc(CollectionUtil.newArrayList(SysPosition::getTreeLevel, SysPosition::getTreeSort)));
        List<SysPositionVO> resultList = positionList.stream().map(sysPosition -> {
            SysPositionVO result = new SysPositionVO();
            BeanUtil.copyProperties(sysPosition, result);
            return result;
        }).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(resultList)) {
            platRedisUtil.setCacheList(SysCacheKeyEnum.POSITION_ALL.getKey(), resultList);
            platRedisUtil.expire(SysCacheKeyEnum.POSITION_ALL.getKey(), SysCacheKeyEnum.POSITION_ALL.getTtl());
        }
        return resultList;
    }

    @Override
    public List<Tree<String>> getTreeData(){
        // 查询岗位列表
        List<SysPositionVO> allPositions = this.listCachedAllPositions();
        // 构造hutool树
        List<TreeNode<String>> treeNodeList = allPositions.stream().map(sysPosition ->
            new TreeNode<>(sysPosition.getPositionId(), sysPosition.getParentId(),
                    sysPosition.getPositionName(),
                    sysPosition.getTreeSort())
                    .setExtra(JSON.parseObject(JSON.toJSONString(sysPosition),
                            new TypeReference<Map<String, Object>>() {}))
        ).collect(Collectors.toList());
        return TreeUtil.build(treeNodeList, "0");
    }

    @Override
    public boolean isExistPositionCode(SysPositionAddOrEditDTO sysPositionAddOrEditDTO){
        LambdaQueryWrapper<SysPosition> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysPosition::getPositionCode, sysPositionAddOrEditDTO.getPositionCode());
        lambdaQueryWrapper.ne(StrUtil.isNotEmpty(sysPositionAddOrEditDTO.getPositionId()),
                SysPosition::getPositionId, sysPositionAddOrEditDTO.getPositionId());
        return this.count(lambdaQueryWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysPositionAddOrEditDTO sysPositionAddOrEditDTO){
        if(StrUtil.isNotEmpty(sysPositionAddOrEditDTO.getPositionCode())){
            boolean isExistPositionCode = this.isExistPositionCode(sysPositionAddOrEditDTO);
            if(isExistPositionCode){
                throw new CommonException("岗位编码重复，请重新输入！");
            }
        }
        // 保存岗位
        SysPosition sysPosition = BeanUtil.copyProperties(sysPositionAddOrEditDTO, SysPosition.class);
        this.saveTreeData(sysPosition);

        CommonDataChangeEventCenter.doAddWithData(SysDataListenerEnum.POSITION.getName(), sysPosition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysPositionAddOrEditDTO sysPositionAddOrEditDTO){
        SysPosition sysPosition = this.getById(sysPositionAddOrEditDTO.getPositionId());
        if(ObjectUtil.isNull(sysPosition)){
            throw new CommonException("该岗位不存在！");
        }
        if(StrUtil.isNotEmpty(sysPositionAddOrEditDTO.getPositionCode())){
            boolean isExistPositionCode = this.isExistPositionCode(sysPositionAddOrEditDTO);
            if(isExistPositionCode){
                throw new CommonException("岗位编码重复，请重新输入！");
            }
        }
        // 更新岗位
        String oldTreePath = sysPosition.getTreePath();
        Integer oldTreeLevel = sysPosition.getTreeLevel();
        BeanUtil.copyProperties(sysPositionAddOrEditDTO, sysPosition);
        this.updateTreeData(sysPosition, oldTreePath, oldTreeLevel);

        CommonDataChangeEventCenter.doEditWithData(SysDataListenerEnum.POSITION.getName(), sysPosition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdDTO idDTO){
        String toDeleteSysPositionId = idDTO.getId();
        long thizChildCount = this.countByParentId(toDeleteSysPositionId);
        if(thizChildCount > 0){
            throw new CommonException("存在孩子节点，无法删除！");
        }
        //删除
        this.removeById(toDeleteSysPositionId);
        //删除关联属性
        LambdaUpdateWrapper<SysUserProp> userPropUpdateWrapper = new LambdaUpdateWrapper<>();
        userPropUpdateWrapper.set(SysUserProp::getPropExtendid, null);
        userPropUpdateWrapper.eq(SysUserProp::getPropTablename, SysUserPropEnum.SYSORG.getValue());
        userPropUpdateWrapper.eq(SysUserProp::getPropExtendid, toDeleteSysPositionId);
        CommonDataChangeEventCenter.doDeleteWithId(SysDataListenerEnum.POSITION.getName(), toDeleteSysPositionId);
    }

    @Override
    public Page<SysPositionUserVO> listUserByPage(SysPositionUserPageDTO sysPositionUserPageDTO){
        QueryWrapper<SysPositionUserVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("up.PROP_EXTENDID");
        queryWrapper.eq("p.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        queryWrapper.eq("u.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        queryWrapper.eq("up.PROP_TABLENAME", SysUserPropEnum.SYSORG.getValue());
        // 查询条件
        queryWrapper.like(StrUtil.isNotEmpty(sysPositionUserPageDTO.getParentId()),
                "p.TREE_PATH", sysPositionUserPageDTO.getParentId()+".");
        queryWrapper.like(StrUtil.isNotEmpty(sysPositionUserPageDTO.getUserName()),
                "u.USER_NAME", sysPositionUserPageDTO.getUserName());
        // 排序
        queryWrapper.orderByAsc(CollectionUtil.newArrayList(
                "p.TREE_LEVEL", "p.TREE_SORT", "o.TREE_LEVEL", "o.TREE_SORT", "up.PROP_SORT"));
        return sysPositionMapper.pageUserByWrapper(CommonPageRequest.constructPage(
                sysPositionUserPageDTO.getCurrent(), sysPositionUserPageDTO.getSize()), queryWrapper);
    }

    @Override
    public List<SysPositionUserVO> listUserByPositionId(String positionId){
        QueryWrapper<SysPositionUserVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("up.PROP_EXTENDID");
        queryWrapper.eq("p.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        queryWrapper.eq("u.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        queryWrapper.eq("up.PROP_TABLENAME", SysUserPropEnum.SYSORG.getValue());
        queryWrapper.eq("p.POSITION_ID", positionId);
        // 排序
        queryWrapper.orderByAsc(CollectionUtil.newArrayList(
                "p.TREE_LEVEL", "p.TREE_SORT", "o.TREE_LEVEL", "o.TREE_SORT", "up.PROP_SORT"));
        return sysPositionMapper.listUserByWrapper(queryWrapper);
    }

    @Override
    public List<SysPositionUserVO> listUserByPositionIds(List<String> positionIds){
        QueryWrapper<SysPositionUserVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("up.PROP_EXTENDID");
        queryWrapper.eq("p.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        queryWrapper.eq("u.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        queryWrapper.eq("up.PROP_TABLENAME", SysUserPropEnum.SYSORG.getValue());
        queryWrapper.in("p.POSITION_ID", positionIds);
        // 排序
        queryWrapper.orderByAsc(CollectionUtil.newArrayList(
                "p.TREE_LEVEL", "p.TREE_SORT", "o.TREE_LEVEL", "o.TREE_SORT", "up.PROP_SORT"));
        return sysPositionMapper.listUserByWrapper(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantUser(SysPositionGrantUserDTO grantUserDTO){
        // 删除旧值
        LambdaUpdateWrapper<SysUserProp> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.eq(SysUserProp::getPropTablename, SysUserPropEnum.SYSORG.getValue());
        deleteWrapper.eq(SysUserProp::getPropExtendid, grantUserDTO.getPositionId());
        deleteWrapper.set(SysUserProp::getPropExtendid, null);
        sysUserPropService.update(deleteWrapper);
        // 更新
        if(CollUtil.isNotEmpty(grantUserDTO.getPropIds())){
            LambdaUpdateWrapper<SysUserProp> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(SysUserProp::getPropId, grantUserDTO.getPropIds());
            updateWrapper.set(SysUserProp::getPropExtendid, grantUserDTO.getPositionId());
            sysUserPropService.update(updateWrapper);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revokeUser(CommonIdListDTO idListDTO){
        LambdaUpdateWrapper<SysUserProp> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.in(SysUserProp::getPropId, idListDTO.getId());
        deleteWrapper.set(SysUserProp::getPropExtendid, null);
        sysUserPropService.update(deleteWrapper);
    }

}
