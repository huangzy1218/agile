package com.agile.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.agile.admin.api.entity.SysMenu;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * Menu permission table service class.
 *
 * @author Huang Z.Y.
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * Query URL permissions by role number.
     *
     * @param roleId Role ID
     * @return Menu list
     */
    List<SysMenu> findMenuByRoleId(Long roleId);

    /**
     * Cascading delete menu.
     *
     * @param id Menu ID
     * @return {@code true} for success
     */
    R removeMenuById(Long id);

    /**
     * Update menu information.
     *
     * @param sysMenu Menu information
     * @return {@code true} for success
     */
    Boolean updateMenuById(SysMenu sysMenu);

    /**
     * Build tree menu.
     *
     * @param parentId Parent node ID
     * @param menuName Menu name
     */
    List<Tree<Long>> treeMenu(Long parentId, String menuName, String type);

    /**
     * Query menu.
     */
    List<Tree<Long>> filterMenu(Set<SysMenu> voSet, String type, Long parentId);

}
