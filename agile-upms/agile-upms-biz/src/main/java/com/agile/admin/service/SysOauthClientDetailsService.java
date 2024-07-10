package com.agile.admin.service;

import com.agile.admin.api.entity.SysOauthClientDetails;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Huang Z.Y.
 */
public interface SysOauthClientDetailsService extends IService<SysOauthClientDetails> {

    Boolean updateClientById(SysOauthClientDetails clientDetails);

    Boolean saveClient(SysOauthClientDetails clientDetails);

    Page queryPage(Page page, SysOauthClientDetails query);

    R syncClientCache();

}
