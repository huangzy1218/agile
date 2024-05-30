package com.agile.common.core.constant;

/**
 * Security constants.
 *
 * @author Huang Z.Y.
 */
public interface SecurityConstants {

    /**
     * Role prefix.
     */
    String ROLE = "ROLE_";

    /**
     * Prefix.
     */
    String PROJECT_PREFIX = "agile";

    /**
     * Inner.
     */
    String FROM_IN = "Y";

    /**
     * Flag.
     */
    String FROM = "from";

    /**
     * Default login URL.
     */
    String OAUTH_TOKEN_URL = "/oauth2/token";

    /**
     * Grant_type.
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * Password mode.
     */
    String PASSWORD = "password";

    /**
     * Mobile phone login.
     */
    String MOBILE = "mobile";

    /**
     * {bcrypt} encrypted prefix.
     */
    String BCRYPT = "{bcrypt}";

    /**
     * {noop} encrypted prefix.
     */
    String NOOP = "{noop}";

    /**
     * Username.
     */
    String USERNAME = "username";

    /**
     * User information.
     */
    String DETAILS_USER = "user_info";

    /**
     * User ID.
     */
    String DETAILS_USER_ID = "user_id";

    /**
     * License field.
     */
    String DETAILS_LICENSE = "license";

    /**
     * Verification code validity period, default 60 seconds.
     */
    long CODE_TIME = 60;

    /**
     * Verification code length.
     */
    String CODE_SIZE = "6";

    /**
     * Client credentials mode.
     */
    String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * Client ID.
     */
    String CLIENT_ID = "clientId";

    /**
     * SMS login parameter name.
     */
    String SMS_PARAMETER_NAME = "mobile";

    /**
     * Authorization code mode confirm.
     */
    String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";

}
    