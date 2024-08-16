/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.agile.codegen.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.codegen.entity.GenDatasourceConf;
import com.agile.codegen.mapper.GenDatasourceConfMapper;
import com.agile.codegen.service.GenDatasourceConfService;
import com.agile.common.core.util.SpringContextHolder;
import com.agile.datasource.enums.DsConfTypeEnum;
import com.agile.datasource.enums.DsJdbcUrlEnum;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.druid.DruidConfig;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Datasource service implementation.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenDatasourceConfServiceImpl extends ServiceImpl<GenDatasourceConfMapper, GenDatasourceConf>
        implements GenDatasourceConfService {

    private final StringEncryptor stringEncryptor;

    private final DefaultDataSourceCreator druidDataSourceCreator;

    /**
     * Save data source and encrypt it.
     *
     * @param conf Data source information
     * @return {@code true} for success.
     */
    @Override
    public Boolean saveDsByEnc(GenDatasourceConf conf) {
        // Verify configuration legality
        if (!checkDataSource(conf)) {
            return Boolean.FALSE;
        }

        // Add dynamic data source
        addDynamicDataSource(conf);

        // Update database configuration
        conf.setPassword(stringEncryptor.encrypt(conf.getPassword()));
        this.baseMapper.insert(conf);
        return Boolean.TRUE;
    }

    /**
     * Update data source.
     *
     * @param conf Data source information
     * @return
     */
    @Override
    public Boolean updateDsByEnc(GenDatasourceConf conf) {
        if (!checkDataSource(conf)) {
            return Boolean.FALSE;
        }
        // 1. Remove
        DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
        dynamicRoutingDataSource.removeDataSource(baseMapper.selectById(conf.getId()).getName());

        // 2. Add
        addDynamicDataSource(conf);

        // 3. Update database configuration
        if (StrUtil.isNotBlank(conf.getPassword())) {
            conf.setPassword(stringEncryptor.encrypt(conf.getPassword()));
        }
        this.baseMapper.updateById(conf);
        return Boolean.TRUE;
    }

    /**
     * Delete by data source name.
     *
     * @param dsIds Data source ID
     * @return
     */
    @Override
    public Boolean removeByDsId(Long[] dsIds) {
        DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
        this.baseMapper.selectBatchIds(CollUtil.toList(dsIds))
                .forEach(ds -> dynamicRoutingDataSource.removeDataSource(ds.getName()));
        this.baseMapper.deleteBatchIds(CollUtil.toList(dsIds));
        return Boolean.TRUE;
    }

    /**
     * Add dynamic data source.
     *
     * @param conf Data source information
     */
    @Override
    public void addDynamicDataSource(GenDatasourceConf conf) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setPoolName(conf.getName());
        dataSourceProperty.setUrl(conf.getUrl());
        dataSourceProperty.setUsername(conf.getUsername());
        dataSourceProperty.setPassword(conf.getPassword());

        // Add ValidationQuery parameter
        DruidConfig druidConfig = new DruidConfig();
        dataSourceProperty.setDruid(druidConfig);
        DataSource dataSource = druidDataSourceCreator.createDataSource(dataSourceProperty);

        DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
        dynamicRoutingDataSource.addDataSource(dataSourceProperty.getPoolName(), dataSource);
    }

    /**
     * Verify whether the data source configuration is valid.
     *
     * @param conf Data source information.
     * @return {@code true} for valid
     */
    @Override
    public Boolean checkDataSource(GenDatasourceConf conf) {
        String url;
        // JDBC configuration form
        if (DsConfTypeEnum.JDBC.getType().equals(conf.getConfType())) {
            url = conf.getUrl();
        } else {
            // Host mode SQL server special processing
            DsJdbcUrlEnum urlEnum = DsJdbcUrlEnum.get(conf.getDsType());
            url = String.format(urlEnum.getUrl(), conf.getHost(), conf.getPort(), conf.getDsName());
        }

        conf.setUrl(url);
        // Try to get connection before real connection
        try (Connection connection = DriverManager.getConnection(url, conf.getUsername(), conf.getPassword())) {
        } catch (SQLException e) {
            log.error("Data source configuration {}, failed to obtain link", conf.getName(), e);
            throw new RuntimeException("Database configuration error, link failure");
        }
        return Boolean.TRUE;
    }

}
