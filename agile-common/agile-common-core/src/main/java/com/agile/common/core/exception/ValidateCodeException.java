package com.agile.common.core.exception;

import com.agile.common.core.constant.CommonConstants;

import java.io.Serial;

/**
 * Verification code exception.
 *
 * @author Huang Z.Y.
 */
public class ValidateCodeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION_UID;

    public ValidateCodeException() {
    }

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
    