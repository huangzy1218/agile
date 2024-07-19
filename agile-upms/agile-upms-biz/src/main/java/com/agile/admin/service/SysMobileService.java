package com.agile.admin.service;

import com.agile.common.core.util.R;

/**
 * @author Huang Z.Y.
 */
public interface SysMobileService {

    /**
     * Send mobile phone verification code.
     *
     * @param mobile mobile
     * @return code
     */
    R<Boolean> sendSmsCode(String mobile);

}

