package com.greatwall.jhgx.entity;

import lombok.Data;

@Data
public class CallbackVo {
    private String merOrderId;
    private String msg;
    private String resCode;
    private String transAmt;
    private String transTime;
    private String settStatus;
    private String settMsg;
    private String sign;
}
