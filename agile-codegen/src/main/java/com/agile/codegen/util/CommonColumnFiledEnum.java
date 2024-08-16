package com.agile.codegen.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Common column fill strategy and display strategy.<br/>
 * This enum defines the fill and display strategies for common columns.
 *
 * @author Huang Z.Y.
 */
@Getter
@AllArgsConstructor
public enum CommonColumnFiledEnum {

    /**
     * create_by field.
     */
    create_by("0", "0", "INSERT", 100),

    /**
     * create_time field.
     */
    create_time("0", "0", "INSERT", 101),

    /**
     * update_by field.
     */
    update_by("0", "0", "INSERT_UPDATE", 102),

    /**
     * update_time field.
     */
    update_time("0", "0", "INSERT_UPDATE", 103),

    /**
     * del_flag field.
     */
    del_flag("0", "0", "DEFAULT", 104),

    /**
     * tenant_id field.
     */
    tenant_id("0", "0", "DEFAULT", 105);

    /**
     * Whether the form item is displayed by default.
     */
    private String formItem;

    /**
     * Whether the grid item is displayed by default.
     */
    private String gridItem;

    /**
     * Auto fill strategy.
     */
    private String autoFill;

    /**
     * Sort order.
     */
    private Integer sort;

}
