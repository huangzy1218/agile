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
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.agile.codegen.entity.GenTable;
import com.agile.codegen.entity.GenTableColumnEntity;
import com.agile.codegen.entity.GenTemplateEntity;
import com.agile.codegen.entity.vo.GroupVo;
import com.agile.codegen.service.*;
import com.agile.codegen.util.VelocityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Code generator service.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

    private final GenTableColumnService columnService;

    private final GenFieldTypeService fieldTypeService;

    private final GenTableService tableService;

    private final GenGroupService genGroupService;

    /**
     * Generate code zip and write it out.
     *
     * @param tableId Table ID
     * @param zip     Output stream
     */
    @Override
    @SneakyThrows
    public void downloadCode(Long tableId, ZipOutputStream zip) {
        // Data type
        Map<String, Object> dataModel = getDataModel(tableId);

        Long style = (Long) dataModel.get("style");

        GroupVo groupVo = genGroupService.getGroupVoById(style);
        List<GenTemplateEntity> templateList = groupVo.getTemplateList();

        Map<String, Object> generatorConfig = tableService.getGeneratorConfig();
        JSONObject project = (JSONObject) generatorConfig.get("project");
        String frontendPath = project.getStr("frontendPath");
        String backendPath = project.getStr("backendPath");

        for (GenTemplateEntity template : templateList) {
            String templateCode = template.getTemplateCode();
            String generatorPath = template.getGeneratorPath();

            dataModel.put("frontendPath", frontendPath);
            dataModel.put("backendPath", backendPath);
            String content = VelocityUtils.renderStr(templateCode, dataModel);
            String path = VelocityUtils.renderStr(generatorPath, dataModel);

            // Add to ZIP
            zip.putNextEntry(new ZipEntry(path));
            IoUtil.writeUtf8(zip, false, content);
            zip.flush();
            zip.closeEntry();
        }

    }

    /**
     * Preview code method for expression optimization.
     *
     * @param tableId Table Id
     * @return [{Template name:Rendering result}]
     */
    @Override
    @SneakyThrows
    public List<Map<String, String>> preview(Long tableId) {
        // Data model
        Map<String, Object> dataModel = getDataModel(tableId);

        Long style = (Long) dataModel.get("style");

        // Get the template list, Lambda expression simplifies the code
        List<GenTemplateEntity> templateList = genGroupService.getGroupVoById(style).getTemplateList();

        Map<String, Object> generatorConfig = tableService.getGeneratorConfig();
        JSONObject project = (JSONObject) generatorConfig.get("project");
        String frontendPath = project.getStr("frontendPath");
        String backendPath = project.getStr("backendPath");

        return templateList.stream().map(template -> {
            String templateCode = template.getTemplateCode();
            String generatorPath = template.getGeneratorPath();

            // In preview mode, use relative paths to display
            dataModel.put("frontendPath", frontendPath);
            dataModel.put("backendPath", backendPath);
            String content = VelocityUtils.renderStr(templateCode, dataModel);
            String path = VelocityUtils.renderStr(generatorPath, dataModel);

            // Use maps to simplify code
            return new HashMap<String, String>(4) {
                {
                    put("code", content);
                    put("codePath", path);
                }
            };
        }).collect(Collectors.toList());
    }

    /**
     * Method for writing rendering results to the target directory.
     *
     * @param tableId Table ID
     */
    @Override
    public void generatorCode(Long tableId) {
        // Data model
        Map<String, Object> dataModel = getDataModel(tableId);
        Long style = (Long) dataModel.get("style");

        // Get the template list, Lambda expression simplifies the code
        List<GenTemplateEntity> templateList = genGroupService.getGroupVoById(style).getTemplateList();

        templateList.forEach(template -> {
            String templateCode = template.getTemplateCode();
            String generatorPath = template.getGeneratorPath();
            String content = VelocityUtils.renderStr(templateCode, dataModel);
            String path = VelocityUtils.renderStr(generatorPath, dataModel);
            FileUtil.writeUtf8String(content, path);
        });
    }

    /**
     * Optimized method of obtaining data model through Lambda expression.
     *
     * @param tableId Table ID
     * @return Data model map object
     */
    private Map<String, Object> getDataModel(Long tableId) {
        // Get form information
        GenTable table = tableService.getById(tableId);
        // Get field list
        List<GenTableColumnEntity> fieldList = columnService.lambdaQuery()
                .eq(GenTableColumnEntity::getDsName, table.getDsName())
                .eq(GenTableColumnEntity::getTableName, table.getTableName())
                .orderByAsc(GenTableColumnEntity::getSort)
                .list();

        table.setFieldList(fieldList);

        // Create data model objects
        Map<String, Object> dataModel = new HashMap<>();

        // Fill the data model
        dataModel.put("dbType", table.getDbType());
        dataModel.put("package", table.getPackageName());
        dataModel.put("packagePath", table.getPackageName().replace(".", File.separator));
        dataModel.put("version", table.getVersion());
        dataModel.put("moduleName", table.getModuleName());
        dataModel.put("ModuleName", StrUtil.upperFirst(table.getModuleName()));
        dataModel.put("functionName", table.getFunctionName());
        dataModel.put("FunctionName", StrUtil.upperFirst(table.getFunctionName()));
        dataModel.put("formLayout", table.getFormLayout());
        dataModel.put("style", table.getStyle());
        dataModel.put("author", table.getAuthor());
        dataModel.put("datetime", DateUtil.now());
        dataModel.put("date", DateUtil.today());
        setFieldTypeList(dataModel, table);

        // Get the list of imported packages
        Set<String> importList = fieldTypeService.getPackageByTableId(table.getDsName(), table.getTableName());
        dataModel.put("importList", importList);
        dataModel.put("tableName", table.getTableName());
        dataModel.put("tableComment", table.getTableComment());
        dataModel.put("className", StrUtil.lowerFirst(table.getClassName()));
        dataModel.put("ClassName", table.getClassName());
        dataModel.put("fieldList", table.getFieldList());

        dataModel.put("backendPath", table.getBackendPath());
        dataModel.put("frontendPath", table.getFrontendPath());
        return dataModel;
    }

    /**
     * Group table fields by type and store them in the data model.
     *
     * @param dataModel Map object to store data
     * @param table     Table information
     */
    private void setFieldTypeList(Map<String, Object> dataModel, GenTable table) {
        // Group by field type, use Map to store different types of field lists
        Map<Boolean, List<GenTableColumnEntity>> typeMap = table.getFieldList()
                .stream()
                .collect(Collectors.partitioningBy(columnEntity -> BooleanUtil.toBoolean(columnEntity.getPrimaryPk())));

        // Get a list of fields of different types from the grouped Map
        List<GenTableColumnEntity> primaryList = typeMap.get(true);
        List<GenTableColumnEntity> formList = typeMap.get(false)
                .stream()
                .filter(columnEntity -> BooleanUtil.toBoolean(columnEntity.getFormItem()))
                .collect(Collectors.toList());
        List<GenTableColumnEntity> gridList = typeMap.get(false)
                .stream()
                .filter(columnEntity -> BooleanUtil.toBoolean(columnEntity.getGridItem()))
                .collect(Collectors.toList());
        List<GenTableColumnEntity> queryList = typeMap.get(false)
                .stream()
                .filter(columnEntity -> BooleanUtil.toBoolean(columnEntity.getQueryItem()))
                .collect(Collectors.toList());

        if (CollUtil.isNotEmpty(primaryList)) {
            dataModel.put("pk", primaryList.get(0));
        }
        dataModel.put("primaryList", primaryList);
        dataModel.put("formList", formList);
        dataModel.put("gridList", gridList);
        dataModel.put("queryList", queryList);
    }

}
