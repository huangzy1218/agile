package com.agile.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.entity.SysOauthClientDetails;
import com.agile.admin.service.SysOauthClientDetailsService;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import com.agile.common.security.annotation.Inner;
import com.agile.plugin.excel.annotation.ResponseExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
@RequestMapping("/client")
@Tag(description = "client", name = "Client management module")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysClientController {

    private final SysOauthClientDetailsService clientDetailsService;

    /**
     * Query by ID.
     *
     * @param clientId clientId
     * @return SysOauthClientDetails
     */
    @GetMapping("/{clientId}")
    public R getByClientId(@PathVariable String clientId) {
        SysOauthClientDetails details = clientDetailsService
                .getOne(Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, clientId));
        return R.ok(details);
    }

    /**
     * Simple paging query.
     *
     * @param page                  Pagination object
     * @param sysOauthClientDetails System terminal
     */
    @GetMapping("/page")
    public R getOauthClientDetailsPage(@ParameterObject Page page,
                                       @ParameterObject SysOauthClientDetails sysOauthClientDetails) {
        LambdaQueryWrapper<SysOauthClientDetails> wrapper = Wrappers.<SysOauthClientDetails>lambdaQuery()
                .like(StrUtil.isNotBlank(sysOauthClientDetails.getClientId()), SysOauthClientDetails::getClientId,
                        sysOauthClientDetails.getClientId())
                .like(StrUtil.isNotBlank(sysOauthClientDetails.getClientSecret()), SysOauthClientDetails::getClientSecret,
                        sysOauthClientDetails.getClientSecret());
        return R.ok(clientDetailsService.page(page, wrapper));
    }

    /**
     * Add terminal.
     *
     * @param clientDetails Entity
     * @return {@code true} for success
     */
    @SysLog("Add terminal")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_client_add')")
    public R add(@Valid @RequestBody SysOauthClientDetails clientDetails) {
        return R.ok(clientDetailsService.saveClient(clientDetails));
    }

    /**
     * Delete.
     *
     * @param ids ID list
     * @return {@code true} for success
     */
    @SysLog("Delete terminal")
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('sys_client_del')")
    public R removeById(@RequestBody Long[] ids) {
        clientDetailsService.removeBatchByIds(CollUtil.toList(ids));
        return R.ok();
    }

    /**
     * Edit.
     *
     * @param clientDetails Entity
     * @return {@code true} for success
     */
    @SysLog("Edit terminal")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_client_edit')")
    public R update(@Valid @RequestBody SysOauthClientDetails clientDetails) {
        return R.ok(clientDetailsService.updateClientById(clientDetails));
    }

    @Inner
    @GetMapping("/getClientDetailsById/{clientId}")
    public R getClientDetailsById(@PathVariable String clientId) {
        return R.ok(clientDetailsService.getOne(
                Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, clientId), false));
    }

    /**
     * Synchronize cache dictionary.
     *
     * @return R
     */
    @SysLog("Sync terminal")
    @PutMapping("/sync")
    public R sync() {
        return clientDetailsService.syncClientCache();
    }

    /**
     * Export all clients.
     *
     * @return excel
     */
    @ResponseExcel
    @SysLog("Export excel")
    @GetMapping("/export")
    public List<SysOauthClientDetails> export(SysOauthClientDetails sysOauthClientDetails) {
        return clientDetailsService.list(Wrappers.query(sysOauthClientDetails));
    }

}
