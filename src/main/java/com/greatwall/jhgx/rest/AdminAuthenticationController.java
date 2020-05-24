package com.greatwall.jhgx.rest;

import com.greatwall.component.ccyl.common.model.Result;
import com.greatwall.componnet.auth.annotation.LoginUser;
import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.constants.DefaultSecurityConstants;
import com.greatwall.jhgx.security.AdminJwtAuthorizationTokenFilter;
import com.greatwall.jhgx.security.model.AdminAuthorizationUser;
import com.greatwall.jhgx.security.model.AuthenticationInfo;
import com.greatwall.jhgx.service.AdminAuthentication;
import com.greatwall.jhgx.service.TSysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author TianLei
 * @date 2019/06/05
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("auth")
public class AdminAuthenticationController {

    @Autowired
    @Qualifier("adminJwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    AdminJwtAuthorizationTokenFilter authenticationTokenFilter;

    @Autowired
    private AdminAuthentication adminAuthentication;

    @Autowired
    private TSysUserService sysUserService;

    private static final String TEST_POWER_MSG= "访问测试接口成功,权限校验成功";

    /**
     * 登录授权
     * @param authorizationUser
     * @return
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody AdminAuthorizationUser authorizationUser, HttpServletRequest request) {

        final DefaultJwtUserDetails jwtUser = (DefaultJwtUserDetails) userDetailsService.loadUserByUsername(authorizationUser.getUsername());

        if (! authorizationUser.getPassword().equals(jwtUser.getPassword())) {
            throw new AccountExpiredException("密码错误");
        }

        AuthenticationInfo authenticationInfo = adminAuthentication.getAuthenticationInfo(jwtUser, authorizationUser.getUsername());

        // 返回 token
        return Result.succeed(authenticationInfo);
    }

    /**
     * 测试权限
     * @return
     */
    @PostMapping(value = "/test")
    public Result test(){
        // 返回 token
        return Result.succeed(TEST_POWER_MSG);
    }

    /**
     * 测试权限
     * @return
     */
    @PostMapping(value = "/test1")
    public Result test1(@LoginUser DefaultJwtUserDetails userDetails){
        // 返回 token
        return Result.succeed(TEST_POWER_MSG+ userDetails.getUsername());
    }

    /**
     * 测试权限
     * @return
     */
    @PostMapping(value = "/test2")
    public Result test2(@LoginUser(isFull = true) DefaultJwtUserDetails userDetails){
        // 返回 token
        return Result.succeed(TEST_POWER_MSG + userDetails.getPassword());
    }

    /**
     * 生成图片验证码保存key
     * @param deviceId  设备ID
     * @return
     */
    private String buildKey(String deviceId) {
        return DefaultSecurityConstants.DEFAULT_CODE_KEY + ":" + deviceId;
    }

}
