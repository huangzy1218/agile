package com.agile.common.mybatis.resolver;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Solve Mybatis Plus Order By SQL injection problem.
 *
 * @author Huang Z.Y.
 */
public class SqlFilterArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * Determine whether the Controller contains page parameters.
     *
     * @param parameter Parameter
     * @return Whether to filter
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Page.class);
    }

    /**
     * NOTE: Page only supports query GET. If you need to parse POST to obtain the request message body processing.
     *
     * @param parameter     Input parameter set
     * @param mavContainer  Model and view
     * @param webRequest    Web request
     * @param binderFactory Input parameter analysis
     * @return Check the new page object
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String[] ascs = request.getParameterValues("ascs");
        String[] descs = request.getParameterValues("descs");
        String current = request.getParameter("current");
        String size = request.getParameter("size");

        Page<?> page = new Page<>();
        if (StrUtil.isNotBlank(current)) {
            page.setCurrent(Long.parseLong(current));
        }

        if (StrUtil.isNotBlank(size)) {
            page.setSize(Long.parseLong(size));
        }

        List<OrderItem> orderItemList = new ArrayList<>();
        Optional.ofNullable(ascs)
                .ifPresent(s -> orderItemList.addAll(Arrays.stream(s)
                        .filter(asc -> !SqlInjectionUtils.check(asc))
                        .map(OrderItem::asc)
                        .toList()));
        Optional.ofNullable(descs)
                .ifPresent(s -> orderItemList.addAll(Arrays.stream(s)
                        .filter(desc -> !SqlInjectionUtils.check(desc))
                        .map(OrderItem::desc)
                        .toList()));
        page.addOrder(orderItemList);

        return page;
    }

}
