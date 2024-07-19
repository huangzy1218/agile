package com.agile.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.agile.admin.api.dto.SysLogDTO;
import com.agile.admin.api.entity.SysLog;
import com.agile.admin.service.SysLogService;
import com.agile.common.core.util.R;
import com.agile.common.security.annotation.Inner;
import com.agile.plugin.excel.annotation.ResponseExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/log")
@Tag(description = "log", name = "Log management module")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysLogController {

    private final SysLogService sysLogService;

    /**
     * Simple paging query.
     *
     * @param page   Pagination object
     * @param sysLog System log
     * @return
     */
    @GetMapping("/page")
    public R getLogPage(@ParameterObject Page page, @ParameterObject SysLogDTO sysLog) {
        return R.ok(sysLogService.getLogByPage(page, sysLog));
    }

    /**
     * Delete logs in batches.
     *
     * @param ids ID
     * @return {@code true} for success
     */
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('sys_log_del')")
    public R removeByIds(@RequestBody Long[] ids) {
        return R.ok(sysLogService.removeBatchByIds(CollUtil.toList(ids)));
    }

    /**
     * Insert log.
     *
     * @param sysLog Log entity
     * @return {@code true} for success
     */
    @Inner
    @PostMapping("/save")
    public R save(@Valid @RequestBody SysLog sysLog) {
        return R.ok(sysLogService.saveLog(sysLog));
    }

    /**
     * Export excel table.
     *
     * @param sysLog Query conditions
     * @return
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('sys_log_export')")
    public List<SysLog> export(SysLogDTO sysLog) {
        return sysLogService.getList(sysLog);
    }

}

