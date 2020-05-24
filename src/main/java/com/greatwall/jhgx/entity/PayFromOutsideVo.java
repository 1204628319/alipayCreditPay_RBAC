package com.greatwall.jhgx.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PayFromOutsideVo {
    /**
     * 付款码
     */
    private String authCode;

    /**
     * 金额（分为单位）
     */
    private String orderAmt;

    /**
     * 进件人id
     */
    private String memberId;

    /**
     * 签名
     */
    private String sign;
}
