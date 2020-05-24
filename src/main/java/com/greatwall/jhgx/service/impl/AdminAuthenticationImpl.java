package com.greatwall.jhgx.service.impl;

import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.componnet.auth.util.DefaultJwtTokenUtil;
import com.greatwall.jhgx.cache.GuavaCacheService;
import com.greatwall.jhgx.security.AdminJwtAuthorizationTokenFilter;
import com.greatwall.jhgx.security.model.AuthenticationInfo;
import com.greatwall.jhgx.service.AdminAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangcan
 * @date 2020/3/5 10:48
 */
@Service
public class AdminAuthenticationImpl implements AdminAuthentication {

    @Autowired
    private DefaultJwtTokenUtil jwtTokenUtil;

    @Autowired
    private GuavaCacheService guavaCacheService;

    @Autowired
    AdminJwtAuthorizationTokenFilter authenticationTokenFilter;

    @Override
    public AuthenticationInfo getAuthenticationInfo(final DefaultJwtUserDetails jwtUser, String userName) {
        // 生成令牌
        final String token = jwtTokenUtil.generateToken(jwtUser);

        // 删除之前登录同一用户的redisTokenKey
        authenticationTokenFilter.clearToken(userName);

        // 缓存登录token，后续用来验证是否是同一用户登录
        String tokenKey = String.join("_", "tokenId", userName);
        guavaCacheService.put(tokenKey, token);

        // 返回 token
        return new AuthenticationInfo(token, jwtUser);
    }
}
