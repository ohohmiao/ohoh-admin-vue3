package com.ohohmiao.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.common.model.vo.CommonSelectVO;
import com.ohohmiao.modules.system.model.entity.SysDic;
import com.ohohmiao.modules.system.model.dto.SysDicAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysDicPageDTO;
import com.ohohmiao.modules.system.model.vo.SysDicVO;

import java.util.List;

/**
 * 系统字典Service
 *
 * @author ohohmiao
 * @date 2023-05-25 11:54
 */
public interface SysDicService extends IService<SysDic> {

    /**
     * 获取系统字典分页列表
     * @param sysDicPageDTO
     * @return
     */
    Page<SysDicVO> listByPage(SysDicPageDTO sysDicPageDTO);

    /**
     * 判断同字典类别下是否重复的字典名称
     * @param sysDicAddOrEditDTO
     * @return
     */
    boolean isExistDicNameInSameDicType(SysDicAddOrEditDTO sysDicAddOrEditDTO);

    /**
     * 判断同字典类别下是否重复的字典编码
     * @param sysDicAddOrEditDTO
     * @return
     */
    boolean isExistDicCodeInSameDicType(SysDicAddOrEditDTO sysDicAddOrEditDTO);

    /**
     * 新增系统字典
     * @param sysDicAddOrEditDTO
     */
    void add(SysDicAddOrEditDTO sysDicAddOrEditDTO);

    /**
     * 编辑系统字典
     * @param sysDicAddOrEditDTO
     */
    void edit(SysDicAddOrEditDTO sysDicAddOrEditDTO);

    /**
     * 删除系统字典
     * @param idListDTO
     */
    void delete(CommonIdListDTO idListDTO);

    /**
     * 根据字典类别id删除
     * @param dictypeId
     */
    void deleteByDictypeId(String dictypeId);

    /**
     * 更新字典的类别编码
     * @param dicTypeCode
     * @param dicTypeId
     */
    void updateDictypeCode(String dicTypeCode, String dicTypeId);

    /**
     * 获取某类别字典的下拉框数据
     * @param dictypeCode
     * @return
     */
    List<CommonSelectVO> select(String dictypeCode);

}
