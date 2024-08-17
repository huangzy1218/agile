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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.codegen.entity.GenFieldType;
import com.agile.codegen.service.GenFieldTypeService;
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
@RequestMapping("/fieldtype")
@Tag(description = "fieldtype", name = "Column attribute management")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenFieldTypeController {

    private final GenFieldTypeService fieldTypeService;

    /**
     * Paging query
     *
     * @param page      Pagination object
     * @param fieldType Column properties
     * @return
     */
    @Operation(summary = "Paging query", description = "Paging query")
    @GetMapping("/page")
    public R getFieldTypePage(Page page, GenFieldType fieldType) {
        return R.ok(fieldTypeService.page(page,
                Wrappers.<GenFieldType>lambdaQuery()
                        .like(StrUtil.isNotBlank(fieldType.getColumnType()), GenFieldType::getColumnType,
                                fieldType.getColumnType())));
    }

    @Operation(summary = "List query", description = "List query")
    @GetMapping("/list")
    public R list(GenFieldType fieldType) {
        return R.ok(fieldTypeService.list(Wrappers.query(fieldType)));
    }

    /**
     * Query column attributes by id.
     *
     * @param id id
     * @return R
     */
    @Operation(summary = "Query column attributes by id", description = "Query column attributes by id")
    @GetMapping("/details/{id}")
    public R getById(@PathVariable("id") Long id) {
        return R.ok(fieldTypeService.getById(id));
    }

    @GetMapping("/details")
    public R getDetails(GenFieldType query) {
        return R.ok(fieldTypeService.getOne(Wrappers.query(query), false));
    }

    /**
     * Add column attributes.
     *
     * @param fieldType Column attributes
     * @return R
     */
    @Operation(summary = "Add column attributes", description = "Add column attributess")
    @SysLog("Add column attributes")
    @PostMapping
    public R save(@RequestBody GenFieldType fieldType) {
        return R.ok(fieldTypeService.save(fieldType));
    }

    /**
     * Modify column attributes.
     *
     * @param fieldType Column attributes
     * @return R
     */
    @Operation(summary = "Modify column properties", description = "Modify column properties")
    @SysLog("Modify column properties")
    @PutMapping
    public R updateById(@RequestBody GenFieldType fieldType) {
        return R.ok(fieldTypeService.updateById(fieldType));
    }

    /**
     * Delete column attribute by id.
     *
     * @param ids id
     * @return R
     */
    @Operation(summary = "Delete column attribute by id", description = "Delete column attribute by id")
    @SysLog("Delete column attribute by id")
    @DeleteMapping
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(fieldTypeService.removeBatchByIds(CollUtil.toList(ids)));
    }

    /**
     * Export excel table
     *
     * @param fieldType Query conditions
     * @return Excel file stream
     */
    @ResponseExcel
    @GetMapping("/export")
    public List<GenFieldType> export(GenFieldType fieldType) {
        return fieldTypeService.list(Wrappers.query(fieldType));
    }

}
