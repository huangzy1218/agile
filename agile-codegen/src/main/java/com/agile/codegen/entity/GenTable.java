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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Column properties.
 *
 * @author Huang Z.Y.
 */
@Data
@TableName("gen_table")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Column properties")
public class GenTable extends Model<GenTable> {

    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    /**
     * Data source name.
     */
    @Schema(description = "Data source name")
    private String dsName;

    /**
     * Data source type.
     */
    @Schema(description = "Data source type")
    private String dbType;

    /**
     * Table name.
     */
    @Schema(description = "Table name")
    private String tableName;

    /**
     * Class name.
     */
    @Schema(description = "Class name")
    private String className;

    /**
     * Description.
     */
    @Schema(description = "Description")
    private String tableComment;

    /**
     * Author.
     */
    @Schema(description = "Author")
    private String author;

    /**
     * Email.
     */
    @Schema(description = "Email")
    private String email;

    /**
     * Package name.
     */
    @Schema(description = "Package name")
    private String packageName;

    /**
     * Version.
     */
    @Schema(description = "Version")
    private String version;

    /**
     * Generation type (0: zip package, 1: custom directory).
     */
    @Schema(description = "Generation type  0: zip package   1: custom directory")
    private String generatorType;

    /**
     * Backend path.
     */
    @Schema(description = "Backend path")
    private String backendPath;

    /**
     * Frontend path.
     */
    @Schema(description = "Frontend path")
    private String frontendPath;

    /**
     * Module name.
     */
    @Schema(description = "Module name")
    private String moduleName;

    /**
     * Function name.
     */
    @Schema(description = "Function name")
    private String functionName;

    /**
     * Form layout (1: one column, 2: two columns).
     */
    @Schema(description = "Form layout  1: one column   2: two columns")
    private Integer formLayout;

    /**
     * Base class ID.
     */
    @Schema(description = "Base class ID")
    private Long baseclassId;

    /**
     * Creation time.
     */
    @Schema(description = "Creation time")
    private LocalDateTime createTime;

    /**
     * Code generation style.
     */
    private Long style;

    /**
     * Field list.
     */
    @TableField(exist = false)
    private List<GenTableColumnEntity> fieldList;

    /**
     * Code style (template group information).
     */
    @TableField(exist = false)
    private List<GenGroupEntity> groupList;

}

