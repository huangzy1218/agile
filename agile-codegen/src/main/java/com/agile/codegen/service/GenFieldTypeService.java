package com.agile.codegen.service;

import com.agile.codegen.entity.GenFieldType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * Column field type generator.
 *
 * @author Huang Z.Y.
 */
public interface GenFieldTypeService extends IService<GenFieldType> {

    /**
     * Get package list by table ID.
     *
     * @param dsName    Data source name
     * @param tableName Table name
     * @return Set of package names.
     */
    Set<String> getPackageByTableId(String dsName, String tableName);

}
