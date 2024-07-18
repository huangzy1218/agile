package com.agile.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.entity.SysDict;
import com.agile.admin.api.entity.SysDictItem;
import com.agile.admin.service.SysDictItemService;
import com.agile.admin.service.SysDictService;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import com.agile.common.security.annotation.Inner;
import com.agile.plugin.excel.annotation.ResponseExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Dictionary management module.
 *
 * @author Huang Z.Y.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dict")
@Tag(description = "dict", name = "Dictionary management module")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysDictController {

    private final SysDictService sysDictService;

    private final SysDictItemService sysDictItemService;

    /**
     * Query dictionary information by ID.
     *
     * @param id ID
     * @return Dictionary information
     */
    @GetMapping("/details/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok(sysDictService.getById(id));
    }

    /**
     * Query dictionary information.
     *
     * @param query Search information
     * @return Dictionary information
     */
    @GetMapping("/details")
    public R getDetails(@ParameterObject SysDict query) {
        return R.ok(sysDictService.getOne(Wrappers.query(query), false));
    }

    /**
     * Query dictionary information by page.
     *
     * @param page Pagination object
     * @return Pagination object
     */
    @GetMapping("/page")
    public R<IPage> getDictPage(@ParameterObject Page page, @ParameterObject SysDict sysDict) {
        return R.ok(sysDictService.page(page,
                Wrappers.<SysDict>lambdaQuery()
                        .eq(StrUtil.isNotBlank(sysDict.getSystemFlag()), SysDict::getSystemFlag, sysDict.getSystemFlag())
                        .like(StrUtil.isNotBlank(sysDict.getDictType()), SysDict::getDictType, sysDict.getDictType())));
    }


    /**
     * Add dictionary
     *
     * @param sysDict Dictionary information
     * @return {@code true} for success
     */
    @SysLog("Add dictionary")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_dict_add')")
    public R save(@Valid @RequestBody SysDict sysDict) {
        sysDictService.save(sysDict);
        return R.ok(sysDict);
    }

    /**
     * Delete the dictionary and clear the dictionary cache.
     *
     * @param ids ID
     * @return R
     */
    @SysLog("DeleteDictionary")
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('sys_dict_del')")
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysDictService.removeDictByIds(ids));
    }

    /**
     * Modify dictionary.
     *
     * @param sysDict Dictionary information
     * @return {@code true} for success
     */
    @PutMapping
    @SysLog("Modify dictionary")
    @PreAuthorize("@pms.hasPermission('sys_dict_edit')")
    public R updateById(@Valid @RequestBody SysDict sysDict) {
        return sysDictService.updateDict(sysDict);
    }

    /**
     * Paging query
     *
     * @param name Name or dictionary item
     * @return
     */
    @GetMapping("/list")
    public R getDictList(String name) {
        return R.ok(sysDictService.list(Wrappers.<SysDict>lambdaQuery()
                .like(StrUtil.isNotBlank(name), SysDict::getDictType, name)
                .or()
                .like(StrUtil.isNotBlank(name), SysDict::getDescription, name)));
    }

    /**
     * Paging query.
     *
     * @param page        Pagination object
     * @param sysDictItem Dictionary item
     */
    @GetMapping("/item/page")
    public R getSysDictItemPage(Page page, SysDictItem sysDictItem) {
        return R.ok(sysDictItemService.page(page, Wrappers.query(sysDictItem)));
    }

    /**
     * Query dictionary items by id.
     *
     * @param id id
     * @return R
     */
    @GetMapping("/item/details/{id}")
    public R getDictItemById(@PathVariable("id") Long id) {
        return R.ok(sysDictItemService.getById(id));
    }

    /**
     * Query dictionary item details.
     *
     * @param query Query conditions
     * @return R
     */
    @GetMapping("/item/details")
    public R getDictItemDetails(SysDictItem query) {
        return R.ok(sysDictItemService.getOne(Wrappers.query(query), false));
    }

    /**
     * Add new dictionary item.
     *
     * @param sysDictItem Dictionary item
     * @return R
     */
    @SysLog("Add new dictionary item")
    @PostMapping("/item")
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public R save(@RequestBody SysDictItem sysDictItem) {
        return R.ok(sysDictItemService.save(sysDictItem));
    }

    /**
     * Modify dictionary item.
     *
     * @param sysDictItem Dictionary item
     * @return R
     */
    @SysLog("Modify dictionary item")
    @PutMapping("/item")
    public R updateById(@RequestBody SysDictItem sysDictItem) {
        return sysDictItemService.updateDictItem(sysDictItem);
    }

    /**
     * Delete dictionary item by id
     *
     * @param id id
     * @return R
     */
    @SysLog("Delete dictionary item")
    @DeleteMapping("/item/{id}")
    public R removeDictItemById(@PathVariable Long id) {
        return sysDictItemService.removeDictItem(id);
    }

    /**
     * Synchronize cache dictionary
     *
     * @return R
     */
    @SysLog("Synchronized dictionary")
    @PutMapping("/sync")
    public R sync() {
        return sysDictService.syncDictCache();
    }

    @ResponseExcel
    @GetMapping("/export")
    public List<SysDictItem> export(SysDictItem sysDictItem) {
        return sysDictItemService.list(Wrappers.query(sysDictItem));
    }

    /**
     * Find dictionary by dictionary type.
     *
     * @param type Type
     * @return Dictionaries of the same type
     */
    @GetMapping("/type/{type}")
    @Cacheable(value = CacheConstants.DICT_DETAILS, key = "#type", unless = "#result.data.isEmpty()")
    public R<List<SysDictItem>> getDictByType(@PathVariable String type) {
        return R.ok(sysDictItemService.list(Wrappers.<SysDictItem>query().lambda().eq(SysDictItem::getDictType, type)));
    }

    /**
     * Search dictionary by dictionary type (called for feign).
     *
     * @param type Type
     * @return Dictionaries of the same type
     */
    @Inner
    @GetMapping("/remote/type/{type}")
    @Cacheable(value = CacheConstants.DICT_DETAILS, key = "#type", unless = "#result.data.isEmpty()")
    public R<List<SysDictItem>> getRemoteDictByType(@PathVariable String type) {
        return R.ok(sysDictItemService.list(Wrappers.<SysDictItem>query().lambda().eq(SysDictItem::getDictType, type)));
    }

}

