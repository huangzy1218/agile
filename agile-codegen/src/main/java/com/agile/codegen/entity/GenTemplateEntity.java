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
 * Template.
 *
 * @author Huang Z.Y.
 */
@Data
@TableName("gen_template")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Template")
public class GenTemplateEntity extends Model<GenTemplateEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Primary key.
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "Primary key")
    private Long id;

    /**
     * Template name.
     */
    @Schema(description = "Template name")
    private String templateName;

    /**
     * Template path.
     */
    @Schema(description = "Template path")
    private String generatorPath;

    /**
     * Template description.
     */
    @Schema(description = "Template description")
    private String templateDesc;

    /**
     * Template code.
     */
    @Schema(description = "Template code")
    private String templateCode;

    /**
     * Created by.
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Created by")
    private String createBy;

    /**
     * Updated by.
     */
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "Updated by")
    private String updateBy;

    /**
     * Creation time.
     */
    @Schema(description = "Creation time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    @Schema(description = "Update time")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * Deletion flag (0 - Active, 1 - Deleted).
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Deletion flag, 1: Deleted, 0: Active")
    private String delFlag;

}
