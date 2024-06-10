package com.agile.admin.api.entity;

import com.agile.common.core.constant.CommonConstants;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "User")
public class SysUser implements Serializable {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION_UID;

    /**
     * Primary key ID.
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    @Schema(description = "Primary key ID")
    private Long userId;

    /**
     * Username.
     */
    @Schema(description = "Username")
    private String username;

    /**
     * Password.
     */
    @Schema(description = "Password")
    private String password;

    /**
     * Random salt.
     */
    @JsonIgnore
    @Schema(description = "Random salt")
    private String salt;

    /**
     * Founder.
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Founder")
    private String createBy;

    /**
     * Modifier.
     */
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "Modifier")
    private String updateBy;

    /**
     * Creation time.
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Creation time")
    private LocalDateTime createTime;

    /**
     * Modification time.
     */
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "Modification time")
    private LocalDateTime updateTime;

    /**
     * 0 for normal, 1 for deleted.
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "0 for normal, 1 for deleted")
    private String delFlag;

    /**
     * Lock mark.
     */
    @Schema(description = "Lock mark")
    private String lockFlag;

    /**
     * Phone number.
     */
    @Schema(description = "Phone number")
    private String phone;

    /**
     * Avatar.
     */
    @Schema(description = "Avatar")
    private String avatar;

    /**
     * Department ID.
     */
    @Schema(description = "Department ID")
    private Long deptId;

    /**
     * Wechat openid.
     */
    @Schema(description = "Wechat openid")
    private String wxOpenid;

    /**
     * QQ openid
     */
    @Schema(description = "QQ openid")
    private String qqOpenid;

    /**
     * Nickname.
     */
    @Schema(description = "Nickname")
    private String nickname;

    /**
     * Name.
     */
    @Schema(description = "Name")
    private String name;

    /**
     * Email.
     */
    @Schema(description = "Email")
    private String email;

}
    