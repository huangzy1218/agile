package com.agile.codegen.mapper;

import com.agile.codegen.entity.GenTemplateEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Template.
 *
 * @author Huang Z.Y.
 */
@Mapper
public interface GenTemplateMapper extends BaseMapper<GenTemplateEntity> {

    /**
     * 根据groupId查询 模板
     *
     * @param groupId
     * @return
     */
    List<GenTemplateEntity> listTemplateById(Long groupId);

}
