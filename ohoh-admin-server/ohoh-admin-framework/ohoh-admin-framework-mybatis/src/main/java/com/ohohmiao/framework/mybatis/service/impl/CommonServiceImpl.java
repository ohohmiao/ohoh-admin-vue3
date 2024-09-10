package com.ohohmiao.framework.mybatis.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.framework.mybatis.service.CommonService;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 基础Service实现类
 *
 * @author ohohmiao
 * @date 2023-08-24 15:32
 */
public class CommonServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements CommonService<T> {

    @Override
    public Object[] getPrimaryKeyVals(T entity){
        List<Object> primarykeyVals = CollectionUtil.newArrayList();
        Field[] fields = entity.getClass().getDeclaredFields();
        for(Field field: fields){
            try {
                if(field.isAnnotationPresent(TableId.class)) {
                    field.setAccessible(true);
                    primarykeyVals.add(field.get(entity));
                    break;
                }
            }catch (IllegalAccessException e) {
                log.error(ExceptionUtil.stacktraceToString(e));
            }
        }
        return primarykeyVals.toArray();
    }

}
