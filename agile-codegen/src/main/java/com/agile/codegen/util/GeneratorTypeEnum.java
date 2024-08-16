package com.agile.codegen.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Generator type enumeration.
 *
 * @author Huang Z.Y.
 */
@Getter
@RequiredArgsConstructor
public enum GeneratorTypeEnum {

    /**
     * Zip compressed package 0.
     */
    ZIP_DOWNLOAD("0"),

    /**
     * Custom directory 1.
     */
    CUSTOM_DIRECTORY("1");

    private final String value;

}
