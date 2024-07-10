package com.agile.common.mybatis;

import com.agile.common.mybatis.config.MybatisPlusMetaObjectHandler;
import com.agile.common.mybatis.plugins.AgilePaginationInnerInterceptor;
import com.agile.common.mybatis.resolver.SqlFilterArgumentResolver;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Mybatis unified configuration.
 *
 * @author Huang Z.Y.
 */
@Configuration(proxyBeanMethods = false)
public class MybatisAutoConfiguration implements WebMvcConfigurer {

    /**
     * SQL filter to avoid SQL injection.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SqlFilterArgumentResolver());
    }

    /**
     * Paging plug-in, for a single database type,
     * it is recommended to configure this value to avoid crawling
     * the database type every time you page.
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new AgilePaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * Audit fields automatically populated.
     *
     * @return {@link MetaObjectHandler}
     */
    @Bean
    public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
        return new MybatisPlusMetaObjectHandler();
    }

}
