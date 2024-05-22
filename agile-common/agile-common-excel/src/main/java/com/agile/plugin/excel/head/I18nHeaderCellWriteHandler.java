package com.agile.plugin.excel.head;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Internationalize the table header.
 *
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
public class I18nHeaderCellWriteHandler implements CellWriteHandler {

    /**
     * Internationalized message source.
     */
    private final MessageSource messageSource;

    /**
     * International translation.
     */
    private final PropertyPlaceholderHelper.PlaceholderResolver placeholderResolver;

    public I18nHeaderCellWriteHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.placeholderResolver = placeholderName -> this.messageSource.getMessage(placeholderName, null,
                LocaleContextHolder.getLocale());
    }

    /**
     * Placeholder process.
     */
    private final PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("{", "}");

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
                                 Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
        if (isHead != null && isHead) {
            List<String> originHeadNameList = head.getHeadNameList();
            if (CollectionUtils.isNotEmpty(originHeadNameList)) {
                // International process
                List<String> i18nHeadNames = originHeadNameList.stream()
                        .map(headName -> propertyPlaceholderHelper.replacePlaceholders(headName, placeholderResolver))
                        .collect(Collectors.toList());
                head.setHeadNameList(i18nHeadNames);
            }
        }
    }

}
    