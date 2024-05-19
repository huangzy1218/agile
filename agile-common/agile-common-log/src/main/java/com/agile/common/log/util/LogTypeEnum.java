package com.agile.common.log.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Huang Z.Y.
 */
@Getter
@RequiredArgsConstructor
public enum LogTypeEnum {

    /**
     * Normal log.
     */
    NORMAL("0", "normal log"),

    /**
     * Error log.
     */
    ERROR("9", "error log");

    /**
     * type.
     */
    private final String type;

    /**
     * description.
     */
    private final String description;
}
    