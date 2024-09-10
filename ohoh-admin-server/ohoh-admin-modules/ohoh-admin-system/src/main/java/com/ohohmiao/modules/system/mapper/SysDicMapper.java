package com.ohohmiao.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.modules.system.model.vo.SysDicVO;
import com.ohohmiao.modules.system.model.entity.SysDic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统字典mapper
 *
 * @author ohohmiao
 * @date 2023-05-25 11:51
 */
@Mapper
public interface SysDicMapper extends BaseMapper<SysDic> {

    /**
     * 根据Wrapper，分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<SysDicVO> pageByWrapper(Page<SysDicVO> page, @Param(Constants.WRAPPER)Wrapper<SysDicVO> queryWrapper);

    /**
     * 获取某字典类别下的最大排序
     * @param dictypeId 字典类别id
     * @return
     */
    Integer getMaxSortByDictypeId(@Param("dictypeId") String dictypeId);

}
