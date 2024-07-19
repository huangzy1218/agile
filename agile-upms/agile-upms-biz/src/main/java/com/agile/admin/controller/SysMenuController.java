package com.agile.admin.controller;

import com.agile.admin.api.entity.SysMenu;
import com.agile.common.core.util.R;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Menu management module.
 *
 * @author Huang Z.Y.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/menu")
@Tag(description = "menu", name = "Menu management module")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysMenuController {

    private final SysMenuService sysMenuService;

    /**
     * Returns the current user's tree menu collection.
     *
     * @param type     Type
     * @param parentId Parent node ID
     * @return Current user's tree menu
     */
    @GetMapping
    public R getUserMenu(String type, Long parentId) {
        // Get eligible menus
        Set<SysMenu> all = new HashSet<>();
        SecurityUtils.getRoles().forEach(roleId -> all.addAll(sysMenuService.findMenuByRoleId(roleId)));
        return R.ok(sysMenuService.filterMenu(all, type, parentId));
    }

    /**
     * Return to tree menu collection.
     *
     * @param parentId Parent node ID
     * @param menuName Menu node
     * @return Tree menu
     */
    @GetMapping(value = "/tree")
    public R getTree(Long parentId, String menuName, String type) {
        return R.ok(sysMenuService.treeMenu(parentId, menuName, type));
    }

    /**
     * Returns the character's menu collection.
     *
     * @param roleId Role ID
     * @return Property collection
     */
    @GetMapping("/tree/{roleId}")
    public R getRoleTree(@PathVariable Long roleId) {
        return R
                .ok(sysMenuService.findMenuByRoleId(roleId).stream().map(SysMenu::getMenuId).collect(Collectors.toList()));
    }

    /**
     * Query menu details by ID.
     *
     * @param id 菜单ID
     * @return 菜单详细信息
     */
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok(sysMenuService.getById(id));
    }

    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return success/false
     */
    @SysLog("新增菜单")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_menu_add')")
    public R save(@Valid @RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return R.ok(sysMenu);
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return success/false
     */
    @SysLog("删除菜单")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_menu_del')")
    public R removeById(@PathVariable Long id) {
        return sysMenuService.removeMenuById(id);
    }

    /**
     * 更新菜单
     *
     * @param sysMenu
     * @return
     */
    @SysLog("更新菜单")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_menu_edit')")
    public R update(@Valid @RequestBody SysMenu sysMenu) {
        return R.ok(sysMenuService.updateMenuById(sysMenu));
    }

}

