package com.greatwall.jhgx.configuration;

import com.greatwall.jhgx.porperties.AdminPermitProperties;
import com.greatwall.jhgx.security.AdminJwtAuthenticationEntryPoint;
import com.greatwall.jhgx.security.AdminJwtAuthorizationTokenFilter;
import com.greatwall.jhgx.security.service.AdminJwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 用户管理安全配置
 *
 * @author zsd
 * @date 2019/8/6
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {
    /**
     * 用户权限处理器
     */
    @Autowired
    private AdminJwtAuthenticationEntryPoint unauthorizedHandler;
    /**
     * 自定义基于JWT的安全过滤器
     */
    @Autowired
    AdminJwtAuthorizationTokenFilter authenticationTokenFilter;
    /**
     * 权限url过滤
     */
    @Autowired
    private AdminPermitProperties adminPermitProperties;
    /**
     * 自定义用户操作类
     */
    @Autowired
    private AdminJwtUserDetailsService adminJwtUserDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminJwtUserDetailsService);
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // Remove the ROLE_ prefix
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity

                // 禁用 CSRF
                .csrf().disable()

                // 授权异常
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // 不创建会话
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                // 过滤请求
                .authorizeRequests()
                .antMatchers(
                        adminPermitProperties.getUrls()
                ).permitAll()

                // 所有请求都需要认证
                .anyRequest().authenticated()
                // 防止iframe 造成跨域
                .and().headers().frameOptions().disable();

        httpSecurity
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
