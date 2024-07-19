package com.agile.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.entity.SysRoleMenu;
import com.agile.admin.mapper.SysRoleMenuMapper;
import com.agile.admin.service.SysRoleMenuService;
import com.agile.common.core.constant.CacheConstants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Role menu table service implementation class.
 *
 * @author Huang Z.Y.
 */
@Service
@AllArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    private final CacheManager cacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.MENU_DETAILS, key = "#roleId")
    public Boolean saveRoleMenus(Long roleId, String menuIds) {
        this.remove(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, roleId));

        if (StrUtil.isBlank(menuIds)) {
            return Boolean.TRUE;
        }
        List<SysRoleMenu> roleMenuList = Arrays.stream(menuIds.split(StrUtil.COMMA)).map(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(Long.valueOf(menuId));
            return roleMenu;
        }).collect(Collectors.toList());

        // Clear userinfo
        cacheManager.getCache(CacheConstants.USER_DETAILS).clear();
        this.saveBatch(roleMenuList);
        return Boolean.TRUE;
    }

}

