package com.ohohmiao.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.modules.system.model.entity.SysResUrl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 资源url Service
 *
 * @author ohohmiao
 * @date 2023-04-06 14:19
 */
public interface SysResUrlService extends IService<SysResUrl> {

    /**
     * 根据资源id集合，列出资源url
     * @param resIds
     * @return
     */
    Set<String> listByResIds(Collection<String> resIds);

    /**
     * 根据资源id删除
     * @param resId
     */
    void deleteByResId(String resId);

    /**
     * 根据资源id，列出资源url
     * @param resId
     * @return
     */
    List<String> listUrlByResId(String resId);

    /**
     * 新增或编辑资源url
     * @param resId
     * @param urlList
     */
    void saveOrUpdate(String resId, List<String> urlList);

    /**
     * 列出SaPcCheckPermission注解的权限url
     * @return
     */
    List<String> listSaPcPermissionUrl();

}
