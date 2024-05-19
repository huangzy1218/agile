package com.agile.plugin.excel.kit;

import java.io.Serial;

/**
 * A exception handles exceptions related to Excel operations.
 *
 * @author Huang Z.Y.
 */
public class ExcelException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2691386627L;

    public ExcelException(String message) {
        super(message);
    }

}
    