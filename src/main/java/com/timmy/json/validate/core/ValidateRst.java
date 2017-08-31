package com.timmy.json.validate.core;

public class ValidateRst {
	String MSGKEY_OPERATION_SUC = "00000";
    /**
     * 校验结果码
     */
    private String code;
    /**
     * 校验结果信息，默认信息是OK
     */
    private String msg = MSGKEY_OPERATION_SUC;
    /**
     * 校验结果：true-校验通过     false-校验失败
     */
    private boolean isValid = true;
    
    public ValidateRst(boolean isValid, String code) {
        this.isValid = isValid;
        this.code = code;
    }
    
    public ValidateRst(boolean isValid, String code, String msg) {
        this(isValid, code);
        this.msg = msg;
    }
    
    public ValidateRst () {
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public boolean isValid() {
        return isValid;
    }
    public void setValid(boolean isValid) {
        this.isValid = isValid;
    } 
}
