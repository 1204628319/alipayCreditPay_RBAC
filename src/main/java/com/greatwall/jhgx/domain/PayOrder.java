package com.greatwall.jhgx.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pay_order")
public class PayOrder implements Serializable {

    @TableId
    private Long id;

    /**
     * 机构商户号
     */
    private String merchantId;

    /**
     * 进件人银行卡号
     */
    private String cardNo;

    /**
     * 进件人身份证号码
     */
    private String certId;

    /**
     * 进件人手机号码
     */
    private String mobile;

    /**
     * 交易金额(单位:分)
     */
    private String orderAmt;

    /**
     * 交易金额(单位:元)
     */
    @TableField(exist = false)
    private String orderAmtByYuan;

    /**
     * 订单号
     */
    private String merOrderId;

    /**
     * 支付类型
     */
    private String payType;

    /**
     * 通道类型
     */
    private String agencyType;

    /**
     * 时间
     */
    private String transTime;

    /**
     * 付款码
     */
    private String authCode;

    /**
     * 支付状态 notPay-未支付 paying-处理中 payed-已支付 fail-失败 abnormal-异常
     */
    private String payStatus;

    /**
     * 支付请求信息
     */
    private String requestMsg;

    /**
     * 支付返回信息
     */
    private String responseMsg;

    /**
     * 支付回调信息
     */
    private String callBackMsg;

    /**
     * 订单查询返回结果
     */
    private String queryResultMsg;

    /**
     * 支付时间
     */
    private Date payTime;

    private Date createTime;
    private Date updateTime;

    @TableField(exist = false)
    private String payStatusName;
}
