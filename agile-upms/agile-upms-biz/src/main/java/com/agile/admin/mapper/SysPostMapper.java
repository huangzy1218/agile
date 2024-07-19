package com.agile.admin.mapper;

import com.agile.admin.api.entity.SysPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
@Mapper
public interface SysPostMapper extends BaseMapper<SysPost> {

    /**
     * Query job information through user ID.
     * @param userId User ID
     * @return Position information
     */
    List<SysPost> listPostsByUserId(Long userId);

}
