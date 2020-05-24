package com.greatwall.jhgx.service;


import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.security.model.AuthenticationInfo;

/**
 * @author wangcan
 * @date 2020/3/5 10:35
 */
public interface AdminAuthentication {

    /**
     * 用户登录
     *
     * @param jwtUser
     * @param userName
     * @return
     */
    AuthenticationInfo getAuthenticationInfo(final DefaultJwtUserDetails jwtUser, String userName);
}
