package com.ohohmiao.modules.workflow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 流程实体
 * @author ohohmiao
 * @date 2025-06-12 15:24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface FlowEntity {

    /**
     * vo类
     * @return
     */
    Class<?> value();

    /**
     * dto类
     * @return
     */
    Class<?> dto();

}
