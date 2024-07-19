package com.agile.admin.service;

import com.agile.admin.api.entity.SysPublicParam;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Public parameter configuration service.
 *
 * @author Huang Z.Y.
 */
public interface SysPublicParamService extends IService<SysPublicParam> {

    /**
     * Query the specified value of public parameters by key.
     *
     * @param publicKey Public key
     */
    String getSysPublicParamKeyToValue(String publicKey);

    /**
     * Update public parameter.
     *
     * @param sysPublicParam Public parameter
     */
    R updateParam(SysPublicParam sysPublicParam);

    /**
     * Delete public parameter.
     *
     * @param publicIds Parameter list
     */
    R removeParamByIds(Long[] publicIds);

    /**
     * Sync cache.
     *
     * @return R
     */
    R syncParamCache();

}

