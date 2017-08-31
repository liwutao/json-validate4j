package com.timmy.json.validate.core;

import java.util.HashMap;
import java.util.Map;

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
