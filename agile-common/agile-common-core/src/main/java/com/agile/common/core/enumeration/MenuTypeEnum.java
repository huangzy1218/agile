package com.agile.common.core.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Menu type.
 *
 * @author Huang Z.Y.
 */
@Getter
@RequiredArgsConstructor
public enum MenuTypeEnum {

    /**
     * Left menu.
     */
    LEFT_MENU("0", "left"),

    /**
     * Top menu.
     */
    TOP_MENU("2", "top"),

    /**
     * Button.
     */
    BUTTON("1", "button");

    /**
     * Type.
     */
    private final String type;

    /**
     * Description.
     */
    private final String description;

}
