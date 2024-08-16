package com.agile.codegen.service;

import com.agile.codegen.entity.GenDatasourceConf;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Datasource configuration service.
 *
 * @author Huang Z.Y.
 */
public interface GenDatasourceConfService extends IService<GenDatasourceConf> {

    /**
     * Save data source and encrypt.
     *
     * @param genDatasourceConf Data source configuration.
     * @return True if the operation was successful, false otherwise.
     */
    Boolean saveDsByEnc(GenDatasourceConf genDatasourceConf);

    /**
     * Update data source.
     *
     * @param genDatasourceConf Data source configuration.
     * @return True if the operation was successful, false otherwise.
     */
    Boolean updateDsByEnc(GenDatasourceConf genDatasourceConf);

    /**
     * Update the list of dynamic data sources.
     *
     * @param datasourceConf Data source configuration.
     */
    void addDynamicDataSource(GenDatasourceConf datasourceConf);

    /**
     * Validate if the data source configuration is valid.
     *
     * @param datasourceConf Data source information.
     * @return True if valid, false otherwise.
     */
    Boolean checkDataSource(GenDatasourceConf datasourceConf);

    /**
     * Delete by data source IDs.
     *
     * @param dsIds Data source IDs.
     * @return True if the operation was successful, false otherwise.
     */
    Boolean removeByDsId(Long[] dsIds);

}
