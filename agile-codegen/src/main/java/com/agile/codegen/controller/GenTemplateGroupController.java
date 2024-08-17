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
import com.agile.codegen.entity.GenTemplateGroupEntity;
import com.agile.codegen.service.GenTemplateGroupService;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import com.agile.plugin.excel.annotation.ResponseExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Template group association controller.
 *
 * @author Huang Z.Y.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/templateGroup")
@Tag(description = "templateGroup", name = "Template group association table management")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenTemplateGroupController {

    private final GenTemplateGroupService genTemplateGroupService;

    /**
     * Pagination query.
     *
     * @param page             Pagination object.
     * @param genTemplateGroup Template group association table.
     * @return
     */
    @Operation(summary = "Pagination query", description = "Pagination query.")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('codegen_templateGroup_view')")
    public R getgenTemplateGroupPage(Page page, GenTemplateGroupEntity genTemplateGroup) {
        LambdaQueryWrapper<GenTemplateGroupEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(genTemplateGroupService.page(page, wrapper));
    }

    /**
     * Query template group association table by id.
     *
     * @param groupId Id.
     * @return R
     */
    @Operation(summary = "Query by id", description = "Query by id.")
    @GetMapping("/{groupId}")
    @PreAuthorize("@pms.hasPermission('codegen_templateGroup_view')")
    public R getById(@PathVariable("groupId") Long groupId) {
        return R.ok(genTemplateGroupService.getById(groupId));
    }

    /**
     * Add new template group association table.
     *
     * @param genTemplateGroup Template group association table.
     * @return R
     */
    @Operation(summary = "Add new template group association table", description = "Add new template group association table.")
    @SysLog("Add new template group association table")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('codegen_templateGroup_add')")
    public R save(@RequestBody GenTemplateGroupEntity genTemplateGroup) {
        return R.ok(genTemplateGroupService.save(genTemplateGroup));
    }

    /**
     * Update template group association table.
     *
     * @param genTemplateGroup Template group association table.
     * @return R
     */
    @Operation(summary = "Update template group association table", description = "Update template group association table.")
    @SysLog("Update template group association table")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('codegen_templateGroup_edit')")
    public R updateById(@RequestBody GenTemplateGroupEntity genTemplateGroup) {
        return R.ok(genTemplateGroupService.updateById(genTemplateGroup));
    }

    /**
     * Delete template group association table by id.
     *
     * @param ids Id list.
     * @return R
     */
    @Operation(summary = "Delete by id", description = "Delete by id.")
    @SysLog("Delete by id")
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('codegen_templateGroup_del')")
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(genTemplateGroupService.removeBatchByIds(CollUtil.toList(ids)));
    }

    /**
     * Export excel sheet.
     *
     * @param genTemplateGroup Query condition.
     * @return Excel file stream.
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('codegen_templateGroup_export')")
    public List<GenTemplateGroupEntity> export(GenTemplateGroupEntity genTemplateGroup) {
        return genTemplateGroupService.list(Wrappers.query(genTemplateGroup));
    }

}

