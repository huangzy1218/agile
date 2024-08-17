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

package com.agile.codegen.controller;

import com.agile.codegen.entity.GenTable;
import com.agile.codegen.entity.GenTableColumnEntity;
import com.agile.codegen.service.GenTableColumnService;
import com.agile.codegen.service.GenTableService;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import com.agile.plugin.excel.annotation.ResponseExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Column properties controller.
 *
 * @author Huang Z.Y.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/table")
@Tag(description = "table", name = "Column properties management")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenTableController {

    private final GenTableColumnService tableColumnService;

    private final GenTableService tableService;

    /**
     * Pagination query.
     *
     * @param page  Pagination object.
     * @param table Column properties.
     * @return
     */
    @Operation(summary = "Pagination query", description = "Pagination query.")
    @GetMapping("/page")
    public R getTablePage(Page page, GenTable table) {
        return R.ok(tableService.list(page, table));
    }

    /**
     * Query column properties by id.
     *
     * @param id Id.
     * @return R
     */
    @Operation(summary = "Query by id", description = "Query by id.")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Long id) {
        return R.ok(tableService.getById(id));
    }

    /**
     * Add new column properties.
     *
     * @param table Column properties.
     * @return R
     */
    @Operation(summary = "Add new column properties", description = "Add new column properties.")
    @SysLog("Add new column properties")
    @PostMapping
    public R save(@RequestBody GenTable table) {
        return R.ok(tableService.save(table));
    }

    /**
     * Update column properties.
     *
     * @param table Column properties.
     * @return R
     */
    @Operation(summary = "Update column properties", description = "Update column properties.")
    @SysLog("Update column properties")
    @PutMapping
    public R updateById(@RequestBody GenTable table) {
        return R.ok(tableService.updateById(table));
    }

    /**
     * Delete column properties by id.
     *
     * @param id Id.
     * @return R
     */
    @Operation(summary = "Delete by id", description = "Delete by id.")
    @SysLog("Delete by id")
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable Long id) {
        return R.ok(tableService.removeById(id));
    }

    /**
     * Export excel sheet.
     *
     * @param table Query condition.
     * @return Excel file stream.
     */
    @ResponseExcel
    @GetMapping("/export")
    public List<GenTable> export(GenTable table) {
        return tableService.list(Wrappers.query(table));
    }

    @GetMapping("/list/{dsName}")
    public R listTable(@PathVariable("dsName") String dsName) {
        return R.ok(tableService.queryDsAllTable(dsName));
    }

    @GetMapping("/column/{dsName}/{tableName}")
    public R column(@PathVariable("dsName") String dsName, @PathVariable String tableName) {
        return R.ok(tableService.queryColumn(dsName, tableName));
    }

    /**
     * Get table information.
     *
     * @param dsName    Data source.
     * @param tableName Table name.
     */
    @GetMapping("/{dsName}/{tableName}")
    public R<GenTable> info(@PathVariable("dsName") String dsName, @PathVariable String tableName) {
        return R.ok(tableService.queryOrBuildTable(dsName, tableName));
    }

    /**
     * Synchronize table information, delete origin information and create new one.
     *
     * @param dsName    Data source.
     * @param tableName Table name.
     */
    @GetMapping("/sync/{dsName}/{tableName}")
    public R<GenTable> sync(@PathVariable("dsName") String dsName, @PathVariable String tableName) {
        // Delete table configuration
        tableService.remove(
                Wrappers.<GenTable>lambdaQuery().eq(GenTable::getDsName, dsName).eq(GenTable::getTableName, tableName));
        // Delete field configuration
        tableColumnService.remove(Wrappers.<GenTableColumnEntity>lambdaQuery()
                .eq(GenTableColumnEntity::getDsName, dsName)
                .eq(GenTableColumnEntity::getTableName, tableName));
        return R.ok(tableService.queryOrBuildTable(dsName, tableName));
    }

    /**
     * Update table field data.
     *
     * @param dsName         Data source.
     * @param tableName      Table name.
     * @param tableFieldList Field list.
     */
    @PutMapping("/field/{dsName}/{tableName}")
    public R<String> updateTableField(@PathVariable("dsName") String dsName, @PathVariable String tableName,
                                      @RequestBody List<GenTableColumnEntity> tableFieldList) {
        tableColumnService.updateTableField(dsName, tableName, tableFieldList);
        return R.ok();
    }

}

