package com.agile.common.feign.sentinel.handle;

import com.agile.common.core.util.R;
import com.alibaba.csp.sentinel.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

/**
 * Global exception handler combined with sentinel global exception handler.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Order(10000)
@RestControllerAdvice
public class GlobalBizExceptionHandler {

    /**
     * Global exception.
     *
     * @param e the e
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handleGlobalException(Exception e) {
        log.error("Global exception information ex={}", e.getMessage(), e);

        // Business exceptions are recorded by sentinel
        Tracer.trace(e);
        return R.failed(e.getLocalizedMessage());
    }

    /**
     * Handle illegal parameter exceptions encountered during business verification.
     * This exception is basically caused by {@link org.springframework.util.Assert}.
     *
     * @param exception Parameter verification exception
     * @return The API returns the error output result wrapped by the result object
     * @see Assert#hasLength(String, String)
     * @see Assert#hasText(String, String)
     * @see Assert#isTrue(boolean, String)
     * @see Assert#isNull(Object, String)
     * @see Assert#notNull(Object, String)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    public R handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error("Illegal parameters, ex = {}", exception.getMessage(), exception);
        return R.failed(exception.getMessage());
    }

    /**
     * AccessDeniedException.
     *
     * @param e The exception
     * @return R
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R handleAccessDeniedException(AccessDeniedException e) {
        String msg = SpringSecurityMessageSource.getAccessor()
                .getMessage("AbstractAccessDecisionManager.accessDenied", e.getMessage());
        log.warn("Authorization denied exception message, ex={}", msg);
        return R.failed(e.getLocalizedMessage());
    }

    /**
     * Validation Exception
     *
     * @param exception The exception
     * @return R
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handleBodyValidException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        log.warn("Parameter valid exception, ex = {}", fieldErrors.get(0).getDefaultMessage());
        return R.failed(String.format("%s %s", fieldErrors.get(0).getField(), fieldErrors.get(0).getDefaultMessage()));
    }

    /**
     * Validation Exception (Pass parameters in the form of form-data)
     *
     * @param exception The exception
     * @return R
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R bindExceptionHandler(BindException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        log.warn("Parameter binding exception, ex = {}", fieldErrors.get(0).getDefaultMessage());
        return R.failed(fieldErrors.get(0).getDefaultMessage());
    }

    /**
     * Keep the behavior consistent with the request path that does not exist in lower versions.
     *
     * @param exception The exception
     * @return R
     */
    @ExceptionHandler({NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R bindExceptionHandler(NoResourceFoundException exception) {
        log.debug("Request path 404, ex = {}", exception.getMessage());
        return R.failed(exception.getMessage());
    }

}
