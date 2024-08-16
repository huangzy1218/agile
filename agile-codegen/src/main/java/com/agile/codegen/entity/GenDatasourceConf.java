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

import java.time.LocalDateTime;

/**
 * Data source table.
 *
 * @author Huang Z.Y.
 */
@Data
@TableName("gen_datasource_conf")
@EqualsAndHashCode(callSuper = true)
public class GenDatasourceConf extends Model<GenDatasourceConf> {

    private static final long serialVersionUID = 1L;

    /**
     * Primary key.
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * Name.
     */
    private String name;

    /**
     * Database type.
     */
    private String dsType;

    /**
     * Configuration Type (0 - Host Form | 1 - URL Form).
     */
    private Integer confType;

    /**
     * Host Address.
     */
    private String host;

    /**
     * Port.
     */
    private Integer port;

    /**
     * JDBC URL.
     */
    private String url;

    /**
     * Instance.
     */
    private String instance;

    /**
     * Database Name.
     */
    private String dsName;

    /**
     * Username.
     */
    private String username;

    /**
     * Password.
     */
    private String password;

    /**
     * Creation Time.
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Update Time.
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 0 - Active, 1 - Deleted.
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private String delFlag;

}
