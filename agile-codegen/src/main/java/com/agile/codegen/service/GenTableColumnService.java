package com.agile.codegen.service;

import com.agile.codegen.entity.GenTableColumnEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Column properties service.
 *
 * @author Huang Z.Y.
 */
public interface GenTableColumnService extends IService<GenTableColumnEntity> {

    /**
     * Initialize field list.
     *
     * @param tableFieldList Table field list.
     */
    void initFieldList(List<GenTableColumnEntity> tableFieldList);

    /**
     * Update table fields.
     *
     * @param dsName         Data source name.
     * @param tableName      Table name.
     * @param tableFieldList Table field list.
     */
    void updateTableField(String dsName, String tableName, List<GenTableColumnEntity> tableFieldList);

}

