package com.agile.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.agile.admin.api.entity.SysUser;
import com.agile.admin.mapper.SysUserMapper;
import com.agile.admin.service.SysMobileService;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.core.exception.ErrorCodes;
import com.agile.common.core.util.MsgUtils;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Mobile phone login related business implementation.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysMobileServiceImpl implements SysMobileService {

    private final RedisTemplate redisTemplate;

    private final SysUserMapper userMapper;

    /**
     * Send mobile phone verification code.
     *
     * @param mobile mobile
     * @return code
     */
    @Override
    public R<Boolean> sendSmsCode(String mobile) {
        List<SysUser> userList = userMapper
                .selectList(Wrappers.<SysUser>query().lambda().eq(SysUser::getPhone, mobile));

        if (CollUtil.isEmpty(userList)) {
            log.info("Mobile phone number not registered: {}", mobile);
            return R.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodes.SYS_APP_PHONE_UNREGISTERED, mobile));
        }

        Object codeObj = redisTemplate.opsForValue().get(CacheConstants.DEFAULT_CODE_KEY + mobile);

        if (codeObj != null) {
            log.info("The mobile phone number verification code has not expired: {}，{}", mobile, codeObj);
            return R.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodes.SYS_APP_SMS_OFTEN));
        }

        String code = RandomUtil.randomNumbers(Integer.parseInt(SecurityConstants.CODE_SIZE));
        log.debug("Mobile phone number generated verification code successfully: {},{}", mobile, code);
        redisTemplate.opsForValue()
                .set(CacheConstants.DEFAULT_CODE_KEY + mobile, code, SecurityConstants.CODE_TIME, TimeUnit.SECONDS);
        return R.ok(Boolean.TRUE, code);
    }

}

