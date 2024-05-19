package com.agile.admin.api.feign;

import com.agile.admin.api.entity.SysLog;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.core.constant.ServiceNameConstants;
import com.agile.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Log remote service interface.
 *
 * @author Huang Z.Y.
 */
@FeignClient(contextId = "remoteLogService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteLogService {

    /**
     * Save log.
     *
     * @param sysLog log entity
     * @param from   Internal call or not
     * @return true or false
     */
    @PostMapping("/log/save")
    R<Boolean> saveLog(@RequestBody SysLog sysLog, @RequestHeader(SecurityConstants.FROM) String from);

}
