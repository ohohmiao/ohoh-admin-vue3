package com.ohohmiao.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.modules.system.model.vo.SysPositionUserVO;
import com.ohohmiao.modules.system.model.entity.SysPosition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 岗位mapper
 *
 * @author ohohmiao
 * @date 2023-06-20 11:36
 */
@Mapper
public interface SysPositionMapper extends BaseMapper<SysPosition> {

    /**
     * 根据Wrapper，分页查询岗位用户
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<SysPositionUserVO> pageUserByWrapper(Page<SysPositionUserVO> page,
                                              @Param(Constants.WRAPPER) Wrapper<SysPositionUserVO> queryWrapper);

    /**
     * 根据Wrapper，列出岗位用户
     * @param queryWrapper
     * @return
     */
    List<SysPositionUserVO> listUserByWrapper(@Param(Constants.WRAPPER) Wrapper<SysPositionUserVO> queryWrapper);

}
