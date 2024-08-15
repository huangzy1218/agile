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

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This class records the configuration information of table columns.
 *
 * @author Huang Z.Y.
 */
@Data
@TableName("gen_table_column")
@EqualsAndHashCode(callSuper = true)
public class GenTableColumnEntity extends Model<GenDatasourceConf> {

    private static final long serialVersionUID = 1L;

    /**
     * Primary key.
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * Data source name.
     */
    private String dsName;

    /**
     * Table name.
     */
    private String tableName;

    /**
     * Field name.
     */
    private String fieldName;

    /**
     * Sort order.
     */
    private Integer sort;

    /**
     * Field type.
     */
    private String fieldType;

    /**
     * Field comment.
     */
    private String fieldComment;

    /**
     * Attribute name.
     */
    private String attrName;

    /**
     * Attribute type.
     */
    private String attrType;

    /**
     * Package name.
     */
    private String packageName;

    /**
     * Auto fill.
     */
    private String autoFill;

    /**
     * Primary key (0: no, 1: yes).
     */
    private String primaryPk;

    /**
     * Base field (0: no, 1: yes).
     */
    private String baseField;

    /**
     * Form item (0: no, 1: yes).
     */
    private String formItem;

    /**
     * Form required (0: no, 1: yes).
     */
    private String formRequired;

    /**
     * Form type.
     */
    private String formType;

    /**
     * Form validator.
     */
    private String formValidator;

    /**
     * Grid item (0: no, 1: yes).
     */
    private String gridItem;

    /**
     * Grid sort (0: no, 1: yes).
     */
    private String gridSort;

    /**
     * Query item (0: no, 1: yes).
     */
    private String queryItem;

    /**
     * Query type.
     */
    private String queryType;

    /**
     * Query form type.
     */
    private String queryFormType;

    /**
     * Field dictionary type.
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String fieldDict;

}
