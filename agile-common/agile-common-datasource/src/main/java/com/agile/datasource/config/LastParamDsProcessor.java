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

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Parameter data source parsing {@code @DS("#last")}.
 *
 * @author Huang Z.Y.
 */
public class LastParamDsProcessor extends DsProcessor {

    private static final String LAST_PREFIX = "#last";

    /**
     * Abstract matching conditions. Only if they match, the current executor will be taken. Otherwise,
     * the next level executor will be taken.
     *
     * @param key Contents in DS annotations
     * @return Does it match
     */
    @Override
    public boolean matches(String key) {
        if (key.startsWith(LAST_PREFIX)) {
            // https://github.com/baomidou/dynamic-datasource-spring-boot-starter/issues/213
            DynamicDataSourceContextHolder.clear();
            return true;
        }
        return false;
    }

    /**
     * Abstraction ultimately determines the data source.
     *
     * @param invocation Method execution information
     * @param key        Contents in DS annotations
     * @return Datasource name
     */
    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        Object[] arguments = invocation.getArguments();
        return String.valueOf(arguments[arguments.length - 1]);
    }

}
