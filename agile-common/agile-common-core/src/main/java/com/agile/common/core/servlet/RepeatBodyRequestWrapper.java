package com.agile.common.core.servlet;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Request wrapper class, allows the body to read repeatedly.
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class RepeatBodyRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] bodyByteArray;

    private final Map<String, String[]> parameterMap;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request the {@link HttpServletRequest} to be wrapped.
     * @throws IllegalArgumentException if the request is null
     */
    public RepeatBodyRequestWrapper(HttpServletRequest request) {
        super(request);
        this.bodyByteArray = getByteBody(request);
        this.parameterMap = super.getParameterMap();
    }

    @Override
    public BufferedReader getReader() {
        return ObjectUtils.isEmpty(this.bodyByteArray) ? null
                : new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.bodyByteArray);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // NOP
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    private static byte[] getByteBody(HttpServletRequest request) {
        byte[] body = new byte[0];
        try {
            body = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            log.error("Data in the parsing flow is abnormal", e);
        }
        return body;
    }

    /**
     * Override the getParameterMap() method to address an issue where streams in undertow are tagged after being read,
     * causing form data in the body to be incorrectly fetched.
     *
     * @return parameter map
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return this.parameterMap;
    }
}
    