package com.agile.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.entity.SysRole;
import com.agile.admin.api.vo.RoleExcelVO;
import com.agile.admin.api.vo.RoleVO;
import com.agile.admin.service.SysRoleService;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import com.agile.plugin.excel.annotation.RequestExcel;
import com.agile.plugin.excel.annotation.ResponseExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Role management module.
 *
 * @author Huang Z.Y.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/role")
@Tag(description = "role", name = "Role management module")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * Query role information by ID.
     *
     * @param id ID
     * @return Role information
     */
    @GetMapping("/details/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok(sysRoleService.getById(id));
    }

    /**
     * Query role information.
     *
     * @param query Query condition
     * @return Role information
     */
    @GetMapping("/details")
    public R getDetails(@ParameterObject SysRole query) {
        return R.ok(sysRoleService.getOne(Wrappers.query(query), false));
    }

    /**
     * Add role.
     *
     * @param sysRole Role information
     * @return {@code true} for success
     */
    @SysLog("Add role")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_role_add')")
    @CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
    public R save(@Valid @RequestBody SysRole sysRole) {
        return R.ok(sysRoleService.save(sysRole));
    }

    /**
     * Modify role.
     *
     * @param sysRole Role information
     * @return {@code true} for success
     */
    @SysLog("Modify role")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_role_edit')")
    @CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
    public R update(@Valid @RequestBody SysRole sysRole) {
        return R.ok(sysRoleService.updateById(sysRole));
    }

    /**
     * Delete role.
     *
     * @param ids ID list
     * @return {@code true} for success
     */
    @SysLog("Delete role")
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('sys_role_del')")
    @CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysRoleService.removeRoleByIds(ids));
    }

    /**
     * Get role list.
     *
     * @return Role list
     */
    @GetMapping("/list")
    public R listRoles() {
        return R.ok(sysRoleService.list(Wrappers.emptyWrapper()));
    }

    /**
     * Query role information by page.
     *
     * @param page Pagination object
     * @param role Query conditions
     * @return Pagination object
     */
    @GetMapping("/page")
    public R getRolePage(Page page, SysRole role) {
        return R.ok(sysRoleService.page(page, Wrappers.<SysRole>lambdaQuery()
                .like(StrUtil.isNotBlank(role.getRoleName()), SysRole::getRoleName, role.getRoleName())));
    }

    /**
     * Update role menu.
     *
     * @param roleVo Role object
     * @return {@code true} for success
     */
    @SysLog("Update role menu")
    @PutMapping("/menu")
    @PreAuthorize("@pms.hasPermission('sys_role_perm')")
    public R saveRoleMenus(@RequestBody RoleVO roleVo) {
        return R.ok(sysRoleService.updateRoleMenus(roleVo));
    }

    /**
     * Query role list by role ID.
     *
     * @param roleIdList Role ID
     */
    @PostMapping("/getRoleList")
    public R getRoleList(@RequestBody List<Long> roleIdList) {
        return R.ok(sysRoleService.findRolesByRoleIds(roleIdList, CollUtil.join(roleIdList, StrUtil.UNDERLINE)));
    }

    /**
     * Export excel table.
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('sys_role_export')")
    public List<RoleExcelVO> export() {
        return sysRoleService.listRole();
    }

    /**
     * Import roles.
     *
     * @param excelVOList   Role list
     * @param bindingResult Error message list
     * @return ok fail
     */
    @PostMapping("/import")
    @PreAuthorize("@pms.hasPermission('sys_role_export')")
    public R importRole(@RequestExcel List<RoleExcelVO> excelVOList, BindingResult bindingResult) {
        return sysRoleService.importRole(excelVOList, bindingResult);
    }

}

