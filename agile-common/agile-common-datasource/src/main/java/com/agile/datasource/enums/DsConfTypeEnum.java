package com.agile.datasource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data source configuration type.
 *
 * @author Huang Z.Y.
 */
@Getter
@AllArgsConstructor
public enum DsConfTypeEnum {

    /**
     * Host.
     */
    HOST(0, "Host link"),

    /**
     * JDBC link.
     */
    JDBC(1, "JDBC link");

    private final Integer type;

    private final String description;

}