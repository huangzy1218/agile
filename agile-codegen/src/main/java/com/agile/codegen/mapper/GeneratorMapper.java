package com.agile.codegen.mapper;

import com.agile.codegen.entity.ColumnEntity;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Code generator.
 * This interface defines methods for querying and managing database tables and columns.
 *
 * @author Huang Z.Y.
 */
public interface GeneratorMapper extends BaseMapper<ColumnEntity> {

    /**
     * Query all tables.
     *
     * @return A list of maps containing table information.
     */
    @InterceptorIgnore(tenantLine = "true")
    List<Map<String, Object>> queryTable();

    /**
     * Query tables with pagination.
     *
     * @param page      Pagination information.
     * @param tableName Table name.
     * @return A paginated list of maps containing table information.
     */
    @InterceptorIgnore(tenantLine = "true")
    IPage<Map<String, Object>> queryTable(Page page, @Param("tableName") String tableName);

    /**
     * Query table information.
     *
     * @param tableName Table name
     * @param dsName    Data source name
     * @return A map containing table information
     */
    @DS("#last")
    @InterceptorIgnore(tenantLine = "true")
    Map<String, String> queryTable(@Param("tableName") String tableName, String dsName);

    /**
     * Query table columns with pagination.
     *
     * @param page      Pagination information
     * @param tableName Table name
     * @param dsName    Data source name
     * @return A paginated list of ColumnEntity objects
     */
    @DS("#last")
    @InterceptorIgnore(tenantLine = "true")
    IPage<ColumnEntity> selectTableColumn(Page page, @Param("tableName") String tableName,
                                          @Param("dsName") String dsName);

    /**
     * Query all columns of a table.
     *
     * @param tableName Table name
     * @param dsName    Data source name
     * @return A list of ColumnEntity objects
     */
    @DS("#last")
    @InterceptorIgnore(tenantLine = "true")
    List<ColumnEntity> selectTableColumn(@Param("tableName") String tableName, @Param("dsName") String dsName);

    /**
     * Query all columns of a table as a map.
     *
     * @param tableName Table name
     * @param dsName    Data source name
     * @return A list of maps containing column information
     */
    @DS("#last")
    @InterceptorIgnore(tenantLine = "true")
    List<Map<String, String>> selectMapTableColumn(@Param("tableName") String tableName, String dsName);

}