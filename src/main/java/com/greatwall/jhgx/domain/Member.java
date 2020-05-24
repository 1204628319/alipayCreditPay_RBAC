package com.greatwall.jhgx.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("member")
public class Member {
    @TableId
    private Long id;
    private String merchantId;
    private String agencyType;
    private String realName;
    private String certId;
    private String merName;
    private String merState;
    private String merCity;//
    private String merAddress;
    private String merOrderId;
    private String consRate;
    private String consFee;
    private String settleCardNo;
    private String settleMobile;

    /**
     * 注册状态 notSign-未开始进件 signing-处理中 signed-进件成功 refused-已拒绝  fail-失败 abnormal-异常
     */
    private String signStatus;

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

    private Long createBy;
    private String createTime;
    private String updateTime;

    @TableField(exist = false)
    private String signStatusName;

    /**
     * 未支付金额
     */
    @TableField(exist = false)
    private String notPayFee;

    /**
     * 处理中金额
     */
    @TableField(exist = false)
    private String payingFee;

    /**
     * 已支付金额
     */
    @TableField(exist = false)
    private String payedFee;

    /**
     * 失败金额
     */
    @TableField(exist = false)
    private String failFee;

    /**
     * 异常金额
     */
    @TableField(exist = false)
    private String abnormalFee;

    /**
     * 总金额
     */
    @TableField(exist = false)
    private String totalFee;

    @TableField(exist = false)
    private Integer page;

    @TableField(exist = false)
    private Integer size;
}
