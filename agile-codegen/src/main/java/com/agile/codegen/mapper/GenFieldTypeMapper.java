package com.agile.codegen.mapper;

import com.agile.codegen.entity.GenFieldType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * Column properties.
 *
 * @author Huang Z.Y.
 */
@Mapper
public interface GenFieldTypeMapper extends BaseMapper<GenFieldType> {

    /**
     * Get the package list based on tableId.
     *
     * @param dsName    Datasource name
     * @param tableName Table name
     * @return Package name
     */
    Set<String> getPackageByTableId(@Param("dsName") String dsName, @Param("tableName") String tableName);

}
