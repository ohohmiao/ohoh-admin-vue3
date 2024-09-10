package com.ohohmiao.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ohohmiao.modules.system.model.entity.SysRes;
import com.ohohmiao.modules.system.model.vo.SysButtonVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统资源mapper
 *
 * @author ohohmiao
 * @date 2023-04-04 11:18
 */
@Mapper
public interface SysResMapper extends BaseMapper<SysRes> {

    /**
     * 根据Wrapper，列出按钮
     * @param queryWrapper
     * @return
     */
    List<SysButtonVO> listButtonByWrapper(@Param(Constants.WRAPPER) Wrapper<SysButtonVO> queryWrapper);

}
