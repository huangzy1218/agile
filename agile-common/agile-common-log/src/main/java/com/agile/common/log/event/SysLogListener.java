package com.agile.common.log.event;

import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.entity.SysLog;
import com.agile.admin.api.feign.RemoteLogService;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.log.config.AgileLogProperties;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;

/**
 * Asynchronous listening log event.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@RequiredArgsConstructor
public class SysLogListener implements InitializingBean {

    /**
     * Create a log desensitization policy prevents the global ObjectMapper from being affected.
     */
    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final RemoteLogService remoteLogService;

    private final AgileLogProperties logProperties;

    @SneakyThrows
    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        SysLogEventSource source = (SysLogEventSource) event.getSource();
        SysLog sysLog = new SysLog();
        BeanUtils.copyProperties(source, sysLog);

        // JSON format parameters are processed asynchronously to improve performance
        if (Objects.nonNull(source.getBody())) {
            String params = objectMapper.writeValueAsString(source.getBody());
            sysLog.setParams(StrUtil.subPre(params, logProperties.getMaxLength()));
        }

        remoteLogService.saveLog(sysLog, SecurityConstants.FROM_IN);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        objectMapper.addMixIn(Object.class, PropertyFilterMixIn.class);
        // The name of the field to exclude
        String[] ignorableFieldNames = logProperties.getExcludeFields().toArray(new String[0]);

        FilterProvider filters = new SimpleFilterProvider().addFilter("filter properties by name",
                SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
        objectMapper.setFilterProvider(filters);
    }

    /**
     * Used as Jackson's MixIn annotation mechanism. <br/>
     * The MixIn mechanism allows you to add annotations to existing classes without modifying the class itself.
     */
    @JsonFilter("filter properties by name")
    class PropertyFilterMixIn {

    }

}
    