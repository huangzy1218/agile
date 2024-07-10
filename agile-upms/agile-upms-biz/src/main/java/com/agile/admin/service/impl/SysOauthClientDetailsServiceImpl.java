/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.agile.admin.service.impl;

import com.agile.admin.api.entity.SysOauthClientDetails;
import com.agile.admin.mapper.SysOauthClientDetailsMapper;
import com.agile.admin.service.SysOauthClientDetailsService;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation.
 *
 * @author Huang Z.Y.
 */
@Service
@RequiredArgsConstructor
public class SysOauthClientDetailsServiceImpl extends ServiceImpl<SysOauthClientDetailsMapper, SysOauthClientDetails>
        implements SysOauthClientDetailsService {

    @Override
    @CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientDetails.clientId")
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateClientById(SysOauthClientDetails clientDetails) {
        this.insertOrUpdate(clientDetails);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveClient(SysOauthClientDetails clientDetails) {
        this.insertOrUpdate(clientDetails);
        return Boolean.TRUE;
    }

    private SysOauthClientDetails insertOrUpdate(SysOauthClientDetails clientDetails) {
        // Update database
        saveOrUpdate(clientDetails);
        return clientDetails;
    }

    @Override
    public Page queryPage(Page page, SysOauthClientDetails query) {
        return baseMapper.selectPage(page, Wrappers.query(query));
    }

    @Override
    @CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, allEntries = true)
    public R syncClientCache() {
        return R.ok();
    }

}
