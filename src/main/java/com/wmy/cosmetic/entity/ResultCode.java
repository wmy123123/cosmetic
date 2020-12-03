package com.wmy.cosmetic.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResultCode {
    //成功功状态码
    SUCCESS(1,"成功"),
    PARAM_NOT_LOGGET_IN(2001,"账号不存在"),
    USER_ACCOUT_FORBIDDEN(2002,"账号已被禁用"),
    USER_NOT_EXIST(2003,"用户不存在"),
    USER_HAS_EXISTED(2004,"用户已存在"),
    USER_ERROR_VerificationCode(2005,"验证码不正确"),
    SUCCESS_FIND_EMPLOYEE_lIST(0,"查询雇员表成功"),
    ERR_FIND_EMPLOYEE_LIST(0,"查询雇员表失败"),
    ERR_REGIST_EMPLOTEE(2006,"用户名已存在"),
    ERR_LOGIN_PASSWORD(2007,"密码错误"),
    ERR_UPDATE_EMPLOYEE(5001,"员工信息修改失败"),
    SUCCESS_UPDATE_EMPLOYEE(2008,"员工信息修改成功"),
    ERR_DELETE_EMPLOYEE(5002,"删除员工失败"),
    SUCCESS_DELETE_EMPLOYEE(2009,"删除员工成功"),
    SUCCESS_ADD_EMPLOYEE(2010,"添加员工成功"),
    ERR_ADD_EMPLOYEE(5003,"添加员工失败"),
    SUCCESS_UPDATE_PASSWORD(2011,"修改密码成功"),
    ERR_UPDATE_PASSWORD(5004,"修改密码失败"),
    ERR_PASSWORD(2013,"原密码不正确,请重新输入");
    private Integer code;
    private String message;
    public Integer code(){
        return this.code;
    }
    public String message(){
        return this.message;
    }
}
