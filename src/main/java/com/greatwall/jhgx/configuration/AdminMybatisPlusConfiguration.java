package com.greatwall.jhgx.configuration;


import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.greatwall.component.ccyl.db.configuration.DefaultMetaObjectHandler;
import com.greatwall.component.ccyl.db.configuration.DefaultMybatisPlusConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * mybatis-plus配置
 *
 * @author TianLei
 */
@Configuration
@MapperScan({"com.greatwall.jhgx.mapper*"})
@Import(DefaultMetaObjectHandler.class)
public class AdminMybatisPlusConfiguration extends DefaultMybatisPlusConfiguration {

    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }
}
