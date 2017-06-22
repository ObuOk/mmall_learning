package com.mmall.common;

/**
 * Created by guojianzhong on 2017/6/22.
 */
public enum ResponseCode {

    SUCESS(0,"SUCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL ARGUMENT");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
