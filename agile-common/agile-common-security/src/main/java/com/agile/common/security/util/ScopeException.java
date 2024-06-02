package com.agile.common.security.util;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * Scope exception.
 *
 * @author Huang Z.Y.
 */
public class ScopeException extends OAuth2AuthenticationException {

    /**
     * Constructs a {@link ScopeException} with the specified message.
     *
     * @param msg The detail message
     */
    public ScopeException(String msg) {
        super(new OAuth2Error(msg), msg);
    }

    /**
     * Constructs a {@code ScopeException} with the specified message and root cause.
     *
     * @param msg   The detail message
     * @param cause Root cause
     */
    public ScopeException(String msg, Throwable cause) {
        super(new OAuth2Error(msg), cause);
    }

}
    