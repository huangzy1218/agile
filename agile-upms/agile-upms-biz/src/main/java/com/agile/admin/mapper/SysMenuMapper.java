package com.agile.admin.mapper;

import com.agile.admin.api.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * Query menu by role ID.
     *
     * @param roleId Role ID
     * @return
     */
    List<SysMenu> listMenusByRoleId(Long roleId);

}
