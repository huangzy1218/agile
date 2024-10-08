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

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.agile.codegen.entity.GenGroupEntity;
import com.agile.codegen.entity.GenTable;
import com.agile.codegen.entity.GenTableColumnEntity;
import com.agile.codegen.mapper.GenTableMapper;
import com.agile.codegen.mapper.GeneratorMapper;
import com.agile.codegen.service.GenGroupService;
import com.agile.codegen.service.GenTableColumnService;
import com.agile.codegen.service.GenTableService;
import com.agile.codegen.util.BoolFillEnum;
import com.agile.codegen.util.CommonColumnFiledEnum;
import com.agile.codegen.util.GenUtils;
import com.agile.codegen.util.GeneratorTypeEnum;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Table service implementation.
 *
 * @author pigx code generator
 * @date 2023-02-06 20:34:55
 */
@Service
@RequiredArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements GenTableService {

    /**
     * Default configuration information.
     */
    private static final String CONFIG_PATH = "template/config.json";

    private final GenTableColumnService columnService;

    private final GenGroupService genGroupService;

    /**
     * Get configuration information.
     */
    @Override
    public Map<String, Object> getGeneratorConfig() {
        ClassPathResource classPathResource = new ClassPathResource(CONFIG_PATH);
        JSONObject jsonObject = JSONUtil.parseObj(IoUtil.readUtf8(classPathResource.getStream()));
        return jsonObject.getRaw();
    }

    @Override
    public List<Map<String, Object>> queryDsAllTable(String dsName) {
        GeneratorMapper mapper = GenUtils.getMapper(dsName);
        // Manually switch data source
        DynamicDataSourceContextHolder.push(dsName);
        return mapper.queryTable();
    }

    @Override
    public List<Map<String, String>> queryColumn(String dsName, String tableName) {
        GeneratorMapper mapper = GenUtils.getMapper(dsName);
        return mapper.selectMapTableColumn(tableName, dsName);
    }

    @Override
    public IPage list(Page<GenTable> page, GenTable table) {
        GeneratorMapper mapper = GenUtils.getMapper(table.getDsName());
        // Manually switch data source
        DynamicDataSourceContextHolder.push(table.getDsName());
        return mapper.queryTable(page, table.getTableName());
    }

    /**
     * Get table information
     *
     * @param dsName    Data source name
     * @param tableName Table name
     * @return Column properties
     */
    @Override
    public GenTable queryOrBuildTable(String dsName, String tableName) {
        GenTable genTable = baseMapper.selectOne(
                Wrappers.<GenTable>lambdaQuery().eq(GenTable::getTableName, tableName).eq(GenTable::getDsName, dsName));
        // If genTable is empty, perform the import
        if (Objects.isNull(genTable)) {
            genTable = tableImport(dsName, tableName);
        }

        List<GenTableColumnEntity> fieldList = columnService.list(Wrappers.<GenTableColumnEntity>lambdaQuery()
                .eq(GenTableColumnEntity::getDsName, dsName)
                .eq(GenTableColumnEntity::getTableName, tableName)
                .orderByAsc(GenTableColumnEntity::getSort));
        genTable.setFieldList(fieldList);

        // 查询模板分组信息
        List<GenGroupEntity> groupEntities = genGroupService.list();
        genTable.setGroupList(groupEntities);
        return genTable;
    }

    @Transactional(rollbackFor = Exception.class)
    public GenTable tableImport(String dsName, String tableName) {
        GeneratorMapper mapper = GenUtils.getMapper(dsName);
        // Manually switch data sources
        DynamicDataSourceContextHolder.push(dsName);

        // Check whether the table exists
        GenTable table = new GenTable();

        // Get table information from database
        Map<String, String> queryTable = mapper.queryTable(tableName, dsName);

        // Get default table configuration information
        Map<String, Object> generatorConfig = getGeneratorConfig();
        JSONObject project = (JSONObject) generatorConfig.get("project");
        JSONObject developer = (JSONObject) generatorConfig.get("developer");

        table.setPackageName(project.getStr("packageName"));
        table.setVersion(project.getStr("version"));
        table.setBackendPath(project.getStr("backendPath"));
        table.setFrontendPath(project.getStr("frontendPath"));
        table.setAuthor(developer.getStr("author"));
        table.setEmail(developer.getStr("email"));
        table.setTableName(tableName);
        table.setDsName(dsName);
        table.setTableComment(MapUtil.getStr(queryTable, "tableComment"));
        table.setDbType(MapUtil.getStr(queryTable, "dbType"));
        table.setFormLayout(2);
        table.setGeneratorType(GeneratorTypeEnum.ZIP_DOWNLOAD.getValue());
        table.setClassName(NamingCase.toPascalCase(tableName));
        table.setModuleName(GenUtils.getModuleName(table.getPackageName()));
        table.setFunctionName(GenUtils.getFunctionName(tableName));
        table.setCreateTime(LocalDateTime.now());
        this.save(table);

        // Get native field data, column map contains column information
        List<Map<String, String>> queryColumnList = mapper.selectMapTableColumn(tableName, dsName);
        List<GenTableColumnEntity> tableFieldList = new ArrayList<>();

        for (Map<String, String> columnMap : queryColumnList) {
            String columnName = MapUtil.getStr(columnMap, "columnName");
            GenTableColumnEntity genTableColumnEntity = new GenTableColumnEntity();
            genTableColumnEntity.setTableName(tableName);
            genTableColumnEntity.setDsName(dsName);
            genTableColumnEntity.setFieldName(MapUtil.getStr(columnMap, "columnName"));
            genTableColumnEntity.setFieldComment(MapUtil.getStr(columnMap, "comments"));
            genTableColumnEntity.setFieldType(MapUtil.getStr(columnMap, "dataType"));
            String columnKey = MapUtil.getStr(columnMap, "columnKey");
            genTableColumnEntity.setAutoFill("DEFAULT");
            genTableColumnEntity.setPrimaryPk((StringUtils.isNotBlank(columnKey) && "PRI".equalsIgnoreCase(columnKey))
                    ? BoolFillEnum.TRUE.getValue() : BoolFillEnum.FALSE.getValue());
            genTableColumnEntity.setAutoFill("DEFAULT");
            genTableColumnEntity.setFormItem(BoolFillEnum.TRUE.getValue());
            genTableColumnEntity.setGridItem(BoolFillEnum.TRUE.getValue());

            // Handle audit field
            if (EnumUtil.contains(CommonColumnFiledEnum.class, columnName)) {
                CommonColumnFiledEnum commonColumnFiledEnum = CommonColumnFiledEnum.valueOf(columnName);
                genTableColumnEntity.setFormItem(commonColumnFiledEnum.getFormItem());
                genTableColumnEntity.setGridItem(commonColumnFiledEnum.getGridItem());
                genTableColumnEntity.setAutoFill(commonColumnFiledEnum.getAutoFill());
                genTableColumnEntity.setSort(commonColumnFiledEnum.getSort());
            }
            tableFieldList.add(genTableColumnEntity);
        }
        // Initialize field data
        columnService.initFieldList(tableFieldList);
        // Save column data
        columnService.saveOrUpdateBatch(tableFieldList);
        table.setFieldList(tableFieldList);
        return table;
    }

}
