package com.agile.common.core.config;

import cn.hutool.core.date.DatePattern;
import com.agile.common.core.jackson.CustomTimeModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Jackson ObjectMapper User-defined configuration class.
 *
 * @author Huang Z.Y.
 */
public class JacksonConfiguration {

    /**
     * Custom Jackson2ObjectMapperBuilder.
     * This includes setting the region to China, time zone to the system default,
     * date format to NORM_DATETIME_PATTERN (yyyy-MM-dd HH:mm:ss), serializing the Long type
     * to a string using ToStringSerializer.
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.locale(Locale.CHINA);
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.modules(new CustomTimeModule());
        };
    }

}
    