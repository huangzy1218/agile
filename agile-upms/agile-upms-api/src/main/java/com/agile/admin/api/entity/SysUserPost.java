package com.agile.admin.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User position table.
 *
 * @author Huang Z.Y.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserPost extends Model<SysUserPost> {

    private static final long serialVersionUID = 1406023148865963125L;

    /**
     * User ID.
     */
    @Schema(description = "User ID")
    private Long userId;

    /**
     * Post ID.
     */
    @Schema(description = "Post ID")
    private Long postId;

}
    