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
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.listener.CommonDataChangeEventCenter;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.impl.CommonTreeServiceImpl;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.modules.system.enums.SysDataListenerEnum;
import com.ohohmiao.modules.system.model.entity.SysDicType;
import com.ohohmiao.modules.system.mapper.SysDicTypeMapper;
import com.ohohmiao.modules.system.model.dto.SysDicTypeAddOrEditDTO;
import com.ohohmiao.modules.system.model.vo.SysDicTypeVO;
import com.ohohmiao.modules.system.service.SysDicService;
import com.ohohmiao.modules.system.service.SysDicTypeService;
import com.ohohmiao.modules.system.enums.SysCacheKeyEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统字典类别Service实现类
 *
 * @author ohohmiao
 * @date 2023-05-25 11:57
 */
@Service
public class SysDicTypeServiceImpl extends CommonTreeServiceImpl<SysDicTypeMapper, SysDicType> implements SysDicTypeService {

    @Resource
    private SysDicTypeMapper sysDicTypeMapper;

    @Resource
    private SysDicService sysDicService;

    @Resource
    private PlatRedisUtil platRedisUtil;

    @Override
    public List<SysDicTypeVO> listCachedAll(){
        //尝试从缓存获取
        List<Map<String, Object>> cachedList = platRedisUtil.getCacheList(SysCacheKeyEnum.DICTYPE_ALL.getKey());
        if(CollUtil.isNotEmpty(cachedList)){
            return cachedList.stream().map(m ->
                    BeanUtil.toBean(m, SysDicTypeVO.class)
            ).collect(Collectors.toList());
        }
        //从库中查找，并写入缓存
        List<SysDicType> dicTypeList = this.list(new LambdaQueryWrapper<SysDicType>()
                .orderByAsc(CollectionUtil.newArrayList(SysDicType::getTreeLevel, SysDicType::getTreeSort)));
        List<SysDicTypeVO> resultList = dicTypeList.stream().map(dicType -> {
            SysDicTypeVO result = new SysDicTypeVO();
            BeanUtil.copyProperties(dicType, result);
            return result;
        }).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(resultList)) {
            platRedisUtil.setCacheList(SysCacheKeyEnum.DICTYPE_ALL.getKey(), resultList);
            platRedisUtil.expire(SysCacheKeyEnum.DICTYPE_ALL.getKey(), SysCacheKeyEnum.DICTYPE_ALL.getTtl());
        }
        return resultList;
    }

    @Override
    public List<Tree<String>> getTreeData(){
        //查询系统字典类别数据
        List<SysDicTypeVO> allDicTypes = this.listCachedAll();
        //构造hutool树
        List<TreeNode<String>> treeNodeList = allDicTypes.stream().map(dicType ->
            new TreeNode<>(dicType.getDictypeId(), dicType.getParentId(),
                    dicType.getDictypeName(),
                    dicType.getTreeSort())
                    .setExtra(JSON.parseObject(JSON.toJSONString(dicType),
                            new TypeReference<Map<String, Object>>() {}))
        ).collect(Collectors.toList());
        return TreeUtil.build(treeNodeList, "0");
    }

    @Override
    public boolean isExistDicTypeCode(SysDicTypeAddOrEditDTO sysDicTypeAddOrEditDTO){
        LambdaQueryWrapper<SysDicType> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(SysDicType::getDictypeCode, sysDicTypeAddOrEditDTO.getDictypeCode());
        countWrapper.ne(StrUtil.isNotEmpty(sysDicTypeAddOrEditDTO.getDictypeId()),
                SysDicType::getDictypeId, sysDicTypeAddOrEditDTO.getDictypeId());
        return this.count(countWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysDicTypeAddOrEditDTO sysDicTypeAddOrEditDTO){
        boolean isExistDicTypeCode = this.isExistDicTypeCode(sysDicTypeAddOrEditDTO);
        if(isExistDicTypeCode){
            throw new CommonException("字典类别编码重复，请重新输入！");
        }
        // 保存字典类别
        SysDicType sysDicType = BeanUtil.copyProperties(sysDicTypeAddOrEditDTO, SysDicType.class);
        this.saveTreeData(sysDicType);

        CommonDataChangeEventCenter.doAddWithData(SysDataListenerEnum.DICTYPE.getName(), sysDicType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysDicTypeAddOrEditDTO sysDicTypeAddOrEditDTO){
        SysDicType sysDicType = this.getById(sysDicTypeAddOrEditDTO.getDictypeId());
        if(ObjectUtil.isNull(sysDicType)){
            throw new CommonException("字典类别不存在！");
        }
        boolean isExistDicTypeCode = this.isExistDicTypeCode(sysDicTypeAddOrEditDTO);
        if(isExistDicTypeCode){
            throw new CommonException("字典类别编码重复，请重新输入！");
        }
        // 更新字典类别
        String oldTreePath = sysDicType.getTreePath();
        Integer oldTreeLevel = sysDicType.getTreeLevel();
        BeanUtil.copyProperties(sysDicTypeAddOrEditDTO, sysDicType);
        this.updateTreeData(sysDicType, oldTreePath, oldTreeLevel);
        // 更新对应字典的冗余字段
        sysDicService.updateDictypeCode(sysDicType.getDictypeCode(), sysDicType.getDictypeId());

        CommonDataChangeEventCenter.doEditWithData(SysDataListenerEnum.DICTYPE.getName(), sysDicType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdDTO idDTO){
        String toDeleteDicTypeId = idDTO.getId();
        SysDicType sysDicType = this.getById(toDeleteDicTypeId);
        if(ObjectUtil.isNull(sysDicType)){
            throw new CommonException("不存在的字典类别！");
        }
        long thizChildCount = this.countByParentId(toDeleteDicTypeId);
        if(thizChildCount > 0){
            throw new CommonException("存在孩子节点，无法删除！");
        }
        this.removeById(toDeleteDicTypeId);
        sysDicService.deleteByDictypeId(sysDicType.getDictypeId());

        CommonDataChangeEventCenter.doDeleteWithId(SysDataListenerEnum.DICTYPE.getName(), toDeleteDicTypeId);
    }

}
