package com.ohohmiao.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.modules.system.model.entity.SysUserProp;
import com.ohohmiao.modules.system.model.vo.SysUserPropVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 系统用户属性Service
 *
 * @author ohohmiao
 * @date 2023-04-06 14:12
 */
public interface SysUserPropService extends IService<SysUserProp> {

    /**
     * 根据用户id和属性类别查找
     * @param userId
     * @param tableName
     * @return
     */
    List<SysUserPropVO> listByUserIdAndTableName(String userId, String tableName);

    /**
     * 根据用户id集合和属性类别查找
     * @param userIds
     * @param tableName
     * @return
     */
    List<SysUserPropVO> listByUserIdsAndTableName(Collection<String> userIds, String tableName);

    /**
     * 根据用户id和属性类别，查找关联记录id
     * @param userId
     * @param tableName
     * @return
     */
    Set<String> listRecordIdsByUserIdAndTableName(String userId, String tableName);

    /**
     * 根据用户id集合和属性类别，查找关联记录id
     * @param userIds
     * @param tableName
     * @return
     */
    Set<String> listRecordIdsByUserIdsAndTableName(Collection<String> userIds, String tableName);

    /**
     * 根据关联表名和记录id查找
     * @param tableName
     * @param recordId
     * @return
     */
    List<SysUserPropVO> listByTableNameAndRecordId(String tableName, String recordId);

    /**
     * 根据关联表名和记录id，列出用户id
     * @param tableName
     * @param recordId
     * @return
     */
    Set<String> listUserIdsByTableNameAndRecordId(String tableName, String recordId);

    /**
     * 根据用户id和属性类别统计
     * @param userId
     * @param tableName
     * @return
     */
    Long countByUserIdAndTableName(String userId, String tableName);

    /**
     * 根据关联表名和记录id统计
     * @param tableName
     * @param recordId
     * @return
     */
    Long countByTableNameAndRecordId(String tableName, String recordId);

    /**
     * 根据关联表名和记录id，删除用户属性
     * @param tableName
     * @param recordId
     */
    void deleteByTableNameAndRecordId(String tableName, String recordId);

    /**
     * 根据关联表名和记录id集合，删除用户属性
     * @param tableName
     * @param recordIds
     */
    void deleteByTableNameAndRecordIds(String tableName, List<String> recordIds);

    /**
     * 根据用户id，删除用户属性
     * @param userId
     */
    void deleteByUserId(String userId);

    /**
     * 根据用户id集合，删除用户属性
     * @param userIds
     */
    void deleteByUserIds(List<String> userIds);

    /**
     * 根据用户id和属性类别，删除
     * @param userId
     * @param tableName
     */
    void deleteByUserIdAndTableName(String userId, String tableName);

    /**
     * 根据用户id集合和属性类别，删除
     * @param userIds
     * @param tableName
     */
    void deleteByUserIdsAndTableName(List<String> userIds, String tableName);

    /**
     * 保存或更新用户属性
     * @param userId
     * @param tableName
     * @param props
     */
    void saveOrUpdate(String userId, String tableName, List<SysUserProp> props);

    /**
     * 保存或更新多个用户的属性
     * @param tableName
     * @param recordId
     * @param userIds
     */
    void saveOrUpdateMultiUserProps(String tableName, String recordId, List<String> userIds);

}
