package com.timmy.json.validate.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.timmy.json.validate.exception.JSONValidateException;
import com.timmy.utils.common.StrUtil;

public class PropertyFormat {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFormat.class);
    /**
     * 属性名称
     */
    private String name;
    /**
     * 是否必填项
     */
    private boolean required;
    /**
     * 数据类型
     */
    private Class<?> type;
    /**
     * 属性值需要符合的正则表达式
     */
    private String regex;
    private boolean hasMax;
    private boolean hasMin;
    /**
     * 属性最大值
     */
    private int max;
    /**
     * 属性最小值
     */
    private int min;
    /**
     * 该属性要比指定某个属性值大
     */
    private String propNameBiggerThan;
    /**
     * 该属性要比指定某个属性值小
     */
    private String propNameSmallerThan;
    /**
     * 指定的大于某个属性实际值，校验中自动装配
     */
    private Object biggerThanRefVal;
    /**
     * 指定的小于某个属性实际值，校验中自动装配
     */
    private Object smallerThanRefVal;
    /**
     * 属性校验不符合要求时使用的错误码
     */
    private String errCode;

    public boolean isRequired() {
        return required;
    }
    public void setRequired(boolean required) {
        this.required = required;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Class<?> getType() {
        return type;
    }
    public void setType(Class<?> type) {
        this.type = type;
    }
    public String getRegex() {
        return regex;
    }
    public void setRegex(String regex) {
        this.regex = regex;
    }
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public String getPropNameBiggerThan() {
        return propNameBiggerThan;
    }
    public void setPropNameBiggerThan(String propNameBiggerThan) {
        this.propNameBiggerThan = propNameBiggerThan;
    }
    public String getPropNameSmallerThan() {
        return propNameSmallerThan;
    }
    public void setPropNameSmallerThan(String propNameSmallerThan) {
        this.propNameSmallerThan = propNameSmallerThan;
    }
    public Object getBiggerThanRefVal() {
        return biggerThanRefVal;
    }
    public void setBiggerThanRefVal(Object biggerThanRefVal) {
        this.biggerThanRefVal = biggerThanRefVal;
    }
    public Object getSmallerThanRefVal() {
        return smallerThanRefVal;
    }
    public void setSmallerThanRefVal(Object smallerThanRefVal) {
        this.smallerThanRefVal = smallerThanRefVal;
    }
    
    public boolean isHasMax() {
        return hasMax;
    }
    public void setHasMax(boolean hasMax) {
        this.hasMax = hasMax;
    }
    public boolean isHasMin() {
        return hasMin;
    }
    public void setHasMin(boolean hasMin) {
        this.hasMin = hasMin;
    }
    public String getErrCode() {
        return errCode;
    }
    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
    /**
     * 根据javabean的属性对象实例化校验规则
     * 
     * @param field javabean属性
     * @return 校验规则实例
     */
    public static PropertyFormat createFormat(Field field) {
        AttrQualify ann = field.getAnnotation(AttrQualify.class);
        if (null == ann) {
            return null;
        }
        PropertyFormat format = new PropertyFormat();
        format.setName(field.getName());
        format.setRegex(ann.regex());
        format.setRequired(ann.required());
        format.setMax(ann.max());
        format.setMin(ann.min());
        format.setType(ann.type());
        format.setPropNameBiggerThan(ann.propNameBiggerThan());
        format.setPropNameSmallerThan(ann.propNameSmallerThan());
        format.setHasMax(ann.hasMax());
        format.setHasMin(ann.hasMin());
        format.setErrCode(ann.errCode());
        return format;
    }
    
    /**
     * 该方法对传入的json属性值进行校验
     * 
     * @param value json属性值
     * @return 校验结果
     * @throws JSONValidateException 
     */
    public List<JSONValidateRst> validate(Object value) throws JSONValidateException {
        List<JSONValidateRst> rsts = new ArrayList<JSONValidateRst>();
        JSONValidateRst rst = checkRequired(value, this.required);
        if (null != rst) {
            rsts.add(rst);
            return rsts;
        }
        if (StrUtil.isEmpty(value)) {
            return null;
        }
        return checkQualify(value.toString(), this);
    }
    
    /**
     * 该方法对传入的json属性值进行校验
     * 
     * @param value json属性值
     * @return 校验结果
     * @throws JSONValidateException 
     */
    public List<JSONValidateRst> validateExt(Object value, PropertyFormat extFormat) throws JSONValidateException {
        List<JSONValidateRst> rsts = new ArrayList<JSONValidateRst>();
        JSONValidateRst rst = checkRequired(value, extFormat.required);
        if (null != rst) {
            rsts.add(rst);
            return rsts;
        }
        if (StrUtil.isEmpty(value)) {
            return null;
        }
        return checkQualify(value.toString(), extFormat);
    }
    
    /**
     * 检查属性格式是否符合要求
     * 
     * @param value
     * @param format
     * @return
     * @throws JSONValidateException
     */
    private List<JSONValidateRst> checkQualify(String value, PropertyFormat format) throws JSONValidateException {
        List<JSONValidateRst> rsts = new ArrayList<JSONValidateRst>();
        JSONValidateRst rst = null;
        if (String.class == type) {
            rst = checkRegex(value);
            if (null != rst) {
                rsts.add(rst);
                return rsts;
            }
        } else if (int.class == type) {
            checkIntValue(value, rsts, format);
        } else if (List.class == type) {
            
        } else {
            return checkMutipleValue(value);
        }
        return rsts;
    }
    
    /**
     * 检查整数类型的值是否符合要求
     * 
     * @param value
     * @param rsts
     * @param extFormat
     */
    private void checkIntValue(String value, List<JSONValidateRst> rsts, PropertyFormat extFormat) {
        JSONValidateRst rst;
        // 校验是不是整数
        int intVal;
        try {
            intVal = Integer.parseInt(value);
            // 校验最大最小值
            // 校验引用值限制
            if (((hasMax && intVal > max) || (hasMin && intVal < min))) {
                    rst = new JSONValidateRst(RetCodes.MSGKEY_ATTR_VALUE_RANGE_ERR, new String[] {name}).setValid(false);
                    rsts.add(rst);
            } else if ((!StrUtil.isEmpty(propNameBiggerThan) && StrUtil.isNum(extFormat.getBiggerThanRefVal()) 
                            && intVal < Integer.parseInt(extFormat.getBiggerThanRefVal().toString()))) {
                rst = new JSONValidateRst(RetCodes.MSGKEY_SHOULD_BIGGER_ERR, new String[] {name, propNameBiggerThan}).setValid(false);
                rsts.add(rst);
            } else if ((!StrUtil.isEmpty(propNameSmallerThan) && StrUtil.isNum(extFormat.getSmallerThanRefVal()) 
                            && intVal > Integer.parseInt(extFormat.getSmallerThanRefVal().toString()))) {
                rst = new JSONValidateRst(RetCodes.MSGKEY_SHOULD_SMALLER_ERR, new String[] {name, propNameSmallerThan}).setValid(false);
                rsts.add(rst);
            }
        } catch (NumberFormatException e) {
            rst = new JSONValidateRst(RetCodes.MSGKEY_NUM_REQUIRED_ERR, new String[] {name}).setValid(false);
            rsts.add(rst);
        }
    }
    
    /**
     * 该方法实现对非java基本类型属性的校验
     * 
     * @param value json属性值
     * @return 校验结果
     * @throws JSONValidateException 
     */
    private List<JSONValidateRst> checkMutipleValue(String value) throws JSONValidateException {
        try {
            return ValidateHelper.validateJSONStr(value, type);
        } catch (Throwable e) {
            String errMsg = "Error occured when validating JSON string";
            LOGGER.error(errMsg, e);
            throw new JSONValidateException(e.getMessage());
        }
    }
    
    /**
     * 该方法实现对json属性值的正则校验
     * 
     * @param value json属性值
     * @return 校验结果
     */
    private JSONValidateRst checkRegex(String value) {
        if (!StrUtil.isEmpty(regex) && !Pattern.matches(regex, value)) {
            JSONValidateRst rst = new JSONValidateRst(this.errCode, new String[] {name}).setValid(false);
            return rst;
        }
        return null;
    }
    
    /**
     * 该方法对必填属性是否为空进行校验
     * 
     * @param value json属性值
     * @return 校验结果
     */
    private JSONValidateRst checkRequired(Object value, boolean required) {
        if (required && StrUtil.isEmpty(value)) {
            JSONValidateRst rst = new JSONValidateRst(RetCodes.MSGKEY_EMPTY_ERR, new String[] {name}).setValid(false);
            return rst;
        }
        return null;
    }
}
