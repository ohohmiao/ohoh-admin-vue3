package com.ohohmiao.framework.common.listener;

import java.util.List;

/**
 * 通用数据变化监听器，实现该接口在新增/修改/删除时做相应操作
 *
 * @author ohohmiao
 * @date 2023-04-14 10:05
 */
public interface CommonDataChangeListener<T> {

    /**
     * 新增
     * @param dataList 数据集合
     */
    void doAddWithDataList(List<T> dataList);

    /**
     * 修改
     * @param dataList 数据集合
     */
    void doEditWithDataList(List<T> dataList);

    /**
     * 删除
     * @param dataIdList id集合
     */
    void doDeleteWithDataIdList(List<String> dataIdList);

}
