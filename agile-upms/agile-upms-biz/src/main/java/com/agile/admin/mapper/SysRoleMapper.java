package com.agile.admin.mapper;

import com.agile.admin.api.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * Query role information through user ID.
     */
    List<SysRole> listRolesByUserId(Long userId);

}
