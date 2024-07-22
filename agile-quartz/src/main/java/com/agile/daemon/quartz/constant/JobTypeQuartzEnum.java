package com.agile.daemon.quartz.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Huang Z.Y.
 */
@Getter
@AllArgsConstructor
public enum JobTypeQuartzEnum {

    /**
     * Reflection java class.
     */
    JAVA("1", "Reflection java class"),

    /**
     * Spring bean way.
     */
    SPRING_BEAN("2", "Spring bean container instance"),

    /**
     * Rest call.
     */
    REST("3", "Rest call"),

    /**
     * Jar call.
     */
    JAR("4", "jar调用");

    /**
     * Type.
     */
    private final String type;

    /**
     * Description.
     */
    private final String description;

}
