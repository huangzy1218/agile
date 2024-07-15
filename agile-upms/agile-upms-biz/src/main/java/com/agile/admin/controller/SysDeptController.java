package com.agile.admin.controller;

import com.agile.admin.api.entity.SysDept;
import com.agile.admin.api.vo.DeptExcelVO;
import com.agile.admin.service.SysDeptService;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import com.agile.plugin.excel.annotation.RequestExcel;
import com.agile.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Department management.
 *
 * @author Huang Z.Y.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dept")
@Tag(description = "dept", name = "Department management module")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysDeptController {

    private final SysDeptService sysDeptService;

    /**
     * Query by ID.
     *
     * @param id ID
     * @return SysDept
     */
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok(sysDeptService.getById(id));
    }

    /**
     * Search all departments.
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(sysDeptService.list());
    }

    /**
     * Returns a collection of tree menus.
     *
     * @param deptName Department name
     * @return Tree menu
     */
    @GetMapping(value = "/tree")
    public R getTree(String deptName) {
        return R.ok(sysDeptService.selectTree(deptName));
    }

    /**
     * Add.
     *
     * @param sysDept Entity
     * @return {@code true} for success
     */
    @SysLog("Add department")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_dept_add')")
    public R save(@Valid @RequestBody SysDept sysDept) {
        return R.ok(sysDeptService.save(sysDept));
    }

    /**
     * Delete,
     *
     * @param id ID
     * @return {@code true} for success
     */
    @SysLog("Delete department")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_dept_del')")
    public R removeById(@PathVariable Long id) {
        return R.ok(sysDeptService.removeDeptById(id));
    }

    /**
     * Update.
     *
     * @param sysDept Entity
     * @return {@code true} for success
     */
    @SysLog("编辑部门")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_dept_edit')")
    public R update(@Valid @RequestBody SysDept sysDept) {
        sysDept.setUpdateTime(LocalDateTime.now());
        return R.ok(sysDeptService.updateById(sysDept));
    }

    /**
     * 查收子级列表
     *
     * @return 返回子级
     */
    @GetMapping(value = "/getDescendantList/{deptId}")
    public R getDescendantList(@PathVariable Long deptId) {
        return R.ok(sysDeptService.listDescendant(deptId));
    }

    /**
     * 导出部门
     *
     * @return
     */
    @ResponseExcel
    @GetMapping("/export")
    public List<DeptExcelVO> export() {
        return sysDeptService.listExcelVo();
    }

    /**
     * 导入部门
     *
     * @param excelVOList
     * @param bindingResult
     * @return
     */
    @PostMapping("import")
    public R importDept(@RequestExcel List<DeptExcelVO> excelVOList, BindingResult bindingResult) {

        return sysDeptService.importDept(excelVOList, bindingResult);
    }

}
    