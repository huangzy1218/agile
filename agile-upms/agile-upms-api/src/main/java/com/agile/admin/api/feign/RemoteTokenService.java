package com.agile.admin.api.feign;

import com.agile.common.core.constant.ServiceNameConstants;
import com.agile.common.core.util.R;
import com.agile.common.feign.annotation.NoToken;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Huang Z.Y.
 */
@FeignClient(contextId = "remoteTokenService", value = ServiceNameConstants.AUTH_SERVICE)
public interface RemoteTokenService {

    /**
     * Query token information by page.
     *
     * @param params Paging parameters
     * @return page
     */
    @NoToken
    @PostMapping("/token/page")
    R<Page> getTokenPage(@RequestBody Map<String, Object> params);

    /**
     * Delete token.
     *
     * @param token Token
     * @return
     */
    @NoToken
    @DeleteMapping("/token/remove/{token}")
    R<Boolean> removeTokenById(@PathVariable("token") String token);

    /**
     * Verify token to obtain user information.
     *
     * @param token Token
     */
    @NoToken
    @GetMapping("/token/query-token")
    R<Map<String, Object>> queryToken(@RequestParam("token") String token);

}

