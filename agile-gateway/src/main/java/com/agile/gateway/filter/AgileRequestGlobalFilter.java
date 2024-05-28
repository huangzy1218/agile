package com.agile.gateway.filter;

import com.agile.common.core.constant.CommonConstants;
import com.agile.common.core.constant.SecurityConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * Global interceptor that acts on all microservices.
 *
 * @author Huang Z.Y.
 */
public class AgileRequestGlobalFilter implements GlobalFilter, Ordered {

    /**
     * Process the Web request and (optionally) delegate to the next {@code WebFilter}
     * through the given {@link GatewayFilterChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1. Clean the "from" parameter in the request header
        ServerHttpRequest request = exchange.getRequest().mutate().headers(httpHeaders -> {
            httpHeaders.remove(SecurityConstants.FROM);
            // Add a header with the current request start time for tracking purposes
            httpHeaders.put(CommonConstants.REQUEST_START_TIME,
                    Collections.singletonList(String.valueOf(System.currentTimeMillis())));
        }).build();

        // 2. Override StripPrefix
        //  Add the original request URL to the exchange attributes
        addOriginalRequestUrl(exchange, request.getURI());
        // Get the raw path of the request URI
        String rawPath = request.getURI().getRawPath();
        // Modify the path by removing the first segment
        String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/"))
                // Skip the first segment
                .skip(1L)
                // Rejoin the remaining segments
                .collect(Collectors.joining("/"));

        // Build a new request with the modified path
        ServerHttpRequest newRequest = request.mutate().path(newPath).build();
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

        // Pass the new request along the filter chain
        return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());
    }

    @Override
    public int getOrder() {
        return 10;
    }

}
    