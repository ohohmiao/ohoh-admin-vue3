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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.listener.CommonDataChangeEventCenter;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.impl.CommonTreeServiceImpl;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.modules.system.enums.SysCacheKeyEnum;
import com.ohohmiao.modules.system.enums.SysDataListenerEnum;
import com.ohohmiao.modules.system.mapper.SysOrgMapper;
import com.ohohmiao.modules.system.model.dto.SysOrgAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysOrgPageDTO;
import com.ohohmiao.modules.system.model.entity.SysOrg;
import com.ohohmiao.modules.system.model.vo.SysOrgVO;
import com.ohohmiao.modules.system.service.SysOrgService;
import com.ohohmiao.modules.system.enums.SysRolePropEnum;
import com.ohohmiao.modules.system.service.SysRolePropService;
import com.ohohmiao.modules.system.service.SysRoleService;
import com.ohohmiao.modules.system.enums.SysUserPropEnum;
import com.ohohmiao.modules.system.service.SysUserPropService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 组织机构Service实现类
 *
 * @author ohohmiao
 * @date 2023-04-06 14:22
 */
@Service
public class SysOrgServiceImpl extends CommonTreeServiceImpl<SysOrgMapper, SysOrg> implements SysOrgService {

    @Resource
    private SysOrgMapper sysOrgMapper;

    @Resource
    private PlatRedisUtil platRedisUtil;

    @Resource
    private SysRolePropService sysRolePropService;

    @Resource
    private SysUserPropService sysUserPropService;

    @Lazy
    @Resource
    private SysRoleService sysRoleService;

    @Override
    public List<SysOrgVO> listCachedAllOrgs(){
        // 尝试从缓存获取
        Object cachedAllOrgs = platRedisUtil.getCacheObject(SysCacheKeyEnum.ORG_ALL.getKey());
        if(ObjectUtil.isNull(cachedAllOrgs)){
            List cachedList = (List) cachedAllOrgs;
            if(CollectionUtil.isNotEmpty(cachedList)){
                return (List<SysOrgVO>) cachedList.stream().map(
                        m -> BeanUtil.toBean(m, SysOrgVO.class)
                ).collect(Collectors.toList());
            }
        }
        // 从库中查找，并写入缓存
        List<SysOrg> orgList = this.list(new LambdaQueryWrapper<SysOrg>()
                .orderByAsc(CollectionUtil.newArrayList(SysOrg::getTreeLevel, SysOrg::getTreeSort)));
        List<SysOrgVO> resultList = orgList.stream().map(sysOrg -> {
            SysOrgVO result = new SysOrgVO();
            BeanUtil.copyProperties(sysOrg, result);
            return result;
        }).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(resultList)) {
            platRedisUtil.setCacheObject(SysCacheKeyEnum.ORG_ALL.getKey(), resultList,
                    SysCacheKeyEnum.ORG_ALL.getTtl(), TimeUnit.SECONDS);
        }
        return resultList;
    }

    @Override
    public List<SysOrgVO> listCachedChildrenByOrgId(String orgId, boolean isIncludeSelf){
        SysOrg thizOrg = this.getById(orgId);
        if(ObjectUtil.isNull(thizOrg)){
            return CollectionUtil.newArrayList();
        }
        List<SysOrgVO> cachedChildOrgs = this.listCachedAllOrgs();
        cachedChildOrgs.removeIf(org -> !(Arrays.asList(org.getTreePath().split("\\.")).contains(orgId) &&
                        org.getTreeLevel().compareTo(thizOrg.getTreeLevel()) >= 0));
        // 不包含父节点情况，移除自身
        if(!isIncludeSelf){
            cachedChildOrgs.removeIf(org -> org.getOrgId().equals(orgId));
        }
        return cachedChildOrgs;
    }

    @Override
    public List<Tree<String>> getAuthTreeData(){
        // 查询组织机构列表
        List<SysOrgVO> grantedOrgs = this.listCachedAllOrgs();
        // 非超管，获取授权的组织机构列表
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            // 筛选授权节点
            Set<String> grantedOrgIds = loginUser.getGrantedDataScopes();
            if(CollUtil.isEmpty(grantedOrgIds)){
                return CollectionUtil.newArrayList();
            }
            List<SysOrgVO> thizOrgs = grantedOrgs.stream().filter(
                    o -> grantedOrgIds.contains(o.getOrgId())).collect(Collectors.toList());
            // 筛选授权节点及其父辈节点
            grantedOrgs.removeIf(org -> {
                boolean isGranted = false;
                for(SysOrgVO thizOrg: thizOrgs){
                    if(Arrays.asList(thizOrg.getTreePath().split("\\.")).contains(org.getOrgId())){
                        isGranted = true;
                        break;
                    }
                }
                return !isGranted;
            });
        }
        // 构造hutool树
        List<TreeNode<String>> treeNodeList = grantedOrgs.stream().map(sysOrg ->
            new TreeNode<>(sysOrg.getOrgId(), sysOrg.getParentId(),
                    sysOrg.getOrgName(),
                    sysOrg.getTreeSort())
                    .setExtra(JSON.parseObject(JSON.toJSONString(sysOrg),
                        new TypeReference<Map<String, Object>>() {}))
        ).collect(Collectors.toList());
        return TreeUtil.build(treeNodeList, "0");
    }

    @Override
    public List<Tree<String>> getTreeData(){
        // 查询组织机构列表
        List<SysOrgVO> allOrgs = this.listCachedAllOrgs();
        // 构造hutool树
        List<TreeNode<String>> treeNodeList = allOrgs.stream().map(sysOrg ->
            new TreeNode<>(sysOrg.getOrgId(), sysOrg.getParentId(),
                    sysOrg.getOrgName(),
                    sysOrg.getTreeSort())
                    .setExtra(JSON.parseObject(JSON.toJSONString(sysOrg),
                        new TypeReference<Map<String, Object>>() {}))
        ).collect(Collectors.toList());
        return TreeUtil.build(treeNodeList, "0");
    }

    @Override
    public Page<SysOrg> listAuthByPage(SysOrgPageDTO sysOrgPageDTO){
        QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotEmpty(sysOrgPageDTO.getParentId())){
            queryWrapper.lambda().like(SysOrg::getTreePath, sysOrgPageDTO.getParentId());
        }
        if(StrUtil.isNotEmpty(sysOrgPageDTO.getOrgName())){
            queryWrapper.lambda().like(SysOrg::getOrgName, sysOrgPageDTO.getOrgName());
        }
        // 非超管，获取授权的组织机构列表
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            Set<String> grantedOrgIds = loginUser.getGrantedDataScopes();
            if(CollUtil.isEmpty(grantedOrgIds)){
                return new Page<>();
            }
            queryWrapper.lambda().in(SysOrg::getOrgId, grantedOrgIds);
        }
        // 排序字段处理
        queryWrapper.lambda().orderByAsc(CollectionUtil.newArrayList(SysOrg::getTreeLevel, SysOrg::getTreeSort));

        return this.page(CommonPageRequest.constructPage(
                sysOrgPageDTO.getCurrent(), sysOrgPageDTO.getSize()), queryWrapper);
    }

    @Override
    public boolean isExistOrgNameInSameLevel(SysOrgAddOrEditDTO sysOrgAddOrEditDTO){
        LambdaQueryWrapper<SysOrg> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysOrg::getParentId, sysOrgAddOrEditDTO.getParentId());
        lambdaQueryWrapper.eq(SysOrg::getOrgName, sysOrgAddOrEditDTO.getOrgName());
        if(StrUtil.isNotEmpty(sysOrgAddOrEditDTO.getOrgId())){
            lambdaQueryWrapper.ne(SysOrg::getOrgId, sysOrgAddOrEditDTO.getOrgId());
        }
        return this.count(lambdaQueryWrapper) > 0;
    }

    @Override
    public boolean isExistOrgCode(SysOrgAddOrEditDTO sysOrgAddOrEditDTO){
        LambdaQueryWrapper<SysOrg> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysOrg::getOrgCode, sysOrgAddOrEditDTO.getOrgCode());
        if(StrUtil.isNotEmpty(sysOrgAddOrEditDTO.getOrgId())){
            lambdaQueryWrapper.ne(SysOrg::getOrgId, sysOrgAddOrEditDTO.getOrgId());
        }
        return this.count(lambdaQueryWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysOrgAddOrEditDTO sysOrgAddOrEditDTO){
        // 新增的数据级权限控制
        if("0".equals(sysOrgAddOrEditDTO.getParentId())){
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            if(!loginUser.isSuperAdmin()){
                throw new CommonException("仅管理员能新增一级组织机构！");
            }
        }else{
            if(!StpPCUtil.isAuthDataScope(sysOrgAddOrEditDTO.getParentId())){
                throw new CommonException("抱歉，您没有新增该层级组织机构的权限！");
            }
        }
        if(StrUtil.isNotEmpty(sysOrgAddOrEditDTO.getOrgCode())){
            boolean isExistOrgCode = this.isExistOrgCode(sysOrgAddOrEditDTO);
            if(isExistOrgCode){
                throw new CommonException("组织机构编码重复，请重新输入！");
            }
        }
        boolean isExistOrgName = this.isExistOrgNameInSameLevel(sysOrgAddOrEditDTO);
        if(isExistOrgName){
            throw new CommonException("存在重名的同级组织机构，请重新输入！");
        }
        // 保存组织数据
        SysOrg sysOrg = BeanUtil.copyProperties(sysOrgAddOrEditDTO, SysOrg.class);
        this.saveTreeData(sysOrg);

        CommonDataChangeEventCenter.doAddWithData(SysDataListenerEnum.ORG.getName(), sysOrg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysOrgAddOrEditDTO sysOrgAddOrEditDTO){
        if(!StpPCUtil.isAuthDataScope(sysOrgAddOrEditDTO.getOrgId())){
            throw new CommonException("抱歉，您没有编辑该组织机构的权限！");
        }
        SysOrg sysOrg = this.getById(sysOrgAddOrEditDTO.getOrgId());
        if(ObjectUtil.isNull(sysOrg)){
            throw new CommonException("该组织机构不存在！");
        }
        if(StrUtil.isNotEmpty(sysOrgAddOrEditDTO.getOrgCode())){
            boolean isExistOrgCode = this.isExistOrgCode(sysOrgAddOrEditDTO);
            if(isExistOrgCode){
                throw new CommonException("组织机构编码重复，请重新输入");
            }
        }
        boolean isExistOrgName = this.isExistOrgNameInSameLevel(sysOrgAddOrEditDTO);
        if(isExistOrgName){
            throw new CommonException("同级的组织机构名称重复，请重新输入！");
        }
        // 更新组织数据
        String oldTreePath = sysOrg.getTreePath();
        Integer oldTreeLevel = sysOrg.getTreeLevel();
        BeanUtil.copyProperties(sysOrgAddOrEditDTO, sysOrg);
        this.updateTreeData(sysOrg, oldTreePath, oldTreeLevel);

        CommonDataChangeEventCenter.doEditWithData(SysDataListenerEnum.ORG.getName(), sysOrg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdDTO idDTO){
        String toDeleteSysOrgId = idDTO.getId();
        long thizChildCount = this.countByParentId(toDeleteSysOrgId);
        if(thizChildCount > 0){
            throw new CommonException("存在孩子节点，无法删除！");
        }
        long thizUserCount = sysUserPropService.countByTableNameAndRecordId(
                SysUserPropEnum.SYSORG.getValue(), toDeleteSysOrgId);
        if(thizUserCount > 0){
            throw new CommonException("存在用户数据，无法删除！");
        }
        long thizRoleCount = sysRoleService.countByOrgId(toDeleteSysOrgId);
        if(thizRoleCount > 0){
            throw new CommonException("存在角色数据，无法删除！");
        }
        //删除
        this.removeById(toDeleteSysOrgId);
        //删除关联属性
        sysRolePropService.deleteByTableNameAndRecordId(SysRolePropEnum.CUSTOM_DATASCOPE.getValue(), toDeleteSysOrgId);
        sysUserPropService.deleteByTableNameAndRecordId(SysUserPropEnum.DATASCOPE.getValue(), toDeleteSysOrgId);

        CommonDataChangeEventCenter.doDeleteWithId(SysDataListenerEnum.ORG.getName(), toDeleteSysOrgId);
    }

    @Override
    public List<SysOrgVO> listByOrgIds(List<String> orgIds){
        return sysOrgMapper.selectBatchIds(orgIds).stream().map(org -> {
            SysOrgVO sysOrgVO = new SysOrgVO();
            BeanUtil.copyProperties(org, sysOrgVO);
            return sysOrgVO;
        }).collect(Collectors.toList());
    }

}
