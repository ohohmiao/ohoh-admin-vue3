package com.ohohmiao.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.modules.system.model.entity.SysRole;
import com.ohohmiao.modules.system.model.vo.SysRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统角色mapper
 *
 * @author ohohmiao
 * @date 2023-04-06 14:06
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据Wrapper，分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<SysRoleVO> pageByWrapper(Page<SysRoleVO> page, @Param(Constants.WRAPPER) Wrapper<SysRoleVO> queryWrapper);

    /**
     * 查找某组织下的最大角色排序
     * @param orgId 组织id
     * @return
     */
    Integer getMaxSortByOrgId(@Param("orgId") String orgId);

}
