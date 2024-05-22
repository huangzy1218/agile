package com.agile.plugin.excel.config;

import com.agile.plugin.excel.aop.DynamicNameAspect;
import com.agile.plugin.excel.aop.RequestExcelArgumentResolver;
import com.agile.plugin.excel.aop.ResponseExcelReturnValueHandler;
import com.agile.plugin.excel.processor.NameProcessor;
import com.agile.plugin.excel.processor.NameSpelExpressionProcessor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration initialization.
 *
 * @author Huang Z.Y.
 */
@AutoConfiguration
@RequiredArgsConstructor
@Import(ExcelHandlerConfiguration.class)
@EnableConfigurationProperties(ExcelConfigProperties.class)
public class ResponseExcelAutoConfiguration {

    /**
     * Injects the RequestMappingHandlerAdapter bean to configure return value handlers.
     */
    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /**
     * Injects the custom ResponseExcelReturnValueHandler to handle Excel responses.
     */
    private final ResponseExcelReturnValueHandler responseExcelReturnValueHandler;

    /**
     * Bean definition for NameProcessor, which is used to process SpEL.
     *
     * @return NameProcessor excel名称解析器
     */
    @Bean
    @ConditionalOnMissingBean
    public NameProcessor nameProcessor() {
        return new NameSpelExpressionProcessor();
    }

    /**
     * Bean definition for DynamicNameAspect, which uses the NameProcessor to handle
     * dynamic name generation for Excel files.
     *
     * @param nameProcessor SPEL 解析处理器
     * @return DynamicNameAspect
     */
    @Bean
    @ConditionalOnMissingBean
    public DynamicNameAspect dynamicNameAspect(NameProcessor nameProcessor) {
        return new DynamicNameAspect(nameProcessor);
    }

    /**
     * Append Excel return value processor to Spring MVC.
     */
    @PostConstruct
    public void setReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter
                .getReturnValueHandlers();

        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
        newHandlers.add(responseExcelReturnValueHandler);
        assert returnValueHandlers != null;
        newHandlers.addAll(returnValueHandlers);
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
    }

    /**
     * Append Excel request processor to Spring MVC.
     */
    @PostConstruct
    public void setRequestExcelArgumentResolver() {
        List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
        List<HandlerMethodArgumentResolver> resolverList = new ArrayList<>();
        resolverList.add(new RequestExcelArgumentResolver());
        resolverList.addAll(argumentResolvers);
        requestMappingHandlerAdapter.setArgumentResolvers(resolverList);
    }

}
    