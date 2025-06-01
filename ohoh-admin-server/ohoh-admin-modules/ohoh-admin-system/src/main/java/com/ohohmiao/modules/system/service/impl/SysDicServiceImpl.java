package com.ohohmiao.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.listener.CommonDataChangeEventCenter;
import com.ohohmiao.framework.common.model.vo.CommonSelectVO;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.modules.system.enums.SysDataListenerEnum;
import com.ohohmiao.modules.system.model.entity.SysDic;
import com.ohohmiao.modules.system.model.entity.SysDicType;
import com.ohohmiao.modules.system.mapper.SysDicMapper;
import com.ohohmiao.modules.system.model.dto.SysDicAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysDicPageDTO;
import com.ohohmiao.modules.system.model.vo.SysDicVO;
import com.ohohmiao.modules.system.service.SysDicService;
import com.ohohmiao.modules.system.service.SysDicTypeService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统字典Service实现类
 *
 * @author ohohmiao
 * @date 2023-05-25 11:57
 */
@Service
public class SysDicServiceImpl extends ServiceImpl<SysDicMapper, SysDic> implements SysDicService, InitializingBean {

    @Resource
    private SysDicMapper sysDicMapper;

    @Lazy
    @Resource
    private SysDicTypeService sysDicTypeService;

    @Override
    public Page<SysDicVO> listByPage(SysDicPageDTO sysDicPageDTO){
        QueryWrapper<SysDicVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("p.DICTYPE_ID");
        queryWrapper.eq("d.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        //查询条件
        queryWrapper.like(StrUtil.isNotEmpty(sysDicPageDTO.getParentId()),
                "p.TREE_PATH", sysDicPageDTO.getParentId()+".");
        queryWrapper.like(StrUtil.isNotEmpty(sysDicPageDTO.getDicName()),
                "d.DIC_NAME", sysDicPageDTO.getDicName());
        //排序
        queryWrapper.orderByAsc(CollectionUtil.newArrayList("p.TREE_LEVEL", "p.TREE_SORT", "d.DIC_SORT"));
        return sysDicMapper.pageByWrapper(CommonPageRequest.constructPage(
                sysDicPageDTO.getCurrent(), sysDicPageDTO.getSize()), queryWrapper);
    }

    @Override
    public boolean isExistDicNameInSameDicType(SysDicAddOrEditDTO sysDicAddOrEditDTO){
        LambdaQueryWrapper<SysDic> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(SysDic::getDictypeId, sysDicAddOrEditDTO.getDictypeId());
        countWrapper.eq(SysDic::getDicName, sysDicAddOrEditDTO.getDicName());
        countWrapper.ne(StrUtil.isNotEmpty(sysDicAddOrEditDTO.getDicId()),
                SysDic::getDicId, sysDicAddOrEditDTO.getDicId());
        return this.count(countWrapper) > 0;
    }

    @Override
    public boolean isExistDicCodeInSameDicType(SysDicAddOrEditDTO sysDicAddOrEditDTO){
        LambdaQueryWrapper<SysDic> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(SysDic::getDictypeId, sysDicAddOrEditDTO.getDictypeId());
        countWrapper.eq(SysDic::getDicCode, sysDicAddOrEditDTO.getDicCode());
        countWrapper.ne(StrUtil.isNotEmpty(sysDicAddOrEditDTO.getDicId()),
                SysDic::getDicId, sysDicAddOrEditDTO.getDicId());
        return this.count(countWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysDicAddOrEditDTO sysDicAddOrEditDTO){
        SysDicType sysDicType = sysDicTypeService.getById(sysDicAddOrEditDTO.getDictypeId());
        if(ObjectUtil.isNull(sysDicType)){
            throw new CommonException("字典类别不存在！");
        }
        boolean isExistDicName = this.isExistDicNameInSameDicType(sysDicAddOrEditDTO);
        if(isExistDicName){
            throw new CommonException("字典名称重复，请重新输入！");
        }
        boolean isExistDicCode = this.isExistDicCodeInSameDicType(sysDicAddOrEditDTO);
        if(isExistDicCode){
            throw new CommonException("字典值重复，请重新输入！");
        }
        SysDic sysDic = BeanUtil.copyProperties(sysDicAddOrEditDTO, SysDic.class);
        sysDic.setDictypeCode(sysDicType.getDictypeCode());
        Integer maxDicSort = sysDicMapper.getMaxSortByDictypeId(sysDicAddOrEditDTO.getDictypeId());
        sysDic.setDicSort(ObjectUtil.isNotNull(maxDicSort)? maxDicSort + 1: 1);
        this.save(sysDic);

        CommonDataChangeEventCenter.doAddWithData(SysDataListenerEnum.DIC.getName(), sysDic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysDicAddOrEditDTO sysDicAddOrEditDTO){
        SysDicType sysDicType = sysDicTypeService.getById(sysDicAddOrEditDTO.getDictypeId());
        if(ObjectUtil.isNull(sysDicType)){
            throw new CommonException("字典类别不存在！");
        }
        SysDic sysDic = this.getById(sysDicAddOrEditDTO.getDicId());
        if(ObjectUtil.isNull(sysDic)){
            throw new CommonException("字典不存在！");
        }
        boolean isExistDicName = this.isExistDicNameInSameDicType(sysDicAddOrEditDTO);
        if(isExistDicName){
            throw new CommonException("字典名称重复，请重新输入！");
        }
        boolean isExistDicCode = this.isExistDicCodeInSameDicType(sysDicAddOrEditDTO);
        if(isExistDicCode){
            throw new CommonException("字典值重复，请重新输入！");
        }
        BeanUtil.copyProperties(sysDicAddOrEditDTO, sysDic);
        sysDic.setDictypeCode(sysDicType.getDictypeCode());
        this.updateById(sysDic);

        CommonDataChangeEventCenter.doEditWithData(SysDataListenerEnum.DIC.getName(), sysDic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdListDTO idListDTO){
        this.removeByIds(idListDTO.getId());

        CommonDataChangeEventCenter.doDeleteWithIdList(SysDataListenerEnum.DIC.getName(), idListDTO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDictypeId(String dictypeId){
        QueryWrapper<SysDic> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(SysDic::getDictypeId, dictypeId);
        this.remove(deleteWrapper);

        CommonDataChangeEventCenter.doDeleteWithId(SysDataListenerEnum.DIC.getName(), null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictypeCode(String dicTypeCode, String dicTypeId){
        LambdaUpdateWrapper<SysDic> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysDic::getDictypeId, dicTypeId);
        updateWrapper.set(SysDic::getDictypeCode, dicTypeCode);
        this.update(updateWrapper);

        CommonDataChangeEventCenter.doEditWithData(SysDataListenerEnum.DIC.getName(), null);
    }

    @Override
    public List<CommonSelectVO> select(String dictypeCode){
        LambdaQueryWrapper<SysDic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDic::getDictypeCode, dictypeCode);
        queryWrapper.orderByAsc(SysDic::getDicSort);
        return this.list(queryWrapper).stream().map(dic -> {
            CommonSelectVO selectVO = new CommonSelectVO();
            selectVO.setValue(dic.getDicCode());
            selectVO.setLabel(dic.getDicName());
            return selectVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet(){
        //TODO 组件实例化后执行
    }

}
