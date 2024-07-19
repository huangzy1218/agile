package com.agile.admin.api.feign;

import com.agile.common.core.constant.ServiceNameConstants;
import com.agile.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Query parameter related interfaces.
 *
 * @author Huang Z.Y.
 */
@FeignClient(contextId = "remoteParamService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteParamService {

    /**
     * Query parameter configuration by key.
     *
     * @param key Key
     */
    @GetMapping("/param/publicValue/{key}")
    R<String> getByKey(@PathVariable("key") String key);

}

