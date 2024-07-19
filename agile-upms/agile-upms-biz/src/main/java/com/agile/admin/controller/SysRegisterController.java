package com.agile.admin.controller;

import com.agile.admin.api.dto.UserDTO;
import com.agile.admin.service.SysUserService;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import com.agile.common.security.annotation.Inner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Client registration function {@code register.user = false}
 *
 * @author Huang Z.Y.
 */
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "register.user", matchIfMissing = true)
public class SysRegisterController {

    private final SysUserService userService;

    /**
     * Register user.
     *
     * @param userDto User information
     * @return {@code true} for success
     */
    @Inner(value = false)
    @SysLog("Register user")
    @PostMapping("/user")
    public R<Boolean> registerUser(@RequestBody UserDTO userDto) {
        return userService.registerUser(userDto);
    }

}
