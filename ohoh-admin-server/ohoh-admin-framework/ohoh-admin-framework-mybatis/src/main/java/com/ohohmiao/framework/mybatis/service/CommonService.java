package com.ohohmiao.framework.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 基础Service
 *
 * @author ohohmiao
 * @date 2023-08-24 15:31
 */
public interface CommonService<T> extends IService<T> {

    /**
     * 获取实体主键值
     * @param entity 实体
     * @return 主键值数组
     */
    Object[] getPrimaryKeyVals(T entity);

}
