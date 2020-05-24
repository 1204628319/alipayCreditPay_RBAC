package com.greatwall.jhgx.entity;

import com.greatwall.component.ccyl.common.consants.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author : TianLei
 */
public class Result<T> implements Serializable {
    /**
     * 数据
     */
    private T data;
    /**
     * 返回编码
     */
    private String resultCode;
    /**
     * 返回消息
     */
    private String resultMsg;
    /**
     * 错误消息
     */
    private String errorMsg;
    /**
     * 服务器返回时间戳
     */
    private long sysTime;

    public static <T> Result<T> succeed(String msg) {
        return succeedWith(null, ResultCodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return succeedWith(model, ResultCodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model) {
        return succeedWith(model, ResultCodeEnum.SUCCESS.getCode(), "");
    }

    public static <T> Result<T> succeedWith(T data, String code, String msg) {
        return new Result<>(data, code, msg, "", System.currentTimeMillis());
    }

    public static <T> Result<T> failed(String msg) {
        return failedWith(null, ResultCodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failed(T model, String msg) {
        return failedWith(model, ResultCodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failedWith(T data, String code, String msg) {
        return new Result<>(data, code, msg, msg, System.currentTimeMillis());
    }

    public static <T> Result<T> paying(T data, String msg) {
        return new Result<>(data, ResultEnum.PAYING.getCode(), msg, null, System.currentTimeMillis());
    }

    public static <T> Result<T> paying(T data) {
        return new Result<>(data, ResultEnum.PAYING.getCode(), StringUtils.EMPTY, null, System.currentTimeMillis());
    }

    public static Result paying(String msg) {
        return new Result(null, ResultEnum.PAYING.getCode(), StringUtils.EMPTY, msg, System.currentTimeMillis());
    }

    public static boolean isSuccess(Result result) {
        if (result != null && StringUtils.isNotEmpty(result.getResultCode())
                && ResultCodeEnum.SUCCESS.getCode().equals(result.getResultCode())) {
            return true;
        } else {
            return false;
        }
    }

    public Result(T data, String resultCode, String resultMsg, String errorMsg, long sysTime) {
        this.data = data;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.errorMsg = errorMsg;
        this.sysTime = sysTime;
    }

    public Result() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public long getSysTime() {
        return sysTime;
    }

    public void setSysTime(long sysTime) {
        this.sysTime = sysTime;
    }
}
