package com.agile.admin.api.vo;

import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class TokenVO {

    private String id;

    private Long userId;

    private String clientId;

    private String username;

    private String accessToken;

    private String issuedAt;

    private String expiresAt;

}
    