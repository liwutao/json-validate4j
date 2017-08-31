package com.timmy.json.validate.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright @2015 海尔集团 All rights reserved.
 * 广科数字技术有限公司专有/保密源代码,未经许可禁止任何人通过任何渠道使用、修改源代码.
 *
 * 该类型负责需要参与JSON校验的javabean对应校验模板管理，提供模板注册和获取能力
 * @author LIWT
 * @create 2015年12月23日下午7:21:11
 */
public class JSONFormatManager {
    private static final JSONFormatManager INSTANCE = new JSONFormatManager();
    private Map<String, JSONSchema> schemas = new HashMap<String, JSONSchema>();
    private Map<String, JSONSchema> extSchemas = new HashMap<String, JSONSchema>();
    
    public static JSONFormatManager getInstance() {
        return INSTANCE;
    }
    
    private JSONFormatManager() {
    }
    
    /**
     * 注册javabean类的JSON模板
     * 
     * @param clazz
     */
    public void registerJSONBean(Class<?> clazz) {
        JSONSchema schema = JSONSchema.createSchema(clazz);
        schemas.put(clazz.getSimpleName(), schema);
    }

    public JSONSchema getSchema(String schemaName) {
        return schemas.get(schemaName);
    }
    public void addExtSchema(String name, JSONSchema schema) {
        extSchemas.put(name, schema);
    }
    
    public JSONSchema getExtSchema(String name) {
        return extSchemas.get(name);
    }
}
