package com.agile.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.entity.SysPublicParam;
import com.agile.admin.service.SysPublicParamService;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import com.agile.common.security.annotation.Inner;
import com.agile.plugin.excel.annotation.ResponseExcel;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public parameter configuration.
 *
 * @author Huang Z.Y.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/param")
@Tag(description = "param", name = "Public parameter configuration")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysPublicParamController {

    private final SysPublicParamService sysPublicParamService;

    /**
     * Query public parameter values by key.
     *
     * @param publicKey Public key
     */
    @Inner(value = false)
    @Operation(description = "Query public parameter values", summary = "Query public parameter values")
    @GetMapping("/publicValue/{publicKey}")
    public R publicKey(@PathVariable("publicKey") String publicKey) {
        return R.ok(sysPublicParamService.getSysPublicParamKeyToValue(publicKey));
    }

    /**
     * Paging query.
     *
     * @param page           Pagination object
     * @param sysPublicParam Public parameters
     * @return
     */
    @Operation(description = "Paging query", summary = "Paging query")
    @GetMapping("/page")
    public R getSysPublicParamPage(@ParameterObject Page page, @ParameterObject SysPublicParam sysPublicParam) {
        LambdaUpdateWrapper<SysPublicParam> wrapper = Wrappers.<SysPublicParam>lambdaUpdate()
                .like(StrUtil.isNotBlank(sysPublicParam.getPublicName()), SysPublicParam::getPublicName,
                        sysPublicParam.getPublicName())
                .like(StrUtil.isNotBlank(sysPublicParam.getPublicKey()), SysPublicParam::getPublicKey,
                        sysPublicParam.getPublicKey())
                .eq(StrUtil.isNotBlank(sysPublicParam.getSystemFlag()), SysPublicParam::getSystemFlag,
                        sysPublicParam.getSystemFlag());

        return R.ok(sysPublicParamService.page(page, wrapper));
    }

    /**
     * Query public parameters by ID.
     *
     * @param publicId Public ID
     */
    @Operation(description = "Query public parameters by ID", summary = "Query public parameters by ID")
    @GetMapping("/details/{publicId}")
    public R getById(@PathVariable("publicId") Long publicId) {
        return R.ok(sysPublicParamService.getById(publicId));
    }

    @GetMapping("/details")
    public R getDetail(@ParameterObject SysPublicParam param) {
        return R.ok(sysPublicParamService.getOne(Wrappers.query(param), false));
    }

    /**
     * Add new public parameters.
     *
     * @param sysPublicParam Public parameters
     * @return R
     */
    @Operation(description = "Add new public parameters", summary = "Add new public parameters")
    @SysLog("Add new public parameters")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_syspublicparam_add')")
    public R save(@RequestBody SysPublicParam sysPublicParam) {
        return R.ok(sysPublicParamService.save(sysPublicParam));
    }

    /**
     * Modify public parameters.
     *
     * @param sysPublicParam Public parameter
     */
    @Operation(description = "Modify public parameters", summary = "Modify public parameters")
    @SysLog("Modify public parameters")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_syspublicparam_edit')")
    public R updateById(@RequestBody SysPublicParam sysPublicParam) {
        return sysPublicParamService.updateParam(sysPublicParam);
    }

    /**
     * Remove public parameters by ID.
     *
     * @param ids ID list
     * @return R
     */
    @Operation(description = "Remove public parameters", summary = "Remove public parameters")
    @SysLog("Remove public parameters")
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('sys_syspublicparam_del')")
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysPublicParamService.removeParamByIds(ids));
    }

    /**
     * Export excel table.
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('sys_syspublicparam_edit')")
    public List<SysPublicParam> export() {
        return sysPublicParamService.list();
    }

    /**
     * Synchronization parameters.
     *
     * @return R
     */
    @SysLog("Synchronization parameters")
    @PutMapping("/sync")
    @PreAuthorize("@pms.hasPermission('sys_syspublicparam_edit')")
    public R sync() {
        return sysPublicParamService.syncParamCache();
    }

}

