package com.ohohmiao.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.modules.system.model.entity.SysRoleProp;
import com.ohohmiao.modules.system.model.vo.SysRolePropVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 系统角色属性Service
 *
 * @author ohohmiao
 * @date 2023-04-06 14:21
 */
public interface SysRolePropService extends IService<SysRoleProp> {

    /**
     * 根据角色id和属性类别，查找
     * @param roleId
     * @param tableName
     * @return
     */
    List<SysRolePropVO> listByRoleIdAndTableName(String roleId, String tableName);

    /**
     * 根据角色id集合和属性类别，查找
     * @param roleIds
     * @param tableName
     * @return
     */
    List<SysRolePropVO> listByRoleIdsAndTableName(Collection<String> roleIds, String tableName);

    /**
     * 根据角色id和属性类别，查找关联记录id
     * @param roleId
     * @param tableName
     * @return
     */
    Set<String> listRecordIdsByRoleIdAndTableName(String roleId, String tableName);

    /**
     * 根据角色id集合和属性类别，查找关联记录id
     * @param roleIds
     * @param tableName
     * @return
     */
    Set<String> listRecordIdsByRoleIdsAndTableName(Collection<String> roleIds, String tableName);

    /**
     * 根据关联表名和记录id查找
     * @param tableName
     * @param recordId
     * @return
     */
    List<SysRolePropVO> listByTableNameAndRecordId(String tableName, String recordId);

    /**
     * 根据关联表名和记录id，列出角色id
     * @param tableName
     * @param recordId
     * @return
     */
    Set<String> listRoleIdsByTableNameAndRecordId(String tableName, String recordId);

    /**
     * 根据角色id和属性类别统计
     * @param roleId
     * @param tableName
     * @return
     */
    Long countByRoleIdAndTableName(String roleId, String tableName);

    /**
     * 根据关联表名和记录id统计
     * @param tableName
     * @param recordId
     * @return
     */
    Long countByTableNameAndRecordId(String tableName, String recordId);

    /**
     * 根据角色id，删除角色属性
     * @param roleId
     */
    void deleteByRoleId(String roleId);

    /**
     * 根据角色id集合，删除角色属性
     * @param roleIds
     */
    void deleteByRoleIds(List<String> roleIds);

    /**
     * 根据角色id和关联表名删除
     * @param roleId
     * @param tableName
     */
    void deleteByRoleIdAndTableName(String roleId, String tableName);

    /**
     * 根据关联表名和记录id，删除角色属性
     * @param tableName
     * @param recordId
     */
    void deleteByTableNameAndRecordId(String tableName, String recordId);

    /**
     * 根据关联表名和记录id集合，删除角色属性
     * @param tableName
     * @param recordIds
     */
    void deleteByTableNameAndRecordIds(String tableName, List<String> recordIds);

    /**
     * 保存或更新单个角色的属性
     * @param roleId
     * @param tableName
     * @param recordIds
     */
    void saveOrUpdate(String roleId, String tableName, List<String> recordIds);

    /**
     * 保存或更新多个角色的属性
     * @param tableName
     * @param recordId
     * @param roleIds
     */
    void saveOrUpdateMultiRoleProps(String tableName, String recordId, List<String> roleIds);

}
