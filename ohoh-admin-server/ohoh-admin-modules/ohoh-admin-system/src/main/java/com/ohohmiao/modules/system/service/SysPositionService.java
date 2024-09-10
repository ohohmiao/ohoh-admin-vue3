package com.ohohmiao.modules.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.CommonTreeService;
import com.ohohmiao.modules.system.model.entity.SysPosition;
import com.ohohmiao.modules.system.model.dto.SysPositionAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysPositionGrantUserDTO;
import com.ohohmiao.modules.system.model.dto.SysPositionUserPageDTO;
import com.ohohmiao.modules.system.model.vo.SysPositionVO;
import com.ohohmiao.modules.system.model.vo.SysPositionUserVO;

import java.util.List;

/**
 * 岗位Service
 *
 * @author ohohmiao
 * @date 2023-06-20 11:38
 */
public interface SysPositionService extends CommonTreeService<SysPosition> {

    /**
     * 从缓存获取全量岗位数据
     * @return
     */
    List<SysPositionVO> listCachedAllPositions();

    /**
     * 获取岗位树
     * @return
     */
    List<Tree<String>> getTreeData();

    /**
     * 判断是否重复的岗位编码
     * @param sysPositionAddOrEditDTO
     * @return
     */
    boolean isExistPositionCode(SysPositionAddOrEditDTO sysPositionAddOrEditDTO);

    /**
     * 新增岗位
     * @param sysPositionAddOrEditDTO
     */
    void add(SysPositionAddOrEditDTO sysPositionAddOrEditDTO);

    /**
     * 编辑岗位
     * @param sysPositionAddOrEditDTO
     */
    void edit(SysPositionAddOrEditDTO sysPositionAddOrEditDTO);

    /**
     * 删除岗位
     * @param idDTO
     */
    void delete(CommonIdDTO idDTO);

    /**
     * 获取岗位用户分页列表
     * @param sysPositionUserPageDTO
     * @return
     */
    Page<SysPositionUserVO> listUserByPage(SysPositionUserPageDTO sysPositionUserPageDTO);

    /**
     * 获取某岗位的用户列表
     * @param positionId
     * @return
     */
    List<SysPositionUserVO> listUserByPositionId(String positionId);

    /**
     * 获取指定岗位集合的用户列表
     * @param positionIds
     * @return
     */
    List<SysPositionUserVO> listUserByPositionIds(List<String> positionIds);

    /**
     * 给岗位授权用户
     * @param grantUserDTO
     */
    void grantUser(SysPositionGrantUserDTO grantUserDTO);

    /**
     * 取消授权岗位用户
     * @param idListDTO
     */
    void revokeUser(CommonIdListDTO idListDTO);

}
