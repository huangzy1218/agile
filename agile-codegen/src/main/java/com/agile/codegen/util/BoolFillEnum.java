package com.agile.codegen.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Boolean fill enumeration.
 *
 * @author Huang Z.Y.
 */

@Getter
@RequiredArgsConstructor
public enum BoolFillEnum {

    /**
     * true
     */
    TRUE("1"),
    /**
     * false
     */
    FALSE("0");

    private final String value;

}
