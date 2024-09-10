package com.ohohmiao.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.impl.CommonTreeServiceImpl;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.modules.system.enums.SysMenuTypeEnum;
import com.ohohmiao.modules.system.enums.SysResTypeEnum;
import com.ohohmiao.modules.system.mapper.SysResMapper;
import com.ohohmiao.modules.system.model.dto.SysResAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysResQueryDTO;
import com.ohohmiao.modules.system.model.entity.SysRes;
import com.ohohmiao.modules.system.model.vo.SysButtonVO;
import com.ohohmiao.modules.system.service.SysResService;
import com.ohohmiao.modules.system.service.SysResUrlService;
import com.ohohmiao.modules.system.enums.SysRolePropEnum;
import com.ohohmiao.modules.system.service.SysRolePropService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统资源Service实现类
 *
 * @author ohohmiao
 * @date 2023-04-04 11:24
 */
@Service
public class SysResServiceImpl extends CommonTreeServiceImpl<SysResMapper, SysRes> implements SysResService {

    @Resource
    private SysResMapper sysResMapper;

    @Resource
    private SysRolePropService sysRolePropService;

    @Resource
    private SysResUrlService sysResUrlService;

    @Override
    public List<Tree<String>> listAuthMenu(){
        // 查询菜单列表
        LambdaQueryWrapper<SysRes> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRes::getResType, SysResTypeEnum.MENU.getType());
        lambdaQueryWrapper.orderByAsc(CollectionUtil.newArrayList(SysRes::getTreeLevel, SysRes::getTreeSort));
        List<SysRes> grantedSysMenus = this.list(lambdaQueryWrapper);
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            // 非超管，获取授权的菜单列表
            // 筛选授权节点
            Set<String> grantedResIds = loginUser.getGrantedResIds();
            if(CollUtil.isEmpty(grantedResIds)){
                return CollectionUtil.newArrayList();
            }
            List<SysRes> thizResList = grantedSysMenus.stream().filter(
                    r -> grantedResIds.contains(r.getResId())).collect(Collectors.toList());
            // 筛选授权节点及其父辈节点
            grantedSysMenus.removeIf(res -> {
                boolean isGranted = false;
                for(SysRes thizRes: thizResList){
                    if(Arrays.asList(thizRes.getTreePath().split("\\.")).contains(res.getResId())){
                        isGranted = true;
                        break;
                    }
                }
                return !isGranted;
            });
        }
        // 组装前端的菜单结构
        List<JSONObject> resultList = grantedSysMenus.stream().map(sysMenu -> {
            JSONObject menuJsonObject = new JSONObject();
            menuJsonObject.put("id", sysMenu.getResId());
            menuJsonObject.put("parentId", sysMenu.getParentId());
            menuJsonObject.put("title", sysMenu.getResName());
            menuJsonObject.put("name", sysMenu.getResCode());
            if(sysMenu.getMenuType() == SysMenuTypeEnum.CATALOG.getType()){
                menuJsonObject.put("path", StrUtil.C_SLASH + sysMenu.getResCode());
            }else{
                menuJsonObject.put("path", sysMenu.getResPath());
            }
            menuJsonObject.put("component", sysMenu.getResPath());
            menuJsonObject.put("sort", sysMenu.getTreeSort());

            JSONObject metaJsonObject = new JSONObject();
            metaJsonObject.put("title", sysMenu.getResName());
            metaJsonObject.put("icon", sysMenu.getResIcon());
            if(sysMenu.getMenuType() != SysMenuTypeEnum.CATALOG.getType()){
                metaJsonObject.put("isHide", sysMenu.getHideFlag() == CommonWhetherEnum.YES.getCode());
            }else{
                metaJsonObject.put("isHide", false);
            }
            if(sysMenu.getMenuType() == SysMenuTypeEnum.MENU.getType()){
                metaJsonObject.put("isFull", sysMenu.getFullscreenFlag() == CommonWhetherEnum.YES.getCode());
            }else{
                metaJsonObject.put("isFull", false);
            }
            metaJsonObject.put("isAffix", "home".equals(sysMenu.getResCode()));  //是否固定的页签
            metaJsonObject.put("isKeepAlive", true);
            if(sysMenu.getMenuType() == SysMenuTypeEnum.LINK.getType()){
                metaJsonObject.put("isLink", sysMenu.getResPath());
            }
            metaJsonObject.put("isLeaf", sysMenu.getMenuType() != SysMenuTypeEnum.CATALOG.getType());
            menuJsonObject.put("meta", metaJsonObject);
            return menuJsonObject;
        }).collect(Collectors.toList());
        // 构造hutool树
        List<TreeNode<String>> treeNodeList = resultList.stream().map(jsonObject ->
            new TreeNode<>(jsonObject.getString("id"), jsonObject.getString("parentId"),
                    jsonObject.getString("title"),
                    jsonObject.getInteger("sort"))
                     .setExtra(JSON.parseObject(JSON.toJSONString(jsonObject),
                             new TypeReference<Map<String, Object>>() {}))
        ).collect(Collectors.toList());
        return TreeUtil.build(treeNodeList, "0");
    }

    @Override
    public Map<String, List<String>> listAuthButton(){
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        QueryWrapper<SysButtonVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("p.RES_ID");
        queryWrapper.eq("t.RES_TYPE", SysResTypeEnum.BUTTON.getType());
        if(!loginUser.isSuperAdmin()){
            // 非超管，筛选授权的按钮列表
            Set<String> grantedResIds = loginUser.getGrantedResIds();
            if(CollUtil.isEmpty(grantedResIds)){
                return MapUtil.newHashMap();
            }
            queryWrapper.in("t.RES_ID", grantedResIds);
        }
        queryWrapper.orderByAsc(CollectionUtil.newArrayList("p.TREE_LEVEL", "p.TREE_SORT", "t.TREE_SORT"));
        List<SysButtonVO> grantedSysButtons = sysResMapper.listButtonByWrapper(queryWrapper);
        // 分组+自定义映射结果
        return grantedSysButtons.stream().collect(
                Collectors.groupingBy(SysButtonVO::getMenuCode,
                        Collectors.mapping(SysButtonVO::getResCode, Collectors.toList())));
    }

    @Override
    public List<Tree<String>> getTreeData(SysResQueryDTO sysResQueryDTO){
        LambdaQueryWrapper<SysRes> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(sysResQueryDTO.getResName()),
                SysRes::getResName, sysResQueryDTO.getResName());
        queryWrapper.eq(ObjectUtil.isNotNull(sysResQueryDTO.getResType()),
                SysRes::getResType, sysResQueryDTO.getResType());
        queryWrapper.orderByAsc(CollectionUtil.newArrayList(SysRes::getTreeLevel, SysRes::getTreeSort));
        List<SysRes> sysResList = this.list(queryWrapper);
        if(CollUtil.isEmpty(sysResList)){
            return CollectionUtil.newArrayList();
        }
        // 筛选查询节点及其父辈节点
        List<SysRes> allResList = this.list();
        allResList.removeIf(res -> {
            boolean isFilter = false;
            for(SysRes thizRes: sysResList){
                if(Arrays.asList(thizRes.getTreePath().split("\\.")).contains(res.getResId())){
                    isFilter = true;
                    break;
                }
            }
            return !isFilter;
        });
        List<TreeNode<String>> treeNodeList = allResList.stream().map(sysRes ->
            new TreeNode<>(sysRes.getResId(), sysRes.getParentId(),
                    sysRes.getResName(),
                    sysRes.getTreeSort())
                    .setExtra(JSON.parseObject(JSON.toJSONString(sysRes),
                        new TypeReference<Map<String, Object>>() {}))
        ).collect(Collectors.toList());
        return TreeUtil.build(treeNodeList, "0");
    }

    @Override
    public List<Tree<String>> getAuthTreeData(){
        LambdaQueryWrapper<SysRes> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(CollectionUtil.newArrayList(SysRes::getTreeLevel, SysRes::getTreeSort));
        List<SysRes> grantedSysResList = this.list(lambdaQueryWrapper);
        // 非超管，获取授权的组织机构列表
        StpLoginUser loginUser = StpPCUtil.getLoginUser();
        if(!loginUser.isSuperAdmin()){
            // 筛选授权节点
            Set<String> grantedResIds = loginUser.getGrantedResIds();
            if(CollUtil.isEmpty(grantedResIds)){
                return CollectionUtil.newArrayList();
            }
            List<SysRes> thizResList = grantedSysResList.stream().filter(
                    r -> grantedResIds.contains(r.getResId())).collect(Collectors.toList());
            // 筛选授权节点及其父辈节点
            grantedSysResList.removeIf(res -> {
                boolean isGranted = false;
                for(SysRes thizRes: thizResList){
                    if(Arrays.asList(thizRes.getTreePath().split("\\.")).contains(res.getResId())){
                        isGranted = true;
                        break;
                    }
                }
                return !isGranted;
            });
        }
        // 构造hutool树
        List<TreeNode<String>> treeNodeList = grantedSysResList.stream().map(sysRes ->
            new TreeNode<>(sysRes.getResId(), sysRes.getParentId(),
                    sysRes.getResName(),
                    sysRes.getTreeSort()).setExtra(
                            JSON.parseObject(JSON.toJSONString(sysRes),
                    new TypeReference<Map<String, Object>>() {}))
        ).collect(Collectors.toList());
        return TreeUtil.build(treeNodeList, "0");
    }

    @Override
    public boolean isExistResNameInSameLevel(SysResAddOrEditDTO sysResAddOrEditDTO){
        LambdaQueryWrapper<SysRes> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRes::getParentId, sysResAddOrEditDTO.getParentId());
        lambdaQueryWrapper.eq(SysRes::getResName, sysResAddOrEditDTO.getResName());
        if(StrUtil.isNotEmpty(sysResAddOrEditDTO.getResId())){
            lambdaQueryWrapper.ne(SysRes::getResId, sysResAddOrEditDTO.getResId());
        }
        return this.count(lambdaQueryWrapper) > 0;
    }

    @Override
    public boolean isExistResCode(SysResAddOrEditDTO sysResAddOrEditDTO){
        LambdaQueryWrapper<SysRes> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRes::getResCode, sysResAddOrEditDTO.getResCode());
        // 同一菜单下的按钮编码不重复
        if(sysResAddOrEditDTO.getResType() == SysResTypeEnum.BUTTON.getType()){
            queryWrapper.eq(SysRes::getParentId, sysResAddOrEditDTO.getParentId());
        }
        if(StrUtil.isNotEmpty(sysResAddOrEditDTO.getResId())){
            queryWrapper.ne(SysRes::getResId, sysResAddOrEditDTO.getResId());
        }
        return this.count(queryWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysResAddOrEditDTO sysResAddOrEditDTO){
        boolean isExistResCode = this.isExistResCode(sysResAddOrEditDTO);
        if(isExistResCode){
            throw new CommonException("资源编码重复，请重新输入");
        }
        boolean isExistResName = this.isExistResNameInSameLevel(sysResAddOrEditDTO);
        if(isExistResName){
            throw new CommonException("存在重名的同级资源，请重新输入！");
        }
        // 保存资源
        SysRes sysRes = BeanUtil.copyProperties(sysResAddOrEditDTO, SysRes.class);
        this.saveTreeData(sysRes);
        // 保存或更新资源url
        sysResUrlService.saveOrUpdate(sysRes.getResId(), sysResAddOrEditDTO.getUrlList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysResAddOrEditDTO sysResAddOrEditDTO){
        SysRes sysRes = this.getById(sysResAddOrEditDTO.getResId());
        if(ObjectUtil.isNull(sysRes)){
            throw new CommonException("该资源不存在！");
        }
        boolean isExistResCode = this.isExistResCode(sysResAddOrEditDTO);
        if(isExistResCode){
            throw new CommonException("资源编码重复，请重新输入");
        }
        boolean isExistResName = this.isExistResNameInSameLevel(sysResAddOrEditDTO);
        if(isExistResName){
            throw new CommonException("资源名称重复，请重新输入！");
        }
        // 更新资源
        String oldTreePath = sysRes.getTreePath();
        Integer oldTreeLevel = sysRes.getTreeLevel();
        BeanUtil.copyProperties(sysResAddOrEditDTO, sysRes);
        this.updateTreeData(sysRes, oldTreePath, oldTreeLevel);
        // 保存或更新资源url
        sysResUrlService.saveOrUpdate(sysRes.getResId(), sysResAddOrEditDTO.getUrlList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdDTO idDTO) {
        String toDeleteResId = idDTO.getId();
        long thizChildCount = this.countByParentId(toDeleteResId);
        if(thizChildCount > 0){
            throw new CommonException("存在孩子节点，无法删除！");
        }
        //删除
        this.removeById(toDeleteResId);
        //删除关联属性数据
        sysResUrlService.deleteByResId(toDeleteResId);
        sysRolePropService.deleteByTableNameAndRecordId(SysRolePropEnum.SYSRES.getValue(), toDeleteResId);
    }

}
