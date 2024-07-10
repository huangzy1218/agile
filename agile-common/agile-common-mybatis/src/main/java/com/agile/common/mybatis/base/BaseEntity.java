package com.agile.common.mybatis.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Base entity.
 *
 * @author Huang Z.Y.
 */
public class BaseEntity {

    /**
     * Creator.
     */
    @Schema(description = "Creator")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * Create time.
     */
    @Schema(description = "Create time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Updater.
     */
    @Schema(description = "Updater")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * Update time.
     */
    @Schema(description = "Update time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
