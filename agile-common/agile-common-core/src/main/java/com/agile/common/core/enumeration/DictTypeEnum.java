package com.agile.common.core.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Huang Z.Y.
 */
@Getter
@RequiredArgsConstructor
public enum DictTypeEnum {

    /**
     * Dictionary type - system built-in (cannot be modified).
     */
    SYSTEM("1", "System built-in"),

    /**
     * Dictionary type - business type.
     */
    BIZ("0", "Business type");

    /**
     * Type.
     */
    private final String type;

    /**
     * Description.
     */
    private final String description;

}

