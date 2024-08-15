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
 * Template group.
 * This class represents a template group, including its name, description, and metadata such as creation and update information.
 *
 * @Author Huang Z.Y.
 * @Date 2023-02-21 20:01:53
 */
@Data
@TableName("gen_group")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Template Group")
public class GenGroupEntity extends Model<GenGroupEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    /**
     * Group name.
     */
    @Schema(description = "Group name")
    private String groupName;

    /**
     * Group description.
     */
    @Schema(description = "Group description")
    @TableField(fill = FieldFill.INSERT)
    private String groupDesc;

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
