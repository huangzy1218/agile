package com.agile.common.security.util;

/**
 * OAuth2 Exception information.
 *
 * @author Huang Z.Y.
 */
public interface OAuth2ErrorCodesExpand {

    /**
     * User name not found.
     */
    String USERNAME_NOT_FOUND = "username_not_found";

    /**
     * Wrong certificate.
     */
    String BAD_CREDENTIALS = "bad_credentials";

    /**
     * User locked.
     */
    String USER_LOCKED = "user_locked";

    /**
     * User disable.
     */
    String USER_DISABLE = "user_disable";

    /**
     * User expired.
     */
    String USER_EXPIRED = "user_expired";

    /**
     * Certificate expired.
     */
    String CREDENTIALS_EXPIRED = "credentials_expired";

    /**
     * Scope is empty.
     */
    String SCOPE_IS_EMPTY = "scope_is_empty";

    /**
     * The token does not exist.
     */
    String TOKEN_MISSING = "token_missing";

    /**
     * Unknown login exception.
     */
    String UN_KNOW_LOGIN_ERROR = "un_know_login_error";

    /**
     * Invalid bearer token.
     */
    String INVALID_BEARER_TOKEN = "invalid_bearer_token";

}
