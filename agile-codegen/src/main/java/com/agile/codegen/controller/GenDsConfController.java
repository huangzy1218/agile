package com.agile.codegen.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.smallbun.screw.boot.config.Screw;
import cn.smallbun.screw.boot.properties.ScrewProperties;
import com.agile.codegen.entity.GenDatasourceConf;
import com.agile.codegen.service.GenDatasourceConfService;
import com.agile.common.core.util.R;
import com.agile.common.core.util.SpringContextHolder;
import com.agile.common.security.annotation.Inner;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;

/**
 * Data source management controller.
 *
 * @author Huang Z.Y.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dsconf")
public class GenDsConfController {

    private final GenDatasourceConfService datasourceConfService;

    private final Screw screw;

    /**
     * Paging query.
     *
     * @param page           Pagination object
     * @param datasourceConf Data source table
     */
    @GetMapping("/page")
    public R getSysDatasourceConfPage(Page page, GenDatasourceConf datasourceConf) {
        return R.ok(datasourceConfService.page(page,
                Wrappers.<GenDatasourceConf>lambdaQuery()
                        .like(StrUtil.isNotBlank(datasourceConf.getDsName()), GenDatasourceConf::getDsName,
                                datasourceConf.getDsName())));
    }

    /**
     * Query all data sources.
     *
     * @return
     */
    @GetMapping("/list")
    @Inner(value = false)
    public R list() {
        return R.ok(datasourceConfService.list());
    }

    /**
     * Query data source config by ID.
     *
     * @param id id
     * @return R
     */
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Long id) {
        return R.ok(datasourceConfService.getById(id));
    }

    /**
     * Add new data source.
     *
     * @param datasourceConf Data source configuration
     * @return R
     */
    @PostMapping
    public R save(@RequestBody GenDatasourceConf datasourceConf) {
        return R.ok(datasourceConfService.saveDsByEnc(datasourceConf));
    }

    /**
     * Modify data source configuration.
     *
     * @param conf Data source configuration
     * @return R
     */
    @PutMapping
    public R updateById(@RequestBody GenDatasourceConf conf) {
        return R.ok(datasourceConfService.updateDsByEnc(conf));
    }

    /**
     * Delete data source table by ID.
     *
     * @param ids ID
     * @return R
     */
    @DeleteMapping
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(datasourceConfService.removeByDsId(ids));
    }

    /**
     * Query the documents corresponding to the data source.
     *
     * @param dsName Data source name
     */
    @SneakyThrows
    @GetMapping("/doc")
    public void generatorDoc(String dsName, HttpServletResponse response) {
        // Set the specified data source
        DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
        DynamicDataSourceContextHolder.push(dsName);
        DataSource dataSource = dynamicRoutingDataSource.determineDataSource();

        // Set the specified target table
        ScrewProperties screwProperties = SpringContextHolder.getBean(ScrewProperties.class);

        // Start to generate
        byte[] data = screw.documentGeneration(dataSource, screwProperties).toByteArray();
        response.reset();
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
        response.setContentType(ContentType.OCTET_STREAM.getValue());
        IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
    }

}
