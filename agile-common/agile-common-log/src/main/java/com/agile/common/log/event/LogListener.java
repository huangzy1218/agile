package com.agile.common.log.event;

import com.agile.common.log.config.AgileLogProperties;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * 异步监听日志事件。
 *
 * @author Huang Z.Y.
 */
@Slf4j
@RequiredArgsConstructor
public class LogListener implements InitializingBean {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final RemoteLogService remoteLogService;

    private final AgileLogProperties logProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        objectMapper.addMixIn(Object.class, PropertyFilterMixIn.class);
        String[] ignorableFieldNames = logProperties.getExcludeFields().toArray(new String[0]);

        FilterProvider filters = new SimpleFilterProvider().addFilter("filter properties by name",
                SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
        objectMapper.setFilterProvider(filters);
    }

    @JsonFilter("filter properties by name")
    class PropertyFilterMixIn() {

    }
}
    