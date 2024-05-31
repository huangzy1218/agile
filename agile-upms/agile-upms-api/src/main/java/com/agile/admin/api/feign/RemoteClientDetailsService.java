package com.agile.admin.api.feign;

import com.agile.admin.api.entity.SysOauthClientDetails;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.core.constant.ServiceNameConstants;
import com.agile.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
@FeignClient(contextId = "remoteClientDetailsService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteClientDetailsService {

    /**
     * Querying client information using clientId.
     *
     * @param clientId Username
     * @param from     Call flag
     * @return R Response result
     */
    @GetMapping("/client/getClientDetailsById/{clientId}")
    R<SysOauthClientDetails> getClientDetailsById(@PathVariable("clientId") String clientId,
                                                  @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * Query all clients.
     *
     * @param from Call flag
     * @return R Response result
     */
    @GetMapping("/client/list")
    R<List<SysOauthClientDetails>> listClientDetails(@RequestHeader(SecurityConstants.FROM) String from);


}
    