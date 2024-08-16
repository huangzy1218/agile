package com.agile.datasource.config;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.core.Ordered;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * Clear the DS settings above to avoid polluting the current thread.
 *
 * @author Huang Z.Y.
 */
public class ClearTtlDataSourceFilter extends GenericFilterBean implements Ordered {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        DynamicDataSourceContextHolder.clear();
        filterChain.doFilter(servletRequest, servletResponse);
        DynamicDataSourceContextHolder.clear();
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

}
