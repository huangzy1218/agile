package com.agile.gateway.handler;

import com.agile.common.core.util.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Gateway exception General purpose processor, applicable only in webflux environments.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Order(-1)
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // Set the response content type to JSON
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatusCode());
        }

        // Write the error response
        // In a non-blocking manner, so that the entire thread is not blocked
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                log.debug("Error Spring Cloud Gateway : {} {}", exchange.getRequest().getPath(), ex.getMessage());
                // Convert the error message to JSON and wrap it in a DataBuffer
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(R.failed(ex.getMessage())));
            } catch (JsonProcessingException e) {
                log.error("Error writing response", ex);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }

}
    