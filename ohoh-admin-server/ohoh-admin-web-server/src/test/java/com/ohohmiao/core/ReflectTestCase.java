package com.ohohmiao.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ohohmiao.BaseTestCase;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.system.model.entity.SysRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author ohohmiao
 * @date 2023-08-24 15:22
 */
@Slf4j
public class ReflectTestCase extends BaseTestCase {

    @Test
    public void getTableIdField(){
        SysRes sysRes = new SysRes();
        sysRes.setResId("1");
        sysRes.setResName("测试菜单名称");
        Field[] fields = sysRes.getClass().getDeclaredFields();
        Object primarykeyVal = null;
        for(Field field: fields){
            try {
                if(field.isAnnotationPresent(TableId.class)) {
                    field.setAccessible(true);
                    primarykeyVal = field.get(sysRes);
                    break;
                }
            }catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void listSaCheckPermissionMethods(){
        List<String> permissionList = CollectionUtil.newArrayList();
        SpringUtil.getApplicationContext().getBeansOfType(RequestMappingHandlerMapping.class).values()
            .forEach(requestMappingHandlerMapping -> requestMappingHandlerMapping.getHandlerMethods().forEach((key, value) -> {
                SaPcCheckPermission saPcCheckPermission = value.getMethod().getAnnotation(SaPcCheckPermission.class);
                if(ObjectUtil.isNotNull(saPcCheckPermission)){
                    PatternsRequestCondition patternsCondition = key.getPatternsCondition();
                    if(ObjectUtil.isNotNull(patternsCondition)){
                        permissionList.add(patternsCondition.getPatterns().iterator().next());
                    }
                }
            }));
        CollectionUtil.sortByPinyin(permissionList).forEach(System.out::println);
    }

}
