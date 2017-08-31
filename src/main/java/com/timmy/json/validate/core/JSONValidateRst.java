package com.timmy.json.validate.core;

/**
 * Copyright @2015 海尔集团 All rights reserved.
 * 广科数字技术有限公司专有/保密源代码,未经许可禁止任何人通过任何渠道使用、修改源代码.
 *
 * JSON字符串格式校验结果
 * @author LIWT
 * @create 2015年12月23日下午7:25:26
 */
public class JSONValidateRst {

    /**
     * 是否合法
     */
    private boolean valid;
    /**
     * 校验结果信息
     */
    private String msg;
    /**
     * 校验错误码
     */
    private String errCode;
    /**
     * 国际化资源中补位参数的值列表
     */
    private Object[] args;
    public JSONValidateRst() {
    }
    
    
    public JSONValidateRst(String errCode) {
        this.errCode = errCode;
    }
    
    public JSONValidateRst(String errCode, Object[] args) {
        this(errCode);
        this.args = args;
    }
    
    public JSONValidateRst(String errCode, String msg) {
        this(errCode);
        this.msg = msg;
    }
    
    public boolean isValid() {
        return valid;
    }
    public JSONValidateRst setValid(boolean valid) {
        this.valid = valid;
        return this;
    }
    public String getMsg() {
        return msg;
    }
    public JSONValidateRst setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    public String getErrCode() {
        return errCode;
    }
    public JSONValidateRst setErrCode(String errCode) {
        this.errCode = errCode;
        return this;
    }
    public Object[] getArgs() {
        return args;
    }
    public void setArgs(Object[] args) {
        this.args = args;
    }
}
