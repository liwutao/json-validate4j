package com.timmy.json.validate.core;

public interface RetCodes {
	
	/**
     * 操作成功消息码
     */
    String MSGKEY_OPERATION_SUC = "00000";
    /**
     * 操作出错消息码
     */
    String MSGKEY_OPERATION_FAIL = "13000";
    /**
     * 字段不能为空消息码
     */
    String MSGKEY_EMPTY_ERR = "10022";
    /**
     * 字段必须是数字错误消息码
     */
    String MSGKEY_NUM_REQUIRED_ERR = "10025";
    /**
     * 解析前台传来的JSON出错消息码
     */
    String MSGKEY_JSONBODY_ERROR = "10026";
    /**
     * json校验模板不存在错误
     */
    String MSGKEY_JSONSCHEMA_NOT_FOUND = "10301";
    /**
     * 客户端请求中的JSON格式错误
     */
    String MSGKEY_INVALID_JSON_FORMAT = "10302";
    /**
     * 客户端请求中的JSON属性列表和JAVA bean属性不能对应
     */
    String MSGKEY_INVALID_JSON_ATTR = "10303";
    /**
     * 客户端请求中的JSON属性值不符合要求
     */
    String MSGKEY_INVALID_ATTR_VAL = "10305";
    /**
     * 校验前台传来json时发生内部错误
     */
    String MSGKEY_JSON_VALIDATE_INTERNAL_ERR = "10306";
    /**
     * 校验前台传来起始和终止行数错误
     */
    String MSGKEY_ROW_NUM_ERR = "10307";
    /**
     * 属性值非法
     */
    String MSGKEY_INVALID_ATTR_VALUE = "10309";
    /**
     * 属性值不在指定范围内
     */
    String MSGKEY_ATTR_VALUE_RANGE_ERR = "10310";
    /**
     * 两个属性大小关系不符合要求
     */
    String MSGKEY_TWO_ATTRS_SIZE_ERR = "10311";
    /**
     * 属性a必须小于属性b
     */
    String MSGKEY_SHOULD_SMALLER_ERR = "10312";
    /**
     * 属性a必须大于属性b
     */
    String MSGKEY_SHOULD_BIGGER_ERR = "10313";
    /**
     * 不是json文件格式
     */
    String MSGKEY_TEMPLATEFILE_JSONFORMAT_ERR = "10401";
    /**
     * 输入的参数不合法
     */
    String MSGKEY_TEMPLATEFILE_PARAMETER_ERR = "10402";
    /**
     * 属性值不在合理值范围
     */
    String MSGKEY_JSON_VALIDATE_ATTR_VALUE = "10503";
    /**
     * 客户端请求中的JSON必填属性值为空
     */
    String MSGKEY_JSON_ATTR_EMPTY = "10504";
}
