package com.greatwall.jhgx.entity;

public enum ResultEnum {

    /**
     * 成功
     */
    SUCCESS("success"),
    /**
     * 失败
     */
    FAIL("fail"),

    PAYING("paying");

    private String code;

    ResultEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
