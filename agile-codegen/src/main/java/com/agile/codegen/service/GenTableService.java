package com.agile.codegen.service;

import com.agile.codegen.entity.GenTable;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * Column properties.
 *
 * @author Huang Z.Y.
 */
public interface GenTableService extends IService<GenTable> {

    /**
     * Get default configuration information.
     *
     * @return Default configuration information.
     */
    Map<String, Object> getGeneratorConfig();

    /**
     * Paginate and query table list.
     *
     * @param page  Pagination object.
     * @param table Query conditions.
     * @return Paginated result of table list.
     */
    IPage list(Page<GenTable> page, GenTable table);

    /**
     * Query or build table by data source name and table name.
     *
     * @param dsName    Data source name.
     * @param tableName Table name.
     * @return Queried table information.
     */
    GenTable queryOrBuildTable(String dsName, String tableName);

    /**
     * Query all tables under the specified data source.
     *
     * @param dsName Data source name.
     * @return List of all tables.
     */
    List<Map<String, Object>> queryDsAllTable(String dsName);

    /**
     * Query column information of the specified data source and table name.
     *
     * @param dsName    Data source name.
     * @param tableName Table name.
     * @return List of column information.
     */
    List<Map<String, String>> queryColumn(String dsName, String tableName);

}
