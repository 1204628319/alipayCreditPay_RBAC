package com.greatwall.jhgx.security.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author jie
 * @date 2018-11-30
 */
@Getter
@Setter
public class AdminAuthorizationUser {

    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 图形验证码
     */
    @NotBlank
    private String imageCode;

    /**
     * 登录类型, 扫码登录时为scan
     */
    private String loginType;

    /**
     * 微信code, 通过code缓存的openId, 绑定账户
     */
    private String code;

    @Override
    public String toString() {
        return "{username=" + username  + ", imageCode=" +imageCode + ", loginType=" +loginType +", password= ******}";
    }
}
