package com.agile.admin.api.vo;

import com.agile.admin.api.entity.SysPost;
import com.agile.admin.api.entity.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Front-end user displays object.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Front-end user displays object")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Primary key ID
     */
    @Schema(description = "Major key")
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
    @Schema(description = "Random salt")
    private String salt;

    /**
     * Wechat openid.
     */
    @Schema(description = " Wechat openid")
    private String wxOpenid;

    /**
     * QQ openid.
     */
    @Schema(description = "qq open id")
    private String qqOpenid;

    /**
     * Creation time.
     */
    @Schema(description = "Creation time")
    private LocalDateTime createTime;

    /**
     * Modification time.
     */
    @Schema(description = "Modification time")
    private LocalDateTime updateTime;

    /**
     * Delete flag.
     */
    @Schema(description = "Delete the flag,1: deleted,0: normal")
    private String delFlag;

    /**
     * Lock mark
     */
    @Schema(description = "Lock mark: 0: normal,9: locked")
    private String lockFlag;

    /**
     * Mobile phone number.
     */
    @Schema(description = "Mobile phone number")
    private String phone;

    /**
     * Avatar.
     */
    @Schema(description = "Avatar")
    private String avatar;

    /**
     * Department ID.
     */
    @Schema(description = "Subordinate department")
    private Long deptId;

    /**
     * Department name.
     */
    @Schema(description = "Department name")
    private String deptName;

    /**
     * Role list.
     */
    @Schema(description = "A list of roles you have")
    private List<SysRole> roleList;

    /**
     * Job list.
     */
    @Schema(description = "Job list")
    private List<SysPost> postList;

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
     * Mailbox.
     */
    @Schema(description = "Mailbox")
    private String email;

}

    