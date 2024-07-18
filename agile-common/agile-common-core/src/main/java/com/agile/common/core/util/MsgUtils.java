package com.agile.common.core.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * i18n Utility.
 *
 * @author Huang Z.Y.
 */
@UtilityClass
public class MsgUtils {

    /**
     * Get Chinese error information through code.
     *
     * @param code Message code
     * @return
     */
    public String getMessage(String code) {
        MessageSource messageSource = SpringContextHolder.getBean("messageSource");
        return messageSource.getMessage(code, null, Locale.CHINA);
    }

    /**
     * Get Chinese error information through code and parameters.
     *
     * @param code Message code
     * @return
     */
    public String getMessage(String code, Object... objects) {
        MessageSource messageSource = SpringContextHolder.getBean("messageSource");
        return messageSource.getMessage(code, objects, Locale.CHINA);
    }

    /**
     * Security obtains Chinese error information through code and parameters.
     *
     * @param code Message code
     * @return
     */
    public String getSecurityMessage(String code, Object... objects) {
        MessageSource messageSource = SpringContextHolder.getBean("securityMessageSource");
        return messageSource.getMessage(code, objects, Locale.CHINA);
    }

}
