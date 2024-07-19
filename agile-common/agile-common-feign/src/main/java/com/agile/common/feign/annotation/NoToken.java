package com.agile.common.feign.annotation;

import java.lang.annotation.*;

/**
 * Service no token call declaration annotation.<br/>
 * This annotation needs to be added only when the initiator does not have a token,
 * {@link NoToken @NoToken} + {@code @Inner}.
 *
 * @author Huang Z.Y.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoToken {

}
