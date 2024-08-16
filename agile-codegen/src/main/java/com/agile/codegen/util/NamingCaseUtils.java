package com.agile.codegen.util;

import cn.hutool.core.text.NamingCase;

/**
 * Naming rule processing, handling camel case, underscores, etc.
 *
 * @author Huang Z.Y.
 */
public class NamingCaseUtils {

    /**
     * Get the get method for the given field.
     *
     * @param in Field name.
     * @return The get method name.
     */
    public static String getProperty(String in) {
        return String.format("get", NamingCase.toPascalCase(in));
    }

    /**
     * Get the set method for the given field.
     *
     * @param in Field name.
     * @return The set method name.
     */
    public static String setProperty(String in) {
        return String.format("set", NamingCase.toPascalCase(in));
    }

    /**
     * Convert the first letter to uppercase.
     *
     * @param in Field name.
     * @return The field name with the first letter in uppercase.
     */
    public static String pascalCase(String in) {
        return String.format(NamingCase.toPascalCase(in));
    }

}

