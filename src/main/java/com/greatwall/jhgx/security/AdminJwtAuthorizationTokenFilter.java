package com.greatwall.jhgx.security;

import com.greatwall.componnet.auth.util.DefaultJwtTokenUtil;
import com.greatwall.jhgx.cache.GuavaCacheService;
import com.greatwall.jhgx.constants.DefaultSecurityConstants;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户token刷新
 * @author zsd
 **/
@Slf4j
@Component
public class AdminJwtAuthorizationTokenFilter extends OncePerRequestFilter {
    /**
     * 用户操作类
     */
    private final UserDetailsService userDetailsService;
    /**
     * 本地缓存
     */
    @Autowired
    private GuavaCacheService guavaCacheService;
    /**
     * jwtToken工具类
     */
    private final DefaultJwtTokenUtil jwtTokenUtil;
    /**
     * tokenHeader设计
     */
    private final String tokenHeader;
    /**
     * 头部token前缀
     */
    private final String tokenPrefix;

    /**
     * 之前的token
     */
    private final String beforeTokenKeyPrefix = "before_Token_Key_";

    /**
     * 构造函数
     * @param userDetailsService
     * @param jwtTokenUtil
     * @param tokenHeader
     */
    public AdminJwtAuthorizationTokenFilter(@Qualifier("adminJwtUserDetailsService") UserDetailsService  userDetailsService,
                                            @Qualifier("jwtTokenUtil") DefaultJwtTokenUtil jwtTokenUtil,
                                            @Value("${jwt.header}") String tokenHeader,
                                            @Value("${jwt.prefix}") String tokenPrefix) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenHeader = tokenHeader;
        this.tokenPrefix = tokenPrefix;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestHeader = request.getHeader(this.tokenHeader);
        String username = null;
        String authToken = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(requestHeader) && requestHeader.startsWith(tokenPrefix)) {
            try {
                authToken = requestHeader.substring(tokenPrefix.length());
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (ExpiredJwtException e) {
                log.error("Jwt调用超时", e);
            }
        }

        if (StringUtils.isNotBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            String tokenKey = buildTokenKey(username);
            // 检验token信息, 现只校验了token是否过期, 若需要增加其它校验在validateToken中增加即可
            String cacheToken = (String) guavaCacheService.getIfPresent(tokenKey);
            if (jwtTokenUtil.validateToken(authToken)) {
                String beforeTokenKey = this.beforeTokenKey(tokenKey);
                // 用来区分是同一用户登录还是token过期
                if (!authToken.equals(cacheToken) && !authToken.equals(guavaCacheService.getIfPresent(beforeTokenKey))) {
                    request.setAttribute("offline", true);
                } else {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("authorizated user '{}', setting security context", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    if(jwtTokenUtil.isTimeToRefresh(authToken)){
                        // 更新token
                        String newToken = jwtTokenUtil.refreshToken(authToken);
                        // 将tokenHeader显示在headers，不然setHeader不生效
                        response.setHeader("Access-Control-Expose-Headers",this.tokenHeader);
                        response.setHeader(this.tokenHeader, newToken);
                        // 更新缓存登录token，用来验证是否是同一用户登录
                        guavaCacheService.put(beforeTokenKey, authToken);
                        guavaCacheService.put(tokenKey, newToken);
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * 生成Token保存key
     * @param userName  登录账户名
     * @return
     */
    private String buildTokenKey(String userName) {
        return DefaultSecurityConstants.DEFAULT_TOKEN_KEY + '_' + userName;
    }

    private String beforeTokenKey(String tokenKey) {
        return beforeTokenKeyPrefix + tokenKey;
    }

    public void clearToken(String userName){
        String tokenKey = buildTokenKey(userName);
        guavaCacheService.invalidate(tokenKey);
        guavaCacheService.invalidate(this.beforeTokenKey(tokenKey));
    }
}
