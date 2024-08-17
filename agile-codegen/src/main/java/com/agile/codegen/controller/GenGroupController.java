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

import cn.hutool.core.util.StrUtil;
import com.agile.codegen.entity.GenGroupEntity;
import com.agile.codegen.entity.vo.GroupVo;
import com.agile.codegen.entity.vo.TemplateGroupDTO;
import com.agile.codegen.service.GenGroupService;
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
 * Template grouping controller.
 *
 * @author Huang Z.Y.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
@Tag(description = "group", name = "Template grouping management")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenGroupController {

    private final GenGroupService genGroupService;

    /**
     * Pagination query.
     *
     * @param page     Pagination object.
     * @param genGroup Template group.
     * @return
     */
    @Operation(summary = "Pagination query", description = "Pagination query.")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('codegen_group_view')")
    public R getgenGroupPage(Page page, GenGroupEntity genGroup) {
        LambdaQueryWrapper<GenGroupEntity> wrapper = Wrappers.<GenGroupEntity>lambdaQuery()
                .like(genGroup.getId() != null, GenGroupEntity::getId, genGroup.getId())
                .like(StrUtil.isNotEmpty(genGroup.getGroupName()), GenGroupEntity::getGroupName, genGroup.getGroupName());
        return R.ok(genGroupService.page(page, wrapper));
    }

    /**
     * Query template group by id.
     *
     * @param id Id.
     * @return R
     */
    @Operation(summary = "Query by id", description = "Query by id.")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('codegen_group_view')")
    public R getById(@PathVariable("id") Long id) {
        return R.ok(genGroupService.getGroupVoById(id));
    }

    /**
     * Add new template group.
     *
     * @param genTemplateGroup Template group.
     * @return R
     */
    @Operation(summary = "Add new template group", description = "Add new template group.")
    @SysLog("Add new template group")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('codegen_group_add')")
    public R save(@RequestBody TemplateGroupDTO genTemplateGroup) {
        genGroupService.saveGenGroup(genTemplateGroup);
        return R.ok();
    }

    /**
     * Update template group.
     *
     * @param groupVo Template group.
     * @return R
     */
    @Operation(summary = "Update template group", description = "Update template group.")
    @SysLog("Update template group")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('codegen_group_edit')")
    public R updateById(@RequestBody GroupVo groupVo) {
        genGroupService.updateGroupAndTemplateById(groupVo);
        return R.ok();
    }

    /**
     * Delete template group by id.
     *
     * @param ids Id list.
     * @return R
     */
    @Operation(summary = "Delete template group by id", description = "Delete template group by id.")
    @SysLog("Delete template group by id")
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('codegen_group_del')")
    public R removeById(@RequestBody Long[] ids) {
        genGroupService.delGroupAndTemplate(ids);
        return R.ok();
    }

    /**
     * Export excel sheet.
     *
     * @param genGroup Query condition.
     * @return Excel file stream.
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('codegen_group_export')")
    public List<GenGroupEntity> export(GenGroupEntity genGroup) {
        return genGroupService.list(Wrappers.query(genGroup));
    }

    @GetMapping("/list")
    @Operation(summary = "Query list", description = "Query list.")
    public R list() {
        List<GenGroupEntity> list = genGroupService.list();
        return R.ok(list);
    }

}
