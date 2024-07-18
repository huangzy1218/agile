package com.agile.admin.service;

import com.agile.admin.api.entity.SysDict;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Huang Z.Y.
 */
public interface SysDictService extends IService<SysDict> {

    /**
     * Delete dictionary based on ID.
     *
     * @param ids ID list
     * @return
     */
    R removeDictByIds(Long[] ids);

    /**
     * Update dictionary.
     *
     * @param sysDict Dictionary
     * @return
     */
    R updateDict(SysDict sysDict);

    /**
     * Sync cache (clear cache)
     *
     * @return R
     */
    R syncDictCache();

}
