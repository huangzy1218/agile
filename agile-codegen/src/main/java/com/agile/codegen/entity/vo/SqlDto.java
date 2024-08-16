package com.agile.codegen.entity.vo;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class SqlDto {

    /**
     * Datasource ID.
     */
    private String dsName;

    /**
     * SQL script.
     */
    private String sql;

}
