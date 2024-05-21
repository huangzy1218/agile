package com.agile.plugin.excel.head;

/**
 * Excel header generator, used to generate custom header information.
 *
 * @author Huang Z.Y.
 */
public interface HeadGenerator {

    /**
     * Customize the header information.
     *
     * @param clazz Data type of the current sheet
     * @return Head information.
     */
    HeadMeta head(Class<?> clazz);
    
}
    