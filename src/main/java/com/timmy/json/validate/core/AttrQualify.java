package com.timmy.json.validate.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AttrQualify {
    /**
     * 是否必填
     */
    boolean required() default false;
    /**
     * 校验属性的正则
     */
    String regex() default "";
    /**
     * 是否有最大值限制
     */
    boolean hasMax() default false;
    /**
     * 是否有最小值限制
     */
    boolean hasMin() default false;
    /**
     * 最大值
     */
    int max() default 0;
    /**
     * 最小值
     */
    int min() default 0;
    /**
     * 指定的大于某个属性实际值，校验中自动装配
     */
    String propNameBiggerThan() default "";
    /**
     * 该属性要比指定某个属性值小
     */
    String propNameSmallerThan() default "";
    /**
     * 属性类型
     */
    Class<?> type() default String.class;
    /**
     * 当type是List/Array时，指定集合中的元素类型
     */
    Class<?> innerType() default String.class;
    /**
     * 属性不符合要求时的错误码
     */
    String errCode() default "";
}
