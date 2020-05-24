package com.greatwall.jhgx.constants;

/**
 * admin公共错误编码
 *
 * @author zsd
 * @date 2020/2/13
 */
public interface AdminErrorCode {
    /**
     * 密码错误次数达到上限
     */
    String MAX_PASSWORD_ERROR_CODE = "00000001";
    /**
     * 已绑定
     */
    String BINDED_ERROR_CODE = "00000002";
    /**
     * 输入要绑定的账号
     */
    String BINDING_CODE = "00000003";
    /**
     * 绑定超时
     */
    String BINDING_TIME_OUT = "00000005";


}
