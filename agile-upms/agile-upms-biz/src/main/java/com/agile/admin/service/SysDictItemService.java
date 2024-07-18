package com.agile.admin.service;

import com.agile.admin.api.entity.SysDictItem;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Huang Z.Y.
 */
public interface SysDictItemService extends IService<SysDictItem> {

    /**
     * Delete dictionary item.
     *
     * @param id Dictionary item ID
     * @return
     */
    R removeDictItem(Long id);

    /**
     * Update dictionary item.
     *
     * @param item Dictionary item
     * @return
     */
    R updateDictItem(SysDictItem item);

}
