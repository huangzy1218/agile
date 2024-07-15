package com.agile.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * File management.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "File")
@EqualsAndHashCode(callSuper = true)
public class SysFile extends Model<SysFile> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "File ID")
    private Long id;

    /**
     * File ID.
     */
    @Schema(description = "File ID")
    private String fileName;

    /**
     * Original file name.
     */
    @Schema(description = "Original file name")
    private String original;

    /**
     * Container name.
     */
    @Schema(description = "Bucket name")
    private String bucketName;

    /**
     * File type
     */
    @Schema(description = "File type")
    private String type;

    /**
     * File size
     */
    @Schema(description = "File size")
    private Long fileSize;

    /**
     * Uplink.
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Creator")
    private String createBy;

    /**
     * Upload time
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Creation time")
    private LocalDateTime createTime;

    /**
     * Updater.
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Updater")
    private String updateBy;

    /**
     * Update time.
     */
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "Update time")
    private LocalDateTime updateTime;

    /**
     * Delete identifier: 1- Delete, 0- Normal.
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Delete the flag,1: deleted,0: normal")
    private String delFlag;

}
    