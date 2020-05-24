package com.greatwall.jhgx.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Description 跨域资源配置
 * @Package com.greatwall.portal.admin.configuration
 * @author TianLei
 * @Date 2019/6/26 13:25
 * @Version V1.0
 */
@Configuration
public class AdminCorsConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        // #允许向该服务器提交请求的URI，*表示全部允许
        configuration.addAllowedOrigin(CorsConfiguration.ALL);
        // 允许cookies跨域
        configuration.setAllowCredentials(true);
        // #允许访问的头信息,*表示全部
        configuration.addAllowedHeader(CorsConfiguration.ALL);
        // 允许提交请求的方法，*表示全部允许
        configuration.addAllowedMethod(CorsConfiguration.ALL);
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

    /**
     * 配置过滤器
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(corsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("corsFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
