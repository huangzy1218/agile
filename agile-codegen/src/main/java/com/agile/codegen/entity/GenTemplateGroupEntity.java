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

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Template group association table.
 *
 * @author Huang Z.Y.
 */
@Data
@TableName("gen_template_group")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Template group association table")
public class GenTemplateGroupEntity extends Model<GenTemplateGroupEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Group ID.
     */
    @Schema(description = "Group ID")
    private Long groupId;

    /**
     * Template ID.
     */
    @Schema(description = "Template ID")
    private Long templateId;

}
