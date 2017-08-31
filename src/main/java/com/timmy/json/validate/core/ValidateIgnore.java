package com.timmy.json.validate.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Copyright @2015 海尔集团 All rights reserved.
 * 广科数字技术有限公司专有/保密源代码,未经许可禁止任何人通过任何渠道使用、修改源代码.
 *
 * 该注解用来标注某个类或者方法或者属性不需要校验
 * @author LIWT
 * @create 2016年1月21日下午2:50:15
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateIgnore {
}
