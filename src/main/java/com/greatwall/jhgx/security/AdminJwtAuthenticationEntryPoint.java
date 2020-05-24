package com.greatwall.jhgx.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

/**
 * 用户token管理
 * @author zsd
 **/
@Component
public class AdminJwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    /**
     * UUID
     */
    private static final long serialVersionUID = -8970718410437077606L;
    /**
     * 用户下线标志
     */
    private static final String OFF_LINE_STATE = "offline";

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        /**
         * 当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法发送401 响应
         */
        if (Objects.nonNull(request.getAttribute(OFF_LINE_STATE)) && (Boolean)request.getAttribute(OFF_LINE_STATE)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "账号已在别处登录，您可以继续留在该页面，或者重新登录");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "登录状态已过期，您可以继续留在该页面，或者重新登录");
        }
    }
}
