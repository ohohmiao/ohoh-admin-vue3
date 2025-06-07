package com.ohohmiao.modules.workflow.service.impl;

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
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.framework.mybatis.service.impl.CommonTreeServiceImpl;
import com.ohohmiao.modules.workflow.enums.FlowCacheKeyEnum;
import com.ohohmiao.modules.workflow.enums.FlowDataListenerEnum;
import com.ohohmiao.modules.workflow.mapper.FlowDefTypeMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowDefTypeAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowDefType;
import com.ohohmiao.modules.workflow.model.vo.FlowDefTypeVO;
import com.ohohmiao.modules.workflow.service.FlowDefTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 流程定义类别Service实现类
 *
 * @author ohohmiao
 * @date 2024-12-01 21:50
 */
@Service
public class FlowDefTypeServiceImpl extends CommonTreeServiceImpl<FlowDefTypeMapper, FlowDefType> implements FlowDefTypeService {

    @Resource
    private PlatRedisUtil platRedisUtil;

    @Override
    public List<FlowDefTypeVO> listCachedAll(){
        //尝试从缓存获取
        Object cachedAllDefTypes = platRedisUtil.getCacheObject(FlowCacheKeyEnum.DEFTYPE_ALL.getKey());
        if(ObjectUtil.isNotNull(cachedAllDefTypes)){
            List cachedList = (List) cachedAllDefTypes;
            if(CollUtil.isNotEmpty(cachedList)){
                return (List<FlowDefTypeVO>) cachedList.stream().map(
                        m -> BeanUtil.toBean(m, FlowDefTypeVO.class)
                ).collect(Collectors.toList());
            }
        }
        //从库中查找，并写入缓存
        List<FlowDefType> defTypeList = this.list(new LambdaQueryWrapper<FlowDefType>()
                .orderByAsc(CollectionUtil.newArrayList(FlowDefType::getTreeLevel, FlowDefType::getTreeSort)));
        List<FlowDefTypeVO> resultList = defTypeList.stream().map(defType -> {
            FlowDefTypeVO result = new FlowDefTypeVO();
            BeanUtil.copyProperties(defType, result);
            return result;
        }).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(resultList)){
            platRedisUtil.setCacheObject(FlowCacheKeyEnum.DEFTYPE_ALL.getKey(), resultList,
                    FlowCacheKeyEnum.DEFTYPE_ALL.getTtl(), TimeUnit.SECONDS);
        }
        return resultList;
    }

    @Override
    public List<Tree<String>> getTreeData(){
        //查询流程定义类别数据
        List<FlowDefTypeVO> allDefTypes = this.listCachedAll();
        //构造hutool树
        List<TreeNode<String>> treeNodeList = allDefTypes.stream().map(defType ->
            new TreeNode<>(defType.getDeftypeId(), defType.getParentId(),
                    defType.getDeftypeName(), defType.getTreeSort())
                    .setExtra(JSON.parseObject(JSON.toJSONString(defType),
                            new TypeReference<Map<String, Object>>() {}))
        ).collect(Collectors.toList());
        return TreeUtil.build(treeNodeList, "0");
    }

    @Override
    public boolean isExistDefTypeCode(FlowDefTypeAddOrEditDTO flowDefTypeAddOrEditDTO){
        LambdaQueryWrapper<FlowDefType> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(FlowDefType::getDeftypeCode, flowDefTypeAddOrEditDTO.getDeftypeCode());
        countWrapper.ne(StrUtil.isNotEmpty(flowDefTypeAddOrEditDTO.getDeftypeId()),
                FlowDefType::getDeftypeId, flowDefTypeAddOrEditDTO.getDeftypeId());
        return this.count(countWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(FlowDefTypeAddOrEditDTO flowDefTypeAddOrEditDTO){
        boolean isExistDefTypeCode = this.isExistDefTypeCode(flowDefTypeAddOrEditDTO);
        if(isExistDefTypeCode){
            throw new CommonException("流程定义类别编码重复，请重新输入！");
        }
        //树层级限制
        if(!"0".equals(flowDefTypeAddOrEditDTO.getParentId())){
            FlowDefType parentType = this.getById(flowDefTypeAddOrEditDTO.getParentId());
            if(ObjectUtil.isNull(parentType)){
                throw new CommonException("父亲节点不存在！");
            }
            if(parentType.getTreeLevel() > 1){
                throw new CommonException("流程类别定义树限制最多两个层级！");
            }
        }
        //保存流程定义类别
        FlowDefType flowDefType = BeanUtil.toBean(flowDefTypeAddOrEditDTO, FlowDefType.class);
        this.saveTreeData(flowDefType);

        CommonDataChangeEventCenter.doAddWithData(FlowDataListenerEnum.DEFTYPE.getName(), flowDefType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(FlowDefTypeAddOrEditDTO flowDefTypeAddOrEditDTO){
        FlowDefType flowDefType = this.getById(flowDefTypeAddOrEditDTO.getDeftypeId());
        if(ObjectUtil.isNull(flowDefType)){
            throw new CommonException("流程定义类别不存在！");
        }
        boolean isExistDefTypeCode = this.isExistDefTypeCode(flowDefTypeAddOrEditDTO);
        if(isExistDefTypeCode){
            throw new CommonException("流程定义类别编码重复，请重新输入！");
        }
        //更新流程定义类别
        String oldTreePath = flowDefType.getTreePath();
        Integer oldTreeLevel = flowDefType.getTreeLevel();
        BeanUtil.copyProperties(flowDefTypeAddOrEditDTO, flowDefType);
        this.updateTreeData(flowDefType, oldTreePath, oldTreeLevel);

        CommonDataChangeEventCenter.doEditWithData(FlowDataListenerEnum.DEFTYPE.getName(), flowDefType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdDTO idDTO){
        //TODO 判断是否存在有流程实例的流程定义
        String toDeleteId = idDTO.getId();
        FlowDefType flowDefType = this.getById(toDeleteId);
        if(ObjectUtil.isNull(flowDefType)){
            throw new CommonException("不存在的流程定义类别！");
        }
        long thizChildCount = this.countByParentId(toDeleteId);
        if(thizChildCount > 0){
            throw new CommonException("存在孩子节点，无法删除！");
        }
        this.removeById(toDeleteId);

        CommonDataChangeEventCenter.doDeleteWithId(FlowDataListenerEnum.DEFTYPE.getName(), toDeleteId);
    }
    @Override
    public List<FlowDefTypeVO> listFirstLevelNodes(){
        LambdaQueryWrapper<FlowDefType> listWrapper = new LambdaQueryWrapper<>();
        listWrapper.eq(FlowDefType::getParentId, "0");
        listWrapper.orderByAsc(FlowDefType::getTreeSort);
        return this.list(listWrapper).stream().map(type ->
                BeanUtil.copyProperties(type, FlowDefTypeVO.class)).collect(Collectors.toList());
    }

}
