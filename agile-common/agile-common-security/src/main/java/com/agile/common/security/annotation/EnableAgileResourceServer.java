package com.agile.common.security.annotation;

import com.agile.common.security.component.AgileResourceServerAutoConfiguration;
import com.agile.common.security.component.AgileResourceServerConfiguration;
import com.agile.common.security.feign.AgileFeignClientConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Huang Z.Y.
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({AgileResourceServerAutoConfiguration.class, AgileResourceServerConfiguration.class,
        AgileFeignClientConfiguration.class})
public @interface EnableAgileResourceServer {
    
}
