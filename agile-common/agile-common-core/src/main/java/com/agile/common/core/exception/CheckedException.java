package com.agile.common.core.exception;

import com.agile.common.core.constant.CommonConstants;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * Exception that require explicit handling.
 *
 * @author Huang Z.Y.
 */
@NoArgsConstructor
public class CheckedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION_UID;

    public CheckedException(String message) {
        super(message);
    }

    public CheckedException(Throwable cause) {
        super(cause);
    }

    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
    