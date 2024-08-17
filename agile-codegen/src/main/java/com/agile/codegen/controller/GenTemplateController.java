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
import com.agile.codegen.entity.GenTemplateEntity;
import com.agile.codegen.service.GenTemplateService;
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
 * Template.
 *
 * @author Huang Z.Y.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/template")
@Tag(description = "template", name = "Template management")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenTemplateController {

    private final GenTemplateService genTemplateService;

    /**
     * Pagination query.
     *
     * @param page        Pagination object.
     * @param genTemplate Template.
     * @return
     */
    @Operation(summary = "Pagination query", description = "Pagination query.")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('codegen_template_view')")
    public R getgenTemplatePage(Page page, GenTemplateEntity genTemplate) {
        LambdaQueryWrapper<GenTemplateEntity> wrapper = Wrappers.<GenTemplateEntity>lambdaQuery()
                .like(genTemplate.getId() != null, GenTemplateEntity::getId, genTemplate.getId())
                .like(StrUtil.isNotEmpty(genTemplate.getTemplateName()), GenTemplateEntity::getTemplateName,
                        genTemplate.getTemplateName());
        return R.ok(genTemplateService.page(page, wrapper));
    }

    /**
     * Query all templates.
     *
     * @return
     */
    @Operation(summary = "Query all", description = "Query all.")
    @GetMapping("/list")
    @PreAuthorize("@pms.hasPermission('codegen_template_view')")
    public R list() {
        return R.ok(genTemplateService.list(Wrappers.emptyWrapper()));
    }

    /**
     * Query template by id.
     *
     * @param id Id.
     * @return R
     */
    @Operation(summary = "Query by id", description = "Query by id.")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('codegen_template_view')")
    public R getById(@PathVariable("id") Long id) {
        return R.ok(genTemplateService.getById(id));
    }

    /**
     * Add new template.
     *
     * @param genTemplate Template.
     * @return R
     */
    @Operation(summary = "Add new template", description = "Add new template.")
    @SysLog("Add new template")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('codegen_template_add')")
    public R save(@RequestBody GenTemplateEntity genTemplate) {
        return R.ok(genTemplateService.save(genTemplate));
    }

    /**
     * Update template.
     *
     * @param genTemplate Template.
     * @return R
     */
    @Operation(summary = "Update template", description = "Update template.")
    @SysLog("Update template")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('codegen_template_edit')")
    public R updateById(@RequestBody GenTemplateEntity genTemplate) {
        return R.ok(genTemplateService.updateById(genTemplate));
    }

    /**
     * Delete template by id.
     *
     * @param ids Id list.
     * @return R
     */
    @Operation(summary = "Delete by id", description = "Delete by id.")
    @SysLog("Delete by id")
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('codegen_template_del')")
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(genTemplateService.removeBatchByIds(CollUtil.toList(ids)));
    }

    /**
     * Export excel sheet.
     *
     * @param genTemplate Query condition.
     * @return Excel file stream.
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('codegen_template_export')")
    public List<GenTemplateEntity> export(GenTemplateEntity genTemplate) {
        return genTemplateService.list(Wrappers.query(genTemplate));
    }

}
