package com.agile.admin.mapper;

import com.agile.admin.api.dto.UserDTO;
import com.agile.admin.api.entity.SysUser;
import com.agile.admin.api.vo.UserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * User table Mapper interface.
 *
 * @author Huang Z.Y.
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * Query user information by username (including role information).
     *
     * @param username 用户名
     * @return userVo
     */
    UserVO getUserVoByUsername(String username);

    /**
     * Query user information (including roles) in pages.
     *
     * @param page    Pagination
     * @param userDTO Query parameters
     * @return list
     */
    IPage<UserVO> getUserVosPage(Page page, @Param("query") UserDTO userDTO);

    /**
     * Query user information by ID.
     *
     * @param id User ID
     * @return userVo
     */
    UserVO getUserVoById(Long id);

    /**
     * Query user list.
     *
     * @param userDTO Query conditions
     * @return
     */
    List<UserVO> selectVoList(@Param("query") UserDTO userDTO);

}

