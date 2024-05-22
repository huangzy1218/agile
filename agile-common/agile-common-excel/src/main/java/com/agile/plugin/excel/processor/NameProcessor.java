package com.agile.plugin.excel.processor;

import java.lang.reflect.Method;

/**
 * Resolve header name.
 *
 * @author Huang Z.Y.
 */
public interface NameProcessor {

    /**
     * Resolve name。
     *
     * @param args   Interceptor object
     * @param method Method
     * @param key    Expression
     * @return Parse result
     */
    String doDetermineName(Object[] args, Method method, String key);

}
    