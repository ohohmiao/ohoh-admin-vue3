package com.ohohmiao.framework.common.annotation;

import com.ohohmiao.framework.common.validation.IsValidEnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Bean validation枚举字段验证扩展
 *
 * @author ohohmiao
 * @date 2023-04-10 14:28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = IsValidEnumValidator.class)
public @interface IsValidEnum {

    /**
     * 枚举的类型
     * @return
     */
    Class<?> value();

    /**
     * 错误信息
     * @return
     */
    String message() default "枚举类型的值不正确";

    /**
     * 获取枚举值的方法
     * @return
     */
    String method() default "getCode";

    /**
     * 验证分组
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * 验证的级别
     * @return
     */
    Class<? extends Payload>[] payload() default {};

}
