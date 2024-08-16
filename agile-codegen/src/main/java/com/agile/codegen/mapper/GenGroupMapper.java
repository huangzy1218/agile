package com.agile.codegen.mapper;

import com.agile.codegen.entity.GenGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Template group.
 *
 * @author Huang Z.Y.
 */
@Mapper
public interface GenGroupMapper extends BaseMapper<GenGroupEntity> {

    GroupVo getGroupVoById(@Param("id") Long id);

}
