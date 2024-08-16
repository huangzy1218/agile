package com.agile.datasource;

import com.agile.datasource.config.ClearTtlDataSourceFilter;
import com.agile.datasource.config.JdbcDynamicDataSourceProvider;
import com.agile.datasource.config.LastParamDsProcessor;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.hikaricp.HikariDataSourceCreator;
import com.baomidou.dynamic.datasource.processor.DsJakartaHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsJakartaSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic data source switching configuration.
 *
 * @author Huang Z.Y.
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DataSourceProperties.class)
public class DynamicDataSourceAutoConfiguration {

    /**
     * Dynamic data source provider.
     *
     * @param defaultDataSourceCreator Default data source creator.
     * @param stringEncryptor          String encryptor.
     * @param properties               Data source configuration properties.
     * @return Dynamic data source provider.
     */
    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator,
                                                               StringEncryptor stringEncryptor, DataSourceProperties properties) {
        return new JdbcDynamicDataSourceProvider(defaultDataSourceCreator, stringEncryptor, properties);
    }

    /**
     * Get data source processor.
     *
     * @param beanFactory Bean factory.
     * @return Data source processor.
     */
    @Bean
    public DsProcessor dsProcessor(BeanFactory beanFactory) {
        DsProcessor lastParamDsProcessor = new LastParamDsProcessor();
        DsProcessor headerProcessor = new DsJakartaHeaderProcessor();
        DsProcessor sessionProcessor = new DsJakartaSessionProcessor();
        DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
        spelExpressionProcessor.setBeanResolver(new BeanFactoryResolver(beanFactory));
        lastParamDsProcessor.setNextProcessor(headerProcessor);
        headerProcessor.setNextProcessor(sessionProcessor);
        sessionProcessor.setNextProcessor(spelExpressionProcessor);
        return lastParamDsProcessor;
    }

    /**
     * Default data source creator.
     *
     * @param hikariDataSourceCreator Hikari data source creator.
     * @return Default data source creator.
     */
    @Bean
    public DefaultDataSourceCreator defaultDataSourceCreator(HikariDataSourceCreator hikariDataSourceCreator) {
        DefaultDataSourceCreator defaultDataSourceCreator = new DefaultDataSourceCreator();
        List<DataSourceCreator> creators = new ArrayList<>();
        creators.add(hikariDataSourceCreator);
        defaultDataSourceCreator.setCreators(creators);
        return defaultDataSourceCreator;
    }

    /**
     * Clear TTL data source filter.
     *
     * @return Clear TTL data source filter.
     */
    @Bean
    public ClearTtlDataSourceFilter clearTtlDsFilter() {
        return new ClearTtlDataSourceFilter();
    }

}
