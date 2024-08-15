/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.agile.codegen.entity;

import lombok.Data;

import java.util.List;

/**
 * Table properties.
 *
 * @author Huang Z.Y.
 */
@Data
public class TableEntity {

    /**
     * Table name.
     */
    private String tableName;

    /**
     * Comments.
     */
    private String comments;

    /**
     * Primary key.
     */
    private ColumnEntity pk;

    /**
     * Columns.
     */
    private List<ColumnEntity> columns;

    /**
     * Camel case type.
     */
    private String caseClassName;

    /**
     * Lower case type.
     */
    private String lowerClassName;

    /**
     * Database type (used for database customization).
     */
    private String dbType;

}

