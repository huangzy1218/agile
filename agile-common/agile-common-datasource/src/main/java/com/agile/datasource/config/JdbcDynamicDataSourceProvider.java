/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.agile.datasource.config;

import com.agile.datasource.support.DataSourceConstants;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import org.jasypt.encryption.StringEncryptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration information from data source provider.
 *
 * @author Huang Z.Y.
 */
public class JdbcDynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {

    private final DataSourceProperties properties;

    private final StringEncryptor stringEncryptor;

    public JdbcDynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator,
                                         StringEncryptor stringEncryptor, DataSourceProperties properties) {
        super(defaultDataSourceCreator, properties.getDriverClassName(), properties.getUrl(), properties.getUsername(),
                properties.getPassword());
        this.stringEncryptor = stringEncryptor;
        this.properties = properties;
    }

    /**
     * Execute the statement to obtain the data source parameters.
     *
     * @param statement Statement
     * @return Data source parameters
     * @throws SQLException SQL exception
     */
    @Override
    protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery(properties.getQueryDsSql());

        Map<String, DataSourceProperty> map = new HashMap<>(8);
        while (rs.next()) {
            String name = rs.getString(DataSourceConstants.DS_NAME);
            String username = rs.getString(DataSourceConstants.DS_USER_NAME);
            String password = rs.getString(DataSourceConstants.DS_USER_PWD);
            String url = rs.getString(DataSourceConstants.DS_JDBC_URL);
            DataSourceProperty property = new DataSourceProperty();
            property.setUsername(username);
            property.setLazy(true);
            property.setPassword(stringEncryptor.decrypt(password));
            property.setUrl(url);
            map.put(name, property);
        }

        // Add default primary data source
        DataSourceProperty property = new DataSourceProperty();
        property.setUsername(properties.getUsername());
        property.setPassword(properties.getPassword());
        property.setUrl(properties.getUrl());
        property.setLazy(true);
        map.put(DataSourceConstants.DS_MASTER, property);
        return map;
    }

}
