package com.agile.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.entity.SysPost;
import com.agile.admin.api.vo.PostExcelVO;
import com.agile.admin.service.SysPostService;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import com.agile.plugin.excel.annotation.RequestExcel;
import com.agile.plugin.excel.annotation.ResponseExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Position information management module.
 *
 * @author Huang Z.Y.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Tag(description = "post", name = "Position information management module")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysPostController {

    private final SysPostService sysPostService;

    /**
     * Get position list.
     *
     * @return Position list
     */
    @GetMapping("/list")
    public R<List<SysPost>> listPosts() {
        return R.ok(sysPostService.list(Wrappers.emptyWrapper()));
    }

    /**
     * Paging query.
     *
     * @param page    Pagination object
     * @param sysPost Position information form
     * @return
     */
    @Operation(description = "Paging query", summary = "Paging query")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('sys_post_view')")
    public R getSysPostPage(@ParameterObject Page page, @ParameterObject SysPost sysPost) {
        return R.ok(sysPostService.page(page, Wrappers.<SysPost>lambdaQuery()
                .like(StrUtil.isNotBlank(sysPost.getPostName()), SysPost::getPostName, sysPost.getPostName())));
    }

    /**
     * Query position information table by ID.
     *
     * @param postId id position ID
     */
    @Operation(description = "Query position information table by ID", summary = "Query position information table by ID")
    @GetMapping("/details/{postId}")
    @PreAuthorize("@pms.hasPermission('sys_post_view')")
    public R getById(@PathVariable("postId") Long postId) {
        return R.ok(sysPostService.getById(postId));
    }

    /**
     * Query position information.
     *
     * @param query Query conditions
     */
    @Operation(description = "Query position information", summary = "Query position information")
    @GetMapping("/details")
    @PreAuthorize("@pms.hasPermission('sys_post_view')")
    public R getDetails(SysPost query) {
        return R.ok(sysPostService.getOne(Wrappers.query(query), false));
    }

    /**
     * Add new position information.
     *
     * @param sysPost Position information
     */
    @Operation(description = "Add new position information", summary = "Add new position information")
    @SysLog("Add new position information")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_post_add')")
    public R save(@RequestBody SysPost sysPost) {
        return R.ok(sysPostService.save(sysPost));
    }

    /**
     * Modify position information.
     *
     * @param sysPost Position information
     */
    @Operation(description = "Modify position information", summary = "Modify position information")
    @SysLog("Modify position information")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_post_edit')")
    public R updateById(@RequestBody SysPost sysPost) {
        return R.ok(sysPostService.updateById(sysPost));
    }

    /**
     * Delete position information by ID.
     *
     * @param ids ID list
     */
    @Operation(description = "Delete position information by ID", summary = "Delete position information by ID")
    @SysLog("Delete position information by ID")
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('sys_post_del')")
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysPostService.removeBatchByIds(CollUtil.toList(ids)));
    }

    /**
     * Export excel table.
     *
     * @return Excel file stream
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('sys_post_export')")
    public List<PostExcelVO> export() {
        return sysPostService.listPost();
    }

    /**
     * Import positions.
     *
     * @param excelVOList   Position list
     * @param bindingResult Error message list
     * @return {@code true} for success
     */
    @PostMapping("/import")
    @PreAuthorize("@pms.hasPermission('sys_post_export')")
    public R importRole(@RequestExcel List<PostExcelVO> excelVOList, BindingResult bindingResult) {
        return sysPostService.importPost(excelVOList, bindingResult);
    }

}

