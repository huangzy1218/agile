package com.agile.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.entity.SysMenu;
import com.agile.admin.api.entity.SysRoleMenu;
import com.agile.admin.mapper.SysMenuMapper;
import com.agile.admin.mapper.SysRoleMenuMapper;
import com.agile.admin.service.SysMenuService;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.constant.CommonConstants;
import com.agile.common.core.enumeration.MenuTypeEnum;
import com.agile.common.core.exception.ErrorCodes;
import com.agile.common.core.util.MsgUtils;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Huang Z.Y.
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    @Cacheable(value = CacheConstants.MENU_DETAILS, key = "#roleId", unless = "#result.isEmpty()")
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        return baseMapper.listMenusByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
    public R removeMenuById(Long id) {
        // Query the node whose parent node is the current node
        List<SysMenu> menuList = this.list(Wrappers.<SysMenu>query().lambda().eq(SysMenu::getParentId, id));
        if (CollUtil.isNotEmpty(menuList)) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_MENU_DELETE_EXISTING));
        }

        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getMenuId, id));
        // Delete the current menu and its submenus
        return R.ok(this.removeById(id));
    }

    @Override
    @CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
    public Boolean updateMenuById(SysMenu sysMenu) {
        return this.updateById(sysMenu);
    }

    /**
     * Building tree query
     * <pre>
     * 1. It is not lazy loading, query all
     * 2. It is lazy loading, query based on parentId
     * 3. If the parent node is empty, query ID -1
     * </pre>
     *
     * @param parentId Parent node ID
     * @param menuName Menu name
     * @return
     */
    @Override
    public List<Tree<Long>> treeMenu(Long parentId, String menuName, String type) {
        Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;

        List<TreeNode<Long>> collect = baseMapper
                .selectList(Wrappers.<SysMenu>lambdaQuery()
                        .like(StrUtil.isNotBlank(menuName), SysMenu::getName, menuName)
                        .eq(StrUtil.isNotBlank(type), SysMenu::getMenuType, type)
                        .orderByAsc(SysMenu::getSortOrder))
                .stream()
                .map(getNodeFunction())
                .collect(Collectors.toList());

        // Fuzzy query does not assemble the tree structure and returns directly.
        if (StrUtil.isNotBlank(menuName)) {
            return collect.stream().map(node -> {
                Tree<Long> tree = new Tree<>();
                tree.putAll(node.getExtra());
                BeanUtils.copyProperties(node, tree);
                return tree;
            }).collect(Collectors.toList());
        }

        return TreeUtil.build(collect, parent);
    }

    /**
     * Query menu.
     *
     * @param all      All Menu
     * @param type     Type
     * @param parentId Parent node ID
     * @return
     */
    @Override
    public List<Tree<Long>> filterMenu(Set<SysMenu> all, String type, Long parentId) {
        List<TreeNode<Long>> collect = all.stream()
                .filter(menuTypePredicate(type))
                .map(getNodeFunction())
                .collect(Collectors.toList());

        Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
        return TreeUtil.build(collect, parent);
    }

    @NotNull
    private Function<SysMenu, TreeNode<Long>> getNodeFunction() {
        return menu -> {
            TreeNode<Long> node = new TreeNode<>();
            node.setId(menu.getMenuId());
            node.setName(menu.getName());
            node.setParentId(menu.getParentId());
            node.setWeight(menu.getSortOrder());
            // 扩展属性
            Map<String, Object> extra = new HashMap<>();
            extra.put("path", menu.getPath());
            extra.put("menuType", menu.getMenuType());
            extra.put("permission", menu.getPermission());
            extra.put("sortOrder", menu.getSortOrder());

            // 适配 vue3
            Map<String, Object> meta = new HashMap<>();
            meta.put("title", menu.getName());
            meta.put("isLink", menu.getPath() != null && menu.getPath().startsWith("http") ? menu.getPath() : "");
            meta.put("isHide", !BooleanUtil.toBooleanObject(menu.getVisible()));
            meta.put("isKeepAlive", BooleanUtil.toBooleanObject(menu.getKeepAlive()));
            meta.put("isAffix", false);
            meta.put("isIframe", BooleanUtil.toBooleanObject(menu.getEmbedded()));
            meta.put("icon", menu.getIcon());
            // Add English
            meta.put("enName", menu.getEnName());

            extra.put("meta", meta);
            node.setExtra(extra);
            return node;
        };
    }

    /**
     * Menu type assertion.
     *
     * @param type Type
     * @return Predicate
     */
    private Predicate<SysMenu> menuTypePredicate(String type) {
        return vo -> {
            if (MenuTypeEnum.TOP_MENU.getDescription().equals(type)) {
                return MenuTypeEnum.TOP_MENU.getType().equals(vo.getMenuType());
            }
            // Other queries left + top
            return !MenuTypeEnum.BUTTON.getType().equals(vo.getMenuType());
        };
    }

}

