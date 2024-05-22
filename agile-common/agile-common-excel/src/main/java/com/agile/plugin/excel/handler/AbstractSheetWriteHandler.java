package com.agile.plugin.excel.handler;

import com.agile.plugin.excel.annotation.ResponseExcel;
import com.agile.plugin.excel.annotation.Sheet;
import com.agile.plugin.excel.aop.DynamicNameAspect;
import com.agile.plugin.excel.config.ExcelConfigProperties;
import com.agile.plugin.excel.converter.*;
import com.agile.plugin.excel.enhance.WriterBuilderEnhancer;
import com.agile.plugin.excel.head.HeadGenerator;
import com.agile.plugin.excel.head.HeadMeta;
import com.agile.plugin.excel.head.I18nHeaderCellWriteHandler;
import com.agile.plugin.excel.util.ExcelException;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Export Excel files. Configure and create {@link ExcelWriter} and write data to Excel files.
 *
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
public abstract class AbstractSheetWriteHandler implements SheetWriteHandler, ApplicationContextAware {

    private final ExcelConfigProperties configProperties;

    private final ObjectProvider<List<Converter<?>>> converterProvider;

    private final WriterBuilderEnhancer excelWriterBuilderEnhance;

    private ApplicationContext applicationContext;

    @Getter
    @Setter
    @Autowired(required = false)
    private I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler;

    @Override
    public void check(ResponseExcel responseExcel) {
        if (responseExcel.sheets().length == 0) {
            throw new ExcelException("The @ResponseExcel sheet configuration is invalid");
        }
    }

    @Override
    @SneakyThrows(UnsupportedEncodingException.class)
    public void export(Object o, HttpServletResponse response, ResponseExcel responseExcel) {
        check(responseExcel);
        // Get the context properties of the current request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // Get the Excel file name from the request properties
        String name = (String) Objects.requireNonNull(requestAttributes)
                .getAttribute(DynamicNameAspect.EXCEL_NAME_KEY, RequestAttributes.SCOPE_REQUEST);
        if (name == null) {
            // Generates a random UUID as the file name
            name = UUID.randomUUID().toString();
        }
        // The file name is encoded using UTF-8 encoding format and the file suffix is added
        String fileName = String.format("%s%s", URLEncoder.encode(name, "UTF-8"), responseExcel.suffix().getValue());
        // Find the corresponding contentType based on the actual file type
        String contentType = MediaTypeFactory.getMediaType(fileName)
                .map(MediaType::toString)
                .orElse("application/vnd.ms-excel");
        response.setContentType(contentType);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.setCharacterEncoding("utf-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
        // Write the object to the response flow
        write(o, response, responseExcel);
    }

    /**
     * A common method for obtaining an {@link ExcelWriter}.
     *
     * @param response      HttpServletResponse
     * @param responseExcel ResponseExcel annotation
     * @return ExcelWriter
     */
    @SneakyThrows(IOException.class)
    public ExcelWriter getExcelWriter(HttpServletResponse response, ResponseExcel responseExcel) {
        // Create an instance of ExcelWriterBuilder and register common converters
        ExcelWriterBuilder writerBuilder = EasyExcel.write(response.getOutputStream())
                .registerConverter(LocalDateStringConverter.INSTANCE)
                .registerConverter(LocalDateTimeStringConverter.INSTANCE)
                .registerConverter(LocalTimeStringConverter.INSTANCE)
                .registerConverter(LongStringConverter.INSTANCE)
                .registerConverter(StringArrayConverter.INSTANCE)
                .autoCloseStream(true)
                .excelType(responseExcel.suffix())
                .inMemory(responseExcel.inMemory());

        // If the password is configured in the ResponseExcel annotation,
        // set the password
        if (StringUtils.hasText(responseExcel.password())) {
            writerBuilder.password(responseExcel.password());
        }

        // If included columns are configured,
        // the included columns are set
        if (responseExcel.include().length != 0) {
            writerBuilder.includeColumnFieldNames(Arrays.asList(responseExcel.include()));
        }
        // If excluded columns are configured,
        // the excluded columns are set
        if (responseExcel.exclude().length != 0) {
            writerBuilder.excludeColumnFieldNames(Arrays.asList(responseExcel.exclude()));
        }

        // Register a custom write processor
        for (Class<? extends WriteHandler> clazz : responseExcel.writeHandler()) {
            writerBuilder.registerWriteHandler(BeanUtils.instantiateClass(clazz));
        }

        // Enable internationalization header processing
        if (responseExcel.i18nHeader() && i18nHeaderCellWriteHandler != null) {
            writerBuilder.registerWriteHandler(i18nHeaderCellWriteHandler);
        }

        // Custom injection converter
        registerCustomConverter(writerBuilder);

        for (Class<? extends Converter> clazz : responseExcel.converter()) {
            writerBuilder.registerConverter(BeanUtils.instantiateClass(clazz));
        }

        // If the template file is configured,
        // the template file is loaded from the template path
        String templatePath = configProperties.getTemplatePath();
        if (StringUtils.hasText(responseExcel.template())) {
            ClassPathResource classPathResource = new ClassPathResource(
                    templatePath + File.separator + responseExcel.template());
            InputStream inputStream = classPathResource.getInputStream();
            writerBuilder.withTemplate(inputStream);
        }

        // Enhance ExcelWriterBuilder instances
        writerBuilder = excelWriterBuilderEnhance.enhanceExcel(writerBuilder, response, responseExcel, templatePath);

        return writerBuilder.build();
    }

    /**
     * Custom injection converter, subclasses override themselves if necessary.
     *
     * @param builder ExcelWriterBuilder
     */
    public void registerCustomConverter(ExcelWriterBuilder builder) {
        converterProvider.ifAvailable(converters -> converters.forEach(builder::registerConverter));
    }

    /**
     * Get the {@link WriteSheet} object.
     *
     * @param sheet                 Sheet annotation info
     * @param dataClass             Data Type
     * @param template              Template
     * @param bookHeadEnhancerClass Custom header processor
     * @return WriteSheet
     */
    public WriteSheet sheet(Sheet sheet, Class<?> dataClass, String template,
                            Class<? extends HeadGenerator> bookHeadEnhancerClass) {

        // Sheet number and name
        Integer sheetNo = sheet.sheetNo() >= 0 ? sheet.sheetNo() : null;
        String sheetName = sheet.sheetName();

        // Template write or not
        ExcelWriterSheetBuilder writerSheetBuilder = StringUtils.hasText(template) ? EasyExcel.writerSheet(sheetNo)
                : EasyExcel.writerSheet(sheetNo, sheetName);

        // Enhanced header information:
        // 1. The header information specified in the sheet is preferentially enhanced
        // 2. The second is enhanced with global header information defined in @ResponseExcel
        Class<? extends HeadGenerator> headGenerateClass = null;
        if (isNotInterface(sheet.headGenerateClass())) {
            headGenerateClass = sheet.headGenerateClass();
        } else if (isNotInterface(bookHeadEnhancerClass)) {
            headGenerateClass = bookHeadEnhancerClass;
        }
        // Definition header enhancement is used to generate header information,
        // otherwise dataClass is used to get it automatically
        if (headGenerateClass != null) {
            fillCustomHeadInfo(dataClass, bookHeadEnhancerClass, writerSheetBuilder);
        } else if (dataClass != null) {
            writerSheetBuilder.head(dataClass);
            if (sheet.excludes().length > 0) {
                writerSheetBuilder.excludeColumnFieldNames(Arrays.asList(sheet.excludes()));
            }
            if (sheet.includes().length > 0) {
                writerSheetBuilder.includeColumnFieldNames(Arrays.asList(sheet.includes()));
            }
        }

        // SheetBuilder enhancement
        writerSheetBuilder = excelWriterBuilderEnhance.enhanceSheet(writerSheetBuilder, sheetNo, sheetName, dataClass,
                template, headGenerateClass);

        return writerSheetBuilder.build();
    }

    /**
     * Populate custom table headers based on data classes and header generator classes.
     *
     * @param dataClass          Generate table header information
     * @param headEnhancerClass  Head enhance class
     * @param writerSheetBuilder Set table header information
     */
    private void fillCustomHeadInfo(Class<?> dataClass, Class<? extends HeadGenerator> headEnhancerClass,
                                    ExcelWriterSheetBuilder writerSheetBuilder) {
        HeadGenerator headGenerator = this.applicationContext.getBean(headEnhancerClass);
        Assert.notNull(headGenerator, "The header generated bean does not exist.");
        HeadMeta head = headGenerator.head(dataClass);
        writerSheetBuilder.head(head.getHead());
        writerSheetBuilder.excludeColumnFieldNames(head.getIgnoreHeadFields());
    }

    /**
     * Whether it is a Null Head Generator.
     *
     * @param headGeneratorClass Header generator type
     * @return true for specified and false for Not specified (default)
     */
    private boolean isNotInterface(Class<? extends HeadGenerator> headGeneratorClass) {
        return !Modifier.isInterface(headGeneratorClass.getModifiers());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
    