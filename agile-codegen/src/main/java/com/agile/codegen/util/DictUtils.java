package com.agile.codegen.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.util.HashSet;
import java.util.List;

/**
 * Utility class for dictionary operations.<br/>
 *
 * @author Huang Z.Y.
 */
public class DictUtils {

    /**
     * Convert a list of fields into a comma-separated string with double quotes.
     *
     * @param fields The list of fields.
     * @return A comma-separated string with double quotes.
     */
    public static String quotation(List<String> fields) {
        return CollUtil.join(new HashSet<>(fields), StrUtil.COMMA, s -> String.format("s'", s));
    }

    /**
     * Convert a list of fields into a comma-separated string.
     *
     * @param fields The list of fields.
     * @return A comma-separated string.
     */
    public static String format(List<String> fields) {
        return CollUtil.join(new HashSet<>(fields), StrUtil.COMMA);
    }

}
