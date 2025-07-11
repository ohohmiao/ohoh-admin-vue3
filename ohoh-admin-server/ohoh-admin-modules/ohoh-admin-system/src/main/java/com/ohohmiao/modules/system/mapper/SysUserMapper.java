package com.ohohmiao.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.modules.system.model.entity.SysUser;
import com.ohohmiao.modules.system.model.pojo.SysReferRes;
import com.ohohmiao.modules.system.model.vo.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户mapper
 *
 * @author ohohmiao
 * @date 2023-03-30 17:03
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据Wrapper，分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<SysUserVO> pageByWrapper(Page<SysUserVO> page, @Param(Constants.WRAPPER)Wrapper<SysUserVO> queryWrapper);

    /**
     * 根据Wrapper，查询
     * @param queryWrapper
     * @return
     */
    List<SysUserVO> listByWrapper(@Param(Constants.WRAPPER)Wrapper<SysUserVO> queryWrapper);

    /**
     * 查找某组织下的最大用户排序
     * @param orgId 组织id
     * @return
     */
    Integer getMaxSortByOrgId(@Param("orgId") String orgId);

    /**
     * 查询出关联资源的系统用户列表
     * @param referUsers
     * @param referOrgs
     * @param referPositions
     * @param referContacts
     * @return
     */
    List<SysUserVO> listByReferRes(@Param("referUsers") List<SysReferRes> referUsers,
                        @Param("referOrgs")List<SysReferRes> referOrgs,
                        @Param("referPositions")List<SysReferRes> referPositions,
                        @Param("referContacts")List<SysReferRes> referContacts);

    /**
     * 从源用户中过滤出与目标组织同一组织的用户列表
     * @param sourceUserIdList
     * @param targetOrgId
     * @return
     */
    List<SysUserVO> doFilterBySameOrg(@Param("sourceUserIdList") List<String> sourceUserIdList,
                            @Param("targetOrgId") String targetOrgId);

    /**
     * 从源用户中过滤出是目标组织上一级组织的用户列表
     * @param sourceUserIdList
     * @param targetOrgId
     * @return
     */
    List<SysUserVO> doFilterByOneLevelUpOrg(@Param("sourceUserIdList") List<String> sourceUserIdList,
                              @Param("targetOrgId") String targetOrgId);

    /**
     * 从源用户中过滤出与目标组织同一或孩子组织的用户列表
     * @param sourceUserIdList
     * @param targetOrgId
     * @return
     */
    List<SysUserVO> doFilterBySameAndChildOrg(@Param("sourceUserIdList") List<String> sourceUserIdList,
                                            @Param("targetOrgId") String targetOrgId);

}
