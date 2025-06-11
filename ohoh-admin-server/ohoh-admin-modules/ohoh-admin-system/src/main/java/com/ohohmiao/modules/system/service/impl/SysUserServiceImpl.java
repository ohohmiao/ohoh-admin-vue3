package com.ohohmiao.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.listener.CommonDataChangeEventCenter;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.framework.common.util.PlatSmCryptoUtil;
import com.ohohmiao.framework.security.enums.AuthConstEnum;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.modules.system.enums.*;
import com.ohohmiao.modules.system.mapper.SysUserMapper;
import com.ohohmiao.modules.system.model.dto.*;
import com.ohohmiao.modules.system.model.entity.SysOrg;
import com.ohohmiao.modules.system.model.entity.SysRole;
import com.ohohmiao.modules.system.model.entity.SysUser;
import com.ohohmiao.modules.system.model.entity.SysUserProp;
import com.ohohmiao.modules.system.model.vo.*;
import com.ohohmiao.modules.system.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 系统用户Service实现
 *
 * @author ohohmiao
 * @date 2023-03-31 10:26
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysOrgService sysOrgService;

    @Resource
    private SysUserPropService sysUserPropService;

    @Resource
    private SysRolePropService sysRolePropService;

    @Resource
    private SysPositionService sysPositionService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private PlatSmCryptoUtil platSmCryptoUtil;

    @Resource
    private PlatRedisUtil platRedisUtil;


    @Override
    public List<SysUserVO> listCachedAllUsers(){
        // 尝试从缓存获取
        Object cachedAllUsers = platRedisUtil.getCacheObject(SysCacheKeyEnum.USER_ALL.getKey());
        if(ObjectUtil.isNotNull(cachedAllUsers)) {
            List cachedList = (List) cachedAllUsers;
            if(CollUtil.isNotEmpty(cachedList)){
                return (List<SysUserVO>) cachedList.stream().map(
                        m -> BeanUtil.toBean(m, SysUserVO.class)
                ).collect(Collectors.toList());
            }
        }
        // 从库中查找，并写入缓存
        QueryWrapper<SysUserVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("p.USER_ID");
        queryWrapper.isNotNull("o.ORG_ID");
        queryWrapper.eq("p.PROP_TABLENAME", SysUserPropEnum.SYSORG.getValue());
        queryWrapper.eq("u.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        // 排除掉超管
        queryWrapper.ne("u.USER_ACCOUNT", AuthConstEnum.SUPERADMIN_ACCOUNT.getValue());
        queryWrapper.orderByAsc(CollectionUtil.newArrayList("o.TREE_LEVEL", "o.TREE_SORT", "p.PROP_SORT"));
        List<SysUserVO> userList = sysUserMapper.listByWrapper(queryWrapper);
        if(CollUtil.isNotEmpty(userList)) {
            platRedisUtil.setCacheObject(SysCacheKeyEnum.USER_ALL.getKey(), userList,
                    SysCacheKeyEnum.USER_ALL.getTtl(), TimeUnit.SECONDS);
        }
        return userList;
    }

    @Override
    public List<SysRoleVO> listSysRoleByUserId(String userId){
        // 非公共角色
        Set<String> roleIds = sysRolePropService.listRoleIdsByTableNameAndRecordId(SysRolePropEnum.SYSUSER.getValue(), userId);
        List<SysRole> notPublicRoles = CollectionUtil.newArrayList();
        if(CollUtil.isNotEmpty(roleIds)){
            notPublicRoles = sysRoleService.listByIds(roleIds);
        }
        // 公共角色
        LambdaQueryWrapper<SysRole> listPublicRoleWrapper = new LambdaQueryWrapper<>();
        listPublicRoleWrapper.eq(SysRole::getOrgId, "0");
        listPublicRoleWrapper.eq(SysRole::getPublicroleFlag, CommonWhetherEnum.YES.getCode());
        List<SysRole> publicRoles = sysRoleService.list(listPublicRoleWrapper);
        if(CollUtil.isEmpty(notPublicRoles) && CollUtil.isEmpty(publicRoles)){
            return CollectionUtil.newArrayList();
        }
        // 合并=>组装角色的数据范围字段
        return Stream.of(notPublicRoles, publicRoles).flatMap(Collection::stream).map(r -> {
            SysRoleVO thizRole = new SysRoleVO();
            BeanUtil.copyProperties(r, thizRole);
            if(!"0".equals(r.getOrgId())){
                SysOrg userOrg = sysOrgService.getById(r.getOrgId());
                if(ObjectUtil.isNotNull(userOrg)){
                    thizRole.setOrgName(userOrg.getOrgName());
                }
            }
            Set<String> grantedOrgIds = sysRoleService.listDataScopesByRoleId(thizRole.getRoleId());
            thizRole.setDatascopeOrgIds(grantedOrgIds);
            return thizRole;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SysRoleVO> listAuthSysRoleByUserId(String userId){
        List<SysRoleVO> sysRoleList = this.listSysRoleByUserId(userId);
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            sysRoleList.removeIf(role -> !loginUser.getGrantedDataScopes().contains(role.getOrgId()));
        }
        return sysRoleList;
    }

    @Override
    public Page<SysUserVO> listAuthByPage(SysUserPageDTO sysUserPageDTO){
        QueryWrapper<SysUserVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("p.USER_ID");
        queryWrapper.isNotNull("o.ORG_ID");
        queryWrapper.eq("p.PROP_TABLENAME", SysUserPropEnum.SYSORG.getValue());
        queryWrapper.eq("u.DELETE_FLAG", CommonWhetherEnum.NO.getCode());

        queryWrapper.like(StrUtil.isNotEmpty(sysUserPageDTO.getOrgId()),
                "o.TREE_PATH", sysUserPageDTO.getOrgId()+".");
        queryWrapper.like(StrUtil.isNotEmpty(sysUserPageDTO.getUserAccount()),
                "u.USER_ACCOUNT", sysUserPageDTO.getUserAccount());
        queryWrapper.like(StrUtil.isNotEmpty(sysUserPageDTO.getUserName()),
                "u.USER_NAME", sysUserPageDTO.getUserName());
        queryWrapper.like(StrUtil.isNotEmpty(sysUserPageDTO.getUserMobile()),
                "u.USER_MOBILE", sysUserPageDTO.getUserMobile());
        queryWrapper.eq(ObjectUtil.isNotNull(sysUserPageDTO.getUserGender()),
                "u.USER_GENDER", sysUserPageDTO.getUserGender());
        queryWrapper.eq(ObjectUtil.isNotNull(sysUserPageDTO.getUserStatus()),
                "u.USER_STATUS", sysUserPageDTO.getUserStatus());
        // 排除掉超管
        queryWrapper.ne("u.USER_ACCOUNT", AuthConstEnum.SUPERADMIN_ACCOUNT.getValue());
        // 非超管，获取授权的组织机构列表
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            Set<String> grantedOrgIds = loginUser.getGrantedDataScopes();
            if(CollUtil.isEmpty(grantedOrgIds)){
                return new Page<>();
            }
            queryWrapper.in("o.ORG_ID", grantedOrgIds);
        }
        // 排序字段处理
        queryWrapper.orderByAsc(CollectionUtil.newArrayList("o.TREE_LEVEL", "o.TREE_SORT", "p.PROP_SORT"));
        return sysUserMapper.pageByWrapper(CommonPageRequest.constructPage(
                sysUserPageDTO.getCurrent(), sysUserPageDTO.getSize()), queryWrapper);
    }

    @Override
    public List<SysUserPropVO> listSysOrgByUserId(String userId){
        List<SysUserPropVO> props = sysUserPropService.listByUserIdAndTableName(userId, SysUserPropEnum.SYSORG.getValue());
        props.forEach(prop -> {
            SysOrg sysOrg = sysOrgService.getById(prop.getPropRecordid());
            if(ObjectUtil.isNotNull(sysOrg)){
                prop.setPropRecordname(sysOrg.getOrgName());
            }
        });
        return props;
    }

    @Override
    public List<String> listSysOrgIdsByUserId(String userId){
        List<SysUserPropVO> props = sysUserPropService.listByUserIdAndTableName(userId, SysUserPropEnum.SYSORG.getValue());
        return props.stream().map(SysUserPropVO::getPropRecordid).collect(Collectors.toList());
    }

    @Override
    public boolean isExistUserAccount(SysUserAddOrEditDTO sysUserAddOrEditDTO){
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUserAccount, sysUserAddOrEditDTO.getUserAccount());
        lambdaQueryWrapper.ne(StrUtil.isNotEmpty(sysUserAddOrEditDTO.getUserId()),
                SysUser::getUserId, sysUserAddOrEditDTO.getUserId());
        return this.count(lambdaQueryWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantRole(SysUserGrantRoleDTO grantRoleDTO){
        // 判断是否有指定用户所在机构的数据权限
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            Set<String> userOrgIds = sysUserPropService.listRecordIdsByUserIdAndTableName(
                    grantRoleDTO.getUserId(), SysUserPropEnum.SYSORG.getValue());
            if(!StpPCUtil.isAuthAnyDataScopes(userOrgIds)){
                throw new CommonException("抱歉，您没有用户所在机构的数据权限！");
            }
        }
        // 最终角色id集合
        List<String> targetRoleIds = grantRoleDTO.getRoleIdList();
        if(!loginUser.isSuperAdmin()){
            if(CollUtil.isNotEmpty(targetRoleIds)){
                // 过滤掉当前登录用户未被授权数据范围的机构角色
                targetRoleIds = sysRoleService.listByIds(targetRoleIds).stream()
                        .filter(role -> loginUser.getGrantedDataScopes().contains(role.getOrgId()))
                        .map(SysRole::getRoleId).collect(Collectors.toList());
            }
            // 加上历史授权的角色
            Set<String> hisGrantRoleIds = sysRolePropService.listRoleIdsByTableNameAndRecordId(
                    SysRolePropEnum.SYSUSER.getValue(), grantRoleDTO.getUserId());
            if(CollUtil.isNotEmpty(hisGrantRoleIds)){
                hisGrantRoleIds = sysRoleService.listByIds(hisGrantRoleIds).stream()
                        .filter(role -> !loginUser.getGrantedDataScopes().contains(role.getOrgId()))
                        .map(SysRole::getRoleId).collect(Collectors.toSet());
            }

            targetRoleIds = Stream.of(targetRoleIds, hisGrantRoleIds)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }else{
            if(CollUtil.isNotEmpty(targetRoleIds)){
                // 过滤掉公共角色
                targetRoleIds = sysRoleService.listByIds(targetRoleIds).stream()
                        .filter(role -> !(ObjectUtil.isNotNull(role.getPublicroleFlag()) &&
                                role.getPublicroleFlag() == CommonWhetherEnum.YES.getCode()))
                        .map(SysRole::getRoleId).collect(Collectors.toList());
            }
        }
        // 保存或更新
        sysRolePropService.saveOrUpdateMultiRoleProps(SysRolePropEnum.SYSUSER.getValue(), grantRoleDTO.getUserId(), targetRoleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserAddOrEditDTO sysUserAddOrEditDTO){
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            List<SysUserPropAddOrEditDTO> userOrgs = sysUserAddOrEditDTO.getOrgList();
            List<String> userOrgIds = userOrgs.stream().map(
                    SysUserPropAddOrEditDTO::getPropRecordid).collect(Collectors.toList());
            if(!StpPCUtil.isAuthDataScopes(userOrgIds)){
                throw new CommonException("抱歉，您没有用户所在机构的数据权限！");
            }
        }
        boolean isExistUserAccount = this.isExistUserAccount(sysUserAddOrEditDTO);
        if(isExistUserAccount){
            throw new CommonException("用户账号重复，请重新输入");
        }
        SysUser sysUser = BeanUtil.copyProperties(sysUserAddOrEditDTO, SysUser.class);
        sysUser.setUserPassword(platSmCryptoUtil.getSm3Hash(AuthConstEnum.DEFAULT_PASSWORD.getValue()));
        this.save(sysUser);
        // 保存用户所在机构属性
        List<SysUserProp> propList = sysUserAddOrEditDTO.getOrgList().stream().map(param -> {
            SysUserProp prop = new SysUserProp();
            param.setPropTablename(SysUserPropEnum.SYSORG.getValue());
            BeanUtil.copyProperties(param, prop);
            prop.setUserId(sysUser.getUserId());
            return prop;
        }).collect(Collectors.toList());
        sysUserPropService.saveOrUpdate(sysUser.getUserId(), SysUserPropEnum.SYSORG.getValue(), propList);

        CommonDataChangeEventCenter.doAddWithData(SysDataListenerEnum.USER.getName(), sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysUserAddOrEditDTO sysUserAddOrEditDTO){
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            List<SysUserPropAddOrEditDTO> userOrgs = sysUserAddOrEditDTO.getOrgList();
            List<String> userOrgIds = userOrgs.stream().map(
                    SysUserPropAddOrEditDTO::getPropRecordid).collect(Collectors.toList());
            if(!StpPCUtil.isAuthAnyDataScopes(userOrgIds)){
                throw new CommonException("抱歉，您没有用户所在机构的数据权限！");
            }
        }
        SysUser sysUser = this.getById(sysUserAddOrEditDTO.getUserId());
        if(ObjectUtil.isNull(sysUser)){
            throw new CommonException("该用户不存在！");
        }
        boolean isExistUserAccount = this.isExistUserAccount(sysUserAddOrEditDTO);
        if(isExistUserAccount){
            throw new CommonException("用户账号重复，请重新输入");
        }
        BeanUtil.copyProperties(sysUserAddOrEditDTO, sysUser);
        this.updateById(sysUser);
        // 更新用户所在机构属性
        List<SysUserProp> propList = sysUserAddOrEditDTO.getOrgList().stream().map(param -> {
            SysUserProp prop = new SysUserProp();
            param.setPropTablename(SysUserPropEnum.SYSORG.getValue());
            BeanUtil.copyProperties(param, prop);
            prop.setUserId(sysUser.getUserId());
            return prop;
        }).collect(Collectors.toList());
        sysUserPropService.saveOrUpdate(sysUser.getUserId(), SysUserPropEnum.SYSORG.getValue(), propList);

        CommonDataChangeEventCenter.doEditWithData(SysDataListenerEnum.USER.getName(), sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdDTO idDTO){
        String toDeleteSysUserId = idDTO.getId();
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            Set<String> toDeleteUserOrgIds = sysUserPropService.listRecordIdsByUserIdAndTableName(
                    toDeleteSysUserId, SysUserPropEnum.SYSORG.getValue());
            if(!StpPCUtil.isAuthDataScopes(toDeleteUserOrgIds)){
                throw new CommonException("抱歉，您没有用户所在机构的数据权限！");
            }
        }
        this.removeById(toDeleteSysUserId);
        //删除用户属性
        sysUserPropService.deleteByUserId(toDeleteSysUserId);
        //删除用户角色关系
        sysRolePropService.deleteByTableNameAndRecordId(SysRolePropEnum.SYSUSER.getValue(), toDeleteSysUserId);
        //TODO 删除用户所创建的群组

        CommonDataChangeEventCenter.doDeleteWithId(SysDataListenerEnum.USER.getName(), toDeleteSysUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(CommonIdDTO idDTO, int userStatus){
        SysUserStatusEnum userStatusEnum = SysUserStatusEnum.assemble(userStatus);
        if(ObjectUtil.isNull(userStatusEnum)){
            throw new CommonException(StrUtil.format("非法的用户状态：{}", userStatusEnum));
        }
        if(StrUtil.isNotEmpty(idDTO.getId())){
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            if(!loginUser.isSuperAdmin()){
                Set<String> sysUserOrgIds = sysUserPropService.listRecordIdsByUserIdAndTableName(
                        idDTO.getId(), SysUserPropEnum.SYSORG.getValue());
                if(!StpPCUtil.isAuthDataScopes(sysUserOrgIds)){
                    throw new CommonException("抱歉，您没有用户所在机构的数据权限！");
                }
            }
            UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(SysUser::getUserStatus, userStatus);
            updateWrapper.lambda().eq(SysUser::getUserId, idDTO.getId());
            updateWrapper.lambda().eq(SysUser::getUserStatus,
                    userStatus == SysUserStatusEnum.ENABLE.getStatus()?
                            SysUserStatusEnum.DISABLED.getStatus():
                            SysUserStatusEnum.ENABLE.getStatus());
            this.update(updateWrapper);

            if(userStatus == SysUserStatusEnum.DISABLED.getStatus()){
                CommonDataChangeEventCenter.doDeleteWithId(SysDataListenerEnum.USER.getName(), idDTO.getId());
            }else{
                CommonDataChangeEventCenter.doEditWithData(SysDataListenerEnum.USER.getName(), getById(idDTO.getId()));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(CommonIdDTO idDTO){
        String toUpdateSysUserId = idDTO.getId();
        if(StrUtil.isNotEmpty(toUpdateSysUserId)){
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            if(!loginUser.isSuperAdmin()){
                Set<String> sysUserOrgIds = sysUserPropService.listRecordIdsByUserIdAndTableName(
                        toUpdateSysUserId, SysUserPropEnum.SYSORG.getValue());
                if(!StpPCUtil.isAuthDataScopes(sysUserOrgIds)){
                    throw new CommonException("抱歉，您没有用户所在机构的数据权限！");
                }
            }
            UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(SysUser::getUserPassword,
                    platSmCryptoUtil.getSm3Hash(AuthConstEnum.DEFAULT_PASSWORD.getValue()));
            updateWrapper.lambda().eq(SysUser::getUserId, toUpdateSysUserId);
            this.update(updateWrapper);
        }
    }

    @Override
    public List<Tree<String>> getAuthOrgUserTree(){
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        Set<String> grantedOrgIds = loginUser.getGrantedDataScopes();
        //获取授权的组织机构列表
        List<SysOrgVO> grantedOrgs = sysOrgService.listCachedAllOrgs();
        if(!loginUser.isSuperAdmin()){
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
        // 获取授权的用户列表
        List<SysUserVO> grantedUsers = this.listCachedAllUsers();
        if(!loginUser.isSuperAdmin()){
            grantedUsers.removeIf(user -> !grantedOrgIds.contains(user.getOrgId()));
        }
        // 构造树
        // 部门节点
        List<TreeNode<String>> treeNodeList = grantedOrgs.stream().map(sysOrg -> {
            Map<String, Object> extraObj = new HashMap<>();
            extraObj.put("type", "org");
            extraObj.put("treePath", sysOrg.getTreePath());
            return new TreeNode<>(sysOrg.getOrgId(), sysOrg.getParentId(),
                    sysOrg.getOrgName(),
                    "a#"+sysOrg.getTreeSort())
                    .setExtra(extraObj);
        }).collect(Collectors.toList());
        // 人员节点
        treeNodeList.addAll(grantedUsers.stream().map(sysUser -> {
            Map<String, Object> extraObj = new HashMap<>();
            extraObj.put("type", "user");
            extraObj.put("extendValue", sysUser.getOrgPropid());
            extraObj.put("parentName", sysUser.getOrgName());
            extraObj.put("treePath", sysUser.getOrgPath()+sysUser.getUserId());
            return new TreeNode<>(sysUser.getOrgId()+"#"+sysUser.getUserId(),
                    sysUser.getOrgId(),
                    sysUser.getUserName(),
                    "b#"+sysUser.getUserSort()).setExtra(extraObj);
        }).collect(Collectors.toList()));
        return TreeUtil.build(treeNodeList, "0");
    }

    @Override
    public List<Tree<String>> getOrgUserTree(){
        List<SysOrgVO> allOrgs = sysOrgService.listCachedAllOrgs();
        List<SysUserVO> allUsers = this.listCachedAllUsers();
        // 构造树
        // 部门节点
        List<TreeNode<String>> treeNodeList = allOrgs.stream().map(sysOrg -> {
            Map<String, Object> extraObj = new HashMap<>();
            extraObj.put("type", "org");
            extraObj.put("treePath", sysOrg.getTreePath());
            return new TreeNode<>(sysOrg.getOrgId(), sysOrg.getParentId(),
                    sysOrg.getOrgName(),
                    "a#"+sysOrg.getTreeSort())
                    .setExtra(extraObj);
        }).collect(Collectors.toList());
        // 人员节点
        treeNodeList.addAll(allUsers.stream().map(sysUser -> {
            Map<String, Object> extraObj = new HashMap<>();
            extraObj.put("type", "user");
            extraObj.put("extendValue", sysUser.getOrgPropid());
            extraObj.put("parentName", sysUser.getOrgName());
            extraObj.put("treePath", sysUser.getOrgPath()+sysUser.getUserId());
            return new TreeNode<>(sysUser.getOrgId()+"#"+sysUser.getUserId(),
                    sysUser.getOrgId(),
                    sysUser.getUserName(),
                    "b#"+sysUser.getUserSort()).setExtra(extraObj);
        }).collect(Collectors.toList()));
        return TreeUtil.build(treeNodeList, "0");
    }

    @Override
    public List<SysUserVO> listAuthSysUsersByRoleId(String roleId){
        // 列出该角色被授权给的所有用户
        Set<String> ownUserIds = sysRolePropService.listRecordIdsByRoleIdAndTableName(
                roleId, SysRolePropEnum.SYSUSER.getValue());
        // 过滤数据级权限
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        List<SysUserVO> ownUserList = CollectionUtil.newArrayList();
        if(CollUtil.isNotEmpty(ownUserIds)){
            QueryWrapper<SysUserVO> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("p.USER_ID", ownUserIds);
            ownUserList = sysUserMapper.listByWrapper(queryWrapper);
        }
        ownUserList.removeIf(user -> {
            boolean isAuthDataScope = true;
            if(!loginUser.isSuperAdmin()){
                isAuthDataScope = loginUser.getGrantedDataScopes().contains(user.getOrgId());
            }
            return !isAuthDataScope;
        });
        return ownUserList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantRoleToUser(SysRoleGrantUserDTO grantUserDTO){
        // 判断是否有指定角色所在机构的数据权限
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        SysRole sysRole = sysRoleService.getById(grantUserDTO.getRoleId());
        if(!loginUser.isSuperAdmin()){
            if(!StpPCUtil.isAuthDataScope(sysRole.getOrgId())){
                throw new CommonException("抱歉，您没有角色所在机构的数据权限！");
            }
        }
        if(ObjectUtil.isNotNull(sysRole.getPublicroleFlag()) &&
           sysRole.getPublicroleFlag() == CommonWhetherEnum.YES.getCode()){
            throw new CommonException("操作失败，公共角色禁止授权用户！");
        }
        // 最终用户id集合
        List<String> targetUserIds = grantUserDTO.getUserIdList();
        if(!loginUser.isSuperAdmin()) {
            List<SysUserVO> targetUserList = CollectionUtil.newArrayList();
            // 过滤掉当前登录用户未被授权的数据
            if(CollUtil.isNotEmpty(targetUserIds)){
                QueryWrapper<SysUserVO> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("u.USER_ID", targetUserIds);
                targetUserList = sysUserMapper.listByWrapper(queryWrapper);
                targetUserList.removeIf(user -> !loginUser.getGrantedDataScopes().contains(user.getOrgId()));
            }
            // 加上当前登录用户未被授权机构的用户数据
            Set<String> hisOwnUserIds = sysRolePropService.listRecordIdsByRoleIdAndTableName(
                    grantUserDTO.getRoleId(), SysRolePropEnum.SYSUSER.getValue());
            List<SysUserVO> hisOwnUserList = CollectionUtil.newArrayList();
            if(CollUtil.isNotEmpty(hisOwnUserIds)){
                QueryWrapper<SysUserVO> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("u.USER_ID", hisOwnUserIds);
                hisOwnUserList = sysUserMapper.listByWrapper(queryWrapper);
                hisOwnUserList.removeIf(user -> loginUser.getGrantedDataScopes().contains(user.getOrgId()));
            }
            targetUserIds = Stream.of(targetUserList, hisOwnUserList)
                    .flatMap(Collection::stream)
                    .map(SysUserVO::getUserId).collect(Collectors.toList());
        }
        // 保存或更新
        sysRolePropService.saveOrUpdate(grantUserDTO.getRoleId(), SysRolePropEnum.SYSUSER.getValue(), targetUserIds);
    }

    @Override
    public Set<String> listDataScopesByUserId(String userId){
        return sysUserPropService.listRecordIdsByUserIdAndTableName(userId, SysUserPropEnum.DATASCOPE.getValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantDataScope(SysUserGrantDataDTO grantDataDTO){
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            List<String> userOrgIds = listSysOrgIdsByUserId(grantDataDTO.getUserId());
            if(!StpPCUtil.isAuthAnyDataScopes(userOrgIds)){
                throw new CommonException("抱歉，您没有该用户所在机构的数据权限！");
            }
        }
        // 最终数据范围id集合
        List<String> targetOrgIds = grantDataDTO.getDatascopeOrgIds();
        if(!loginUser.isSuperAdmin()){
            // 过滤掉当前登录用户未被授权的数据
            targetOrgIds.removeIf(orgId -> !loginUser.getGrantedDataScopes().contains(orgId));
            // 加上历史授权数据范围
            Set<String> hisGrantOrgIds = listDataScopesByUserId(grantDataDTO.getUserId());
            hisGrantOrgIds.removeIf(orgId -> loginUser.getGrantedDataScopes().contains(orgId));
            targetOrgIds.addAll(hisGrantOrgIds);
        }
        // 保存或更新
        List<SysUserProp> props = targetOrgIds.stream().map(orgId -> {
            SysUserProp prop = new SysUserProp();
            prop.setPropRecordid(orgId);
            return prop;
        }).collect(Collectors.toList());
        sysUserPropService.saveOrUpdate(grantDataDTO.getUserId(), SysUserPropEnum.DATASCOPE.getValue(), props);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGrantDataScope4LoginUser(List<String> addDatascopeOrgIds){
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(loginUser.isSuperAdmin()){
            return;
        }
        Set<String> hisUserDatasScopes = sysUserPropService.listRecordIdsByUserIdAndTableName(
                loginUser.getUserId(), SysUserPropEnum.DATASCOPE.getValue());
        hisUserDatasScopes.addAll(addDatascopeOrgIds);
        List<SysUserProp> targetUserProps = hisUserDatasScopes.stream().map(dataScope -> {
            SysUserProp prop = new SysUserProp();
            prop.setPropRecordid(dataScope);
            return prop;
        }).collect(Collectors.toList());
        sysUserPropService.saveOrUpdate(loginUser.getUserId(), SysUserPropEnum.DATASCOPE.getValue(), targetUserProps);
    }

    @Override
    public List<Tree<String>> getPositionUserTree(){
        // 获取岗位列表
        List<SysPositionVO> allPositions = sysPositionService.listCachedAllPositions();
        // 获取用户列表
        List<String> allPositionIds = allPositions.stream().map(p -> p.getPositionId()).collect(Collectors.toList());
        List<SysPositionUserVO> allUsers = CollectionUtil.newArrayList();
        if(CollUtil.isNotEmpty(allPositionIds)){
            allUsers = sysPositionService.listUserByPositionIds(allPositionIds);
        }
        // 构造树
        // 岗位节点
        List<TreeNode<String>> treeNodeList = allPositions.stream().map(position -> {
            Map<String, Object> extraObj = new HashMap<>();
            extraObj.put("type", "position");
            extraObj.put("treePath", position.getTreePath());
            return new TreeNode<>(position.getPositionId(), position.getParentId(),
                    position.getPositionName(), "a#"+position.getTreeSort())
                    .setExtra(extraObj);
        }).collect(Collectors.toList());
        // 人员节点
        treeNodeList.addAll(allUsers.stream().map(sysUser -> {
            Map<String, Object> extraObj = new HashMap<>();
            extraObj.put("type", "user");
            extraObj.put("extendValue", sysUser.getPropId());
            extraObj.put("parentName", sysUser.getPositionName());
            extraObj.put("treePath", sysUser.getPositionPath()+sysUser.getUserId());
            return new TreeNode<>(sysUser.getPositionId()+"#"+sysUser.getUserId(), sysUser.getPositionId(),
                    sysUser.getUserName(),
                    "b#"+sysUser.getUserId())
                    .setExtra(extraObj);
        }).collect(Collectors.toList()));
        return TreeUtil.build(treeNodeList, "0");
    }

}
