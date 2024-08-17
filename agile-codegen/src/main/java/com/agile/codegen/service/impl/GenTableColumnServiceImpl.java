package com.agile.codegen.service.impl;

import cn.hutool.core.text.NamingCase;
import com.agile.codegen.entity.GenFieldType;
import com.agile.codegen.entity.GenTableColumnEntity;
import com.agile.codegen.mapper.GenFieldTypeMapper;
import com.agile.codegen.mapper.GenTableColumnMapper;
import com.agile.codegen.service.GenTableColumnService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Table field information management service.
 *
 * @author Huang Z.Y.
 */
@Service
@RequiredArgsConstructor
public class GenTableColumnServiceImpl extends ServiceImpl<GenTableColumnMapper, GenTableColumnEntity>
        implements GenTableColumnService {

    private final GenFieldTypeMapper fieldTypeMapper;

    /**
     * Initialize the form field list, mainly converting the fields in the database table
     * into the field data format required by the form, and sorting the audit fields.
     *
     * @param tableFieldList Form field list
     */
    @Override
    public void initFieldList(List<GenTableColumnEntity> tableFieldList) {
        // Field type, attribute type mapping
        List<GenFieldType> list = fieldTypeMapper.selectList(Wrappers.emptyWrapper());
        Map<String, GenFieldType> fieldTypeMap = new LinkedHashMap<>(list.size());
        list.forEach(
                fieldTypeMapping -> fieldTypeMap.put(fieldTypeMapping.getColumnType().toLowerCase(), fieldTypeMapping));

        // Index counter
        AtomicInteger index = new AtomicInteger(0);
        tableFieldList.forEach(field -> {
            // Convert field names to camelCase format
            field.setAttrName(NamingCase.toCamelCase(field.getFieldName()));

            // Get the type corresponding to the field
            GenFieldType fieldTypeMapping = fieldTypeMap.getOrDefault(field.getFieldType().toLowerCase(), null);
            if (fieldTypeMapping == null) {
                // If the corresponding type is not found, it is the Object type.
                field.setAttrType("Object");
            } else {
                field.setAttrType(fieldTypeMapping.getAttrType());
                field.setPackageName(fieldTypeMapping.getPackageName());
            }

            // Set both query type and form query type to "="
            field.setQueryType("=");
            field.setQueryFormType("text");

            // Set the form type to text box type
            field.setFormType("text");

            // Ensure audit fields are displayed last
            field.setSort(Objects.isNull(field.getSort()) ? index.getAndIncrement() : field.getSort());
        });
    }

    /**
     * Update form field information for the specified data source and table name.
     *
     * @param dsName         Data source name
     * @param tableName      Table name
     * @param tableFieldList Form field list
     */
    @Override
    public void updateTableField(String dsName, String tableName, List<GenTableColumnEntity> tableFieldList) {
        AtomicInteger sort = new AtomicInteger();
        this.updateBatchById(tableFieldList.stream()
                .peek(field -> field.setSort(sort.getAndIncrement()))
                .collect(Collectors.toList()));
    }

}
