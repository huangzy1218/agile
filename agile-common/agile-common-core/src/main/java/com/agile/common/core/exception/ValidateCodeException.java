package com.agile.common.core.exception;

import java.io.Serial;

/**
 * Verification code exception.
 *
 * @author Huang Z.Y.
 */
public class ValidateCodeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2691386627L;

    public ValidateCodeException() {
    }

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
    