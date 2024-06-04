package com.agile.common.core.util;

import cn.hutool.core.codec.Base64;
import com.agile.common.core.exception.CheckedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

/**
 * Miscellaneous utilities for web applications.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@UtilityClass
public class WebUtils extends org.springframework.web.util.WebUtils {

    private final String BASIC_ = "Basic ";

    private final String UNKNOWN = "unknown";

    @SneakyThrows
    public String getClientId() {
        if (WebUtils.getRequest().isPresent()) {
            String header = WebUtils.getRequest().get().getHeader(HttpHeaders.AUTHORIZATION);
            return splitClient(header)[0];
        }
        return null;
    }

    /**
     * Get HttpServletRequest.
     *
     * @return {HttpServletRequest}
     */
    public Optional<HttpServletRequest> getRequest() {
        return Optional.of(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest());
    }

    /**
     * Splits the client information from the HTTP Basic Authentication header.
     *
     * @param header The HTTP Basic Authentication header to be processed
     * @return A string array containing the username and password (username:password) extracted from the header
     */
    @NotNull
    private static String[] splitClient(String header) {
        // Check if the header is null or does not start with the expected prefix "Basic "
        if (header == null || !header.startsWith(BASIC_)) {
            throw new CheckedException("The client information in the request header is empty");
        }
        // Extract the base64-encoded part of the header and convert it to bytes
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new CheckedException("Failed to decode basic authentication token");
        }

        // Convert the decoded bytes back to a string
        String token = new String(decoded, StandardCharsets.UTF_8);

        // Find the position of the colon (":") in the decoded string
        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new CheckedException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

    /**
     * Get {@link HttpServletResponse}.
     *
     * @return HttpServletResponse
     */
    public HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

}
    