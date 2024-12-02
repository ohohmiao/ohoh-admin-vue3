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
import com.ohohmiao.modules.workflow.enums.WorkflowCacheKeyEnum;
import com.ohohmiao.modules.workflow.enums.WorkflowDataListenerEnum;
import com.ohohmiao.modules.workflow.mapper.WorkflowDefTypeMapper;
import com.ohohmiao.modules.workflow.model.dto.WorkflowDefTypeAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.entity.WorkflowDefType;
import com.ohohmiao.modules.workflow.model.vo.WorkflowDefTypeVO;
import com.ohohmiao.modules.workflow.service.WorkflowDefTypeService;
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
public class WorkflowDefTypeServiceImpl extends CommonTreeServiceImpl<WorkflowDefTypeMapper, WorkflowDefType> implements WorkflowDefTypeService {

    @Resource
    private PlatRedisUtil platRedisUtil;

    @Override
    public List<WorkflowDefTypeVO> listCachedAll(){
        //尝试从缓存获取
        Object cachedAllDefTypes = platRedisUtil.getCacheObject(WorkflowCacheKeyEnum.DEFTYPE_ALL.getKey());
        if(ObjectUtil.isNotNull(cachedAllDefTypes)){
            List cachedList = (List) cachedAllDefTypes;
            if(CollUtil.isNotEmpty(cachedList)){
                return (List<WorkflowDefTypeVO>) cachedList.stream().map(
                        m -> BeanUtil.toBean(m, WorkflowDefTypeVO.class)
                ).collect(Collectors.toList());
            }
        }
        //从库中查找，并写入缓存
        List<WorkflowDefType> defTypeList = this.list(new LambdaQueryWrapper<WorkflowDefType>()
                .orderByAsc(CollectionUtil.newArrayList(WorkflowDefType::getTreeLevel, WorkflowDefType::getTreeSort)));
        List<WorkflowDefTypeVO> resultList = defTypeList.stream().map(defType -> {
            WorkflowDefTypeVO result = new WorkflowDefTypeVO();
            BeanUtil.copyProperties(defType, result);
            return result;
        }).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(resultList)){
            platRedisUtil.setCacheObject(WorkflowCacheKeyEnum.DEFTYPE_ALL.getKey(), resultList,
                    WorkflowCacheKeyEnum.DEFTYPE_ALL.getTtl(), TimeUnit.SECONDS);
        }
        return resultList;
    }

    @Override
    public List<Tree<String>> getTreeData(){
        //查询流程定义类别数据
        List<WorkflowDefTypeVO> allDefTypes = this.listCachedAll();
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
    public boolean isExistDefTypeCode(WorkflowDefTypeAddOrEditDTO workflowDefTypeAddOrEditDTO){
        LambdaQueryWrapper<WorkflowDefType> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(WorkflowDefType::getDeftypeCode, workflowDefTypeAddOrEditDTO.getDeftypeCode());
        countWrapper.ne(StrUtil.isNotEmpty(workflowDefTypeAddOrEditDTO.getDeftypeId()),
                WorkflowDefType::getDeftypeId, workflowDefTypeAddOrEditDTO.getDeftypeId());
        return this.count(countWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WorkflowDefTypeAddOrEditDTO workflowDefTypeAddOrEditDTO){
        boolean isExistDefTypeCode = this.isExistDefTypeCode(workflowDefTypeAddOrEditDTO);
        if(isExistDefTypeCode){
            throw new CommonException("流程定义类别编码重复，请重新输入！");
        }
        //树层级限制
        if(!"0".equals(workflowDefTypeAddOrEditDTO.getParentId())){
            WorkflowDefType parentType = this.getById(workflowDefTypeAddOrEditDTO.getParentId());
            if(ObjectUtil.isNull(parentType)){
                throw new CommonException("父亲节点不存在！");
            }
            if(parentType.getTreeLevel() > 1){
                throw new CommonException("流程类别定义树限制最多两个层级！");
            }
        }
        //保存流程定义类别
        WorkflowDefType workflowDefType = BeanUtil.toBean(workflowDefTypeAddOrEditDTO, WorkflowDefType.class);
        this.saveTreeData(workflowDefType);

        CommonDataChangeEventCenter.doAddWithData(WorkflowDataListenerEnum.DEFTYPE.getName(), workflowDefType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(WorkflowDefTypeAddOrEditDTO workflowDefTypeAddOrEditDTO){
        WorkflowDefType workflowDefType = this.getById(workflowDefTypeAddOrEditDTO.getDeftypeId());
        if(ObjectUtil.isNull(workflowDefType)){
            throw new CommonException("流程定义类别不存在！");
        }
        boolean isExistDefTypeCode = this.isExistDefTypeCode(workflowDefTypeAddOrEditDTO);
        if(isExistDefTypeCode){
            throw new CommonException("流程定义类别编码重复，请重新输入！");
        }
        //更新流程定义类别
        String oldTreePath = workflowDefType.getTreePath();
        Integer oldTreeLevel = workflowDefType.getTreeLevel();
        BeanUtil.copyProperties(workflowDefTypeAddOrEditDTO, workflowDefType);
        this.updateTreeData(workflowDefType, oldTreePath, oldTreeLevel);

        CommonDataChangeEventCenter.doEditWithData(WorkflowDataListenerEnum.DEFTYPE.getName(), workflowDefType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdDTO idDTO){
        //TODO 判断是否存在有流程实例的流程定义
        String toDeleteId = idDTO.getId();
        WorkflowDefType workflowDefType = this.getById(toDeleteId);
        if(ObjectUtil.isNull(workflowDefType)){
            throw new CommonException("不存在的流程定义类别！");
        }
        long thizChildCount = this.countByParentId(toDeleteId);
        if(thizChildCount > 0){
            throw new CommonException("存在孩子节点，无法删除！");
        }
        this.removeById(toDeleteId);

        CommonDataChangeEventCenter.doDeleteWithId(WorkflowDataListenerEnum.DEFTYPE.getName(), toDeleteId);
    }

}
