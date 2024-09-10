package com.ohohmiao.framework.common.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Map;

/**
 * 数据变更监听事件中心
 *
 * @author ohohmiao
 * @date 2023-04-14 10:09
 */
public class CommonDataChangeEventCenter {

    /**
     * 监听器类名与监听器映射
     */
    private static Map<String, CommonDataChangeListener> listenerMap = MapUtil.newHashMap();

    /**
     * 注册-供spring上下文注入
     * @param listenerList 监听器集合
     */
    public static void registerListenerList(List<CommonDataChangeListener> listenerList) {
        for(CommonDataChangeListener listener: listenerList) {
            listenerMap.put(listener.getClass().getSimpleName(), listener);
        }
    }

    /**
     * 新增数据
     * @param listenerName 监听器名称
     * @param data 数据
     */
    public static void doAddWithData(String listenerName, Object data) {
        CommonDataChangeListener listener = listenerMap.get(listenerName);
        if(ObjectUtil.isNotNull(listener)) {
            listener.doAddWithDataList(ObjectUtil.isNotNull(data)? CollUtil.newArrayList(data): CollUtil.newArrayList());
        }
    }

    /**
     * 新增数据集合
     * @param listenerName 监听器名称
     * @param dataList 数据集合
     */
    public static void doAddWithDataList(String listenerName, List<Object> dataList) {
        CommonDataChangeListener listener = listenerMap.get(listenerName);
        if(ObjectUtil.isNotNull(listener)) {
            listener.doAddWithDataList(dataList);
        }
    }

    /**
     * 修改数据
     * @param listenerName 监听器名称
     * @param data 数据
     */
    public static void doEditWithData(String listenerName, Object data){
        CommonDataChangeListener listener = listenerMap.get(listenerName);
        if(ObjectUtil.isNotNull(listener)) {
            listener.doEditWithDataList(ObjectUtil.isNotNull(data)? CollUtil.newArrayList(data): CollUtil.newArrayList());
        }
    }

    /**
     * 修改数据集合
     * @param listenerName 监听器名称
     * @param dataList 数据集合
     */
    public static void doEditWithDataList(String listenerName, List<Object> dataList){
        CommonDataChangeListener listener = listenerMap.get(listenerName);
        if(ObjectUtil.isNotNull(listener)) {
            listener.doEditWithDataList(dataList);
        }
    }

    /**
     * 删除数据
     * @param listenerName 监听器名称
     * @param id 主键id
     */
    public static void doDeleteWithId(String listenerName, String id){
        CommonDataChangeListener listener = listenerMap.get(listenerName);
        if(ObjectUtil.isNotNull(listener)) {
            listener.doDeleteWithDataIdList(StrUtil.isNotEmpty(id)? CollUtil.newArrayList(id): CollUtil.newArrayList());
        }
    }

    /**
     * 删除数据集合
     * @param listenerName 监听器名称
     * @param idList 主键id集合
     */
    public static void doDeleteWithIdList(String listenerName, List<String> idList){
        CommonDataChangeListener listener = listenerMap.get(listenerName);
        if(ObjectUtil.isNotNull(listener)) {
            listener.doDeleteWithDataIdList(idList);
        }
    }

}
