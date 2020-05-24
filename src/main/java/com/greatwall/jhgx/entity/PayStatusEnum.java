
package com.greatwall.jhgx.entity;

public enum PayStatusEnum {  //notPay-未支付 paying-处理中 payed-已支付 fail-交易失败 abnormal-交易异常
    /**
     * 支付
     **/
    notPay("notPay", "未支付"),
    /**
     * 处理中
     **/
    paying("paying", "处理中"),
    /**
     * 已支付
     **/
    payed("payed", "已支付"),
    /**
     * 交易失败
     **/
    fail("fail", "交易失败"),
    /**
     * 交易异常
     **/
    abnormal("abnormal", "交易异常");

    private String value;
    private String name;

    PayStatusEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
