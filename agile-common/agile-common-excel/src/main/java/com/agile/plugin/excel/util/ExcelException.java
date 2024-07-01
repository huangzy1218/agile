package com.agile.plugin.excel.util;

import com.agile.common.core.constant.CommonConstants;

import java.io.Serial;

/**
 * A exception handles exceptions related to Excel operations.
 *
 * @author Huang Z.Y.
 */
public class ExcelException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION_UID;

    public ExcelException(String message) {
        super(message);
    }

}
    