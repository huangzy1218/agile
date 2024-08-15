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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Column Properties.
 *
 * @author Huang Z.Y.
 */
@Data
@TableName("gen_field_type")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Column Properties")
public class GenFieldType extends Model<GenFieldType> {

    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    /**
     * Column type.
     */
    @Schema(description = "Column Type")
    private String columnType;

    /**
     * Attribute type.
     */
    @Schema(description = "Attribute Type")
    private String attrType;

    /**
     * Package name.
     */
    @Schema(description = "Package Name")
    private String packageName;

    /**
     * Created by.
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Created By")
    private String createBy;

    /**
     * Updated By.
     */
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "Updated By")
    private String updateBy;

    /**
     * Creation time.
     */
    @Schema(description = "Creation Time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    @Schema(description = "Update Time")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * Deletion flag (0 - Active, 1 - Deleted).
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Deletion Flag, 1: Deleted, 0: Active")
    private String delFlag;

}
