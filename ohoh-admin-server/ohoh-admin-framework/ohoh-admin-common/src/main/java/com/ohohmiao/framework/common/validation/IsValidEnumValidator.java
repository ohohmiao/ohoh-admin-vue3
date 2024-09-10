package com.ohohmiao.framework.common.validation;

import cn.hutool.core.collection.CollectionUtil;
import com.ohohmiao.framework.common.annotation.IsValidEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 枚举字段Bean Validation验证
 *
 * @author ohohmiao
 * @date 2023-04-10 14:32
 */
@Slf4j
public class IsValidEnumValidator implements ConstraintValidator<IsValidEnum, Object> {

    /**
     * 存具体枚举的值
     */
    private final List<Object> values = CollectionUtil.newArrayList();

    @Override
    public void initialize(IsValidEnum constraintAnnotation) {
        Class<?> enumClazz = constraintAnnotation.value();
        Object[] enumConstants = enumClazz.getEnumConstants();
        if (enumConstants == null) {
            return;
        }
        Method method = BeanUtils.findMethod(enumClazz, constraintAnnotation.method());
        if (method == null) {
            log.warn("枚举对象：[{}]中不存在方法:[{}]，请检查！", enumClazz.getName(), constraintAnnotation.method());
            throw new CommonException();
        }

        method.setAccessible(true);
        try {
            for (Object enumConstant : enumConstants) {
                values.add(method.invoke(enumConstant));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.warn("获取枚举类：[{}]的枚举对象的值失败！", enumClazz);
            throw new CommonException();
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value == null || values.contains(value);
    }

}
