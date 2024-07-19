package com.agile.admin.controller;

import com.agile.admin.api.feign.RemoteTokenService;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Token management module.
 *
 * @author Huang Z.Y.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-token")
@Tag(description = "token", name = "Token management module")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysTokenController {

    private final RemoteTokenService remoteTokenService;

    /**
     * Pagination token information.
     *
     * @param params Parameter set
     * @return Token collection
     */
    @RequestMapping("/page")
    public R getTokenPage(@RequestBody Map<String, Object> params) {
        return remoteTokenService.getTokenPage(params);
    }

    /**
     * Delete user token.
     *
     * @param tokens Tokens
     * @return {@code true} for success
     */
    @SysLog("Delete user token")
    @DeleteMapping("/delete")
    @PreAuthorize("@pms.hasPermission('sys_token_del')")
    public R removeById(@RequestBody String[] tokens) {
        for (String token : tokens) {
            remoteTokenService.removeTokenById(token);
        }
        return R.ok();
    }

}

