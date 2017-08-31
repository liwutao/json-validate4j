package com.timmy.json.validate.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.timmy.json.validate.exception.JSONValidateException;
import com.timmy.utils.common.CollectionUtil;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public final class ValidateHelper {
    public static List<JSONValidateRst> validateJSONStr(String jsonBody, Class<?> clazz) throws JSONValidateException {
        JSONSchema schema = JSONFormatManager.getInstance().getSchema(clazz.getSimpleName());
        if (null == schema) {
            return new ArrayList<JSONValidateRst>();
        }
        List<JSONValidateRst> result = schema.validate(jsonBody);
        return result;
    }
    
    public static List<JSONValidateRst> validateJSONObjAttr(JSONObject jsonObj, Class<?> clazz) throws JSONValidateException {
        JSONSchema schema = JSONFormatManager.getInstance().getSchema(clazz.getSimpleName());
        if (null == schema) {
            return new ArrayList<JSONValidateRst>();
        }
        List<JSONValidateRst> rsts = new ArrayList<JSONValidateRst>(10);
        // 校验属性名称
        JSONValidateRst rst = schema.checkPropertyName(jsonObj);
        if (null != rst) {
            rsts.add(rst);
            return rsts;
        }
        // 校验属性值
        return rsts;
    }
    
    public static List<JSONValidateRst> validateJSONObjsAttr(List<JSONObject> jsonObjs, Class<?> clazz) throws JSONValidateException {
        JSONSchema schema = JSONFormatManager.getInstance().getSchema(clazz.getSimpleName());
        if (null == schema) {
            return new ArrayList<JSONValidateRst>();
        }
        List<JSONValidateRst> rsts = new ArrayList<JSONValidateRst>(10);
        for (JSONObject jsonObject : jsonObjs) {
         // 校验属性名称
            JSONValidateRst rst = schema.checkPropertyName(jsonObject);
            if (null != rst) {
                rsts.add(rst);
                return rsts;
            }
        }
        return rsts;
    }
    
    /**
     * 该方法校验JSON字符串中的属性是否符合后台处理要求，主要检查是否存在不可识别的属性
     * 
     * @param jsonBody JSON字符串
     * @param clazz JSON字符串对应后台的javabean类
     * @return 校验结果
     * @throws JSONValidateException
     */
    public static List<JSONValidateRst> validateJSONAttrName(String jsonBody, Class<?> clazz) throws JSONValidateException {
        JSONSchema schema = JSONFormatManager.getInstance().getSchema(clazz.getSimpleName());
        if (null == schema) {
            return new ArrayList<JSONValidateRst>();
        }
        JSONObject jsonObject = null;
        List<JSONValidateRst> rsts = new ArrayList<JSONValidateRst>(10);
        try {
            jsonObject = JSONObject.fromObject(jsonBody);
            rsts.add(schema.checkPropertyName(jsonObject));
        } catch (JSONException e) {
            JSONValidateRst rst = new JSONValidateRst(RetCodes.MSGKEY_INVALID_JSON_FORMAT, new String[] { e.getMessage() }).setValid(false);
            rsts.add(rst);
        }
        return rsts;
    }

    /**
     * 校验扩展能力，主要实现javabean校验模板结合外部传入的模板一起实现参数校验，校验时参数格式主要参照javabean原始模板，是否必填参照外部传入模板
     * 
     * @param clazz 校验参数对应的javabean类
     * @param extSchema 外部模板
     * @param parmValMap 参数名和值映射表
     * @return 校验结果
     */
    public static List<JSONValidateRst> validatePropertiesExt(Class<?> clazz, JSONSchema extSchema, Map<String, Object> parmValMap) {
        if (null == clazz) {
            return new ArrayList<JSONValidateRst>();
        }
        JSONSchema schema = JSONFormatManager.getInstance().getSchema(clazz.getSimpleName());
        if (null == schema) {
            return new ArrayList<JSONValidateRst>();
        }
        // 将参数表转换成JSONObject对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulateAll(parmValMap);
        List<JSONValidateRst> result = schema.validatePropsExt(extSchema, jsonObject);
        return result;
    }
    
    /**
     * 校验扩展能力，主要实现javabean校验模板结合外部传入的模板一起实现json串校验，校验时参数格式主要参照javabean原始模板，是否必填参照外部传入模板
     * 
     * @param clazz 校验参数对应的javabean类
     * @param extSchema 外部模板
     * @param jsonStr
     * @return 校验结果
     */
    public static List<JSONValidateRst> validatePropertiesExt(Class<?> clazz, JSONSchema extSchema, String jsonStr) {
        JSONSchema schema = JSONFormatManager.getInstance().getSchema(clazz.getSimpleName());
        if (null == schema) {
            return new ArrayList<JSONValidateRst>();
        }
        List<JSONValidateRst> rsts = new ArrayList<JSONValidateRst>();
        JSONObject jsonObject = schema.checkJSONBasicFormat(jsonStr, rsts);
        if (CollectionUtil.isEmpty(rsts)) {
            return rsts;
        }
        JSONValidateRst rst = schema.checkPropertyName(jsonObject);
        if (null != rst) {
            rsts.add(rst);
            return rsts;
        }
        rsts = schema.validatePropsExt(extSchema, jsonObject);
        return rsts;
    }
}
