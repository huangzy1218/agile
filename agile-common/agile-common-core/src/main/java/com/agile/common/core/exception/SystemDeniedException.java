package com.agile.common.core.exception;

import com.agile.common.core.constant.CommonConstants;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * Exception that deny authorization.
 *
 * @author Huang Z.Y.
 */
@NoArgsConstructor
public class SystemDeniedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION_UID;

    public SystemDeniedException(String message) {
        super(message);
    }

    public SystemDeniedException(Throwable cause) {
        super(cause);
    }

    public SystemDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
    