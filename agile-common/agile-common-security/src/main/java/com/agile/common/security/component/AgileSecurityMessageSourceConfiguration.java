package com.agile.common.security.component;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * Injection of custom error handling,
 * covering org/springframework/security/messages built-in exceptions.
 *
 * @author Huang Z.Y.
 */
public class AgileSecurityMessageSourceConfiguration {

    @Bean
    @Primary
    public MessageSource securityMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:i18n/errors/messages");
        messageSource.setDefaultLocale(Locale.CHINA);
        return messageSource;
    }

}
    