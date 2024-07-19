package com.agile.admin.controller;

import com.agile.admin.service.SysMobileService;
import com.agile.common.core.util.R;
import com.agile.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mobile management module.
 *
 * @author Huang Z.Y.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/mobile")
@Tag(description = "mobile", name = "Mobile management module")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysMobileController {

    private final SysMobileService mobileService;

    @Inner(value = false)
    @GetMapping("/{mobile}")
    public R sendSmsCode(@PathVariable String mobile) {
        return mobileService.sendSmsCode(mobile);
    }

}

