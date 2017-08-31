package com.timmy.json.validate.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.timmy.json.validate.exception.JSONValidateException;
import com.timmy.utils.common.CollectionUtil;
import com.timmy.utils.common.ReflectionUtil;
import com.timmy.utils.common.StrUtil;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * Copyright @2015 海尔集团 All rights reserved.
 * 广科数字技术有限公司专有/保密源代码,未经许可禁止任何人通过任何渠道使用、修改源代码.<br><br>
 *
 * 该类型描述需要参与JSON格式校验的javabean的校验模板，一个JavaBean类对应一个校验模板实例
 * @author LIWT
 * @create 2015年12月23日下午7:22:43
 */
public class JSONSchema {
    /**
     * 模板名称--也就是javabean的类名
     */
    private String name;
    /**
     * javabean中所有属性名称
     */
    private Set<String> propertyNames = new HashSet<String>();
    /**
     * javabean中所有属性对象和名称映射表
     */
    private Map<String, Field> fieldMap = new HashMap<String, Field>();
    /**
     * 属性名称和属性格式校验对象
     */
    private Map<String, PropertyFormat> formatMap = new HashMap<String, PropertyFormat>();


    public Set<String> getPropertyNames() {
        return propertyNames;
    }

    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }

    public Map<String, PropertyFormat> getFormatMap() {
        return formatMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 为指定java类创建对应JSON串的校验模板，遍历类中所有字段并未每个字段创建格式模板
     * 
     * @param clazz 类对象
     * @return JSON校验模板
     */
    public static JSONSchema createSchema(Class<?> clazz) {
        List<Field> fields = ReflectionUtil.getAllFieds(clazz);
        JSONSchema schema = new JSONSchema();
        for (Field field : fields) {
            String fieldName = field.getName();
            schema.getPropertyNames().add(fieldName);
            schema.getFieldMap().put(fieldName, field);
            // 创建每个字段的校验格式对象
            PropertyFormat format = PropertyFormat.createFormat(field);
            if (null == format) {
                continue;
            }
            schema.getFormatMap().put(fieldName, format);
        }
        return schema;
    }

    /**
     * JSON校验的入口方法，会校验JSON中属性是否和对应类字段匹配，如果JSON中出现了类中没有的字段，则返回校验失败的结果。进一步校验JSON属性值是否符合类字段对值得限制。
     * 
     * @param jsonStr JSON字符串
     * @return 校验结果  如果校验通过返回--null,  存在校验失败则返回校验失败结果列表
     * @throws JSONValidateException
     */
    public List<JSONValidateRst> validate(String jsonStr) throws JSONValidateException {
        List<JSONValidateRst> rsts = new ArrayList<JSONValidateRst>(10);
        JSONObject jsonObject = checkJSONBasicFormat(jsonStr, rsts);
        if (!CollectionUtil.isEmpty(rsts)) {
            return rsts;
        }
        // 校验属性名称
        JSONValidateRst rst = checkPropertyName(jsonObject);
        if (null != rst) {
            rsts.add(rst);
            return rsts;
        }
        // 校验属性值
        return checkPropertyQualify(jsonObject);
    }

    public JSONObject checkJSONBasicFormat(String jsonStr, List<JSONValidateRst> rsts) {
        try {
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            return jsonObject;
        } catch (JSONException e) {
            JSONValidateRst rst = new JSONValidateRst(RetCodes.MSGKEY_INVALID_JSON_FORMAT, new String[] { e.getMessage() }).setValid(false);
            rsts.add(rst);
            return null;
        }
    }

    /**
     * 校验JSON串中属性值是否符合模板中各属性要求。
     * 
     * @param jsonObject JSON字符串
     * @return 返回校验失败结果列表
     * @throws JSONValidateException
     */
    public List<JSONValidateRst> checkPropertyQualify(JSONObject jsonObject) throws JSONValidateException {
        List<JSONValidateRst> rsts = new ArrayList<JSONValidateRst>();
        Set<Entry<String, PropertyFormat>> propertyFormats = formatMap.entrySet();
        for (Entry<String, PropertyFormat> entry : propertyFormats) {
            Object propValue = jsonObject.get(entry.getKey());
            PropertyFormat format = entry.getValue();
            if (null == format) {
                continue;
            }
            List<JSONValidateRst> propRsts = format.validate(propValue);
            if (!CollectionUtil.isEmpty(propRsts)) {
                rsts.addAll(propRsts);
                return rsts;
            }
        }
        return rsts;
    }

    /**
     * 校验JSON串中的属性名称，JSON中的属性名称集合必须是类对应shcema中属性集合的子集。
     * 
     * @param jsonObject JSON字符串
     * @return 校验结果  如果校验通过返回--null,  存在校验失败则返回校验失败结果列表
     */
    @SuppressWarnings("unchecked")
    public JSONValidateRst checkPropertyName(JSONObject jsonObject) {
        // JSON字符串中的属性名称列表
        Set<String> jsonProps = jsonObject.keySet();
        Set<String> jsonPropsCopy = new HashSet<String>(jsonProps);
        if (!propertyNames.containsAll(jsonProps)) {
            JSONValidateRst rst = new JSONValidateRst(RetCodes.MSGKEY_INVALID_JSON_ATTR,
                    new String[] {jsonPropsCopy.toString()}).setValid(false);
            jsonPropsCopy.removeAll(propertyNames);
            return rst;
        }
        return null;
    }

    /**
     * 根据外部传入的扩展校验模板和JSONObject完成校验
     * 
     * @param extSchema 扩展模板
     * @param jsonObject json对象
     * @return 校验结果
     */
    public List<JSONValidateRst> validatePropsExt(JSONSchema extSchema, JSONObject jsonObject) {
        Set<Entry<String, PropertyFormat>> extProps = extSchema.getFormatMap().entrySet();
        List<JSONValidateRst> rsts = new ArrayList<JSONValidateRst>();
        for (Entry<String, PropertyFormat> entry : extProps) {
            Object value = jsonObject.get(entry.getKey());
            PropertyFormat extFormat = entry.getValue();
            PropertyFormat format = formatMap.get(extFormat.getName());
            if (null == format) {
                continue;
            }
            // 填充引用值
            if (!StrUtil.isEmpty(format.getPropNameSmallerThan())) {
                extFormat.setSmallerThanRefVal(jsonObject.get(format.getPropNameSmallerThan()));
            }
            if (!StrUtil.isEmpty(format.getPropNameBiggerThan())) {
                extFormat.setBiggerThanRefVal(jsonObject.get(format.getPropNameBiggerThan()));
            }
            try {
                List<JSONValidateRst> propRsts = format.validateExt(value, extFormat);;
                if (!CollectionUtil.isEmpty(propRsts)) {
                    rsts.addAll(propRsts);
                    return rsts;
                }
            } catch (JSONValidateException e) {
                // 此处不会出现该异常，不予处理
                continue;
            }
        }
        return null;
    }
}
