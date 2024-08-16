package com.agile.codegen.entity;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class ColumnEntity {

    /**
     * Column name.
     */
    private String columnName;

    /**
     * Data type.
     */
    private String dataType;

    /**
     * Java data type.
     */
    private String javaType;

    /**
     * Comments.
     */
    private String comments;

    /**
     * Case attribute name.
     */
    private String caseAttrName;

    /**
     * Common properties.
     */
    private String lowerAttrName;

    /**
     * Property type.
     */
    private String attrType;

    /**
     * Extra information.
     */
    private String extra;

    /**
     * Field type.
     */
    private String columnType;

    /**
     * Whether it can be empty.
     */
    private Boolean nullable;

    /**
     * Whether to hide.
     */
    private Boolean hidden;

}
