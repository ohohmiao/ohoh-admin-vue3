package com.ohohmiao.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.listener.CommonDataChangeEventCenter;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.modules.system.enums.SysDataListenerEnum;
import com.ohohmiao.modules.system.model.entity.SysRole;
import com.ohohmiao.modules.system.mapper.SysRoleMapper;
import com.ohohmiao.modules.system.model.dto.SysRoleAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysRoleGrantDataDTO;
import com.ohohmiao.modules.system.model.dto.SysRoleGrantResDTO;
import com.ohohmiao.modules.system.model.dto.SysRolePageDTO;
import com.ohohmiao.modules.system.service.SysRolePropService;
import com.ohohmiao.modules.system.service.SysRoleService;
import com.ohohmiao.modules.system.model.vo.SysOrgVO;
import com.ohohmiao.modules.system.service.SysOrgService;
import com.ohohmiao.modules.system.enums.SysDataScopeTypeEnum;
import com.ohohmiao.modules.system.enums.SysRolePropEnum;
import com.ohohmiao.modules.system.model.vo.SysRoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统角色Service实现类
 *
 * @author ohohmiao
 * @date 2023-04-06 14:31
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRolePropService sysRolePropService;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysOrgService sysOrgService;

    @Override
    public Set<String> listDataScopesByRoleId(String roleId){
        Set<String> grantedOrgIds = CollectionUtil.newHashSet();

        SysRole sysRole = this.getById(roleId);
        if(ObjectUtil.isNull(sysRole)){
            return grantedOrgIds;
        }
        if(ObjectUtil.isNull(sysRole.getDatascopeType())){
            return grantedOrgIds;
        }

        SysDataScopeTypeEnum typeEnum = SysDataScopeTypeEnum.assemble(sysRole.getDatascopeType());
        if(ObjectUtil.isNull(typeEnum)){
            throw new CommonException(StrUtil.format("非法的数据范围定义：{}", sysRole.getDatascopeType()));
        }
        //仅机构角色
        switch (typeEnum) {
        case SELF:
            //仅本人数据权限
            break;
        case ALL:
            //全部机构
            List<SysOrgVO> allOrgs = sysOrgService.listCachedAllOrgs();
            List<String> allOrgIds = allOrgs.stream().map(SysOrgVO::getOrgId).collect(Collectors.toList());
            grantedOrgIds.addAll(allOrgIds);
            break;
        case ROLEORG:
            //角色机构
            grantedOrgIds.add(sysRole.getOrgId());
            break;
        case ROLEORGANDCHILD:
            //角色机构及下属
            List<SysOrgVO> thizRoleOrgAndChildList =
                    sysOrgService.listCachedChildrenByOrgId(sysRole.getOrgId(), true);
            if(CollUtil.isNotEmpty(thizRoleOrgAndChildList)){
                List<String> thizRoleOrgAndChildIdList =
                        thizRoleOrgAndChildList.stream().map(SysOrgVO::getOrgId)
                                .collect(Collectors.toList());
                grantedOrgIds.addAll(thizRoleOrgAndChildIdList);
            }
            break;
        case CUSTOM:
            //自定义数据权限
            Set<String> thizCustomOrgIds =
                    sysRolePropService.listRecordIdsByRoleIdAndTableName(
                            sysRole.getRoleId(), SysRolePropEnum.CUSTOM_DATASCOPE.getValue());
            grantedOrgIds.addAll(thizCustomOrgIds);
            break;
        default:
        }

        return grantedOrgIds;
    }

    @Override
    public Page<SysRoleVO> listAuthByPage(SysRolePageDTO sysRolePageDTO){
        QueryWrapper<SysRoleVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("r.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        queryWrapper.like(StrUtil.isNotEmpty(sysRolePageDTO.getOrgId()),
                "o.TREE_PATH", sysRolePageDTO.getOrgId()+".");
        queryWrapper.like(StrUtil.isNotEmpty(sysRolePageDTO.getRoleName()),
                "r.ROLE_NAME", sysRolePageDTO.getRoleName());

        // 非超管，获取授权的组织机构列表
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            Set<String> grantedOrgIds = loginUser.getGrantedDataScopes();
            if(CollUtil.isEmpty(grantedOrgIds)){
                return new Page<>();
            }
            queryWrapper.in("r.ORG_ID", grantedOrgIds);
        }

        // 排序字段处理
        queryWrapper.orderByAsc(CollectionUtil.newArrayList("o.TREE_LEVEL", "o.TREE_SORT", "r.ROLE_SORT"));

        Page<SysRoleVO> resultPage = sysRoleMapper.pageByWrapper(CommonPageRequest.constructPage(
                sysRolePageDTO.getCurrent(), sysRolePageDTO.getSize()), queryWrapper);
        resultPage.getRecords().forEach(result -> {
            if(ObjectUtil.isNotNull(result.getDatascopeType()) &&
                    result.getDatascopeType() == SysDataScopeTypeEnum.CUSTOM.getType()){
                Set<String> grantedOrgIds = sysRolePropService.listRecordIdsByRoleIdAndTableName(
                        result.getRoleId(), SysRolePropEnum.CUSTOM_DATASCOPE.getValue());
                result.setDatascopeOrgIds(CollUtil.newHashSet(grantedOrgIds));
            }
        });
        return resultPage;
    }

    @Override
    public boolean isExistRoleName(SysRoleAddOrEditDTO sysRoleAddOrEditDTO){
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRole::getRoleName, sysRoleAddOrEditDTO.getRoleName());
        if(StrUtil.isNotEmpty(sysRoleAddOrEditDTO.getRoleId())){
            lambdaQueryWrapper.ne(SysRole::getRoleId, sysRoleAddOrEditDTO.getRoleId());
        }
        return this.count(lambdaQueryWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysRoleAddOrEditDTO sysRoleAddOrEditDTO){
        // 新增的数据级权限控制
        if("0".equals(sysRoleAddOrEditDTO.getOrgId())){
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            if(!loginUser.isSuperAdmin()){
                throw new CommonException("仅管理员能配置全局的角色！");
            }
        }else{
            if(!StpPCUtil.isAuthDataScope(sysRoleAddOrEditDTO.getOrgId())){
                throw new CommonException("抱歉，您没有新增该组织角色的权限！");
            }
        }

        boolean isExistRoleName = this.isExistRoleName(sysRoleAddOrEditDTO);
        if(isExistRoleName){
            throw new CommonException("角色名称重复，请重新输入");
        }

        SysRole sysRole = BeanUtil.copyProperties(sysRoleAddOrEditDTO, SysRole.class);
        Integer maxSort = sysRoleMapper.getMaxSortByOrgId(sysRole.getOrgId());
        sysRole.setRoleSort(ObjectUtil.isNull(maxSort)? 1: maxSort + 1);
        this.save(sysRole);

        CommonDataChangeEventCenter.doAddWithData(SysDataListenerEnum.ROLE.getName(), sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysRoleAddOrEditDTO sysRoleAddOrEditDTO){
        // 编辑的数据级权限控制
        if("0".equals(sysRoleAddOrEditDTO.getOrgId())){
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            if(!loginUser.isSuperAdmin()){
                throw new CommonException("仅管理员能配置全局的角色！");
            }
        }else{
            if(!StpPCUtil.isAuthDataScope(sysRoleAddOrEditDTO.getOrgId())){
                throw new CommonException("抱歉，您没有编辑该组织机构的权限！");
            }
        }

        SysRole sysRole = this.getById(sysRoleAddOrEditDTO.getRoleId());
        if(ObjectUtil.isNull(sysRole)){
            throw new CommonException("该角色不存在！");
        }
        boolean isExistRoleName = this.isExistRoleName(sysRoleAddOrEditDTO);
        if(isExistRoleName){
            throw new CommonException("角色名称重复，请重新输入");
        }

        // 非公共角色 => 公共角色情况，清空角色用户关系
        if(ObjectUtil.isNotNull(sysRoleAddOrEditDTO.getPublicroleFlag()) &&
           sysRoleAddOrEditDTO.getPublicroleFlag() == CommonWhetherEnum.YES.getCode()){
            sysRolePropService.deleteByRoleIdAndTableName(sysRoleAddOrEditDTO.getRoleId(), SysRolePropEnum.SYSUSER.getValue());
        }
        BeanUtil.copyProperties(sysRoleAddOrEditDTO, sysRole);
        this.updateById(sysRole);

        CommonDataChangeEventCenter.doEditWithData(SysDataListenerEnum.ROLE.getName(), sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdDTO idDTO){
        String toDeleteRoleId = idDTO.getId();
        SysRole sysRole = this.getById(toDeleteRoleId);
        if(!StpPCUtil.isAuthDataScope(sysRole.getOrgId())){
            throw new CommonException("抱歉，您没有角色所在机构的数据权限！");
        }

        // 删除角色
        this.removeById(toDeleteRoleId);
        // 删除角色属性
        sysRolePropService.deleteByRoleId(toDeleteRoleId);

        CommonDataChangeEventCenter.doDeleteWithId(SysDataListenerEnum.ROLE.getName(), toDeleteRoleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantDataScope(SysRoleGrantDataDTO sysRoleGrantDataDTO){
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if("0".equals(sysRoleGrantDataDTO.getOrgId())){
            if(!loginUser.isSuperAdmin()){
                throw new CommonException("仅管理员能配置全局的角色！");
            }
        }else{
            if(!StpPCUtil.isAuthDataScope(sysRoleGrantDataDTO.getOrgId())){
                throw new CommonException("抱歉，您没有该组织角色的数据权限！");
            }
        }

        if(sysRoleGrantDataDTO.getDatascopeType() == SysDataScopeTypeEnum.CUSTOM.getType()){
            // 最终数据范围id集合
            List<String> targetOrgIds = sysRoleGrantDataDTO.getDatascopeOrgIds();
            if(!loginUser.isSuperAdmin()){
                // 仅保留当前登录用户已授权的数据范围
                targetOrgIds.removeIf(orgId -> !loginUser.getGrantedDataScopes().contains(orgId));
            }
            sysRolePropService.saveOrUpdate(
                    sysRoleGrantDataDTO.getRoleId(),
                    SysRolePropEnum.CUSTOM_DATASCOPE.getValue(),
                    targetOrgIds);
        }else{
            sysRolePropService.deleteByRoleIdAndTableName(
                    sysRoleGrantDataDTO.getRoleId(),
                    SysRolePropEnum.CUSTOM_DATASCOPE.getValue());
        }

        UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(SysRole::getRoleId, sysRoleGrantDataDTO.getRoleId());
        updateWrapper.lambda().set(SysRole::getOrgId, sysRoleGrantDataDTO.getOrgId());
        updateWrapper.lambda().set(SysRole::getDatascopeType, sysRoleGrantDataDTO.getDatascopeType());
        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantRes(SysRoleGrantResDTO grantResDTO){
        // 判断是否有角色所在机构的数据权限
        SysRole sysRole = getById(grantResDTO.getRoleId());
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if("0".equals(sysRole.getOrgId())){
            if(!loginUser.isSuperAdmin()){
                throw new CommonException("仅管理员能配置全局的角色！");
            }
        }else{
            if(!loginUser.isSuperAdmin()){
                if(!StpPCUtil.isAuthDataScope(sysRole.getOrgId())){
                    throw new CommonException("抱歉，您没有角色所在机构的数据权限！");
                }
            }
        }
        // 最终资源id集合
        List<String> targetResIds = grantResDTO.getResIdList();
        if(!loginUser.isSuperAdmin()){
            // 仅保留当前登录用户已授权的资源
            targetResIds.removeIf(resId -> !loginUser.getGrantedResIds().contains(resId));
            // 加上历史授权的资源
            Set<String> hisResIds = sysRolePropService.listRecordIdsByRoleIdAndTableName(
                    grantResDTO.getRoleId(), SysRolePropEnum.SYSRES.getValue());
            hisResIds.removeIf(resId -> loginUser.getGrantedResIds().contains(resId));

            targetResIds.addAll(hisResIds);
        }
        // 保存或更新
        sysRolePropService.saveOrUpdate(
                    grantResDTO.getRoleId(),
                    SysRolePropEnum.SYSRES.getValue(),
                    targetResIds);
    }

    @Override
    public Set<String> listGrantedResByRoleId(String roleId){
        return sysRolePropService.listRecordIdsByRoleIdAndTableName(roleId, SysRolePropEnum.SYSRES.getValue());
    }

    @Override
    public Long countByOrgId(String orgId){
        LambdaQueryWrapper<SysRole> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(SysRole::getOrgId, orgId);
        return this.count(countWrapper);
    }

}
