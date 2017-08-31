package com.timmy.json.validate.core;

/**
 * Copyright @2015 海尔集团 All rights reserved.
 * 广科数字技术有限公司专有/保密源代码,未经许可禁止任何人通过任何渠道使用、修改源代码.
 *
 * 该接口适用于需要进行数据校验的对象，需要校验的类型实现该类后，具体的校验行为的在validate方法中实现
 * @author LIWT
 * @create 2015年11月16日 下午3:28:12
 */
public interface Validatable {

    /**
     * 该接口方法主要实现校验的具体实现
     * 
     * @return 校验结果
     */
    ValidateRst validate();
}
